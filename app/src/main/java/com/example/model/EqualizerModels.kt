package com.example.model

data class EqualizerBand(
    val index: Int,
    val frequencyLabel: String,
    val bandRole: String,
    val gain: Float = 0f // -12f to +12f dB
)

data class EqualizerPreset(
    val name: String,
    val gains: List<Float>,
    val description: String = ""
)

data class EqualizerState(
    val isEnabled: Boolean = true,
    val currentPresetName: String = "原声HIFI",
    val bands: List<EqualizerBand> = DEFAULT_BANDS,
    val bassBoost: Float = 15f,     // 0% to 100%
    val surround3d: Float = 20f,    // 0% to 100%
    val isCustom: Boolean = false
) {
    val gains: List<Float> get() = bands.map { it.gain }
}

val DEFAULT_BAND_DEFINITIONS = listOf(
    "60 Hz" to "超低音",
    "230 Hz" to "低音",
    "910 Hz" to "中频人声",
    "3.6 kHz" to "明亮中高",
    "14 kHz" to "极高频泛音"
)

val DEFAULT_BANDS = DEFAULT_BAND_DEFINITIONS.mapIndexed { index, (freq, role) ->
    EqualizerBand(index = index, frequencyLabel = freq, bandRole = role, gain = 0f)
}

val BUILT_IN_EQUALIZER_PRESETS = listOf(
    EqualizerPreset(
        name = "原声HIFI",
        gains = listOf(0f, 0f, 0f, 0f, 0f),
        description = "高保真母带直通，原汁原味还原录音室细节"
    ),
    EqualizerPreset(
        name = "流行",
        gains = listOf(3f, 1.5f, 4f, 2f, 3f),
        description = "增强前置人声与轻快鼓点，动听入耳"
    ),
    EqualizerPreset(
        name = "古典",
        gains = listOf(4.5f, 2.5f, -1f, 3.5f, 4.5f),
        description = "拓展交响管弦乐声场与泛音空气感"
    ),
    EqualizerPreset(
        name = "摇滚",
        gains = listOf(6f, 4.5f, -2f, 4f, 6.5f),
        description = "电吉他撕裂感与冲击力十足的双踩底鼓"
    ),
    EqualizerPreset(
        name = "重低音",
        gains = listOf(8.5f, 6f, 1.5f, 0f, -2f),
        description = "深沉下潜澎湃低频，能量爆发感极强"
    ),
    EqualizerPreset(
        name = "清澈人声",
        gains = listOf(-2f, 1f, 6.5f, 4.5f, 2.5f),
        description = "突显细腻唇齿音与温润情感表达"
    ),
    EqualizerPreset(
        name = "电音舞曲",
        gains = listOf(7.5f, 5.5f, 0f, 3.5f, 6f),
        description = "高能合成器音色与律动低频推进"
    ),
    EqualizerPreset(
        name = "纯净爵士",
        gains = listOf(3f, 2f, 1.5f, 3f, 2.5f),
        description = "萨克斯与低音大提琴温润醇厚"
    )
)
