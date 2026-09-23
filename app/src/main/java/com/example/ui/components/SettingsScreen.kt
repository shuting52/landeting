package com.example.ui.components

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PlaybackSettings
import com.example.model.SoundQuality
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.HiResGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settings: PlaybackSettings,
    currentEqPreset: String = "原声HIFI",
    currentVersion: String = "1.0.1",
    onOpenEqualizer: (() -> Unit)? = null,
    onUpdateSettings: ((PlaybackSettings) -> PlaybackSettings) -> Unit,
    onCheckUpdate: (() -> Unit)? = null,
    onBack: () -> Unit
) {
    var showQualityDialog by remember { mutableStateOf(false) }
    var isEditingDownloadQuality by remember { mutableStateOf(false) }
    var showVideoQualityDialog by remember { mutableStateOf(false) }
    var showDownloadPathDialog by remember { mutableStateOf(false) }
    var showLyricSettingDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("settings_screen"),
        color = DarkBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Header Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("settings_back_button")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "返回",
                        tint = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "系统与播放设置",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Section: 播放与下载 (Playback & Download - Required Category)
                Text(
                    text = "播放与下载",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp, top = 8.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(DarkSurface)
                        .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(16.dp))
                ) {
                    // 1. 在线播放音质
                    SettingClickItem(
                        icon = Icons.Default.HighQuality,
                        title = "在线播放音质",
                        subtitle = settings.onlineQuality.title + " · " + settings.onlineQuality.desc,
                        badge = settings.onlineQuality.tag,
                        onClick = {
                            isEditingDownloadQuality = false
                            showQualityDialog = true
                        }
                    )
                    Divider(color = DarkSurfaceBorder, thickness = 0.5.dp)

                    // 1.5 均衡器与场景音效 (EQ)
                    SettingClickItem(
                        icon = Icons.Default.Equalizer,
                        title = "均衡器与场景音效 (EQ)",
                        subtitle = "5频段高精度频响调节 · 流行/古典/摇滚/自定义预设",
                        badge = currentEqPreset,
                        onClick = { onOpenEqualizer?.invoke() }
                    )
                    Divider(color = DarkSurfaceBorder, thickness = 0.5.dp)

                    // 2. 播放设置
                    SettingSwitchItem(
                        icon = Icons.Default.MusicNote,
                        title = "播放设置 · 歌曲淡入淡出",
                        subtitle = "切歌与暂停时智能平滑过渡，保护听力",
                        checked = settings.playFadeInOut,
                        onCheckedChange = { checked ->
                            onUpdateSettings { it.copy(playFadeInOut = checked) }
                        }
                    )
                    Divider(color = DarkSurfaceBorder, thickness = 0.5.dp)

                    SettingSwitchItem(
                        icon = Icons.Default.MusicNote,
                        title = "智能音量均衡与拔出暂停",
                        subtitle = "统一音量响度，耳机拔出时自动暂停播放",
                        checked = settings.smartVolumeBalance,
                        onCheckedChange = { checked ->
                            onUpdateSettings { it.copy(smartVolumeBalance = checked, unplugPause = checked) }
                        }
                    )
                    Divider(color = DarkSurfaceBorder, thickness = 0.5.dp)

                    // 3. 边听边存
                    SettingSwitchItem(
                        icon = Icons.Default.Save,
                        title = "边听边存",
                        subtitle = "在线听过的歌曲自动缓存到本地，无网也能顺畅听 (上限 ${settings.cacheLimitMb}MB)",
                        checked = settings.cacheWhileListening,
                        onCheckedChange = { checked ->
                            onUpdateSettings { it.copy(cacheWhileListening = checked) }
                        }
                    )
                    Divider(color = DarkSurfaceBorder, thickness = 0.5.dp)

                    // 4. 歌曲下载目录
                    SettingClickItem(
                        icon = Icons.Default.Folder,
                        title = "歌曲下载目录",
                        subtitle = settings.downloadDirectory,
                        onClick = { showDownloadPathDialog = true }
                    )
                    Divider(color = DarkSurfaceBorder, thickness = 0.5.dp)

                    // 5. 下载音质
                    SettingClickItem(
                        icon = Icons.Default.Download,
                        title = "下载音质",
                        subtitle = settings.downloadQuality.title + " (${settings.downloadQuality.tag})",
                        badge = "默认",
                        onClick = {
                            isEditingDownloadQuality = true
                            showQualityDialog = true
                        }
                    )
                    Divider(color = DarkSurfaceBorder, thickness = 0.5.dp)

                    // 6. 画质 (MV与视觉背景)
                    SettingClickItem(
                        icon = Icons.Default.Videocam,
                        title = "画质设置",
                        subtitle = "MV 与动态背景画质: " + settings.videoQuality,
                        onClick = { showVideoQualityDialog = true }
                    )
                    Divider(color = DarkSurfaceBorder, thickness = 0.5.dp)

                    // 7. 桌面与歌词
                    SettingClickItem(
                        icon = Icons.Default.Subtitles,
                        title = "桌面与歌词",
                        subtitle = if (settings.desktopLyricEnabled) "桌面歌词已开启 · 双行模式/字体大小自定" else "桌面歌词未开启",
                        badge = if (settings.desktopLyricEnabled) "运行中" else "未开启",
                        onClick = { showLyricSettingDialog = true }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Section: 辅助与通知
                Text(
                    text = "系统与隐私",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(DarkSurface)
                        .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(16.dp))
                ) {
                    SettingSwitchItem(
                        icon = Icons.Default.MusicNote,
                        title = "锁屏控制面板",
                        subtitle = "在系统锁屏界面显示黑胶封面与快捷切歌按钮",
                        checked = settings.lockscreenControls,
                        onCheckedChange = { checked ->
                            onUpdateSettings { it.copy(lockscreenControls = checked) }
                        }
                    )
                    Divider(color = DarkSurfaceBorder, thickness = 0.5.dp)
                    SettingClickItem(
                        icon = Icons.Default.Folder,
                        title = "清理本地缓存",
                        subtitle = "当前已占用 142.6 MB (包含专辑封面与离线试听片段)",
                        onClick = {
                            // Clean cache toast / action
                        }
                    )
                    Divider(color = DarkSurfaceBorder, thickness = 0.5.dp)

                    // 8. 版本更新检查
                    SettingClickItem(
                        icon = Icons.Default.SystemUpdate,
                        title = "检查更新",
                        subtitle = "自动检测最新版本，一键升级到新版 APK",
                        badge = "v$currentVersion",
                        onClick = { onCheckUpdate?.invoke() }
                    )
                }

                Spacer(modifier = Modifier.height(36.dp))
            }
        }
    }

    // Sound Quality Selection Dialog
    if (showQualityDialog) {
        val qualities = SoundQuality.values()
        val currentSelection = if (isEditingDownloadQuality) settings.downloadQuality else settings.onlineQuality
        AlertDialog(
            onDismissRequest = { showQualityDialog = false },
            containerColor = DarkSurfaceElevated,
            title = {
                Text(
                    text = if (isEditingDownloadQuality) "选择下载音质" else "选择在线播放音质",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    qualities.forEach { q ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    if (isEditingDownloadQuality) {
                                        onUpdateSettings { it.copy(downloadQuality = q) }
                                    } else {
                                        onUpdateSettings { it.copy(onlineQuality = q) }
                                    }
                                    showQualityDialog = false
                                }
                                .padding(vertical = 10.dp, horizontal = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (q == currentSelection),
                                onClick = null,
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.colorScheme.primary
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = q.title, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                                Text(text = q.desc, color = TextSecondary, fontSize = 12.sp)
                            }
                            if (q == SoundQuality.HI_RES) {
                                Text(
                                    text = "Hi-Res",
                                    color = HiResGold,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showQualityDialog = false }) {
                    Text("完成", color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }

    // Video Quality Dialog
    if (showVideoQualityDialog) {
        val vQualities = listOf("1080P 高清 (推荐)", "4K 极清超采样", "720P 标清", "智能自适应 (根据网络波动切换)")
        AlertDialog(
            onDismissRequest = { showVideoQualityDialog = false },
            containerColor = DarkSurfaceElevated,
            title = { Text(text = "MV与视觉背景画质", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    vQualities.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onUpdateSettings { it.copy(videoQuality = item) }
                                    showVideoQualityDialog = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (settings.videoQuality == item),
                                onClick = null,
                                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = item, color = TextPrimary, fontSize = 14.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showVideoQualityDialog = false }) {
                    Text("确定", color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }

    // Download Directory Dialog
    if (showDownloadPathDialog) {
        val paths = listOf(
            "/storage/emulated/0/Music/Landeting",
            "/storage/emulated/0/Download/LandetingMusic",
            "/sdcard/Music/HiRes_Landeting"
        )
        AlertDialog(
            onDismissRequest = { showDownloadPathDialog = false },
            containerColor = DarkSurfaceElevated,
            title = { Text(text = "歌曲下载目录", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(text = "选择歌曲、伴奏及歌词文件的存储路径：", color = TextSecondary, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    paths.forEach { path ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onUpdateSettings { it.copy(downloadDirectory = path) }
                                    showDownloadPathDialog = false
                                }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (settings.downloadDirectory == path),
                                onClick = null,
                                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = path, color = TextPrimary, fontSize = 13.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDownloadPathDialog = false }) {
                    Text("确定", color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }

    // Desktop & Lyrics Sub-Dialog
    if (showLyricSettingDialog) {
        AlertDialog(
            onDismissRequest = { showLyricSettingDialog = false },
            containerColor = DarkSurfaceElevated,
            title = { Text(text = "桌面与歌词设置", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "开启桌面悬浮歌词", color = TextPrimary, fontSize = 14.sp)
                        Switch(
                            checked = settings.desktopLyricEnabled,
                            onCheckedChange = { checked ->
                                onUpdateSettings { it.copy(desktopLyricEnabled = checked) }
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "双行歌词展示", color = TextPrimary, fontSize = 14.sp)
                        Switch(
                            checked = settings.lyricDoubleLine,
                            onCheckedChange = { checked ->
                                onUpdateSettings { it.copy(lyricDoubleLine = checked) }
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "点击穿透与锁定歌词", color = TextPrimary, fontSize = 14.sp)
                        Switch(
                            checked = settings.lyricTouchLock,
                            onCheckedChange = { checked ->
                                onUpdateSettings { it.copy(lyricTouchLock = checked) }
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary)
                        )
                    }

                    Column {
                        Text(
                            text = "歌词字体大小: ${settings.lyricFontSizeSp} sp",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                        Slider(
                            value = settings.lyricFontSizeSp.toFloat(),
                            onValueChange = { sp ->
                                onUpdateSettings { it.copy(lyricFontSizeSp = sp.toInt()) }
                            },
                            valueRange = 14f..28f,
                            steps = 7,
                            colors = SliderDefaults.colors(thumbColor = MaterialTheme.colorScheme.primary)
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLyricSettingDialog = false }) {
                    Text("完成", color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }
}

@Composable
private fun SettingClickItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    badge: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(3.dp))
            Text(text = subtitle, color = TextSecondary, fontSize = 12.sp)
        }
        if (badge != null) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.White.copy(alpha = 0.1f))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(text = badge, fontSize = 11.sp, color = TextSecondary)
            }
            Spacer(modifier = Modifier.width(6.dp))
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun SettingSwitchItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(3.dp))
            Text(text = subtitle, color = TextSecondary, fontSize = 12.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary)
        )
    }
}
