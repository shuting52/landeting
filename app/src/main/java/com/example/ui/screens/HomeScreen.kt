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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AudiobookItem
import com.example.model.Song
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.HiResGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.HomeSubTab

@Composable
fun HomeScreen(
    currentSubTab: HomeSubTab,
    searchQuery: String,
    searchResults: List<Song>,
    currentPlayingSongId: String?,
    onSelectSubTab: (HomeSubTab) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onPlaySong: (Song) -> Unit,
    modifier: Modifier = Modifier,
    localSongs: List<Song> = emptyList(),
    isSearching: Boolean = false,
    onScanLocalSongs: (() -> Unit)? = null,
    audiobooks: List<AudiobookItem> = emptyList()
) {
    // 歌曲全部来自本地自动识别扫描结果（项目不再内置任何歌曲）
    val displaySongs = localSongs
    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen")
    ) {
        // Search Bar at Top
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(DarkSurfaceElevated)
                    .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(24.dp))
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = {
                        Text(
                            text = "搜索歌曲 / 歌手 / 专辑 / 听书",
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "搜索",
                            tint = TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "清除",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("song_search_input")
                )
            }
        }

        // If user is searching, show search results view
        if (searchQuery.isNotBlank()) {
            SearchResultsSection(
                query = searchQuery,
                results = searchResults,
                isSearching = isSearching,
                currentPlayingSongId = currentPlayingSongId,
                onPlaySong = onPlaySong
            )
        } else {
            // Category Tabs: 热门 / 发现 / 推荐 / 听书
            val subTabs = HomeSubTab.values()
            ScrollableTabRow(
                selectedTabIndex = currentSubTab.ordinal,
                edgePadding = 16.dp,
                containerColor = Color.Transparent,
                divider = {},
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[currentSubTab.ordinal]),
                        color = MaterialTheme.colorScheme.primary,
                        height = 3.dp
                    )
                }
            ) {
                subTabs.forEach { tab ->
                    Tab(
                        selected = currentSubTab == tab,
                        onClick = { onSelectSubTab(tab) },
                        text = {
                            Text(
                                text = tab.label,
                                fontSize = if (currentSubTab == tab) 16.sp else 14.sp,
                                fontWeight = if (currentSubTab == tab) FontWeight.Bold else FontWeight.Normal,
                                color = if (currentSubTab == tab) TextPrimary else TextSecondary
                            )
                        }
                    )
                }
            }

            // Tab Content
            when (currentSubTab) {
                HomeSubTab.HOT -> HotTabContent(
                    songs = displaySongs,
                    currentPlayingSongId = currentPlayingSongId,
                    onPlaySong = onPlaySong,
                    onScanLocalSongs = onScanLocalSongs
                )
                HomeSubTab.DISCOVER -> DiscoverTabContent(
                    songs = displaySongs,
                    currentPlayingSongId = currentPlayingSongId,
                    onPlaySong = onPlaySong
                )
                HomeSubTab.RECOMMEND -> RecommendTabContent(
                    songs = displaySongs,
                    currentPlayingSongId = currentPlayingSongId,
                    onPlaySong = onPlaySong
                )
                HomeSubTab.AUDIOBOOK -> AudiobookTabContent(
                    audiobooks = audiobooks,
                    onPlayBook = { book ->
                        // Play demo chapter using a sample song structure
                        val song = Song(
                            id = "book_${book.id}",
                            title = book.title,
                            artist = book.narrator,
                            album = book.category,
                            durationMs = 360000,
                            lyrics = listOf(
                                com.example.model.LyricLine(0, "${book.title} - 第 1 回"),
                                com.example.model.LyricLine(4000, "播音：${book.narrator}"),
                                com.example.model.LyricLine(8000, "偷得浮生半日闲，听书好时光...")
                            ),
                            coverRes = R.drawable.home_banner_art,
                            toneFrequency = 330f
                        )
                        onPlaySong(song)
                    }
                )
            }
        }
    }
}

@Composable
private fun SearchResultsSection(
    query: String,
    results: List<Song>,
    isSearching: Boolean,
    currentPlayingSongId: String?,
    onPlaySong: (Song) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("search_results_list")
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "全网 & 本地搜索 “$query” (${results.size})",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
                if (isSearching) {
                    Text(
                        text = "正在联网搜索...",
                        fontSize = 12.sp,
                        color = HiResGold
                    )
                }
            }
        }

        if (results.isEmpty() && !isSearching) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "未找到匹配曲目，尝试搜索其它歌手或关键词", color = TextMuted, fontSize = 14.sp)
                }
            }
        } else {
            items(results) { song ->
                SongRowItem(
                    song = song,
                    isPlaying = song.id == currentPlayingSongId,
                    onClick = { onPlaySong(song) }
                )
            }
        }
    }
}

