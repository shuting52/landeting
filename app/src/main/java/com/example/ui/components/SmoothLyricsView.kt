package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LyricLine
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.HiResGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlin.math.abs

@Composable
fun SmoothLyricsView(
    lyrics: List<LyricLine>,
    currentPositionMs: Long,
    onSeekTo: (Long) -> Unit,
    onOpenLrcImportDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (lyrics.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .testTag("empty_lyrics_view"),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "暂无匹配歌词", color = TextMuted, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(DarkSurfaceElevated)
                        .clickable { onOpenLrcImportDialog() }
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.FileUpload, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "导入 / 解析 .lrc 文件", color = TextPrimary, fontSize = 12.sp)
                }
            }
        }
        return
    }

    // Determine current active lyric index
    val activeLyricIndex = remember(lyrics, currentPositionMs) {
        val index = lyrics.indexOfLast { it.timeMs <= currentPositionMs }
        if (index == -1) 0 else index
    }

    val listState = rememberLazyListState()
    val isDragged by listState.interactionSource.collectIsDraggedAsState()

    // Track user manual scrolling interaction state
    var isUserInteracting by remember { mutableStateOf(false) }

    LaunchedEffect(isDragged) {
        if (isDragged) {
            isUserInteracting = true
        }
    }

    // Auto-reset manual interaction after 3.5 seconds of inactivity
    LaunchedEffect(isUserInteracting, isDragged) {
        if (isUserInteracting && !isDragged) {
            delay(3500)
            isUserInteracting = false
        }
    }

    // Smooth auto-scrolling to keep active line centered
    LaunchedEffect(activeLyricIndex, isUserInteracting) {
        if (!isUserInteracting && lyrics.isNotEmpty() && activeLyricIndex in lyrics.indices) {
            // Center the target line (offset around middle viewport)
            listState.animateScrollToItem(
                index = (activeLyricIndex - 2).coerceAtLeast(0),
                scrollOffset = 0
            )
        }
    }

    // Calculate center visible line index when user manually drags
    val centerVisibleIndex by remember {
        derivedStateOf {
            val visibleItems = listState.layoutInfo.visibleItemsInfo
            if (visibleItems.isEmpty()) activeLyricIndex
            else {
                val centerOffset = listState.layoutInfo.viewportEndOffset / 2
                val itemNearCenter = visibleItems.minByOrNull {
                    abs((it.offset + it.size / 2) - centerOffset)
                }
                itemNearCenter?.index ?: activeLyricIndex
            }
        }
    }

    val hoveredLyric = lyrics.getOrNull(centerVisibleIndex)

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("smooth_lyrics_view")
    ) {
        // Vertical gradient masks at top and bottom for smooth fading
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 140.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(lyrics) { index, lyric ->
                val isActive = index == activeLyricIndex
                val distance = abs(index - activeLyricIndex)

                val targetAlpha = when {
                    isActive -> 1.0f
                    distance == 1 -> 0.70f
                    distance == 2 -> 0.45f
                    distance == 3 -> 0.25f
                    else -> 0.15f
                }

                val animatedAlpha by animateFloatAsState(
                    targetValue = targetAlpha,
                    animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
                    label = "lyric_alpha"
                )

                val animatedColor by animateColorAsState(
                    targetValue = if (isActive) MaterialTheme.colorScheme.primary else TextPrimary,
                    animationSpec = tween(durationMillis = 350),
                    label = "lyric_color"
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onSeekTo(lyric.timeMs) }
                        .padding(horizontal = 24.dp, vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = lyric.text,
                        fontSize = if (isActive) 21.sp else 16.sp,
                        fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.Medium,
                        color = animatedColor,
                        textAlign = TextAlign.Center,
                        lineHeight = if (isActive) 28.sp else 22.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(animatedAlpha)
                    )

                    // Optional translation line if present
                    if (lyric.translation != null) {
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = lyric.translation,
                            fontSize = if (isActive) 14.sp else 12.sp,
                            color = TextSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .alpha(animatedAlpha * 0.8f)
                        )
                    }
                }
            }
        }

        // Top Gradient Fading Edge
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(DarkBackground, Color.Transparent)
                    )
                )
        )

        // Bottom Gradient Fading Edge
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, DarkBackground)
                    )
                )
        )

        // Manual Scroll Seek Target Line & Quick Jump Button (Visible when user scrolls)
        AnimatedVisibility(
            visible = isUserInteracting && hoveredLyric != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Timestamp on Left
                Text(
                    text = formatMs(hoveredLyric?.timeMs ?: 0),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(DarkSurfaceElevated)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )

                // Center dashed guideline
                Divider(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    thickness = 1.dp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                )

                // Seek Jump Play Button on Right
                IconButton(
                    onClick = {
                        hoveredLyric?.let { onSeekTo(it.timeMs) }
                        isUserInteracting = false
                    },
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .testTag("lyric_seek_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "跳转到此处播放",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Top-Right Utility button to import / edit .lrc
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 12.dp, top = 8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black.copy(alpha = 0.45f))
                .clickable { onOpenLrcImportDialog() }
                .padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.FileUpload,
                contentDescription = "导入歌词",
                tint = HiResGold,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "解析 LRC",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = HiResGold
            )
        }
    }
}

private fun formatMs(ms: Long): String {
    val totalSeconds = (ms / 1000).coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
