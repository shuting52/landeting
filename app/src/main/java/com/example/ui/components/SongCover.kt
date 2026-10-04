package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.model.Song
import com.example.ui.theme.TextPrimary

/**
 * 动态歌手封面组件：
 * - 网络歌曲带 coverUrl 时，用 Coil 加载真实专辑封面
 * - 本地/无封面歌曲时，根据歌手名+歌名哈希生成专属渐变色封面与首字母
 * 让每首歌/歌手都有独特、可辨识的封面。
 */
@Composable
fun SongCover(
    song: Song,
    size: Int,
    cornerRadius: Int = 8,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(cornerRadius.dp)
    Box(
        modifier = modifier
            .clip(shape)
            .background(
                Brush.linearGradient(
                    colors = gradientFor(song)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        if (!song.coverUrl.isNullOrBlank()) {
            // 网络真实封面
            AsyncImage(
                model = song.coverUrl,
                contentDescription = song.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // 动态生成：歌手/歌名首字母 + 渐变
            val initial = song.artist
                .takeIf { it.isNotBlank() && it != "网络歌手" }
                ?.trim()
                ?.take(1)
                ?.uppercase()
                ?: song.title.take(1).uppercase()
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = androidx.compose.ui.res.painterResource(R.drawable.hifi_vinyl_cover),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.55f))
                )
                androidx.compose.material3.Text(
                    text = initial,
                    color = Color.White,
                    fontSize = (size * 0.32f).sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

/** 根据歌手/歌名生成独特的渐变双色 */
private fun gradientFor(song: Song): List<Color> {
    val seed = kotlin.math.abs((song.artist + song.title).hashCode())
    val palette = listOf(
        0xFF6C5CE7 to 0xFF341F97,
        0xFF00B894 to 0xFF0A3D62,
        0xFFE17055 to 0xFF6D214F,
        0xFF0984E3 to 0xFF130F40,
        0xFFE84393 to 0xFF2C3A47,
        0xFFFDCB6E to 0xFFB33771,
        0xFF00CEC9 to 0xFF1B1464,
        0xFFD63031 to 0xFF2D3436,
        0xFF6D214F to 0xFF130F40,
        0xFF0ABDE3 to 0xFF0C2461,
        0xFFFDA7DF to 0xFF6C5CE7,
        0xFF20BF6B to 0xFF0652DD,
        0xFFFA8231 to 0xFF6D214F,
        0xFF2BCBBA to 0xFF182C61
    )
    val (c1, c2) = palette[seed % palette.size]
    return listOf(Color(c1), Color(c2))
}
