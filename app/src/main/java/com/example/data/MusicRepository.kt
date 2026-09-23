package com.example.data

import com.example.model.AudiobookItem
import com.example.model.Playlist
import com.example.model.Song

/**
 * 音乐仓库（纯本地版）
 *
 * 项目不再内置任何歌曲 / 歌单 / 有声书数据。
 * 所有内容均通过 [LocalAudioScanner] 自动识别本地设备音频文件获得，
 * 网络搜索由 [NetworkMusicSearcher] 实时检索云端曲库。
 */
object MusicRepository {

    /** 内置示例歌曲：已全部移除，保持为空 */
    val sampleSongs: List<Song> get() = emptyList()

    /** 内置有声书：已全部移除，保持为空 */
    val audiobooks: List<AudiobookItem> get() = emptyList()

    /** 内置歌单：已全部移除，保持为空 */
    val defaultPlaylists: List<Playlist> get() = emptyList()

    /** 本地歌曲：由 LocalAudioScanner 扫描后填充，这里初始为空 */
    val localSongs: List<Song> get() = emptyList()
}