@Composable
private fun HotTabContent(
    songs: List<Song>,
    currentPlayingSongId: String?,
    onPlaySong: (Song) -> Unit,
    onScanLocalSongs: (() -> Unit)? = null
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Hero Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF221A30))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.home_banner_art),
                    contentDescription = "热门横幅",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color.Black.copy(alpha = 0.75f), Color.Transparent)
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Whatshot,
                            contentDescription = null,
                            tint = Color(0xFFFF453A),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "懒听实时本地热歌 TOP 榜", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "通过本地音频识别引擎自动获取高保真音质", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "热门榜单 TOP 榜", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF2C223E))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = "本地识别", color = HiResGold, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (onScanLocalSongs != null) {
                        Text(
                            text = "识别扫描",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .clickable { onScanLocalSongs() }
                                .padding(end = 12.dp)
                        )
                    }
                    Text(
                        text = "一键播放全部",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable {
                            songs.firstOrNull()?.let { onPlaySong(it) }
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        itemsIndexed(songs) { index, song ->
            SongRankingItem(
                rank = index + 1,
                song = song,
                isPlaying = song.id == currentPlayingSongId,
                onClick = { onPlaySong(song) }
            )
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
private fun DiscoverTabContent(
    songs: List<Song>,
    currentPlayingSongId: String?,
    onPlaySong: (Song) -> Unit
) {
    val genres = listOf("流行流行", "中国风韵", "摇滚狂欢", "电子迷幻", "民谣拾光", "影视原声")
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        item {
            Text(text = "音乐流派雷达", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(genres) { genre ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkSurfaceElevated)
                            .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(12.dp))
                            .clickable {
                                songs.firstOrNull()?.let { onPlaySong(it) }
                            }
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text(text = genre, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Text(text = "最新发掘好歌", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(songs.shuffled()) { song ->
            SongRowItem(
                song = song,
                isPlaying = song.id == currentPlayingSongId,
                onClick = { onPlaySong(song) }
            )
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
private fun RecommendTabContent(
    songs: List<Song>,
    currentPlayingSongId: String?,
    onPlaySong: (Song) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        item {
            // Recommendation Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF381A28),
                                Color(0xFF1F1626)
                            )
                        )
                    )
                    .padding(18.dp)
            ) {
                Column {
                    Text(text = "今日为你推荐 30 首", color = HiResGold, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "根据你的黑胶发烧喜好算法定制", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable {
                                songs.firstOrNull()?.let { onPlaySong(it) }
                            }
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "开始心动漫游", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "推荐歌曲列表", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(songs) { song ->
            SongRowItem(
                song = song,
                isPlaying = song.id == currentPlayingSongId,
                onClick = { onPlaySong(song) }
            )
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
private fun AudiobookTabContent(
    audiobooks: List<AudiobookItem>,
    onPlayBook: (AudiobookItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("audiobook_list")
    ) {
        item {
            Text(text = "听书 · 经典有声剧场", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
        }

        items(audiobooks) { book ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(DarkSurface)
                    .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(14.dp))
                    .clickable { onPlayBook(book) }
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Book,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = book.title,
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "播讲: ${book.narrator} · ${book.category}",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "共 ${book.episodesCount} 集 (${book.updateStatus}) · 时长: ${book.playbackDuration}",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }

                IconButton(onClick = { onPlayBook(book) }) {
                    Icon(
                        imageVector = Icons.Default.Headphones,
                        contentDescription = "收听",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
fun SongRowItem(
    song: Song,
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(DarkSurfaceElevated),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = song.coverRes ?: R.drawable.app_icon_art),
                contentDescription = song.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            if (isPlaying) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = song.title,
                    color = if (isPlaying) MaterialTheme.colorScheme.primary else TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (song.isHiRes) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Hi-Res",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = HiResGold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(HiResGold.copy(alpha = 0.15f))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "${song.artist} · ${song.album}",
                color = TextSecondary,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "播放",
                tint = if (isPlaying) MaterialTheme.colorScheme.primary else TextMuted
            )
        }
    }
}

@Composable
fun SongRankingItem(
    rank: Int,
    song: Song,
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Rank Number
        Text(
            text = rank.toString(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = when (rank) {
                1 -> Color(0xFFFF3B30)
                2 -> Color(0xFFFF9500)
                3 -> HiResGold
                else -> TextMuted
            },
            modifier = Modifier.width(28.dp)
        )

        Image(
            painter = painterResource(id = song.coverRes ?: R.drawable.app_icon_art),
            contentDescription = song.title,
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = song.title,
                    color = if (isPlaying) MaterialTheme.colorScheme.primary else TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (song.isHiRes) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Hi-Res",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = HiResGold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(HiResGold.copy(alpha = 0.15f))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
            }
            Text(
                text = "${song.artist} · ${song.album}",
                color = TextSecondary,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "播放",
                tint = if (isPlaying) MaterialTheme.colorScheme.primary else TextMuted
            )
        }
    }
}
