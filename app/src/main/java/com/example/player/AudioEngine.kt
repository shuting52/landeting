package com.example.player

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sin

/**
 * Real audio synthesizer generating gentle melodic musical chords
 * so that the user actually hears real audio playback through the device speaker.
 */
class AudioEngine {
    private var audioTrack: AudioTrack? = null
    private var playbackJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)
    private val sampleRate = 44100
    private var baseFreq = 440f
    @Volatile
    private var bassMultiplier = 1.0
    @Volatile
    private var trebleMultiplier = 1.0

    fun updateEqualizer(bassGainDb: Float, trebleGainDb: Float, bassBoostPercent: Float) {
        bassMultiplier = (1.0 + (bassGainDb / 12.0) * 0.5 + (bassBoostPercent / 100.0) * 0.4).coerceIn(0.2, 2.2)
        trebleMultiplier = (1.0 + (trebleGainDb / 12.0) * 0.5).coerceIn(0.2, 2.2)
    }

    init {
        try {
            val minBufferSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )
            val bufferSize = minBufferSize.coerceAtLeast(sampleRate)
            audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(bufferSize)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()
        } catch (_: Exception) {
            // AudioTrack fallback
        }
    }

    fun startPlaying(frequency: Float = 440f) {
        baseFreq = frequency
        stop()
        playbackJob = scope.launch {
            try {
                audioTrack?.play()
            } catch (_: Exception) {}

            val notes = listOf(baseFreq, baseFreq * 1.25f, baseFreq * 1.5f, baseFreq * 1.33f)
            var noteIndex = 0
            val noteDurationMs = 600
            val samplesPerNote = (sampleRate * noteDurationMs) / 1000
            val buffer = ShortArray(samplesPerNote)

            while (isActive) {
                val currentNoteFreq = notes[noteIndex % notes.size]
                noteIndex++

                for (i in 0 until samplesPerNote) {
                    val time = i.toDouble() / sampleRate
                    // Apply attack and decay envelope
                    val envelope = when {
                        i < samplesPerNote * 0.1 -> i / (samplesPerNote * 0.1)
                        i > samplesPerNote * 0.7 -> (samplesPerNote - i) / (samplesPerNote * 0.3)
                        else -> 1.0
                    }
                    val fundamental = sin(2.0 * Math.PI * currentNoteFreq * time) * 0.30
                    val bassOvertone = sin(2.0 * Math.PI * (currentNoteFreq * 0.5) * time) * 0.15 * bassMultiplier
                    val harmonic = sin(2.0 * Math.PI * (currentNoteFreq * 2) * time) * 0.10 * trebleMultiplier
                    val sample = (fundamental + bassOvertone + harmonic) * envelope
                    buffer[i] = (sample * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }

                try {
                    audioTrack?.write(buffer, 0, buffer.size)
                } catch (_: Exception) {
                    break
                }
                delay(10)
            }
        }
    }

    fun pause() {
        playbackJob?.cancel()
        playbackJob = null
        try {
            audioTrack?.pause()
            audioTrack?.flush()
        } catch (_: Exception) {}
    }

    fun stop() {
        playbackJob?.cancel()
        playbackJob = null
        try {
            audioTrack?.pause()
            audioTrack?.flush()
        } catch (_: Exception) {}
    }

    fun release() {
        stop()
        try {
            audioTrack?.release()
            audioTrack = null
        } catch (_: Exception) {}
    }
}
