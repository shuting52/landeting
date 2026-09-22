package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "equalizer_settings")
data class EqualizerEntity(
    @PrimaryKey val id: Int = 1,
    val presetName: String = "原声HIFI",
    val bandGains: String = "0.0,0.0,0.0,0.0,0.0", // Gains for 60Hz, 230Hz, 910Hz, 3.6kHz, 14kHz
    val bassBoost: Float = 0f,
    val surround3d: Float = 0f,
    val isEnabled: Boolean = true,
    val isCustomPreset: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
)
