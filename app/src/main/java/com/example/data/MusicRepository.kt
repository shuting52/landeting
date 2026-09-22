package com.example.data

import com.example.R
import com.example.model.AudiobookItem
import com.example.model.LyricLine
import com.example.model.Playlist
import com.example.model.Song
import com.example.model.SoundQuality

object MusicRepository {

    // The initial library is dynamically recognized and scanned from local storage.
    // Legacy sampleSongs is aliased to identified local songs for full backwards compatibility.
    val sampleSongs: List<Song> get() = LocalAudioScannerHolder.getIdentifiedLocalSongs()

    val audiobooks: List<AudiobookItem> = listOf(
        AudiobookItem(
            id = "book_1",
            title = "三体 · 全景3D声效广播剧",
            narrator = "王明军 / 729声工场",
            episodesCount = 120,
            updateStatus = "完结",
            category = "科幻巨著",
            playbackDuration = "48小时"
        ),
        AudiobookItem(
            id = "book_2",
            title = "明朝那些事儿 · 经典白话评书",
            narrator = "当年明月 / 单田芳风格演绎",
            episodesCount = 380,
            updateStatus = "完结",
            category = "历史风云",
            playbackDuration = "96小时"
        ),
        AudiobookItem(
            id = "book_3",
            title = "深夜睡眠助眠 · 雨打芭蕉白噪音",
            narrator = "懒听自然声实验室",
            episodesCount = 24,
            updateStatus = "连载中",
            category = "助眠疗愈",
            playbackDuration = "12小时"
        ),
        AudiobookItem(
            id = "book_4",
            title = "福尔摩斯探案全集 · 沉浸剧场",
            narrator = "声音导师张震",
            episodesCount = 68,
            updateStatus = "完结",
            category = "悬疑推理",
            playbackDuration = "36小时"
        )
    )

    val defaultPlaylists: List<Playlist> = listOf(
        Playlist(
            id = "pl_custom_1",
            title = "我的夜间私藏歌单",
            description = "深夜放空专属，偷得浮生半日闲",
            songIds = listOf("identified_local_1", "identified_local_2", "identified_local_3"),
            isCustom = true,
            coverTag = "私享"
        ),
        Playlist(
            id = "pl_custom_2",
            title = "单曲循环 100 遍",
            description = "百听不厌的经典旋律",
            songIds = listOf("identified_local_1", "identified_local_4"),
            isCustom = true,
            coverTag = "单曲"
        ),
        Playlist(
            id = "pl_fav_1",
            title = "Hi-Res 发烧友试金石",
            description = "母带级高动态音质，殿堂级听觉享受",
            songIds = listOf("identified_local_2", "identified_local_4", "identified_local_5"),
            isCustom = false,
            coverTag = "HIFI"
        ),
        Playlist(
            id = "pl_fav_2",
            title = "本地识别精品音乐榜",
            description = "本地音频识别技术自动扫描的精选高品质歌曲",
            songIds = listOf("identified_local_1", "identified_local_2", "identified_local_3", "identified_local_5"),
            isCustom = false,
            coverTag = "识别"
        )
    )

    val localSongs: List<Song> get() = LocalAudioScannerHolder.getIdentifiedLocalSongs()
}

/**
 * Static provider for fallback recognized songs before context scanner runs
 */
