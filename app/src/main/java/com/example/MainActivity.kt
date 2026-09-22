package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.PlayMode
import com.example.ui.components.EqualizerBottomSheet
import com.example.ui.components.FullScreenPlayerSheet
import com.example.ui.components.MiniPlayerBar
import com.example.ui.components.SettingsScreen
import com.example.ui.components.SidebarDrawerContent
import com.example.ui.components.SplashScreen
import com.example.ui.screens.HifiScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MyLibraryScreen
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.GoldVip
import com.example.ui.theme.LandetingMusicTheme
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.MainTab
import com.example.viewmodel.MusicPlayerViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: MusicPlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val currentTheme by viewModel.currentTheme.collectAsStateWithLifecycle()

            LandetingMusicTheme(palette = currentTheme) {
                MusicAppRoot(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MusicAppRoot(viewModel: MusicPlayerViewModel) {
    val showSplash by viewModel.showSplash.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val homeSubTab by viewModel.homeSubTab.collectAsStateWithLifecycle()
    val currentSong by viewModel.currentSong.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
    val currentPositionMs by viewModel.currentPositionMs.collectAsStateWithLifecycle()
    val playMode by viewModel.playMode.collectAsStateWithLifecycle()
    val favoriteSongIds by viewModel.favoriteSongIds.collectAsStateWithLifecycle()
    val recentPlayed by viewModel.recentPlayed.collectAsStateWithLifecycle()
    val localSongs by viewModel.localSongs.collectAsStateWithLifecycle()
    val customPlaylists by viewModel.customPlaylists.collectAsStateWithLifecycle()
    val favoritePlaylists by viewModel.favoritePlaylists.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
    val playbackSettings by viewModel.playbackSettings.collectAsStateWithLifecycle()
    val currentTheme by viewModel.currentTheme.collectAsStateWithLifecycle()
    val equalizerPreset by viewModel.equalizerPreset.collectAsStateWithLifecycle()
    val showEqualizerPanel by viewModel.showEqualizerPanel.collectAsStateWithLifecycle()
    val equalizerState by viewModel.equalizerState.collectAsStateWithLifecycle()
    val customEqPresets by viewModel.customEqPresets.collectAsStateWithLifecycle()
    val playbackSpeed by viewModel.playbackSpeed.collectAsStateWithLifecycle()
    val sleepTimerSecondsLeft by viewModel.sleepTimerSecondsLeft.collectAsStateWithLifecycle()
    val youthModeActive by viewModel.youthModeActive.collectAsStateWithLifecycle()
    val recognitionState by viewModel.recognitionState.collectAsStateWithLifecycle()
    val showFullScreenPlayer by viewModel.showFullScreenPlayer.collectAsStateWithLifecycle()

    var showSettingsScreen by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    if (showSplash) {
        SplashScreen(onDismiss = { viewModel.dismissSplash() })
    } else if (showSettingsScreen) {
        BackHandler { showSettingsScreen = false }
        SettingsScreen(
            settings = playbackSettings,
            currentEqPreset = equalizerState.currentPresetName,
            onOpenEqualizer = { viewModel.openEqualizerPanel() },
            onUpdateSettings = { viewModel.updateSettings(it) },
            onBack = { showSettingsScreen = false }
        )
    } else {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = !showFullScreenPlayer,
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = DarkBackground
                ) {
                    SidebarDrawerContent(
                        currentTheme = currentTheme,
                        sleepTimerSecondsLeft = sleepTimerSecondsLeft,
                        youthModeActive = youthModeActive,
                        recognitionState = recognitionState,
                        onOpenSettings = {
                            scope.launch { drawerState.close() }
                            showSettingsScreen = true
                        },
                        onSetTheme = { viewModel.setTheme(it) },
                        onSetSleepTimer = { viewModel.setSleepTimer(it) },
                        onSetSleepTimerEndOfSong = { viewModel.setSleepTimerEndOfSong() },
                        onToggleYouthMode = { viewModel.toggleYouthMode() },
                        onStartRecognition = { viewModel.startSongRecognition() },
                        onResetRecognition = { viewModel.resetRecognition() },
                        onPlaySong = { viewModel.playSong(it) },
                        onCloseDrawer = { scope.launch { drawerState.close() } }
                    )
                }
            }
        ) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("main_app_scaffold"),
                containerColor = DarkBackground,
                topBar = {
                    // Top App Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar / Drawer Trigger
                        IconButton(
                            onClick = { scope.launch { drawerState.open() } },
                            modifier = Modifier.testTag("open_sidebar_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "打开侧边栏",
                                tint = TextPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        // App Title & Brand
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "懒得听",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(MaterialTheme.colorScheme.primary)
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "MUSIC",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        // Equalizer Quick Shortcut
                        IconButton(
                            onClick = { viewModel.openEqualizerPanel() },
                            modifier = Modifier.testTag("equalizer_shortcut_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "声学均衡器",
                                tint = GoldVip
                            )
                        }

                        // Song Recognition Quick Shortcut
                        IconButton(
                            onClick = {
                                viewModel.startSongRecognition()
                                scope.launch { drawerState.open() }
                            },
                            modifier = Modifier.testTag("recognition_shortcut_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Hearing,
                                contentDescription = "听歌识曲",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                bottomBar = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                    ) {
                        // Bottom Mini Player Bar
                        MiniPlayerBar(
                            song = currentSong,
                            isPlaying = isPlaying,
                            currentPositionMs = currentPositionMs,
                            onTogglePlay = { viewModel.togglePlayPause() },
                            onNext = { viewModel.playNext() },
                            onBarClick = { viewModel.openFullScreenPlayer() }
                        )

                        // Bottom Navigation Bar (首页 / HIFI / 我的)
                        NavigationBar(
                            containerColor = DarkSurface,
                            tonalElevation = 8.dp
                        ) {
                            NavigationBarItem(
                                selected = currentTab == MainTab.HOME,
                                onClick = { viewModel.setTab(MainTab.HOME) },
                                icon = {
                                    Icon(
                                        imageVector = if (currentTab == MainTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                                        contentDescription = "首页"
                                    )
                                },
                                label = { Text("首页", fontWeight = if (currentTab == MainTab.HOME) FontWeight.Bold else FontWeight.Normal) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                    unselectedIconColor = TextSecondary,
                                    unselectedTextColor = TextSecondary
                                ),
                                modifier = Modifier.testTag("tab_home")
                            )

                            NavigationBarItem(
                                selected = currentTab == MainTab.HIFI,
                                onClick = { viewModel.setTab(MainTab.HIFI) },
                                icon = {
                                    Icon(
                                        imageVector = if (currentTab == MainTab.HIFI) Icons.Filled.GraphicEq else Icons.Outlined.GraphicEq,
                                        contentDescription = "HIFI"
                                    )
                                },
                                label = { Text("HIFI", fontWeight = if (currentTab == MainTab.HIFI) FontWeight.Bold else FontWeight.Normal) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = GoldVip,
                                    selectedTextColor = GoldVip,
                                    indicatorColor = GoldVip.copy(alpha = 0.15f),
                                    unselectedIconColor = TextSecondary,
                                    unselectedTextColor = TextSecondary
                                ),
                                modifier = Modifier.testTag("tab_hifi")
                            )

                            NavigationBarItem(
                                selected = currentTab == MainTab.MINE,
                                onClick = { viewModel.setTab(MainTab.MINE) },
                                icon = {
                                    Icon(
                                        imageVector = if (currentTab == MainTab.MINE) Icons.Filled.Person else Icons.Outlined.Person,
                                        contentDescription = "我的"
                                    )
                                },
                                label = { Text("我的", fontWeight = if (currentTab == MainTab.MINE) FontWeight.Bold else FontWeight.Normal) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                    unselectedIconColor = TextSecondary,
                                    unselectedTextColor = TextSecondary
                                ),
                                modifier = Modifier.testTag("tab_mine")
                            )
                        }
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (currentTab) {
                        MainTab.HOME -> HomeScreen(
                            currentSubTab = homeSubTab,
                            searchQuery = searchQuery,
                            searchResults = searchResults,
                            currentPlayingSongId = currentSong?.id,
                            onSelectSubTab = { viewModel.setHomeSubTab(it) },
                            onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                            onPlaySong = { viewModel.playSong(it) }
                        )
                        MainTab.HIFI -> HifiScreen(
                            currentPlayingSongId = currentSong?.id,
                            onPlaySong = { viewModel.playSong(it) }
                        )
                        MainTab.MINE -> MyLibraryScreen(
                            favoriteSongIds = favoriteSongIds,
                            recentPlayed = recentPlayed,
                            localSongs = localSongs,
                            customPlaylists = customPlaylists,
                            favoritePlaylists = favoritePlaylists,
                            currentPlayingSongId = currentSong?.id,
                            onPlaySong = { viewModel.playSong(it) },
                            onCreateCustomPlaylist = { title, desc -> viewModel.createCustomPlaylist(title, desc) },
                            onScanLocalSongs = { viewModel.scanLocalSongs() }
                        )
                    }
                }
            }
        }

        // Full Screen Vinyl Turntable Player
        AnimatedVisibility(
            visible = showFullScreenPlayer,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            BackHandler { viewModel.closeFullScreenPlayer() }
            FullScreenPlayerSheet(
                song = currentSong,
                isPlaying = isPlaying,
                currentPositionMs = currentPositionMs,
                playMode = playMode,
                isFavorite = favoriteSongIds.contains(currentSong?.id ?: ""),
                equalizerPreset = equalizerPreset,
                playbackSpeed = playbackSpeed,
                onClose = { viewModel.closeFullScreenPlayer() },
                onTogglePlay = { viewModel.togglePlayPause() },
                onNext = { viewModel.playNext() },
                onPrevious = { viewModel.playPrevious() },
                onSeekTo = { viewModel.seekTo(it) },
                onTogglePlayMode = { viewModel.togglePlayMode() },
                onToggleFavorite = { currentSong?.id?.let { viewModel.toggleFavorite(it) } },
                onSetEqualizer = { viewModel.setEqualizerPreset(it) },
                onSetSpeed = { viewModel.setPlaybackSpeed(it) },
                onUpdateLyrics = { viewModel.updateCurrentSongLyrics(it) },
                onOpenEqualizer = { viewModel.openEqualizerPanel() }
            )
        }

        // Equalizer Adjustment Bottom Sheet
        if (showEqualizerPanel) {
            EqualizerBottomSheet(
                equalizerState = equalizerState,
                customPresets = customEqPresets,
                onSelectPreset = { viewModel.selectEqualizerPreset(it) },
                onBandGainChange = { index, gain -> viewModel.setEqualizerBandGain(index, gain) },
                onBassBoostChange = { viewModel.setBassBoost(it) },
                onSurround3dChange = { viewModel.setSurround3d(it) },
                onToggleEnable = { viewModel.toggleEqualizer(it) },
                onSaveCustomPreset = { viewModel.saveCustomPreset(it) },
                onReset = { viewModel.resetEqualizer() },
                onDismiss = { viewModel.closeEqualizerPanel() }
            )
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme { Greeting("Android") }
}
