package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.PlayMode
import com.example.model.Song
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.GoldVip
import com.example.ui.theme.HiResGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenPlayerSheet(
    song: Song?,
    isPlaying: Boolean,
    currentPositionMs: Long,
    playMode: PlayMode,
    isFavorite: Boolean,
    equalizerPreset: String,
    playbackSpeed: Float,
    onClose: () -> Unit,
    onTogglePlay: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onSeekTo: (Long) -> Unit,
    onTogglePlayMode: () -> Unit,
    onToggleFavorite: () -> Unit,
    onSetEqualizer: (String) -> Unit,
    onSetSpeed: (Float) -> Unit,
    onUpdateLyrics: ((List<com.example.model.LyricLine>) -> Unit)? = null,
    onOpenEqualizer: (() -> Unit)? = null
) {
    if (song == null) return

    var showLyricsView by remember { mutableStateOf(false) }
    var showEqDialog by remember { mutableStateOf(false) }
    var showSpeedDialog by remember { mutableStateOf(false) }
    var showLrcImportDialog by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "player_disc_spin")
    val discRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 16000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "disc_spin_angle"
    )

    // Needle arm angle
    val needleAngle by animateFloatAsState(
        targetValue = if (isPlaying && !showLyricsView) 25f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "needle_angle"
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("full_screen_player"),
        color = DarkBackground
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF20182C),
                            DarkBackground,
                            Color(0xFF0C0B12)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Header Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.testTag("close_player_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "关闭",
                            tint = TextPrimary
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = song.title,
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = song.artist,
                            color = TextSecondary,
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    IconButton(
                        onClick = { showLyricsView = !showLyricsView },
                        modifier = Modifier.testTag("toggle_lyrics_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = "切换歌词/黑胶",
                            tint = if (showLyricsView) MaterialTheme.colorScheme.primary else TextSecondary
                        )
                    }
                }

                // Audio Quality Badge Bar
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.08f))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (song.isHiRes) "Hi-Res 无损母带" else "SQ 超品质",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (song.isHiRes) HiResGold else MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = song.bitRate,
                        fontSize = 10.sp,
                        color = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Center Stage: Vinyl Turntable OR Synchronized Smooth Lyrics
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (!showLyricsView) {
                        // Vinyl Record Disc View
                        Box(
                            modifier = Modifier
                                .size(280.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            Color(0xFF2A2A34),
                                            Color(0xFF14141A),
                                            Color(0xFF08080C)
                                        )
                                    )
                                )
                                .border(4.dp, Color.White.copy(alpha = 0.12f), CircleShape)
                                .clickable { showLyricsView = true },
                            contentAlignment = Alignment.Center
                        ) {
                            // Circular Vinyl concentric lines
                            for (r in listOf(240, 200, 160)) {
                                Box(
                                    modifier = Modifier
                                        .size(r.dp)
                                        .border(1.dp, Color.White.copy(alpha = 0.04f), CircleShape)
                                )
                            }

                            // Rotating Center Album Art
                            Image(
                                painter = painterResource(id = song.coverRes ?: R.drawable.hifi_vinyl_cover),
                                contentDescription = song.title,
                                modifier = Modifier
                                    .size(150.dp)
                                    .clip(CircleShape)
                                    .rotate(if (isPlaying) discRotation else 0f),
                                contentScale = ContentScale.Crop
                            )

                            // Center Spindle
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black)
                                    .border(2.dp, GoldVip, CircleShape)
                            )
                        }

                        // Tonearm / Stylus indicator
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(end = 30.dp, top = 0.dp)
                                .rotate(needleAngle)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(Color.LightGray)
                                )
                                Box(
                                    modifier = Modifier
                                        .width(3.dp)
                                        .height(70.dp)
                                        .background(Color.Gray)
                                )
                                Box(
                                    modifier = Modifier
                                        .width(10.dp)
                                        .height(14.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(MaterialTheme.colorScheme.primary)
                                )
                            }
                        }
                    } else {
                        // Real-time Synchronized Smooth Scrolling Lyrics View
                        SmoothLyricsView(
                            lyrics = song.lyrics,
                            currentPositionMs = currentPositionMs,
                            onSeekTo = onSeekTo,
                            onOpenLrcImportDialog = { showLrcImportDialog = true }
                        )
                    }
                }

                // Sub-controls (Equalizer, Speed, Favorites)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onToggleFavorite) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "收藏",
                            tint = if (isFavorite) Color.Red else TextSecondary
                        )
                    }

                    // Equalizer Button
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.08f))
                            .clickable { onOpenEqualizer?.invoke() ?: run { showEqDialog = true } }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Equalizer,
                            contentDescription = "音效",
                            tint = TextPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = equalizerPreset,
                            fontSize = 12.sp,
                            color = TextPrimary
                        )
                    }

                    // Playback Speed Button
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.08f))
                            .clickable { showSpeedDialog = true }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = "倍速",
                            tint = TextPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${playbackSpeed}x",
                            fontSize = 12.sp,
                            color = TextPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Progress Bar & Timestamp
                val currentSec = currentPositionMs / 1000
                val totalSec = song.durationMs / 1000
                val currentFormatted = String.format(Locale.getDefault(), "%02d:%02d", currentSec / 60, currentSec % 60)
                val totalFormatted = String.format(Locale.getDefault(), "%02d:%02d", totalSec / 60, totalSec % 60)

                Column(modifier = Modifier.fillMaxWidth()) {
                    Slider(
                        value = (currentPositionMs.toFloat() / song.durationMs.coerceAtLeast(1L)).coerceIn(0f, 1f),
                        onValueChange = { percent ->
                            onSeekTo((percent * song.durationMs).toLong())
                        },
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary,
                            inactiveTrackColor = Color.White.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("player_progress_slider")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = currentFormatted, fontSize = 12.sp, color = TextMuted)
                        Text(text = totalFormatted, fontSize = 12.sp, color = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Playback Control Buttons (Mode, Prev, Play/Pause, Next, List)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onTogglePlayMode,
                        modifier = Modifier.testTag("toggle_play_mode_button")
                    ) {
                        Icon(
                            imageVector = when (playMode) {
                                PlayMode.SEQUENCE -> Icons.Default.Repeat
                                PlayMode.REPEAT_ONE -> Icons.Default.RepeatOne
                                PlayMode.SHUFFLE -> Icons.Default.Shuffle
                            },
                            contentDescription = playMode.label,
                            tint = TextSecondary,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    IconButton(
                        onClick = onPrevious,
                        modifier = Modifier.testTag("player_prev_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipPrevious,
                            contentDescription = "上一首",
                            tint = TextPrimary,
                            modifier = Modifier.size(38.dp)
                        )
                    }

                    // Big Round Play / Pause Button
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.secondary
                                    )
                                )
                            )
                            .clickable { onTogglePlay() }
                            .testTag("player_play_pause_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "暂停" else "播放",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    IconButton(
                        onClick = onNext,
                        modifier = Modifier.testTag("player_next_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "下一首",
                            tint = TextPrimary,
                            modifier = Modifier.size(38.dp)
                        )
                    }

                    IconButton(onClick = { showLyricsView = !showLyricsView }) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = "音效",
                            tint = TextSecondary,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }
        }
    }

    // EQ Preset Dialog / BottomSheet
    if (showEqDialog) {
        ModalBottomSheet(
            onDismissRequest = { showEqDialog = false },
            sheetState = rememberModalBottomSheetState(),
            containerColor = DarkSurfaceElevated
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "HIFI 音效均衡器",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(14.dp))
                val presets = listOf("原声HIFI", "流行人声", "超重低音", "清亮高音", "空间环绕全景声", "发烧黑胶")
                presets.forEach { preset ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                onSetEqualizer(preset)
                                showEqDialog = false
                            }
                            .padding(vertical = 12.dp, horizontal = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = preset,
                            color = if (preset == equalizerPreset) MaterialTheme.colorScheme.primary else TextPrimary,
                            fontWeight = if (preset == equalizerPreset) FontWeight.Bold else FontWeight.Normal
                        )
                        if (preset == equalizerPreset) {
                            Icon(
                                imageVector = Icons.Default.Equalizer,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }

    // Playback Speed Dialog
    if (showSpeedDialog) {
        ModalBottomSheet(
            onDismissRequest = { showSpeedDialog = false },
            sheetState = rememberModalBottomSheetState(),
            containerColor = DarkSurfaceElevated
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "播放倍速",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(14.dp))
                val speeds = listOf(0.75f, 1.0f, 1.25f, 1.5f, 2.0f)
                speeds.forEach { speed ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                onSetSpeed(speed)
                                showSpeedDialog = false
                            }
                            .padding(vertical = 12.dp, horizontal = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${speed}x",
                            color = if (speed == playbackSpeed) MaterialTheme.colorScheme.primary else TextPrimary,
                            fontWeight = if (speed == playbackSpeed) FontWeight.Bold else FontWeight.Normal
                        )
                        if (speed == playbackSpeed) {
                            Text(
                                text = "当前生效",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }

    // LRC Lyrics Parser & Import Dialog
    if (showLrcImportDialog) {
        LrcImportDialog(
            onDismiss = { showLrcImportDialog = false },
            onApplyLyrics = { newLyrics ->
                onUpdateLyrics?.invoke(newLyrics)
            }
        )
    }
}
