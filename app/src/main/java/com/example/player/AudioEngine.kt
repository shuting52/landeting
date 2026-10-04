package com.example.player

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.MediaPlayer
import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sin

/**
 * 播放引擎：优先播放真实音频文件（MediaPlayer，支持 content:// URI 与文件路径），
 * 当歌曲没有真实音频源（如网络搜索结果尚未缓存）时，降级为软件合成器
 * 生成轻柔旋律，保证任何场景都能出声。
 */
class AudioEngine(private val context: Context) {

    // ---------- 真实音频播放（MediaPlayer）----------
    private var mediaPlayer: MediaPlayer? = null

    // ---------- 合成器兜底 ----------
    private var audioTrack: AudioTrack? = null
    private var playbackJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)
    private val sampleRate = 44100
    private var baseFreq = 440f
    @Volatile
    private var bassMultiplier = 1.0
    @Volatile
    private var trebleMultiplier = 1.0

    /** 当前是否在播放真实音频文件 */
    fun isRealAudioPlaying(): Boolean = mediaPlayer?.isPlaying == true

    /** 真实音频当前位置（毫秒）；非真实播放返回 0 */
    fun currentPosition(): Long = mediaPlayer?.currentPosition?.toLong() ?: 0L

    /** 真实音频总时长（毫秒）；非真实播放返回 0 */
    fun duration(): Long = mediaPlayer?.duration?.toLong() ?: 0L

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

    /**
     * 播放歌曲：优先真实音频源，否则合成器兜底。
     * @param frequency 合成器音高（仅在无真实音频源时使用）
     * @param uri       真实音频源：content:// URI / file:// URI / 文件绝对路径；空则合成器
     */
    fun startPlaying(frequency: Float = 440f, uri: String? = null) {
        stop()

        if (!uri.isNullOrBlank()) {
            try {
                val mp = MediaPlayer()
                mp.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                when {
                    uri.startsWith("content://") || uri.startsWith("file://") ->
                        mp.setDataSource(context, Uri.parse(uri))
                    else -> mp.setDataSource(uri) // 文件绝对路径
                }
                mp.setOnErrorListener { _, _, _ ->
                    // 真实音频播放失败时释放并降级合成器
                    try { mp.release() } catch (_: Exception) {}
                    if (mediaPlayer === mp) mediaPlayer = null
                    startSynthesizer(frequency)
                    true
                }
                mp.setOnCompletionListener {
                    // 播放完成：保持 mediaPlayer 引用（进度追踪据此切歌）
                }
                mp.prepare()
                mp.start()
                mediaPlayer = mp
                return
            } catch (_: Exception) {
                // 播放失败，降级合成器
                try { mediaPlayer?.release() } catch (_: Exception) {}
                mediaPlayer = null
            }
        }
        startSynthesizer(frequency)
    }

    /** 合成器兜底播放 */
    private fun startSynthesizer(frequency: Float) {
        baseFreq = frequency
        stopSynthesizer()
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

    /** 恢复暂停的播放：真实音频从中断位置继续；合成器重新从头开始（兜底） */
    fun resume(frequency: Float = 440f, uri: String? = null) {
        val mp = mediaPlayer
        if (mp != null) {
            try {
                if (!mp.isPlaying) mp.start()
                return
            } catch (_: Exception) {
                // 恢复失败，降级重建
            }
        }
        startPlaying(frequency, uri)
    }

    fun pause() {
        mediaPlayer?.let {
            if (it.isPlaying) it.pause()
        }
        stopSynthesizer()
    }

    fun stop() {
        mediaPlayer?.let {
            try {
                it.stop()
            } catch (_: Exception) {}
            try {
                it.release()
            } catch (_: Exception) {}
        }
        mediaPlayer = null
        stopSynthesizer()
    }

    private fun stopSynthesizer() {
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
