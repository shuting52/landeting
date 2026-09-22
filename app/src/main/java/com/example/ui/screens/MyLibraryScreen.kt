package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.MusicRepository
import com.example.model.Playlist
import com.example.model.Song
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.GoldVip
import com.example.ui.theme.HiResGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

enum class LibrarySubView(val label: String) {
    FAVORITES("收藏"),
    LOCAL("本地"),
    RECENT("最近播放"),
    CUSTOM_PLAYLISTS("自建歌单"),
    FAVORITE_PLAYLISTS("收藏歌单")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyLibraryScreen(
    favoriteSongIds: Set<String>,
    recentPlayed: List<Song>,
    localSongs: List<Song>,
    customPlaylists: List<Playlist>,
    favoritePlaylists: List<Playlist>,
    currentPlayingSongId: String?,
    onPlaySong: (Song) -> Unit,
    onCreateCustomPlaylist: (String, String) -> Unit,
    onScanLocalSongs: () -> Unit,
    modifier: Modifier = Modifier
) {
    var activeSubView by remember { mutableStateOf(LibrarySubView.FAVORITES) }
    var showCreatePlaylistDialog by remember { mutableStateOf(false) }
    var selectedPlaylistForDetail by remember { mutableStateOf<Playlist?>(null) }

    val allSongsMap = remember {
        (MusicRepository.sampleSongs + MusicRepository.localSongs).associateBy { it.id }
    }

    val favoriteSongs = favoriteSongIds.mapNotNull { allSongsMap[it] }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("my_library_screen")
    ) {
        // User Profile Summary Card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_icon_art),
                contentDescription = "用户头像",
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .border(2.dp, GoldVip, CircleShape)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "懒听发烧友", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(GoldVip)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = "黑胶 VIP", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "听你想听 · 偷得浮生半日闲", fontSize = 12.sp, color = TextSecondary)
            }
        }

        // 5 Required Sub-view Tabs: 收藏 / 本地 / 最近播放 / 自建歌单 / 收藏歌单
        val subViews = LibrarySubView.values()
        ScrollableTabRow(
            selectedTabIndex = activeSubView.ordinal,
            edgePadding = 16.dp,
            containerColor = Color.Transparent,
            divider = {},
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[activeSubView.ordinal]),
                    color = MaterialTheme.colorScheme.primary,
                    height = 3.dp
                )
            }
        ) {
            subViews.forEach { view ->
                Tab(
                    selected = activeSubView == view,
                    onClick = { activeSubView = view },
                    text = {
                        Text(
                            text = view.label,
                            fontSize = if (activeSubView == view) 15.sp else 13.sp,
                            fontWeight = if (activeSubView == view) FontWeight.Bold else FontWeight.Normal,
                            color = if (activeSubView == view) TextPrimary else TextSecondary
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Main Tab Content
        when (activeSubView) {
            // 1. 收藏
            LibrarySubView.FAVORITES -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .testTag("favorites_list")
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "我喜欢的音乐 (${favoriteSongs.size})",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            if (favoriteSongs.isNotEmpty()) {
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(MaterialTheme.colorScheme.primary)
                                        .clickable { onPlaySong(favoriteSongs.first()) }
                                        .padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "播放全部", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    if (favoriteSongs.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "暂无收藏歌曲，在播放界面点击红心即可加入收藏", color = TextMuted, fontSize = 13.sp)
                            }
                        }
                    } else {
                        items(favoriteSongs) { song ->
                            SongRowItem(
                                song = song,
                                isPlaying = song.id == currentPlayingSongId,
                                onClick = { onPlaySong(song) }
                            )
                        }
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }

            // 2. 本地
            LibrarySubView.LOCAL -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .testTag("local_music_list")
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "本地歌曲 (${localSongs.size} 首)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(DarkSurfaceElevated)
                                    .clickable { onScanLocalSongs() }
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.Default.Sync, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "一键扫描本地", color = TextPrimary, fontSize = 12.sp)
                            }
                        }
                    }

                    items(localSongs) { song ->
                        SongRowItem(
                            song = song,
                            isPlaying = song.id == currentPlayingSongId,
                            onClick = { onPlaySong(song) }
                        )
                    }

                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }

            // 3. 最近播放
            LibrarySubView.RECENT -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .testTag("recent_played_list")
                ) {
                    item {
                        Text(
                            text = "最近播放曲目 (${recentPlayed.size})",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }

                    items(recentPlayed) { song ->
                        SongRowItem(
                            song = song,
                            isPlaying = song.id == currentPlayingSongId,
                            onClick = { onPlaySong(song) }
                        )
                    }

                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }

            // 4. 自建歌单
            LibrarySubView.CUSTOM_PLAYLISTS -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .testTag("custom_playlists_list")
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "我创建的歌单 (${customPlaylists.size})",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(MaterialTheme.colorScheme.primary)
                                    .clickable { showCreatePlaylistDialog = true }
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "新建歌单", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    items(customPlaylists) { pl ->
                        PlaylistItemRow(
                            playlist = pl,
                            onClick = { selectedPlaylistForDetail = pl }
                        )
                    }

                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }

            // 5. 收藏歌单
            LibrarySubView.FAVORITE_PLAYLISTS -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .testTag("favorite_playlists_list")
                ) {
                    item {
                        Text(
                            text = "收藏的精选歌单 (${favoritePlaylists.size})",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }

                    items(favoritePlaylists) { pl ->
                        PlaylistItemRow(
                            playlist = pl,
                            onClick = { selectedPlaylistForDetail = pl }
                        )
                    }

                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }

    // Dialog: Create custom playlist
    if (showCreatePlaylistDialog) {
        var playlistTitle by remember { mutableStateOf("") }
        var playlistDesc by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showCreatePlaylistDialog = false },
            containerColor = DarkSurfaceElevated,
            title = {
                Text(text = "新建自建歌单", color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = playlistTitle,
                        onValueChange = { playlistTitle = it },
                        label = { Text("歌单名称") },
                        placeholder = { Text("例如：深夜学习白噪音") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    OutlinedTextField(
                        value = playlistDesc,
                        onValueChange = { playlistDesc = it },
                        label = { Text("歌单描述") },
                        placeholder = { Text("记录这份好心情...") },
                        maxLines = 2,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (playlistTitle.isNotBlank()) {
                        onCreateCustomPlaylist(playlistTitle, playlistDesc)
                        showCreatePlaylistDialog = false
                    }
                }) {
                    Text("创建", color = MaterialTheme.colorScheme.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreatePlaylistDialog = false }) {
                    Text("取消", color = TextSecondary)
                }
            }
        )
    }

    // Modal BottomSheet for playlist details
    if (selectedPlaylistForDetail != null) {
        val pl = selectedPlaylistForDetail!!
        val plSongs = pl.songIds.mapNotNull { allSongsMap[it] }

        ModalBottomSheet(
            onDismissRequest = { selectedPlaylistForDetail = null },
            sheetState = rememberModalBottomSheetState(),
            containerColor = DarkSurfaceElevated
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = pl.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(text = pl.description, fontSize = 12.sp, color = TextSecondary)
                    }
                    if (plSongs.isNotEmpty()) {
                        Button(
                            onClick = {
                                onPlaySong(plSongs.first())
                                selectedPlaylistForDetail = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("全部播放")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = DarkSurfaceBorder)

                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(plSongs) { song ->
                        SongRowItem(
                            song = song,
                            isPlaying = song.id == currentPlayingSongId,
                            onClick = {
                                onPlaySong(song)
                                selectedPlaylistForDetail = null
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PlaylistItemRow(
    playlist: Playlist,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurface)
            .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF2B2538)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.QueueMusic,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(26.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = playlist.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "${playlist.songIds.size} 首歌曲 · ${playlist.description}",
                fontSize = 12.sp,
                color = TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color.White.copy(alpha = 0.08f))
                .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Text(text = playlist.coverTag, fontSize = 11.sp, color = TextSecondary)
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}
