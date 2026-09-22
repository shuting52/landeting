package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SpatialAudio
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.MusicRepository
import com.example.model.Song
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.GoldVip
import com.example.ui.theme.HiResGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun HifiScreen(
    currentPlayingSongId: String?,
    onPlaySong: (Song) -> Unit,
    modifier: Modifier = Modifier
) {
    var spatialAudioActive by remember { mutableStateOf(true) }
    val hifiSongs = MusicRepository.sampleSongs.filter { it.isHiRes }

    val infiniteTransition = rememberInfiniteTransition(label = "hifi_wave")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("hifi_screen")
    ) {
        // HIFI Header Hero
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF281C38),
                                Color(0xFF14121E)
                            )
                        )
                    )
                    .border(1.dp, HiResGold.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.hifi_vinyl_cover),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    alpha = 0.4f
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(HiResGold)
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Hi-Res AUDIO",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.Black
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "黑胶母带专区", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }

                        // Spatial Audio switch
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (spatialAudioActive) MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                                    else Color.White.copy(alpha = 0.1f)
                                )
                                .border(
                                    1.dp,
                                    if (spatialAudioActive) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    RoundedCornerShape(14.dp)
                                )
                                .clickable { spatialAudioActive = !spatialAudioActive }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (spatialAudioActive) "全景声: 开" else "全景声: 关",
                                fontSize = 11.sp,
                                color = if (spatialAudioActive) MaterialTheme.colorScheme.primary else TextSecondary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Column {
                        Text(
                            text = "24bit / 192kHz 极高采样率",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "动态范围超过 120dB · 完美还原录音室母带真实质感",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }

                    // Dynamic wave spectrum indicator
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val heights = listOf(10.dp, 22.dp, 16.dp, 28.dp, 18.dp, 12.dp, 24.dp, 14.dp, 26.dp, 12.dp)
                        heights.forEachIndexed { i, h ->
                            val dynamicH by infiniteTransition.animateFloat(
                                initialValue = 0.3f,
                                targetValue = 1.2f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(350 + i * 50, easing = FastOutSlowInEasing),
                                    repeatMode = RepeatMode.Reverse
                                ),
                                label = "hifi_spec_$i"
                            )
                            Box(
                                modifier = Modifier
                                    .width(3.dp)
                                    .height(h * dynamicH)
                                    .clip(RoundedCornerShape(1.dp))
                                    .background(HiResGold)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "实时 9216Kbps 动态比特流", fontSize = 10.sp, color = HiResGold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Spatial 3D Audio Experience Cards
        item {
            Text(
                text = "空间全景声音效模式",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                HifiFeatureCard(
                    title = "黑胶留声机",
                    desc = "温暖模拟底噪",
                    icon = Icons.Default.Album,
                    modifier = Modifier.weight(1f)
                )
                HifiFeatureCard(
                    title = "杜比空间声",
                    desc = "360°沉浸声场",
                    icon = Icons.Default.Headphones,
                    modifier = Modifier.weight(1f)
                )
                HifiFeatureCard(
                    title = "录音室监听",
                    desc = "零音染真实还原",
                    icon = Icons.Default.Equalizer,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Hi-Res Tracks Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hi-Res 发烧母带必听榜",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "播放全部",
                    fontSize = 13.sp,
                    color = HiResGold,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        hifiSongs.firstOrNull()?.let { onPlaySong(it) }
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Hi-Res Song list
        items(hifiSongs) { song ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(DarkSurface)
                    .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(14.dp))
                    .clickable { onPlaySong(song) }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = song.coverRes ?: R.drawable.hifi_vinyl_cover),
                        contentDescription = song.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = song.title,
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "24bit",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = HiResGold,
                            modifier = Modifier
                                .clip(RoundedCornerShape(3.dp))
                                .background(HiResGold.copy(alpha = 0.15f))
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "${song.artist} · ${song.bitRate}",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(HiResGold.copy(alpha = 0.15f))
                        .clickable { onPlaySong(song) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "播放",
                        tint = HiResGold,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        item { Spacer(modifier = Modifier.height(90.dp)) }
    }
}

@Composable
private fun HifiFeatureCard(
    title: String,
    desc: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurfaceElevated)
            .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = HiResGold,
            modifier = Modifier.size(26.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = desc, fontSize = 10.sp, color = TextSecondary)
    }
}
