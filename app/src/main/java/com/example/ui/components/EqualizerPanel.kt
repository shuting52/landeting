package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.SurroundSound
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BUILT_IN_EQUALIZER_PRESETS
import com.example.model.EqualizerBand
import com.example.model.EqualizerPreset
import com.example.model.EqualizerState
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.GoldVip
import com.example.ui.theme.HiResGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EqualizerBottomSheet(
    equalizerState: EqualizerState,
    customPresets: List<EqualizerPreset>,
    onSelectPreset: (String) -> Unit,
    onBandGainChange: (Int, Float) -> Unit,
    onBassBoostChange: (Float) -> Unit,
    onSurround3dChange: (Float) -> Unit,
    onToggleEnable: (Boolean) -> Unit,
    onSaveCustomPreset: (String) -> Unit,
    onReset: () -> Unit,
    onDismiss: () -> Unit
) {
    var showSavePresetDialog by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = DarkBackground,
        modifier = Modifier.testTag("equalizer_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header: Title, Enable Switch, Close Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "专业级声学均衡器",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Hi-Res 5频段动态频响调节 · 实时调音",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = equalizerState.isEnabled,
                        onCheckedChange = { onToggleEnable(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = MaterialTheme.colorScheme.primary,
                            uncheckedTrackColor = DarkSurfaceElevated
                        ),
                        modifier = Modifier.testTag("equalizer_enable_switch")
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "关闭",
                            tint = TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Dynamic Frequency Response Curve Canvas
            FrequencyResponseCurve(
                bands = equalizerState.bands,
                isEnabled = equalizerState.isEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkSurface)
                    .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(12.dp))
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Presets Selection Carousel
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "预设场景模式",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = HiResGold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextButton(
                        onClick = { showSavePresetDialog = true },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "存为自定义", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                    }
                    TextButton(
                        onClick = onReset,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "重置", fontSize = 11.sp, color = TextMuted)
                    }
                }
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Built-in presets
                items(BUILT_IN_EQUALIZER_PRESETS) { preset ->
                    val isSelected = equalizerState.currentPresetName == preset.name && !equalizerState.isCustom
                    PresetChip(
                        name = preset.name,
                        isSelected = isSelected,
                        onClick = { onSelectPreset(preset.name) }
                    )
                }

                // Custom user saved presets
                items(customPresets) { customPreset ->
                    val isSelected = equalizerState.currentPresetName == customPreset.name
                    PresetChip(
                        name = customPreset.name,
                        isSelected = isSelected,
                        isCustom = true,
                        onClick = { onSelectPreset(customPreset.name) }
                    )
                }

                // Custom currently active item
                item {
                    val isCustomSelected = equalizerState.isCustom
                    PresetChip(
                        name = "自定义",
                        isSelected = isCustomSelected,
                        onClick = { /* already active */ }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 5-Band Vertical Equalizer Faders
            Text(
                text = "频段精细增益调节 (-12dB ~ +12dB)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(DarkSurfaceElevated)
                    .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(14.dp))
                    .padding(vertical = 14.dp, horizontal = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    equalizerState.bands.forEachIndexed { index, band ->
                        VerticalEqualizerFader(
                            band = band,
                            isEnabled = equalizerState.isEnabled,
                            onGainChange = { newGain ->
                                onBandGainChange(index, newGain)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Additional Audio Enhancements (Bass Boost & 3D Surround)
            Text(
                text = "空间与声效渲染",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = HiResGold
            )
            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkSurface)
                    .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(12.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Bass Boost
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "超低音下潜增强 (Bass Boost)", fontSize = 13.sp, color = TextPrimary)
                        }
                        Text(
                            text = "${equalizerState.bassBoost.toInt()}%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Slider(
                        value = equalizerState.bassBoost,
                        onValueChange = onBassBoostChange,
                        valueRange = 0f..100f,
                        enabled = equalizerState.isEnabled,
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary,
                            inactiveTrackColor = DarkSurfaceElevated
                        ),
                        modifier = Modifier.testTag("bass_boost_slider")
                    )
                }

                // 3D Spatial Surround
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.SurroundSound,
                                contentDescription = null,
                                tint = HiResGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "3D 空间全景环绕 (Surround)", fontSize = 13.sp, color = TextPrimary)
                        }
                        Text(
                            text = "${equalizerState.surround3d.toInt()}%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = HiResGold
                        )
                    }
                    Slider(
                        value = equalizerState.surround3d,
                        onValueChange = onSurround3dChange,
                        valueRange = 0f..100f,
                        enabled = equalizerState.isEnabled,
                        colors = SliderDefaults.colors(
                            thumbColor = HiResGold,
                            activeTrackColor = HiResGold,
                            inactiveTrackColor = DarkSurfaceElevated
                        ),
                        modifier = Modifier.testTag("surround_3d_slider")
                    )
                }
            }
        }
    }

    // Save Custom Preset Dialog
    if (showSavePresetDialog) {
        var presetNameInput by remember { mutableStateOf("我的定制调音") }
        AlertDialog(
            onDismissRequest = { showSavePresetDialog = false },
            containerColor = DarkSurfaceElevated,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Bookmark, contentDescription = null, tint = HiResGold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "保存自定义预设", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        text = "将当前 5 个频段增益与音效设置永久保存到本地数据库：",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = presetNameInput,
                        onValueChange = { presetNameInput = it },
                        singleLine = true,
                        placeholder = { Text("输入预设名称 (如：动感车载、睡眠人声)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("custom_preset_name_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = DarkSurfaceBorder
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (presetNameInput.isNotBlank()) {
                            onSaveCustomPreset(presetNameInput.trim())
                            showSavePresetDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.testTag("confirm_save_preset_button")
                ) {
                    Text("保存到本地")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSavePresetDialog = false }) {
                    Text("取消", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
private fun PresetChip(
    name: String,
    isSelected: Boolean,
    isCustom: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)
                else DarkSurface
            )
            .border(
                width = 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else DarkSurfaceBorder,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            } else if (isCustom) {
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = null,
                    tint = HiResGold,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = name,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) MaterialTheme.colorScheme.primary else TextSecondary
            )
        }
    }
}

