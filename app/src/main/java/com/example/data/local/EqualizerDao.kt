package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EqualizerDao {

    @Query("SELECT * FROM equalizer_settings WHERE id = 1 LIMIT 1")
    fun getActiveEqualizer(): Flow<EqualizerEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(setting: EqualizerEntity)

    @Query("SELECT * FROM equalizer_settings WHERE isCustomPreset = 1 ORDER BY updatedAt DESC")
    fun getCustomPresets(): Flow<List<EqualizerEntity>>

    @Query("DELETE FROM equalizer_settings WHERE id = :id AND id != 1")
    suspend fun deleteCustomPreset(id: Int)
}
