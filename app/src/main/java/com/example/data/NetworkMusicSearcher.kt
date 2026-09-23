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

/**
 * 真实网络音乐搜索器：
 * 调用网易云音乐公开搜索接口（music.163.com），实时检索全网曲库，
 * 支持任意歌手 / 歌曲名 / 专辑关键词。网络不可用时返回空列表，
 * 由上层回退到内置曲库（OnlineMusicCatalog）。
 */
object NetworkMusicSearcher {

    private val client = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(6, TimeUnit.SECONDS)
        .build()

    private const val SEARCH_API =
        "https://music.163.com/api/search/get/web?csrf_token=&type=1&offset=0&limit=20&s="

    /** 网络搜索歌曲；任何异常 / 无结果均返回空列表 */
    suspend fun search(query: String): List<Song> = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext emptyList()
        try {
            val encoded = URLEncoder.encode(query.trim(), "UTF-8")
            val request = Request.Builder()
                .url(SEARCH_API + encoded)
                .header("Referer", "https://music.163.com/")
                .header(
                    "User-Agent",
                    "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 " +
                            "(KHTML, like Gecko) Chrome/120.0 Mobile Safari/537.36"
                )
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext emptyList()
                val body = response.body?.string() ?: return@withContext emptyList()
                parseSearchResponse(body)
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun parseSearchResponse(json: String): List<Song> {
        val root = JSONObject(json)
        val result = root.optJSONObject("result") ?: return emptyList()
        val songs = result.optJSONArray("songs") ?: return emptyList()

        val list = mutableListOf<Song>()
        for (i in 0 until songs.length()) {
            try {
                val item = songs.getJSONObject(i)
                val id = item.optLong("id")
                val title = item.optString("name").ifBlank { continue }

                val artist = buildString {
                    val artists = item.optJSONArray("artists")
                    if (artists != null && artists.length() > 0) {
                        for (a in 0 until artists.length().coerceAtMost(2)) {
                            val name = artists.getJSONObject(a).optString("name")
                            if (name.isNotBlank()) {
                                if (isNotEmpty()) append(" / ")
                                append(name)
                            }
                        }
                    }
                    if (isEmpty()) append("网络歌手")
                }

                val album = item.optJSONObject("album")?.optString("name")?.ifBlank { null } ?: "网络曲库"
                val durationMs = item.optLong("duration", 0L)
                val fee = item.optInt("fee", 0)

                list.add(
                    Song(
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
                        )
                    )
                )
            } catch (_: Exception) {
                // 单条解析失败跳过
            }
        }
        return list
    }
}
