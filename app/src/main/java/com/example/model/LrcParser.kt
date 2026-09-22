package com.example.model

import java.util.regex.Pattern

/**
 * Data structure representing parsed LRC file contents.
 */
data class LrcData(
    val title: String? = null,
    val artist: String? = null,
    val album: String? = null,
    val author: String? = null,
    val offsetMs: Long = 0L,
    val lines: List<LyricLine> = emptyList()
)

/**
 * Robust .lrc format parser supporting:
 * - Standard timestamps: [mm:ss.xx] and [mm:ss.xxx]
 * - Basic timestamps: [mm:ss]
 * - Multi-timestamp per line: [00:12.50][01:23.40]Lyrics
 * - ID tags: [ti:Title], [ar:Artist], [al:Album], [by:Editor], [offset:+/-ms]
 * - Chronological sorting
 */
object LrcParser {

    private val TIME_TAG_PATTERN = Pattern.compile("\\[(\\d{1,2}):(\\d{1,2})(?:[.:](\\d{1,3}))?]")
    private val META_TAG_PATTERN = Pattern.compile("^\\[(ti|ar|al|by|offset):([^\\]]*)]$")

    fun parse(lrcContent: String): LrcData {
        if (lrcContent.isBlank()) {
            return LrcData()
        }

        var title: String? = null
        var artist: String? = null
        var album: String? = null
        var author: String? = null
        var offsetMs = 0L

        val lyricLines = mutableListOf<LyricLine>()

        val rawLines = lrcContent.lines()
        for (rawLine in rawLines) {
            val line = rawLine.trim()
            if (line.isEmpty()) continue

            // 1. Check for Metadata Tags like [ti:Title]
            val metaMatcher = META_TAG_PATTERN.matcher(line)
            if (metaMatcher.matches()) {
                val tag = metaMatcher.group(1)?.lowercase()
                val value = metaMatcher.group(2)?.trim() ?: ""
                when (tag) {
                    "ti" -> title = value
                    "ar" -> artist = value
                    "al" -> album = value
                    "by" -> author = value
                    "offset" -> offsetMs = value.toLongOrNull() ?: 0L
                }
                continue
            }

            // 2. Extract Timestamps and lyric text
            val matcher = TIME_TAG_PATTERN.matcher(line)
            val timeMsList = mutableListOf<Long>()
            var lastMatchEnd = 0

            while (matcher.find()) {
                val minStr = matcher.group(1) ?: "0"
                val secStr = matcher.group(2) ?: "0"
                val msStr = matcher.group(3)

                val minutes = minStr.toLongOrNull() ?: 0L
                val seconds = secStr.toLongOrNull() ?: 0L
                val millis = when {
                    msStr == null -> 0L
                    msStr.length == 1 -> (msStr.toLongOrNull() ?: 0L) * 100
                    msStr.length == 2 -> (msStr.toLongOrNull() ?: 0L) * 10
                    msStr.length >= 3 -> (msStr.take(3).toLongOrNull() ?: 0L)
                    else -> 0L
                }

                val timeMs = minutes * 60 * 1000 + seconds * 1000 + millis
                timeMsList.add(timeMs)
                lastMatchEnd = matcher.end()
            }

            if (timeMsList.isNotEmpty()) {
                val content = line.substring(lastMatchEnd).trim()
                // If content is empty or purely whitespace, it's often an instrumental gap
                val displayContent = if (content.isEmpty()) "(间奏)" else content
                for (time in timeMsList) {
                    lyricLines.add(LyricLine(timeMs = time, text = displayContent))
                }
            }
        }

        // Apply offset if specified
        val adjustedLines = if (offsetMs != 0L) {
            lyricLines.map { it.copy(timeMs = (it.timeMs + offsetMs).coerceAtLeast(0L)) }
        } else {
            lyricLines
        }

        // Sort chronologically
        val sortedLines = adjustedLines.sortedBy { it.timeMs }

        return LrcData(
            title = title,
            artist = artist,
            album = album,
            author = author,
            offsetMs = offsetMs,
            lines = sortedLines
        )
    }
}
