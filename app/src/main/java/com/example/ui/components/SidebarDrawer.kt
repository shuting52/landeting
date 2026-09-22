package com.example.ui.components

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.Song
import com.example.model.ThemePalette
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.GoldVip
import com.example.ui.theme.HiResGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.RecognitionState

@Composable
fun SidebarDrawerContent(
    currentTheme: ThemePalette,
    sleepTimerSecondsLeft: Int?,
    youthModeActive: Boolean,
    recognitionState: RecognitionState,
    onOpenSettings: () -> Unit,
    onSetTheme: (ThemePalette) -> Unit,
    onSetSleepTimer: (Int?) -> Unit,
    onSetSleepTimerEndOfSong: () -> Unit,
    onToggleYouthMode: () -> Unit,
    onStartRecognition: () -> Unit,
    onResetRecognition: () -> Unit,
    onPlaySong: (Song) -> Unit,
    onCloseDrawer: () -> Unit
) {
    var showRecognitionDialog by remember { mutableStateOf(false) }
    var showSleepTimerDialog by remember { mutableStateOf(false) }
    var showDressUpDialog by remember { mutableStateOf(false) }
    var showYouthModeDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(320.dp)
            .background(DarkBackground)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("sidebar_drawer_content")
    ) {
        // VIP User Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF2C243B),
                            Color(0xFF1B1626)
                        )
                    )
                )
                .border(1.dp, GoldVip.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.app_icon_art),
                        contentDescription = "用户头像",
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .border(2.dp, GoldVip, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "懒听发烧友",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(GoldVip)
                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "黑胶 VIP",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.Black
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "累计听歌 248 小时 · 收藏 128 首",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "功能中心",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )

        // 7 Sidebar Features requested by user:
        // 听歌识曲 / 睡眠定时 / 设置 / 装扮中心 / 未成年人模式 / 帮助与反馈 / 关于
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(DarkSurface)
                .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(16.dp))
        ) {
            // 1. 听歌识曲
            DrawerMenuItem(
                icon = Icons.Default.Hearing,
                title = "听歌识曲",
                subtitle = "智能麦克风识别周围背景音乐",
                badge = "雷达",
                onClick = {
                    showRecognitionDialog = true
                    onStartRecognition()
                }
            )
            Divider(color = DarkSurfaceBorder, thickness = 0.5.dp)

            // 2. 睡眠定时
            val sleepTimerBadge = if (sleepTimerSecondsLeft != null) {
                "${sleepTimerSecondsLeft / 60}m"
            } else null

            DrawerMenuItem(
                icon = Icons.Default.AvTimer,
                title = "睡眠定时",
                subtitle = if (sleepTimerSecondsLeft != null) "倒计时中: ${sleepTimerSecondsLeft / 60} 分钟后停止" else "设定播放停止时间，安稳入睡",
                badge = sleepTimerBadge,
                onClick = { showSleepTimerDialog = true }
            )
            Divider(color = DarkSurfaceBorder, thickness = 0.5.dp)

            // 3. 设置 (opens SettingsScreen)
            DrawerMenuItem(
                icon = Icons.Default.Settings,
                title = "设置",
                subtitle = "在线音质 / 边听边存 / 歌词与下载",
                onClick = {
                    onCloseDrawer()
                    onOpenSettings()
                }
            )
            Divider(color = DarkSurfaceBorder, thickness = 0.5.dp)

            // 4. 装扮中心
            DrawerMenuItem(
                icon = Icons.Default.ColorLens,
                title = "装扮中心",
                subtitle = "当前: " + currentTheme.displayName,
                badge = "主题",
                onClick = { showDressUpDialog = true }
            )
            Divider(color = DarkSurfaceBorder, thickness = 0.5.dp)

            // 5. 未成年人模式
            DrawerMenuItem(
                icon = Icons.Default.ChildCare,
                title = "未成年人模式",
                subtitle = if (youthModeActive) "已开启 · 每日限额 40 分钟" else "守护青少年身心健康",
                badge = if (youthModeActive) "守护中" else null,
                onClick = { showYouthModeDialog = true }
            )
            Divider(color = DarkSurfaceBorder, thickness = 0.5.dp)

            // 6. 帮助与反馈
            DrawerMenuItem(
                icon = Icons.Default.HelpOutline,
                title = "帮助与反馈",
                subtitle = "常见问题解决与建议提交",
                onClick = { showHelpDialog = true }
            )
            Divider(color = DarkSurfaceBorder, thickness = 0.5.dp)

            // 7. 关于
            DrawerMenuItem(
                icon = Icons.Default.Info,
                title = "关于",
                subtitle = "懒得听 v1.0.0 · 偷得浮生半日闲",
                onClick = { showAboutDialog = true }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // App Motto
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "偷得浮生半日闲 · 懒得听音乐",
                fontSize = 12.sp,
                color = TextMuted
            )
        }
    }

    // 1. Song Recognition Dialog
    if (showRecognitionDialog) {
        val infiniteTransition = rememberInfiniteTransition(label = "radar")
        val radarScale by infiniteTransition.animateFloat(
            initialValue = 0.8f,
            targetValue = 1.35f,
            animationSpec = infiniteRepeatable(
                animation = tween(1200, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "radar_pulse"
        )

        AlertDialog(
            onDismissRequest = {
                showRecognitionDialog = false
                onResetRecognition()
            },
            containerColor = DarkSurfaceElevated,
            title = {
                Text(text = "听歌识曲", color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (recognitionState) {
                        is RecognitionState.Idle, is RecognitionState.Listening -> {
                            Box(
                                modifier = Modifier
                                    .size(120.dp)
                                    .scale(radarScale)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(54.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "正在倾听周围音乐声音...", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                            Text(text = "请将手机麦克风靠近音源", color = TextSecondary, fontSize = 12.sp)
                        }
                        is RecognitionState.Analyzing -> {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF232030)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "声纹匹配中", color = HiResGold, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "正在云端 HIFI 声纹指纹库比对...", color = TextSecondary, fontSize = 13.sp)
                        }
                        is RecognitionState.Matched -> {
                            val song = recognitionState.song
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(DarkSurface)
                                    .padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "识别成功！${recognitionState.confidence}",
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = song.title, color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Text(text = song.artist + " · " + song.album, color = TextSecondary, fontSize = 13.sp)
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = {
                                        onPlaySong(song)
                                        showRecognitionDialog = false
                                        onResetRecognition()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("立即播放这首歌")
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showRecognitionDialog = false
                    onResetRecognition()
                }) {
                    Text("关闭", color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }

    // 2. Sleep Timer Dialog
    if (showSleepTimerDialog) {
        val options = listOf(
            Pair("关闭定时", null),
            Pair("15 分钟", 15),
            Pair("30 分钟", 30),
            Pair("45 分钟", 45),
            Pair("60 分钟", 60),
            Pair("播完当前歌曲后停止", -1)
        )
        AlertDialog(
            onDismissRequest = { showSleepTimerDialog = false },
            containerColor = DarkSurfaceElevated,
            title = {
                Text(text = "睡眠定时器", color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    if (sleepTimerSecondsLeft != null) {
                        Text(
                            text = "当前倒计时：${sleepTimerSecondsLeft / 60} 分 ${sleepTimerSecondsLeft % 60} 秒",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }
                    options.forEach { (label, minutes) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    if (minutes == -1) {
                                        onSetSleepTimerEndOfSong()
                                    } else {
                                        onSetSleepTimer(minutes)
                                    }
                                    showSleepTimerDialog = false
                                }
                                .padding(vertical = 12.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = label, color = TextPrimary, fontSize = 14.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSleepTimerDialog = false }) {
                    Text("取消", color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }

    // 4. Dress-up Center Dialog
    if (showDressUpDialog) {
        val standardPalettes = ThemePalette.values().filter { !it.isStarTheme }
        val starPalettes = ThemePalette.values().filter { it.isStarTheme }
        AlertDialog(
            onDismissRequest = { showDressUpDialog = false },
            containerColor = DarkSurfaceElevated,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.ColorLens, contentDescription = null, tint = GoldVip, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "装扮中心 · 主题与明星壁纸", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Star Celebrity Section
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "明星专属壁纸背景", color = GoldVip, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(GoldVip.copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 1.dp)
                        ) {
                            Text(text = "明星特供", color = GoldVip, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    starPalettes.forEach { palette ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (palette == currentTheme) Color(0xFF2C1E3A) else DarkSurface)
                                .border(
                                    width = if (palette == currentTheme) 1.5.dp else 0.5.dp,
                                    color = if (palette == currentTheme) GoldVip else DarkSurfaceBorder,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    onSetTheme(palette)
                                    showDressUpDialog = false
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (palette.bgDrawableRes != null) {
                                Image(
                                    painter = painterResource(id = palette.bgDrawableRes),
                                    contentDescription = palette.displayName,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(palette.primaryHex))
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = palette.displayName,
                                        color = TextPrimary,
                                        fontWeight = if (palette == currentTheme) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(Color(0xFF3B2544))
                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                    ) {
                                        Text(text = "明星高清壁纸", color = Color(0xFFFFB74D), fontSize = 9.sp)
                                    }
                                }
                                if (palette.starDescription != null && palette.starDescription.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = palette.starDescription,
                                        color = TextSecondary,
                                        fontSize = 11.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                            if (palette == currentTheme) {
                                Text(text = "使用中", fontSize = 12.sp, color = GoldVip, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Classic Theme Palettes Section
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "经典配色主题", color = TextSecondary, fontWeight = FontWeight.Bold, fontSize = 13.sp)

                    standardPalettes.forEach { palette ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(DarkSurface)
                                .border(
                                    width = if (palette == currentTheme) 1.dp else 0.dp,
                                    color = if (palette == currentTheme) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    onSetTheme(palette)
                                    showDressUpDialog = false
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(palette.primaryHex))
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = palette.displayName,
                                color = TextPrimary,
                                fontWeight = if (palette == currentTheme) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.weight(1f)
                            )
                            if (palette == currentTheme) {
                                Text(text = "使用中", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDressUpDialog = false }) {
                    Text("完成", color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }

    // 5. Minor Mode Dialog
    if (showYouthModeDialog) {
        var passwordInput by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showYouthModeDialog = false },
            containerColor = DarkSurfaceElevated,
            title = {
                Text(text = "未成年人模式", color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "开启后将启用防沉迷时长限制（每日限额40分钟），过滤不适宜有声剧与嘈杂曲目，提供健康益智纯净音乐。",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "未成年人模式开关", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                        Switch(
                            checked = youthModeActive,
                            onCheckedChange = { onToggleYouthMode() },
                            colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary)
                        )
                    }
                    if (!youthModeActive) {
                        OutlinedTextField(
                            value = passwordInput,
                            onValueChange = { passwordInput = it },
                            label = { Text("设置4位监护人密码") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showYouthModeDialog = false }) {
                    Text("确定", color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }

    // 6. Help and Feedback Dialog
    if (showHelpDialog) {
        var feedbackText by remember { mutableStateOf("") }
        var submitted by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            containerColor = DarkSurfaceElevated,
            title = {
                Text(text = "帮助与反馈", color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "常见问题：", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    Text(text = "Q: 什么是 Hi-Res 超清母带？\nA: 采样率达 24bit/96kHz 以上，还原录音棚现场细节。", color = TextSecondary, fontSize = 12.sp)
                    Text(text = "Q: 如何将歌曲导入本地？\nA: 在设置中配置下载目录，支持全自动后台解析缓存。", color = TextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "提交反馈意见：", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    OutlinedTextField(
                        value = feedbackText,
                        onValueChange = { feedbackText = it },
                        placeholder = { Text("请输入您遇到的问题或功能建议...") },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    if (submitted) {
                        Text(text = "感谢您的反馈，工程师团队已收到！", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (feedbackText.isNotBlank()) {
                        submitted = true
                    } else {
                        showHelpDialog = false
                    }
                }) {
                    Text(if (submitted) "关闭" else "提交反馈", color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }

    // 7. About Dialog
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            containerColor = DarkSurfaceElevated,
            title = {
                Text(text = "关于 懒得听", color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.app_icon_art),
                        contentDescription = "懒得听 标识",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "懒得听 音乐播放器", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(text = "版本 v1.0.0 · 64-Bit Release", fontSize = 12.sp, color = TextSecondary)
                    Text(text = "包名: com.landeting", fontSize = 11.sp, color = TextMuted)
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "“偷得浮生半日闲 · 懒听好音乐”\n专为音乐发烧友打造的高保真声学音乐体验，支持全景声与无损黑胶母带音质。",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text("知道了", color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }
}

@Composable
private fun DrawerMenuItem(
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
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text(text = subtitle, color = TextSecondary, fontSize = 11.sp)
        }
        if (badge != null) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = badge,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(18.dp)
        )
    }
}
