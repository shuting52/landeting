package com.example.data

import com.example.R
import com.example.model.AudiobookItem
import com.example.model.LyricLine
import com.example.model.Playlist
import com.example.model.Song
import com.example.model.SoundQuality

object MusicRepository {

    val sampleSongs: List<Song> = listOf(
        Song(
            id = "song_1",
            title = "兰亭序",
            artist = "周杰伦",
            album = "魔杰座",
            durationMs = 254000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 392f,
            genre = "中国风",
            bitRate = "24bit/96kHz 4608Kbps",
            lyrics = listOf(
                LyricLine(0, "兰亭序 - 周杰伦"),
                LyricLine(4000, "作词：方文山 / 作曲：周杰伦"),
                LyricLine(12000, "兰亭临帖 行书如行云流水"),
                LyricLine(18000, "月下门推 心细如你脚步碎"),
                LyricLine(24000, "忙不迭 千年碑易拓 却难拓你的美"),
                LyricLine(31000, "真迹绝 真心能给谁"),
                LyricLine(37000, "牧笛横吹 黄酒小菜又几杯"),
                LyricLine(43000, "竹篱茅舍 风雨过几度明灭"),
                LyricLine(49000, "悬笔一绝 那岸边浪千叠"),
                LyricLine(56000, "情字何解 怎落笔都不对"),
                LyricLine(62000, "而我独缺 你一生的了解"),
                LyricLine(70000, "弹指岁月 倾城那一阕"),
                LyricLine(77000, "情字何解 怎落笔都不对"),
                LyricLine(85000, "而我独缺 你一生的了解")
            )
        ),
        Song(
            id = "song_2",
            title = "晴天",
            artist = "周杰伦",
            album = "叶惠美",
            durationMs = 269000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 440f,
            genre = "经典流行",
            bitRate = "24bit/96kHz 4608Kbps",
            lyrics = listOf(
                LyricLine(0, "晴天 - 周杰伦"),
                LyricLine(5000, "故事的小黄花 从出生那年就飘着"),
                LyricLine(12000, "童年的荡秋千 随记忆一直晃到现在"),
                LyricLine(20000, "Re So So Si Do Si La So La Si Si Si Si La Si La So"),
                LyricLine(27000, "吹着前奏望着天空 我想起花瓣试着掉落"),
                LyricLine(35000, "为你翘课的那一天 花落的那一天"),
                LyricLine(43000, "教室的那一间 我怎么看不见"),
                LyricLine(51000, "消失的下雨天 我好想再淋一遍"),
                LyricLine(59000, "没想到失去的勇气我还留着"),
                LyricLine(66000, "好想再问一遍 你会等待还是离开")
            )
        ),
        Song(
            id = "song_3",
            title = "渡口 (Hi-Res 试音母带)",
            artist = "蔡琴",
            album = "民歌蔡琴",
            durationMs = 225000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 293.66f,
            genre = "HIFI试音",
            bitRate = "24bit/192kHz 9216Kbps",
            lyrics = listOf(
                LyricLine(0, "渡口 (发烧试音典范) - 蔡琴"),
                LyricLine(5000, "前奏鼓点 低频下潜质感纯净"),
                LyricLine(18000, "让我与你握别 再轻轻抽出我的手"),
                LyricLine(28000, "知道思念从此生根 浮云白日 山川庄严温柔"),
                LyricLine(42000, "让我与你握别 再轻轻抽出我的手"),
                LyricLine(55000, "华年从此停顿 热泪在心中汇成河流"),
                LyricLine(70000, "那是怎样的一段往事")
            )
        ),
        Song(
            id = "song_4",
            title = "加州旅馆 (Hotel California 现场版)",
            artist = "老鹰乐队 (Eagles)",
            album = "Hell Freezes Over",
            durationMs = 432000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 329.63f,
            genre = "经典摇滚",
            bitRate = "24bit/192kHz 9216Kbps",
            lyrics = listOf(
                LyricLine(0, "Hotel California (Live Master) - Eagles"),
                LyricLine(8000, "On a dark desert highway, cool wind in my hair"),
                LyricLine(16000, "Warm smell of colitas, rising up through the air"),
                LyricLine(24000, "Up ahead in the distance, I saw a shimmering light"),
                LyricLine(32000, "Welcome to the Hotel California"),
                LyricLine(40000, "Such a lovely place, such a lovely face")
            )
        ),
        Song(
            id = "song_5",
            title = "如愿",
            artist = "王菲",
            album = "我和我的父辈 电影主题曲",
            durationMs = 278000,
            soundQuality = SoundQuality.SQ_LOSSLESS,
            isHiRes = false,
            coverRes = R.drawable.app_icon_art,
            toneFrequency = 523.25f,
            genre = "抒情流行",
            bitRate = "16bit/44.1kHz 1411Kbps",
            lyrics = listOf(
                LyricLine(0, "如愿 - 王菲"),
                LyricLine(6000, "你是 岁月长河 星火燃起的天空"),
                LyricLine(14000, "我是 漫漫长夜 渴望黎明的苍穹"),
                LyricLine(22000, "你看啊 孩童走在田野上"),
                LyricLine(30000, "如果说 你曾苦过我的甜 我愿活成你的愿"),
                LyricLine(39000, "愿不枉啊 这个时代有你有我")
            )
        ),
        Song(
            id = "song_6",
            title = "起风了",
            artist = "买辣椒也用券",
            album = "起风了",
            durationMs = 312000,
            soundQuality = SoundQuality.SQ_LOSSLESS,
            isHiRes = false,
            coverRes = R.drawable.app_icon_art,
            toneFrequency = 493.88f,
            genre = "治愈流行",
            bitRate = "16bit/44.1kHz 1411Kbps",
            lyrics = listOf(
                LyricLine(0, "起风了 - 买辣椒也用券"),
                LyricLine(6000, "这一路上走走停停 顺着少年漂流的痕迹"),
                LyricLine(14000, "迈出车站的前一刻 竟有些犹豫"),
                LyricLine(22000, "不禁笑这近乡情怯 仍无可避免"),
                LyricLine(30000, "我曾难自拔于世界之大 也沉溺于其中梦话"),
                LyricLine(38000, "不得真假 不做挣扎 哪怕受过的伤")
            )
        ),
        Song(
            id = "song_7",
            title = "大鱼",
            artist = "周深",
            album = "大鱼海棠 电影印象曲",
            durationMs = 313000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.home_banner_art,
            toneFrequency = 587.33f,
            genre = "空灵国风",
            bitRate = "24bit/96kHz 4608Kbps",
            lyrics = listOf(
                LyricLine(0, "大鱼 - 周深"),
                LyricLine(7000, "海浪无声将夜幕深深淹没"),
                LyricLine(15000, "漫过天空尽头的角落"),
                LyricLine(24000, "大鱼在梦境的缝隙里游过"),
                LyricLine(32000, "凝望你沉睡的轮廓"),
                LyricLine(40000, "看海天一色 听风起雨落"),
                LyricLine(48000, "执子手吹散苍茫茫烟波"),
                LyricLine(56000, "怕你飞远去 怕你离我而去")
            )
        ),
        Song(
            id = "song_8",
            title = "山丘 (原生黑胶母带)",
            artist = "李宗盛",
            album = "山丘",
            durationMs = 405000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 349.23f,
            genre = "人文民谣",
            bitRate = "24bit/192kHz 9216Kbps",
            lyrics = listOf(
                LyricLine(0, "山丘 - 李宗盛"),
                LyricLine(6000, "想说却还没说的 还很多"),
                LyricLine(15000, "窜到嘴边了 却又忘了"),
                LyricLine(24000, "越过山丘 虽然已白了头"),
                LyricLine(33000, "喋喋不休 动情往往败给时间"),
                LyricLine(42000, "无知的索求 羞耻的退缩")
            )
        )
    )

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
            songIds = listOf("song_1", "song_2", "song_7"),
            isCustom = true,
            coverTag = "私享"
        ),
        Playlist(
            id = "pl_custom_2",
            title = "单曲循环 100 遍",
            description = "百听不厌的经典旋律",
            songIds = listOf("song_2", "song_6"),
            isCustom = true,
            coverTag = "单曲"
        ),
        Playlist(
            id = "pl_fav_1",
            title = "Hi-Res 发烧友试金石",
            description = "母带级高动态音质，殿堂级听觉享受",
            songIds = listOf("song_3", "song_4", "song_8"),
            isCustom = false,
            coverTag = "HIFI"
        ),
        Playlist(
            id = "pl_fav_2",
            title = "华语传唱殿堂金曲榜",
            description = "岁月如歌，记忆中永不褪色的旋律",
            songIds = listOf("song_1", "song_2", "song_5", "song_6"),
            isCustom = false,
            coverTag = "热门"
        )
    )

    val localSongs: List<Song> = listOf(
        Song(
            id = "local_1",
            title = "稻香.flac",
            artist = "周杰伦",
            album = "本地存储/Music",
            durationMs = 223000,
            soundQuality = SoundQuality.SQ_LOSSLESS,
            isHiRes = false,
            toneFrequency = 440f,
            genre = "本地无损",
            lyrics = listOf(
                LyricLine(0, "稻香 - 周杰伦 (本地文件)"),
                LyricLine(5000, "对这个世界如果你有太多的抱怨"),
                LyricLine(12000, "跌倒了 就不敢继续往前走"),
                LyricLine(18000, "为什么 人要这么的脆弱 堕落")
            )
        ),
        Song(
            id = "local_2",
            title = "特别的人.wav",
            artist = "方大同",
            album = "本地存储/Download",
            durationMs = 260000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            toneFrequency = 392f,
            genre = "本地母带",
            lyrics = listOf(
                LyricLine(0, "特别的人 - 方大同 (本地文件)"),
                LyricLine(6000, "爱一个人或许要慷慨"),
                LyricLine(14000, "若只想要被爱 最后没有了对白")
            )
        )
    )
}
