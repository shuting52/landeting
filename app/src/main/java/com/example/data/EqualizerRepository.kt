package com.example.data

import com.example.data.local.EqualizerDao
import com.example.data.local.EqualizerEntity
import com.example.model.BUILT_IN_EQUALIZER_PRESETS
import com.example.model.DEFAULT_BANDS
import com.example.model.DEFAULT_BAND_DEFINITIONS
import com.example.model.EqualizerBand
import com.example.model.EqualizerPreset
import com.example.model.EqualizerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EqualizerRepository(private val equalizerDao: EqualizerDao) {

    val activeEqualizerFlow: Flow<EqualizerState> = equalizerDao.getActiveEqualizer().map { entity ->
        if (entity == null) {
            EqualizerState()
        } else {
            val gainStrings = entity.bandGains.split(",")
            val bands = DEFAULT_BAND_DEFINITIONS.mapIndexed { index, (freq, role) ->
                val gain = gainStrings.getOrNull(index)?.toFloatOrNull() ?: 0f
                EqualizerBand(
                    index = index,
                    frequencyLabel = freq,
                    bandRole = role,
                    gain = gain
                )
            }
            EqualizerState(
                isEnabled = entity.isEnabled,
                currentPresetName = entity.presetName,
                bands = bands,
                bassBoost = entity.bassBoost,
                surround3d = entity.surround3d,
                isCustom = entity.isCustomPreset
            )
        }
    }

    val customPresetsFlow: Flow<List<EqualizerPreset>> = equalizerDao.getCustomPresets().map { list ->
        list.map { entity ->
            val gains = entity.bandGains.split(",").mapNotNull { it.toFloatOrNull() }
            EqualizerPreset(
                name = entity.presetName,
                gains = if (gains.size == 5) gains else listOf(0f, 0f, 0f, 0f, 0f),
                description = "用户自定义保存的 EQ 场景模式"
            )
        }
    }

    suspend fun saveEqualizerState(state: EqualizerState) {
        val gainsStr = state.bands.joinToString(",") { it.gain.toString() }
        val entity = EqualizerEntity(
            id = 1,
            presetName = state.currentPresetName,
            bandGains = gainsStr,
            bassBoost = state.bassBoost,
            surround3d = state.surround3d,
            isEnabled = state.isEnabled,
            isCustomPreset = state.isCustom,
            updatedAt = System.currentTimeMillis()
        )
        equalizerDao.insertOrUpdate(entity)
    }

    suspend fun saveCustomPreset(presetName: String, gains: List<Float>) {
        val gainsStr = gains.joinToString(",") { it.toString() }
        val entity = EqualizerEntity(
            id = (System.currentTimeMillis() % 1000000).toInt() + 10,
            presetName = presetName,
            bandGains = gainsStr,
            bassBoost = 0f,
            surround3d = 0f,
            isEnabled = true,
            isCustomPreset = true,
            updatedAt = System.currentTimeMillis()
        )
        equalizerDao.insertOrUpdate(entity)
    }
}