object LocalAudioScannerHolder {
    private val fallback = listOf(
        Song(
            id = "identified_local_1",
            title = "稻香.flac",
            artist = "周杰伦",
            album = "本地存储/Music/JayChou",
            durationMs = 223000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 440f,
            genre = "本地无损 · 民谣流行",
            bitRate = "24bit/96kHz 4608Kbps",
            lyrics = listOf(
                LyricLine(0, "稻香 - 周杰伦 [本地识别技术获取]"),
                LyricLine(4000, "词 / 曲：周杰伦"),
                LyricLine(8000, "对这个世界如果你有太多的抱怨"),
                LyricLine(14000, "跌倒了 就不敢继续往前走"),
                LyricLine(20000, "为什么 人要这么的脆弱 堕落"),
                LyricLine(26000, "请你打开电视看看 多少人为生命在努力走下去"),
                LyricLine(33000, "我们是不是该知足 珍惜一切 就算没有拥有"),
                LyricLine(40000, "还记得你说家是唯一的城堡 随着稻香河流继续奔跑"),
                LyricLine(47000, "微微笑 小时候的梦我知道"),
                LyricLine(54000, "不要哭 让萤火虫带着你逃跑"),
                LyricLine(61000, "乡间的歌谣 永远的依靠"),
                LyricLine(68000, "回家吧 回到最初的美好")
            )
        ),
        Song(
            id = "identified_local_2",
            title = "特别的人.wav",
            artist = "方大同",
            album = "本地存储/Download/Soul",
            durationMs = 260000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 392f,
            genre = "本地母带 · R&B",
            bitRate = "24bit/192kHz 9216Kbps",
            lyrics = listOf(
                LyricLine(0, "特别的人 - 方大同 [本地识别技术获取]"),
                LyricLine(4000, "爱一个人或许要慷慨"),
                LyricLine(12000, "若只想要被爱 最后没有了对白"),
                LyricLine(20000, "必须学懂我如何去爱 明白了怎样去爱"),
                LyricLine(28000, "我们都是一个人 加上另一个人的长相"),
                LyricLine(36000, "时间的沙漏沉淀着 无法逃避的重量"),
                LyricLine(44000, "我们是对方 特别的人 奋不顾身 难舍难分")
            )
        ),
        Song(
            id = "identified_local_3",
            title = "七里香.flac",
            artist = "周杰伦",
            album = "本地存储/Music/Landeting",
            durationMs = 299000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 440f,
            genre = "本地无损 · 经典金曲",
            bitRate = "24bit/96kHz 4608Kbps",
            lyrics = listOf(
                LyricLine(0, "七里香 - 周杰伦 [本地识别技术获取]"),
                LyricLine(5000, "窗外的麻雀 在电线杆上多嘴"),
                LyricLine(12000, "你说这一句 很有夏天的感觉"),
                LyricLine(18000, "手中的铅笔 在纸上来来回回"),
                LyricLine(24000, "我用几行字形容你是我的谁"),
                LyricLine(30000, "秋刀鱼的滋味 猫跟你都想了解"),
                LyricLine(36000, "初恋的香味就这样被我们寻回"),
                LyricLine(42000, "那温暖的阳光 像刚切的草莓"),
                LyricLine(48000, "雨下整夜 我的爱溢出就像雨水"),
                LyricLine(54000, "院子落叶 跟我的思念厚厚一叠")
            )
        ),
        Song(
            id = "identified_local_4",
            title = "夜曲.wav",
            artist = "周杰伦",
            album = "本地存储/Music/Classic",
            durationMs = 226000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 329.63f,
            genre = "本地母带 · 古典流行",
            bitRate = "24bit/192kHz 9216Kbps",
            lyrics = listOf(
                LyricLine(0, "夜曲 - 周杰伦 [本地识别技术获取]"),
                LyricLine(4000, "一群嗜血的蚂蚁 被腐肉所吸引"),
                LyricLine(10000, "我面无表情 看孤独的风景"),
                LyricLine(16000, "失去你 爱恨开始分明"),
                LyricLine(22000, "为你弹奏萧邦的夜曲 纪念我死去的爱情"),
                LyricLine(28000, "跟夜风一样的声音 心碎的很好听")
            )
        ),
        Song(
            id = "identified_local_5",
            title = "海阔天空.flac",
            artist = "Beyond",
            album = "本地存储/Music/Rock",
            durationMs = 324000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 392f,
            genre = "本地无损 · 摇滚经典",
            bitRate = "24bit/96kHz 4608Kbps",
            lyrics = listOf(
                LyricLine(0, "海阔天空 - Beyond [本地识别技术获取]"),
                LyricLine(5000, "今天我 寒夜里看雪飘过"),
                LyricLine(12000, "怀着冷却了的心窝飘远方"),
                LyricLine(19000, "风雨里追赶 雾里分不清影踪"),
                LyricLine(26000, "天空海阔你与我 可会变"),
                LyricLine(33000, "原谅我这一生不羁放纵爱自由"),
                LyricLine(40000, "也会怕有一天会跌倒"),
                LyricLine(47000, "背弃了理想 谁人都可以"),
                LyricLine(54000, "哪会怕有一天只你共我")
            )
        ),
        Song(
            id = "identified_local_6",
            title = "光年之外.m4a",
            artist = "邓紫棋 (G.E.M.)",
            album = "本地存储/Download",
            durationMs = 235000,
            soundQuality = SoundQuality.SQ_LOSSLESS,
            isHiRes = false,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 523.25f,
            genre = "本地无损 · 电影原声",
            bitRate = "16bit/44.1kHz 1411Kbps",
            lyrics = listOf(
                LyricLine(0, "光年之外 - 邓紫棋 [本地识别技术获取]"),
                LyricLine(6000, "感受停在我发端的指尖"),
                LyricLine(13000, "如何瞬间 冻结时间"),
                LyricLine(20000, "记住望着我双眼的瞬间"),
                LyricLine(27000, "神圣的誓言"),
                LyricLine(34000, "缘分让我们相遇乱世以外"),
                LyricLine(41000, "命运却要我们危难中相爱")
            )
        )
    )

    fun getIdentifiedLocalSongs(): List<Song> = fallback
}
