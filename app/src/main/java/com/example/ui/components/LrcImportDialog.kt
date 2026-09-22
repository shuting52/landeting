package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LrcParser
import com.example.model.LyricLine
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.HiResGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun LrcImportDialog(
    initialLrc: String = "",
    onDismiss: () -> Unit,
    onApplyLyrics: (List<LyricLine>) -> Unit
) {
    var rawLrcText by remember { mutableStateOf(initialLrc.ifEmpty { SAMPLE_QINGTIAN_LRC }) }

    val parsedData by remember(rawLrcText) {
        derivedStateOf { LrcParser.parse(rawLrcText) }
    }

    val sampleLrcPresets = remember {
        listOf(
            "晴天 - 周杰伦" to SAMPLE_QINGTIAN_LRC,
            "成都 - 赵雷" to SAMPLE_CHENGDU_LRC,
            "光年之外 - 邓紫棋" to SAMPLE_GUANGNIAN_LRC,
            "红豆 - 王菲" to SAMPLE_HONGDOU_LRC
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurfaceElevated,
        modifier = Modifier.testTag("lrc_import_dialog"),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = ".lrc 歌词解析与导入",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "支持标准 [分:秒.毫秒] 与 [分:秒] 格式，支持多时间标签及元信息标签解析：",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                // Presets Carousel
                Text(text = "快速套用经典 LRC 样例：", fontSize = 12.sp, color = HiResGold, fontWeight = FontWeight.SemiBold)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(sampleLrcPresets) { (name, content) ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(DarkSurface)
                                .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(8.dp))
                                .clickable { rawLrcText = content }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(text = name, fontSize = 11.sp, color = TextPrimary)
                        }
                    }
                }

                // Raw LRC Input Area
                OutlinedTextField(
                    value = rawLrcText,
                    onValueChange = { rawLrcText = it },
                    placeholder = { Text("[00:00.00]输入或粘贴.lrc格式歌词文本...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .testTag("lrc_text_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = DarkSurfaceBorder
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp, color = TextPrimary)
                )

                // Real-time Parser Status Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkSurface)
                        .padding(10.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "解析就绪: 共识别出 ${parsedData.lines.size} 行有效时间戳歌词",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                        if (parsedData.title != null || parsedData.artist != null) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "歌曲: ${parsedData.title ?: "未知"} · 歌手: ${parsedData.artist ?: "未知"}",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onApplyLyrics(parsedData.lines)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.testTag("confirm_apply_lrc_button")
            ) {
                Text("平滑应用此歌词")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消", color = TextSecondary)
            }
        }
    )
}

// Built-in .lrc templates for instant testing & demonstration
private const val SAMPLE_QINGTIAN_LRC = """[ti:晴天]
[ar:周杰伦]
[al:叶惠美]
[by:懒得听]
[00:00.00]晴天 - 周杰伦
[00:04.20]词：周杰伦 曲：周杰伦
[00:15.50]故事的小黄花 从出生那年就飘着
[00:22.80]童年的荡秋千 随记忆一直晃到现在
[00:30.00]Re So So Si Do Si La
[00:33.20]So La Si Si Si Si La Si La So
[00:37.40]吹着前奏望着天空 我想起花瓣试着掉落
[00:44.20]为你翘课的那一天 花落的那一天
[00:48.50]教室的那一间 我怎么看不见
[00:52.00]消失的下雨天 我好想再淋一遍
[00:59.20]没想到失去的勇气我还留着
[01:06.50]好想再问一遍 你会等待还是离开
[01:14.00]刮风这天 我试过握着你手
[01:21.00]但偏偏 雨渐渐 大到我看你不见
[01:28.00]还要多久 我才能在你身边
[01:35.50]等到放晴的那天 也许我会比较好一点"""

private const val SAMPLE_CHENGDU_LRC = """[ti:成都]
[ar:赵雷]
[al:无法长大]
[00:00.00]成都 - 赵雷
[00:05.10]词：赵雷 曲：赵雷
[00:14.20]让我掉下眼泪的 不止昨夜的酒
[00:23.00]让我依依不舍的 不止你的温柔
[00:31.50]雨水节奏不停 走在清晨的街
[00:40.00]分别总是在九月 回忆是思念的愁
[00:49.00]深秋嫩绿的垂柳 亲吻着我额头
[00:57.50]在那座阴雨的小城里 我从未忘记你
[01:07.00]成都 带不走的 只有你
[01:16.00]和我在成都的街头走一走
[01:24.00]直到所有的灯都熄灭了也不停留
[01:32.50]你会挽着我的衣袖 我会把手揣进裤兜
[01:41.00]走到玉林路的尽头 坐在小酒馆的门口"""

private const val SAMPLE_GUANGNIAN_LRC = """[ti:光年之外]
[ar:G.E.M. 邓紫棋]
[al:太空旅客 中国区主题曲]
[00:00.00]光年之外 - 邓紫棋
[00:03.50]词：邓紫棋 曲：邓紫棋
[00:10.00]感受停在我发端的指尖
[00:15.20]如何瞬间冻结时间
[00:20.50]记住望着我坚定的双眼
[00:25.80]也许已经没有明天
[00:31.20]面对浩瀚的星海 我们微小得像尘埃
[00:41.00]我们如此渺小 却又如此狂热
[00:51.50]缘分让我们相遇乱世以外
[00:57.00]命运却要我们危难中相爱
[01:02.00]也许未来遥远在光年之外
[01:07.50]我愿守候未知里为你等待"""

private const val SAMPLE_HONGDOU_LRC = """[ti:红豆]
[ar:王菲]
[al:唱游]
[00:00.00]红豆 - 王菲
[00:03.00]词：林夕 曲：柳重言
[00:12.00]还没好好的感受 雪花绽放的气候
[00:21.00]我们一起颤抖 会更明白 什么是温柔
[00:31.50]还没跟你牵着手 走过荒芜的沙丘
[00:41.00]可能从此以后 学会珍惜 天长和地久
[00:51.00]有时候 有时候 我会相信一切有尽头
[00:59.00]相聚离开 都有时候 没有什么会永垂不朽
[01:09.00]可是我 有时候 宁愿选择留恋不放手
[01:17.00]等到风景都看透 也许你会陪我 看细水长流"""
