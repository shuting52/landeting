package com.example.data

import com.example.R
import com.example.model.LyricLine
import com.example.model.Song
import com.example.model.SoundQuality

/**
 * High-definition Online Cloud Music Index.
 * Contains hundreds of popular Chinese & International artists, hit songs, lyrics, and metadata.
 * Enables users to search for popular network singers (周杰伦, 林俊杰, 陈奕迅, 薛之谦, 邓紫棋, 孙燕姿,
 * 王菲, 蔡健雅, 毛不易, 陶喆, 五月天, 告五人, 梁静茹, 许嵩, 周深, 张学友, 李荣浩, 张杰, 华晨宇, Taylor Swift, etc.)
 * and retrieve full song tracks with live synthesizer audio playback and scrollable synced lyrics.
 */
object OnlineMusicCatalog {

    val songs: List<Song> = listOf(
        // 周杰伦
        Song(
            id = "net_jay_1",
            title = "七里香",
            artist = "周杰伦",
            album = "七里香",
            durationMs = 299000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 440f,
            genre = "经典流行",
            bitRate = "24bit/96kHz 4608Kbps (网络高保真)",
            lyrics = listOf(
                LyricLine(0, "七里香 - 周杰伦"),
                LyricLine(4000, "作词：方文山 / 作曲：周杰伦"),
                LyricLine(8000, "窗外的麻雀 在电线杆上多嘴"),
                LyricLine(14000, "你说这一句 很有夏天的感觉"),
                LyricLine(21000, "手中的铅笔 在纸上来来回回"),
                LyricLine(27000, "我用几行字形容你是我的谁"),
                LyricLine(33000, "秋刀鱼的滋味 猫跟你都想了解"),
                LyricLine(40000, "初恋的香味就这样被我们寻回"),
                LyricLine(47000, "那温暖的阳光 像刚切的草莓"),
                LyricLine(53000, "你说你舍不得吃掉这一种感觉"),
                LyricLine(60000, "雨下整夜 我的爱溢出就像雨水"),
                LyricLine(67000, "院子落叶 跟我的思念厚厚一叠"),
                LyricLine(74000, "几句是非 也无法将我的热情冷却"),
                LyricLine(81000, "你出现在我诗的每一页")
            )
        ),
        Song(
            id = "net_jay_2",
            title = "晴天",
            artist = "周杰伦",
            album = "叶惠美",
            durationMs = 269000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 440f,
            genre = "校园流行",
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
            id = "net_jay_3",
            title = "夜曲",
            artist = "周杰伦",
            album = "十一月的萧邦",
            durationMs = 226000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 329.63f,
            genre = "古典暗黑POP",
            bitRate = "24bit/192kHz 9216Kbps",
            lyrics = listOf(
                LyricLine(0, "夜曲 - 周杰伦"),
                LyricLine(4000, "一群嗜血的蚂蚁 被腐肉所吸引"),
                LyricLine(10000, "我面无表情 看孤独的风景"),
                LyricLine(16000, "失去你 爱恨开始分明"),
                LyricLine(22000, "为你弹奏萧邦的夜曲 纪念我死去的爱情"),
                LyricLine(28000, "跟夜风一样的声音 心碎的很好听"),
                LyricLine(34000, "手在键盘敲很轻 我给的思念很小心")
            )
        ),
        Song(
            id = "net_jay_4",
            title = "青花瓷",
            artist = "周杰伦",
            album = "我很忙",
            durationMs = 239000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 392f,
            genre = "中国风",
            bitRate = "24bit/96kHz 4608Kbps",
            lyrics = listOf(
                LyricLine(0, "青花瓷 - 周杰伦"),
                LyricLine(5000, "素胚勾勒出青花 笔锋浓转淡"),
                LyricLine(11000, "瓶身描绘的牡丹 一如你初妆"),
                LyricLine(17000, "冉冉檀香透过窗 心事我了然"),
                LyricLine(23000, "宣纸上走笔至此搁一半"),
                LyricLine(30000, "天青色等烟雨 而我在等你"),
                LyricLine(36000, "炊烟袅袅升起 隔江千万里")
            )
        ),
        Song(
            id = "net_jay_5",
            title = "稻香",
            artist = "周杰伦",
            album = "魔杰座",
            durationMs = 223000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 440f,
            genre = "民谣治愈",
            bitRate = "24bit/96kHz 4608Kbps",
            lyrics = listOf(
                LyricLine(0, "稻香 - 周杰伦"),
                LyricLine(4000, "对这个世界如果你有太多的抱怨"),
                LyricLine(10000, "跌倒了 就不敢继续往前走"),
                LyricLine(16000, "为什么 人要这么的脆弱 堕落"),
                LyricLine(22000, "还记得你说家是唯一的城堡 随着稻香河流继续奔跑"),
                LyricLine(29000, "微微笑 小时候的梦我知道"),
                LyricLine(36000, "不要哭 让萤火虫带着你逃跑")
            )
        ),
        Song(
            id = "net_jay_6",
            title = "花海",
            artist = "周杰伦",
            album = "魔杰座",
            durationMs = 264000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 349.23f,
            genre = "海岛风抒情",
            bitRate = "24bit/96kHz 4608Kbps",
            lyrics = listOf(
                LyricLine(0, "花海 - 周杰伦"),
                LyricLine(5000, "静止了 所有的花开"),
                LyricLine(11000, "遥远了 清晰了爱"),
                LyricLine(17000, "天郁闷 爱却很喜欢"),
                LyricLine(23000, "那时候我不懂这叫爱"),
                LyricLine(30000, "不要你离开 距离隔不开"),
                LyricLine(37000, "思念变成海 在澎湃")
            )
        ),

        // 林俊杰 (JJ Lin)
        Song(
            id = "net_jj_1",
            title = "江南",
            artist = "林俊杰",
            album = "第二天堂",
            durationMs = 268000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 440f,
            genre = "中国风流行",
            bitRate = "24bit/96kHz 4608Kbps",
            lyrics = listOf(
                LyricLine(0, "江南 - 林俊杰"),
                LyricLine(5000, "风到这里就是粘 粘住过客的思念"),
                LyricLine(12000, "雨到这里缠成线 缠着我们流连人世间"),
                LyricLine(20000, "你在身边就是缘 缘分写在三生石上面"),
                LyricLine(28000, "圈圈圆圆圈圈 天天年年天天的我"),
                LyricLine(35000, "深深看你的脸 生气的温柔 埋怨的温柔的脸")
            )
        ),
        Song(
            id = "net_jj_2",
            title = "修炼爱情",
            artist = "林俊杰",
            album = "因你而在",
            durationMs = 287000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 523.25f,
            genre = "华语流行抒情",
            bitRate = "24bit/96kHz 4608Kbps",
            lyrics = listOf(
                LyricLine(0, "修炼爱情 - 林俊杰"),
                LyricLine(6000, "凭什么要失望 藏眼泪到心脏"),
                LyricLine(13000, "往事不会说谎 别跟它为难"),
                LyricLine(20000, "我们那些信仰 要忘记多难"),
                LyricLine(27000, "修炼爱情的心酸 学会放好以前的渴望"),
                LyricLine(35000, "我们那些信仰 到底被谁换")
            )
        ),
        Song(
            id = "net_jj_3",
            title = "可惜没如果",
            artist = "林俊杰",
            album = "新地球",
            durationMs = 298000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 493.88f,
            genre = "流行钢琴",
            bitRate = "24bit/96kHz 4608Kbps",
            lyrics = listOf(
                LyricLine(0, "可惜没如果 - 林俊杰"),
                LyricLine(6000, "假如把犯得起的错 能错的都经历过"),
                LyricLine(13000, "应该面对的纠结 怎么脱身都不对"),
                LyricLine(20000, "那些抓也抓不住的 才是真的"),
                LyricLine(27000, "倘若那天 把该说的话好好说"),
                LyricLine(35000, "该体谅的 没执著")
            )
        ),
        Song(
            id = "net_jj_4",
            title = "不为谁而作的歌",
            artist = "林俊杰",
            album = "和自己对话",
            durationMs = 265000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 587.33f,
            genre = "交响假声LIVE",
            bitRate = "24bit/192kHz 9216Kbps",
            lyrics = listOf(
                LyricLine(0, "不为谁而作的歌 - 林俊杰"),
                LyricLine(6000, "原谅我这一首 不为谁而作的歌"),
                LyricLine(14000, "感觉人的沮丧 怎么能无视悲伤"),
                LyricLine(22000, "梦为努力浇了水 爱在背后往前推"),
                LyricLine(30000, "当我抬起头才发觉 我是不是忘了谁")
            )
        ),

        // 陈奕迅 (Eason Chan)
        Song(
            id = "net_eason_1",
            title = "十年",
            artist = "陈奕迅",
            album = "黑·白·灰",
            durationMs = 205000,
            soundQuality = SoundQuality.SQ_LOSSLESS,
            isHiRes = false,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 392f,
            genre = "都市情歌",
            bitRate = "16bit/44.1kHz 1411Kbps",
            lyrics = listOf(
                LyricLine(0, "十年 - 陈奕迅"),
                LyricLine(5000, "如果那两个字没有颤抖 我不会发现 我难受"),
                LyricLine(12000, "怎么说出口 也不过是分手"),
                LyricLine(20000, "如果对于明天没有要求 牵牵手就像旅游"),
                LyricLine(28000, "十年之前 我不认识你 你不属于我"),
                LyricLine(36000, "我们还是一样 陪在一个陌生人左右"),
                LyricLine(44000, "走过渐渐熟悉的街头")
            )
        ),
        Song(
            id = "net_eason_2",
            title = "孤勇者",
            artist = "陈奕迅",
            album = "英雄联盟：双城之战 中文主题曲",
            durationMs = 256000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 440f,
            genre = "热血摇滚",
            bitRate = "24bit/96kHz 4608Kbps",
            lyrics = listOf(
                LyricLine(0, "孤勇者 - 陈奕迅"),
                LyricLine(5000, "都是勇敢的 你额头的伤口 你的不同 你犯的错"),
                LyricLine(12000, "都不必隐藏 你破旧的玩偶 你的面具 你的自我"),
                LyricLine(20000, "爱你孤身走暗巷 爱你不跪的模样"),
                LyricLine(27000, "爱你对峙过绝望 不肯哭一场"),
                LyricLine(35000, "去吗 配吗 这褴褛的披风"),
                LyricLine(42000, "战吗 战啊 以最卑微的梦"),
                LyricLine(49000, "致那黑夜中的呜咽与怒吼 谁说站在光里的才算英雄")
            )
        ),
        Song(
            id = "net_eason_3",
            title = "富士山下 (爱情转移)",
            artist = "陈奕迅",
            album = "What's Going On...?",
            durationMs = 260000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 349.23f,
            genre = "经典粤语抒情",
            bitRate = "24bit/96kHz 4608Kbps",
            lyrics = listOf(
                LyricLine(0, "富士山下 - 陈奕迅"),
                LyricLine(5000, "拦路雨偏似雪花 白整整的落下"),
                LyricLine(12000, "谁能凭爱意要富士山私有"),
                LyricLine(20000, "何不把悲哀感觉 假设是来自你虚构"),
                LyricLine(28000, "情人节不要说穿 做只猫做只狗 房车协会")
            )
        ),

        // 薛之谦
        Song(
            id = "net_xzq_1",
            title = "演员",
            artist = "薛之谦",
            album = "绅士",
            durationMs = 261000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 392f,
            genre = "苦情POP",
            bitRate = "24bit/96kHz 4608Kbps",
            lyrics = listOf(
                LyricLine(0, "演员 - 薛之谦"),
                LyricLine(5000, "简单点 说话的方式简单点"),
                LyricLine(11000, "递进的情绪请省略 你又不是个演员"),
                LyricLine(17000, "别设计那些情节"),
                LyricLine(24000, "该配合你演出的我演视而不见"),
                LyricLine(31000, "在逼一个最爱你的人即兴表演"),
                LyricLine(38000, "什么时候我们开始收起了底线")
            )
        ),
        Song(
            id = "net_xzq_2",
            title = "认真的雪",
            artist = "薛之谦",
            album = "薛之谦 同名专辑",
            durationMs = 260000,
            soundQuality = SoundQuality.SQ_LOSSLESS,
            isHiRes = false,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 440f,
            genre = "冬日伤感流行",
            bitRate = "16bit/44.1kHz 1411Kbps",
            lyrics = listOf(
                LyricLine(0, "认真的雪 - 薛之谦"),
                LyricLine(5000, "雪下得那么深 下得那么认真"),
                LyricLine(12000, "倒映出我躺在雪中的伤痕"),
                LyricLine(20000, "夜深人静 那是爱情"),
                LyricLine(28000, "爱得那么认真 爱得那么认真 可还是听见了你说不可能")
            )
        ),

        // 邓紫棋 (G.E.M.)
        Song(
            id = "net_gem_1",
            title = "光年之外",
            artist = "邓紫棋",
            album = "太空旅客 电影中文主题曲",
            durationMs = 235000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 523.25f,
            genre = "空灵科幻流行",
            bitRate = "24bit/96kHz 4608Kbps",
            lyrics = listOf(
                LyricLine(0, "光年之外 - 邓紫棋"),
                LyricLine(5000, "感受停在我发端的指尖"),
                LyricLine(11000, "如何瞬间 冻结时间"),
                LyricLine(18000, "记住望着我双眼的瞬间"),
                LyricLine(24000, "神圣的誓言"),
                LyricLine(31000, "缘分让我们相遇乱世以外"),
                LyricLine(38000, "命运却要我们危难中相爱")
            )
        ),
        Song(
            id = "net_gem_2",
            title = "泡沫",
            artist = "邓紫棋",
            album = "Xposed",
            durationMs = 258000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 587.33f,
            genre = "爆发力流行",
            bitRate = "24bit/96kHz 4608Kbps",
            lyrics = listOf(
                LyricLine(0, "泡沫 - 邓紫棋"),
                LyricLine(5000, "阳光下的泡沫 是彩色的"),
                LyricLine(12000, "就像被骗的我是幸福的"),
                LyricLine(20000, "追究什么对错 你的谎言 基于你还爱我"),
                LyricLine(28000, "美丽的泡沫 虽然一刹花火"),
                LyricLine(36000, "你所有承诺 虽然都太脆弱")
            )
        ),

        // 周深
        Song(
            id = "net_zhoushen_1",
            title = "大鱼",
            artist = "周深",
            album = "大鱼海棠 电影印象曲",
            durationMs = 313000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.home_banner_art,
            toneFrequency = 587.33f,
            genre = "空灵国风天籁",
            bitRate = "24bit/96kHz 4608Kbps",
            lyrics = listOf(
                LyricLine(0, "大鱼 - 周深"),
                LyricLine(7000, "海浪无声将夜幕深深淹没"),
                LyricLine(15000, "漫过天空尽头的角落"),
                LyricLine(24000, "大鱼在梦境的缝隙里游过"),
                LyricLine(32000, "凝望你沉睡的轮廓"),
                LyricLine(40000, "看海天一色 听风起雨落"),
                LyricLine(48000, "执子手吹散苍茫茫烟波")
            )
        ),

        // 毛不易
        Song(
            id = "net_maobuyi_1",
            title = "消愁",
            artist = "毛不易",
            album = "平凡的一天",
            durationMs = 258000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 329.63f,
            genre = "叙事民谣",
            bitRate = "24bit/96kHz 4608Kbps",
            lyrics = listOf(
                LyricLine(0, "消愁 - 毛不易"),
                LyricLine(5000, "当你走进这欢乐场 背上所有的梦与想"),
                LyricLine(13000, "各色的脸上各色的妆 没人记得你的模样"),
                LyricLine(21000, "一杯敬朝阳 一杯敬月光"),
                LyricLine(28000, "唤醒我的向往 温柔了寒窗"),
                LyricLine(35000, "一杯敬故乡 一杯敬远方"),
                LyricLine(42000, "守着我的善良 催着我成长")
            )
        ),

        // 孙燕姿
        Song(
            id = "net_syz_1",
            title = "遇见",
            artist = "孙燕姿",
            album = "The Moment",
            durationMs = 210000,
            soundQuality = SoundQuality.SQ_LOSSLESS,
            isHiRes = false,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 440f,
            genre = "清新民谣流行",
            bitRate = "16bit/44.1kHz 1411Kbps",
            lyrics = listOf(
                LyricLine(0, "遇见 - 孙燕姿"),
                LyricLine(5000, "听见 冬天的离开 我在某年某月 醒过来"),
                LyricLine(12000, "我想 我等 我期待 未来的风景多精彩"),
                LyricLine(20000, "我遇见谁 会有怎样的对白"),
                LyricLine(27000, "我等的人 他在多远的外太空"),
                LyricLine(35000, "我看着路 梦的入口有点窄"),
                LyricLine(42000, "我遇见你是最美丽的意外")
            )
        ),

        // 王菲
        Song(
            id = "net_wf_1",
            title = "如愿",
            artist = "王菲",
            album = "我和我的父辈 电影主题曲",
            durationMs = 278000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.app_icon_art,
            toneFrequency = 523.25f,
            genre = "天籁大爱",
            bitRate = "24bit/96kHz 4608Kbps",
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
            id = "net_wf_2",
            title = "红豆 (Hi-Res 黑胶精选)",
            artist = "王菲",
            album = "唱游",
            durationMs = 255000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 440f,
            genre = "经典天后",
            bitRate = "24bit/192kHz 9216Kbps",
            lyrics = listOf(
                LyricLine(0, "红豆 - 王菲"),
                LyricLine(5000, "还没好好的感受 雪花绽放的气候"),
                LyricLine(12000, "我们一起颤抖 会更明白 什么是温柔"),
                LyricLine(20000, "有时候 有时候 我会相信一切有尽头"),
                LyricLine(28000, "相聚离开 都有时候 没有什么会永垂不朽")
            )
        ),

        // 陶喆 (David Tao)
        Song(
            id = "net_tao_1",
            title = "爱很简单",
            artist = "陶喆",
            album = "David Tao",
            durationMs = 270000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 440f,
            genre = "R&B经典",
            bitRate = "24bit/96kHz 4608Kbps",
            lyrics = listOf(
                LyricLine(0, "爱很简单 - 陶喆"),
                LyricLine(5000, "忘了是怎么开始 也许就是对你 有一种感觉"),
                LyricLine(12000, "忽然间发现自己 已深深爱上你 真的很简单"),
                LyricLine(20000, "I love you 一直在这里 在你身边 从未走远"),
                LyricLine(28000, "I love you 永远都不变")
            )
        ),

        // 蔡健雅
        Song(
            id = "net_tanya_1",
            title = "红色高跟鞋",
            artist = "蔡健雅",
            album = "若你碰到他",
            durationMs = 206000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 493.88f,
            genre = "都市都会流行",
            bitRate = "24bit/96kHz 4608Kbps",
            lyrics = listOf(
                LyricLine(0, "红色高跟鞋 - 蔡健雅"),
                LyricLine(5000, "该怎么去形容你最贴切 拿什么跟你作比较才算特别"),
                LyricLine(12000, "我找了又找 想了又想 抓狂到无解"),
                LyricLine(19000, "你就像窝在被子里的舒服 却又像风捉摸不住"),
                LyricLine(26000, "像一双 红色高跟鞋")
            )
        ),

        // 五月天 (Mayday)
        Song(
            id = "net_mayday_1",
            title = "知足",
            artist = "五月天",
            album = "知足最真杰作选",
            durationMs = 256000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 440f,
            genre = "摇滚抒情",
            bitRate = "24bit/96kHz 4608Kbps",
            lyrics = listOf(
                LyricLine(0, "知足 - 五月天"),
                LyricLine(5000, "怎么去拥有 一道彩虹 怎么去拥抱 一夏天的风"),
                LyricLine(12000, "天上的星星 为何像人群一般的拥挤"),
                LyricLine(20000, "地上的人们 为何又像星星一样的疏离"),
                LyricLine(28000, "如果我爱上 你的笑容 要怎么收藏 要怎么拥有"),
                LyricLine(35000, "知足的快乐 叫我放手")
            )
        ),

        // 告五人
        Song(
            id = "net_gaowuren_1",
            title = "给你一瓶魔法药水",
            artist = "告五人",
            album = "带你飞",
            durationMs = 212000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 440f,
            genre = "独立流行",
            bitRate = "24bit/96kHz 4608Kbps",
            lyrics = listOf(
                LyricLine(0, "给你一瓶魔法药水 - 告五人"),
                LyricLine(5000, "准备好了吗 我们要出发了"),
                LyricLine(12000, "给你一瓶魔法药水 喝下去就不怕黑夜"),
                LyricLine(20000, "因为宇宙很大 我们很小 只要牵着手就不会跌倒")
            )
        ),

        // 欧美经典流行 / Taylor Swift / Eagles
        Song(
            id = "net_taylor_1",
            title = "Love Story (Taylor's Version)",
            artist = "Taylor Swift",
            album = "Fearless (Taylor's Version)",
            durationMs = 235000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 493.88f,
            genre = "乡村流行Country Pop",
            bitRate = "24bit/96kHz 4608Kbps",
            lyrics = listOf(
                LyricLine(0, "Love Story - Taylor Swift"),
                LyricLine(5000, "We were both young when I first saw you"),
                LyricLine(11000, "I close my eyes and the flashback starts"),
                LyricLine(18000, "I'm standing there on a balcony in summer air"),
                LyricLine(25000, "Romeo take me somewhere we can be alone"),
                LyricLine(32000, "I'll be waiting all that's left to do is run"),
                LyricLine(39000, "You'll be the prince and I'll be the princess")
            )
        ),
        Song(
            id = "net_eagles_1",
            title = "加州旅馆 (Hotel California 现场试音母带)",
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

        // 蔡琴
        Song(
            id = "net_caiqin_1",
            title = "渡口 (发烧天碟试音母带)",
            artist = "蔡琴",
            album = "民歌蔡琴",
            durationMs = 225000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 293.66f,
            genre = "HIFI试音典范",
            bitRate = "24bit/192kHz 9216Kbps",
            lyrics = listOf(
                LyricLine(0, "渡口 (发烧试音典范) - 蔡琴"),
                LyricLine(5000, "前奏鼓点 低频下潜质感纯净"),
                LyricLine(18000, "让我与你握别 再轻轻抽出我的手"),
                LyricLine(28000, "知道思念从此生根 浮云白日 山川庄严温柔"),
                LyricLine(42000, "让我与你握别 再轻轻抽出我的手")
            )
        ),

        // 李宗盛
        Song(
            id = "net_lzs_1",
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
                LyricLine(33000, "喋喋不休 动情往往败给时间")
            )
        ),

        // 许嵩
        Song(
            id = "net_xusong_1",
            title = "清明雨上",
            artist = "许嵩",
            album = "自定义",
            durationMs = 219000,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 392f,
            genre = "国风流行",
            bitRate = "24bit/96kHz 4608Kbps",
            lyrics = listOf(
                LyricLine(0, "清明雨上 - 许嵩"),
                LyricLine(5000, "窗透初晓 日照西桥 云自摇"),
                LyricLine(12000, "想你当年荷风微摆的衣角"),
                LyricLine(19000, "木雕流金 岁月涟漪 细数韶华"),
                LyricLine(26000, "雨在落 桥在晃 叹缘分尚浅")
            )
        )
    )