/**
 * Visual Frequency Response curve connecting the dB points across the 5 bands.
 */
@Composable
private fun FrequencyResponseCurve(
    bands: List<EqualizerBand>,
    isEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val gridColor = Color.White.copy(alpha = 0.08f)
    val curveColor = if (isEnabled) primaryColor else TextMuted

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val centerY = height / 2f

        // Draw horizontal grid lines (-12dB, -6dB, 0dB, +6dB, +12dB)
        for (factor in listOf(0.1f, 0.3f, 0.5f, 0.7f, 0.9f)) {
            val y = height * factor
            drawLine(
                color = if (factor == 0.5f) gridColor.copy(alpha = 0.25f) else gridColor,
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = if (factor == 0.5f) 1.5f else 1f
            )
        }

        if (bands.isEmpty()) return@Canvas

        val stepX = width / (bands.size + 1)
        val points = bands.mapIndexed { index, band ->
            val x = stepX * (index + 1)
            // gain is -12f to +12f. map to y: +12dB -> 0.1*height, -12dB -> 0.9*height
            val normalizedGain = if (isEnabled) (band.gain / 12f).coerceIn(-1f, 1f) else 0f
            val y = centerY - (normalizedGain * (height * 0.4f))
            Offset(x, y)
        }

        // Build smooth curve path
        val path = Path()
        val fillPath = Path()

        path.moveTo(0f, centerY)
        fillPath.moveTo(0f, centerY)

        for (i in 0 until points.size) {
            val current = points[i]
            val prev = if (i == 0) Offset(0f, centerY) else points[i - 1]
            val next = if (i == points.size - 1) Offset(width, centerY) else points[i + 1]

            val cx1 = prev.x + (current.x - prev.x) / 2f
            val cy1 = prev.y
            val cx2 = prev.x + (current.x - prev.x) / 2f
            val cy2 = current.y

            path.cubicTo(cx1, cy1, cx2, cy2, current.x, current.y)
            fillPath.cubicTo(cx1, cy1, cx2, cy2, current.x, current.y)
        }

        // Connect to right edge
        val lastPoint = points.last()
        path.cubicTo(
            lastPoint.x + (width - lastPoint.x) / 2f, lastPoint.y,
            lastPoint.x + (width - lastPoint.x) / 2f, centerY,
            width, centerY
        )
        fillPath.cubicTo(
            lastPoint.x + (width - lastPoint.x) / 2f, lastPoint.y,
            lastPoint.x + (width - lastPoint.x) / 2f, centerY,
            width, centerY
        )

        fillPath.lineTo(width, height)
        fillPath.lineTo(0f, height)
        fillPath.close()

        // Draw gradient area underneath
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    curveColor.copy(alpha = if (isEnabled) 0.35f else 0.08f),
                    Color.Transparent
                )
            )
        )

        // Draw curve outline
        drawPath(
            path = path,
            color = curveColor,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )

        // Draw points on the curve
        for (point in points) {
            drawCircle(
                color = Color.Black,
                radius = 5.dp.toPx(),
                center = point
            )
            drawCircle(
                color = curveColor,
                radius = 3.5.dp.toPx(),
                center = point
            )
        }
    }
}

