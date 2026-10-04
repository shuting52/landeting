package com.example.data

import com.example.R
import com.example.model.LyricLine
import com.example.model.Song
import com.example.model.SoundQuality
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.net.URLEncoder
import java.util.concurrent.TimeUnit
import kotlin.random.Random

/**
 * 真实网络音乐搜索器（网易云公开接口）：
 * - 搜索歌曲（type=1）/ 歌手（type=100），支持任意关键词
 * - 返回真实专辑封面 picUrl 与可播放外链 uri
 * - 热门榜单拉取（热歌榜 / 飙升榜）
 * 网络不可用时返回空列表。
 */
object NetworkMusicSearcher {

    private val client = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(8, TimeUnit.SECONDS)
        .build()

    private const val SEARCH_API = "https://music.163.com/api/search/get/web"
    private const val PLAYLIST_API = "https://music.163.com/api/playlist/detail"
    private const val HOT_PLAYLIST_ID = "3778678"   // 网易云热歌榜
    private const val TOP_PLAYLIST_ID = "19723756"  // 飙升榜

    private fun newRequest(url: String): Request =
        Request.Builder()
            .url(url)
            .header("Referer", "https://music.163.com/")
            .header("User-Agent", "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0 Mobile Safari/537.36")
            .build()

    private fun getJson(url: String): JSONObject? = try {
        client.newCall(newRequest(url)).execute().use { resp ->
            if (!resp.isSuccessful) return null
            val body = resp.body?.string() ?: return null
            if (body.isBlank()) null else JSONObject(body)
        }
    } catch (_: Exception) {
        null
    }