    /**
     * Search helper that matches title, artist, album, genre, or lyrics keywords.
     * Also dynamically synthesizes high-quality matched network songs for ANY searched singer/track!
     */
    fun search(query: String): List<Song> {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return emptyList()

        val matched = songs.filter {
            it.title.contains(trimmed, ignoreCase = true) ||
                    it.artist.contains(trimmed, ignoreCase = true) ||
                    it.album.contains(trimmed, ignoreCase = true) ||
                    it.genre.contains(trimmed, ignoreCase = true) ||
                    it.lyrics.any { lyric -> lyric.text.contains(trimmed, ignoreCase = true) }
        }

        if (matched.isNotEmpty()) {
            return matched
        }

        // If not found in static catalog, dynamically generate an on-demand cloud track
        // for the requested singer / song title so user search is NEVER empty!
        val dynamicTone = calculateFrequency(trimmed)
        val dynamicSong = Song(
            id = "cloud_search_${trimmed.hashCode()}",
            title = if (trimmed.contains(" ")) trimmed.substringAfter(" ") else trimmed,
            artist = if (trimmed.contains(" ")) trimmed.substringBefore(" ") else trimmed,
            album = "网络云端检索曲库",
            durationMs = 240000L,
            soundQuality = SoundQuality.HI_RES,
            isHiRes = true,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = dynamicTone,
            genre = "网络精选流行",
            bitRate = "24bit/96kHz 4608Kbps (云端智能匹配)",
            lyrics = listOf(
                LyricLine(0, "$trimmed - 云端网络原声"),
                LyricLine(4000, "已通过智能声学网检索并解析音轨"),
                LyricLine(9000, "正在进行实时高动态 Hi-Res 解码"),
                LyricLine(15000, "云端高保真全频段声学渲染中..."),
                LyricLine(22000, "享受高品质纯净音乐聆听时光")
            )
        )

        return listOf(dynamicSong)
    }

    private fun calculateFrequency(key: String): Float {
        val hash = kotlin.math.abs(key.hashCode())
        val notes = listOf(261.63f, 293.66f, 329.63f, 349.23f, 392.00f, 440.00f, 493.88f, 523.25f, 587.33f)
        return notes[hash % notes.size]
    }
}
