package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("懒得听", appName)
  }

  @Test
  fun `test lrc parser handles metadata and timestamps`() {
    val sampleLrc = """
      [ti:晴天]
      [ar:周杰伦]
      [al:叶惠美]
      [offset:500]
      [00:04.20]词：周杰伦 曲：周杰伦
      [00:15.50][01:15.50]故事的小黄花 从出生那年就飘着
    """.trimIndent()

    val lrcData = com.example.model.LrcParser.parse(sampleLrc)
    assertEquals("晴天", lrcData.title)
    assertEquals("周杰伦", lrcData.artist)
    assertEquals("叶惠美", lrcData.album)
    assertEquals(500L, lrcData.offsetMs)
    assertEquals(3, lrcData.lines.size)
    // 00:04.20 = 4200ms + 500ms offset = 4700ms
    assertEquals(4700L, lrcData.lines[0].timeMs)
    assertEquals("词：周杰伦 曲：周杰伦", lrcData.lines[0].text)
  }

  @Test
  fun `test built-in equalizer presets have 5 bands`() {
    val presets = com.example.model.BUILT_IN_EQUALIZER_PRESETS
    assert(presets.isNotEmpty())
    val pop = presets.find { it.name == "流行" }
    assert(pop != null)
    assertEquals(5, pop?.gains?.size)
  }

  @Test
  fun `test equalizer state custom band adjustment`() {
    val defaultState = com.example.model.EqualizerState()
    assertEquals("原声HIFI", defaultState.currentPresetName)
    assertEquals(5, defaultState.bands.size)
    assertEquals(0f, defaultState.bands[0].gain)

    // Update 60Hz band to +6dB
    val updatedBands = defaultState.bands.mapIndexed { index, band ->
      if (index == 0) band.copy(gain = 6.0f) else band
    }
    val customState = defaultState.copy(
      currentPresetName = "自定义",
      bands = updatedBands,
      isCustom = true
    )
    assertEquals("自定义", customState.currentPresetName)
    assertEquals(6.0f, customState.bands[0].gain)
    assert(customState.isCustom)
  }

  @Test
  fun `test online music catalog search matches known singer and unknown singer`() {
    // 1. Known singer search
    val jayResults = com.example.data.OnlineMusicCatalog.search("周杰伦")
    assert(jayResults.isNotEmpty())
    assert(jayResults.any { it.artist.contains("周杰伦") })

    // 2. English / International singer search
    val swiftResults = com.example.data.OnlineMusicCatalog.search("Taylor Swift")
    assert(swiftResults.isNotEmpty())
    assert(swiftResults.any { it.artist.contains("Taylor Swift") })

    // 3. Dynamic generation for arbitrary query
    val arbitraryResults = com.example.data.OnlineMusicCatalog.search("未知宝藏歌手")
    assert(arbitraryResults.isNotEmpty())
    assert(arbitraryResults.first().artist == "未知宝藏歌手" || arbitraryResults.first().title.contains("未知宝藏歌手"))
  }

  @Test
  fun `test celebrity star themes configured in ThemePalette`() {
    val starThemes = com.example.model.ThemePalette.values().filter { it.isStarTheme }
    assertEquals(3, starThemes.size)
    starThemes.forEach { theme ->
      assert(theme.bgDrawableRes != null)
      assert(theme.starDescription.isNotEmpty())
    }
  }

  @Test
  fun `test local audio scanner returns recognized songs`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val scanner = com.example.data.LocalAudioScanner(context)
    val songs = scanner.scanAndRecognizeDeviceSongs()
    assert(songs.isNotEmpty())
    // Should have valid song attributes recognized
    val first = songs.first()
    assert(first.title.isNotEmpty())
    assert(first.artist.isNotEmpty())
    assert(first.durationMs > 0)
    assert(first.soundQuality != null)
  }
}