    /** 歌曲搜索（网易云 type=1）：返回带真实封面与播放链接的歌曲列表 */
    suspend fun search(query: String): List<Song> = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext emptyList()
        try {
            val encoded = URLEncoder.encode(query.trim(), "UTF-8")
            val json = getJson("$SEARCH_API?csrf_token=&type=1&offset=0&limit=30&s=$encoded")
                ?: return@withContext emptyList()
            val songs = json.optJSONObject("result")?.optJSONArray("songs") ?: return@withContext emptyList()
            val list = mutableListOf<Song>()
            for (i in 0 until songs.length()) {
                try {
                    val item = songs.getJSONObject(i)
                    list.add(parseSong(item))
                } catch (_: Exception) {}
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }

    /** 歌手搜索（网易云 type=100）：返回歌手结果列表（以专辑作品形式展示） */
    suspend fun searchArtists(query: String): List<Song> = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext emptyList()
        try {
            val encoded = URLEncoder.encode(query.trim(), "UTF-8")
            val json = getJson("$SEARCH_API?csrf_token=&type=100&offset=0&limit=30&s=$encoded")
                ?: return@withContext emptyList()
            val artists = json.optJSONObject("result")?.optJSONArray("artists") ?: return@withContext emptyList()
            val list = mutableListOf<Song>()
            for (i in 0 until artists.length()) {
                try {
                    val item = artists.getJSONObject(i)
                    val name = item.optString("name").ifBlank { continue }
                    val id = item.optLong("id")
                    val picUrl = item.optString("picUrl").ifBlank {
                        item.optJSONObject("cover")?.optString("url") ?: ""
                    }
                    val alias = item.optJSONArray("alias")?.optString(0) ?: ""
                    val hotSongs = item.optInt("albumSize", 0)
                    list.add(
                        Song(
                            id = "net_artist_$id",
                            title = name,
                            artist = if (alias.isNotBlank()) alias else "歌手 · ${hotSongs}张专辑",
                            album = "歌手主页",
                            durationMs = 0L,
                            soundQuality = SoundQuality.HIGH,
                            isHiRes = false,
                            coverRes = R.drawable.hifi_vinyl_cover,
                            toneFrequency = 330f,
                            genre = "网络歌手",
                            bitRate = "歌手 · 曲库直达",
                            lyrics = listOf(LyricLine(0, name)),
                            coverUrl = picUrl.ifBlank { null },
                            uri = null
                        )
                    )
                } catch (_: Exception) {}
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }

    /** 热歌榜 / 飙升榜：返回榜单歌曲（带真实封面与播放链接） */
    suspend fun fetchHotSongs(): List<Song> = withContext(Dispatchers.IO) {
        try {
            val json = getJson("$PLAYLIST_API?id=$HOT_PLAYLIST_ID") ?: return@withContext emptyList()
            val tracks = json.optJSONObject("result")?.optJSONArray("tracks") ?: return@withContext emptyList()
            val list = mutableListOf<Song>()
            for (i in 0 until tracks.length().coerceAtMost(30)) {
                try {
                    list.add(parseSong(tracks.getJSONObject(i)))
                } catch (_: Exception) {}
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }

    /** 解析单曲 JSON → Song（补充真实封面与播放外链） */
    private fun parseSong(item: JSONObject): Song {
        val id = item.optLong("id")
        val title = item.optString("name").ifBlank { "未知歌曲" }
        val artist = buildString {
            val artists = item.optJSONArray("artists")
            if (artists != null && artists.length() > 0) {
                for (a in 0 until artists.length().coerceAtMost(3)) {
                    val name = artists.getJSONObject(a).optString("name")
                    if (name.isNotBlank()) {
                        if (isNotEmpty()) append(" / ")
                        append(name)
                    }
                }
            }
            if (isEmpty()) append("网络歌手")
        }
        val albumObj = item.optJSONObject("album")
        val album = albumObj?.optString("name")?.ifBlank { null } ?: "网络曲库"
        // 真实专辑封面（网易云 picUrl 可加 ?param=300x300 调整尺寸）
        val picUrl = albumObj?.optString("picUrl")?.ifBlank { null }
        val coverUrl = picUrl?.let {
            if (it.contains("?")) "${it.split("?")[0]}?param=300x300" else "$it?param=300x300"
        }
        val durationMs = item.optLong("duration", 0L)
        val fee = item.optInt("fee", 0)
        // 官方播放外链（可直接流式播放）
        val playUrl = "https://music.163.com/song/media/outer/url?id=$id.mp3"

        return Song(
            id = "net_online_$id",
            title = title,
            artist = artist,
            album = album,
            durationMs = if (durationMs > 0) durationMs else 240000L,
            soundQuality = SoundQuality.HIGH,
            isHiRes = false,
            coverRes = R.drawable.hifi_vinyl_cover,
            toneFrequency = 440f,
            genre = if (fee != 0) "网络在线 · VIP" else "网络在线",
            bitRate = "320Kbps 云端音源",
            lyrics = listOf(
                LyricLine(0, "$title - $artist"),
                LyricLine(3000, "【网络实时搜索】已通过云端曲库检索到该歌曲"),
                LyricLine(9000, "正在加载高保真在线音源…")
            ),
            coverUrl = coverUrl,
            uri = playUrl
        )
    }

    /** 生成歌手专属动态封面配色（0xFFRRGGBB）—— 供本地歌曲使用 */
    fun coverColorFor(name: String): Long {
        val palettes = listOf(
            0xFF6C5CE7, 0xFF00B894, 0xFFE17055, 0xFF0984E3, 0xFFE84393,
            0xFFFDCB6E, 0xFF00CEC9, 0xFFD63031, 0xFF6D214F, 0xFFB33771,
            0xFF0ABDE3, 0xFFFDA7DF, 0xFF20BF6B, 0xFFFA8231, 0xFF2BCBBA
        )
        val hash = kotlin.math.abs(name.hashCode())
        return palettes[hash % palettes.size]
    }

    /** 歌手热歌快速检索：以歌手名+热门关键词做一次歌曲搜索 */
    suspend fun searchSongsByArtist(artist: String): List<Song> = search("$artist 热门歌曲")
}
