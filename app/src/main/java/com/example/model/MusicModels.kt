package com.example.model

data class LyricLine(
    val timeMs: Long,
    val text: String,
    val translation: String? = null
)

enum class PlayMode(val label: String) {
    SEQUENCE("顺序播放"),
    REPEAT_ONE("单曲循环"),
    SHUFFLE("随机播放")
}

enum class SoundQuality(val title: String, val desc: String, val tag: String) {
    STANDARD("标准音质", "128Kbps MP3 极速畅听", "标准"),
    HIGH("极高音质", "320Kbps MP3 细节丰富", "HQ"),
    SQ_LOSSLESS("无损音质", "16bit/44.1kHz FLAC 原声还原", "SQ"),
    HI_RES("Hi-Res 超清母带", "24bit/96kHz 9216Kbps 录音室级", "Hi-Res")
}

data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val durationMs: Long,
    val lyrics: List<LyricLine>,
    val soundQuality: SoundQuality = SoundQuality.SQ_LOSSLESS,
    val isHiRes: Boolean = false,
    val coverRes: Int? = null,
    val toneFrequency: Float = 440f, // For melody synthesizer
    val genre: String = "流行",
    val bitRate: String = "1411kbps",
    val rawLrc: String? = null
)

data class Playlist(
    val id: String,
    val title: String,
    val description: String,
    val songIds: List<String>,
    val isCustom: Boolean = false,
    val coverTag: String = "精选",
    val playCount: String = "12.8万"
)

data class AudiobookItem(
    val id: String,
    val title: String,
    val narrator: String,
    val episodesCount: Int,
    val updateStatus: String,
    val category: String,
    val playbackDuration: String
)

data class PlaybackSettings(
    val onlineQuality: SoundQuality = SoundQuality.HI_RES,
    val playFadeInOut: Boolean = true,
    val smartVolumeBalance: Boolean = true,
    val unplugPause: Boolean = true,
    val lockscreenControls: Boolean = true,
    val cacheWhileListening: Boolean = true,
    val cacheLimitMb: Int = 2048,
    val downloadDirectory: String = "/storage/emulated/0/Music/Landeting",
    val downloadQuality: SoundQuality = SoundQuality.HI_RES,
    val videoQuality: String = "1080P 高清 (推荐)",
    val desktopLyricEnabled: Boolean = true,
    val lyricDoubleLine: Boolean = false,
    val lyricFontSizeSp: Int = 18,
    val lyricColorHex: String = "#FF6D00",
    val lyricTouchLock: Boolean = false
)

enum class ThemePalette(val displayName: String, val primaryHex: Long, val accentHex: Long, val bgDarkHex: Long) {
    RED_BLACK("潮酷红黑", 0xFFFF3B30, 0xFFFF7A00, 0xFF121214),
    AURORA_PURPLE("极光霓虹", 0xFFAF52DE, 0xFF007AFF, 0xFF100E17),
    EMERALD_GREEN("浅夏青绿", 0xFF34C759, 0xFF30D158, 0xFF0E1712),
    SUNSET_GOLD("暮光琥珀", 0xFFFF9500, 0xFFFFCC00, 0xFF17130E),
    CYBERPUNK("赛博幻境", 0xFF00E5FF, 0xFFFF007F, 0xFF0A0F1D)
}