/**
 * Vertical Fader for a single Equalizer Band (-12dB to +12dB).
 */
@Composable
private fun VerticalEqualizerFader(
    band: EqualizerBand,
    isEnabled: Boolean,
    onGainChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val faderHeight = 135.dp
    val primaryColor = MaterialTheme.colorScheme.primary

    Column(
        modifier = modifier.width(56.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Gain dB label
        val formattedGain = if (band.gain > 0) "+%.1f".format(Locale.US, band.gain) else "%.1f".format(Locale.US, band.gain)
        Text(
            text = "$formattedGain dB",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = when {
                !isEnabled -> TextMuted
                band.gain > 0.1f -> primaryColor
                band.gain < -0.1f -> HiResGold
                else -> TextSecondary
            }
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Vertical Track Canvas with Drag Detection
        Box(
            modifier = Modifier
                .height(faderHeight)
                .width(36.dp)
                .pointerInput(isEnabled) {
                    if (!isEnabled) return@pointerInput
                    detectDragGestures { change, _ ->
                        change.consume()
                        val y = change.position.y.coerceIn(0f, size.height.toFloat())
                        // y = 0 -> +12dB, y = size.height -> -12dB
                        val fraction = 1f - (y / size.height.toFloat())
                        val newGain = ((fraction * 24f) - 12f).coerceIn(-12f, 12f)
                        val roundedGain = (Math.round(newGain * 2) / 2.0).toFloat()
                        onGainChange(roundedGain)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val centerX = w / 2f
                val trackWidth = 5.dp.toPx()

                // Background track
                drawRoundRect(
                    color = Color.Black.copy(alpha = 0.5f),
                    size = androidx.compose.ui.geometry.Size(trackWidth, h),
                    topLeft = Offset(centerX - trackWidth / 2f, 0f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(trackWidth / 2f)
                )

                // 0dB Center Detent Marker
                val centerY = h / 2f
                drawLine(
                    color = Color.White.copy(alpha = 0.35f),
                    start = Offset(centerX - 9.dp.toPx(), centerY),
                    end = Offset(centerX + 9.dp.toPx(), centerY),
                    strokeWidth = 2.dp.toPx()
                )

                // Thumb Y position based on gain (-12dB .. +12dB)
                val normalized = (band.gain + 12f) / 24f // 0f .. 1f
                val thumbY = h * (1f - normalized)

                // Active glowing track portion from 0dB to current thumb
                val activeTop = minOf(centerY, thumbY)
                val activeBottom = maxOf(centerY, thumbY)
                if (isEnabled && activeBottom > activeTop) {
                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            colors = if (band.gain >= 0) listOf(primaryColor, primaryColor.copy(alpha = 0.4f))
                            else listOf(HiResGold.copy(alpha = 0.4f), HiResGold)
                        ),
                        size = androidx.compose.ui.geometry.Size(trackWidth, activeBottom - activeTop),
                        topLeft = Offset(centerX - trackWidth / 2f, activeTop),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(trackWidth / 2f)
                    )
                }

                // Thumb Handle
                val thumbRadius = 9.dp.toPx()
                drawCircle(
                    color = if (isEnabled) Color(0xFF1E1E24) else Color(0xFF2A2A2A),
                    radius = thumbRadius,
                    center = Offset(centerX, thumbY)
                )
                drawCircle(
                    color = if (isEnabled) (if (band.gain >= 0) primaryColor else HiResGold) else TextMuted,
                    radius = thumbRadius,
                    center = Offset(centerX, thumbY),
                    style = Stroke(width = 2.5.dp.toPx())
                )
                drawCircle(
                    color = if (isEnabled) (if (band.gain >= 0) primaryColor else HiResGold) else TextMuted,
                    radius = 3.5.dp.toPx(),
                    center = Offset(centerX, thumbY)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Frequency label (e.g. 60 Hz)
        Text(
            text = band.frequencyLabel,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        // Frequency Role (e.g. 超低音)
        Text(
            text = band.bandRole,
            fontSize = 10.sp,
            color = TextMuted
        )
    }
}
