package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.model.ThemePalette

@Composable
fun LandetingMusicTheme(
    palette: ThemePalette = ThemePalette.RED_BLACK,
    content: @Composable () -> Unit
) {
    val primaryColor = Color(palette.primaryHex)
    val accentColor = Color(palette.accentHex)
    val bgColor = Color(palette.bgDarkHex)

    val colorScheme = darkColorScheme(
        primary = primaryColor,
        secondary = accentColor,
        tertiary = HiResGold,
        background = bgColor,
        surface = DarkSurface,
        surfaceVariant = DarkSurfaceElevated,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = TextPrimary,
        onSurface = TextPrimary,
        onSurfaceVariant = TextSecondary,
        outline = DarkSurfaceBorder
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    LandetingMusicTheme(palette = ThemePalette.RED_BLACK, content = content)
}
