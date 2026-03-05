package com.swimscape.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SpotDao {
    @Query("SELECT * FROM spots ORDER BY name ASC")
    fun observeSpots(): Flow<List<SpotEntity>>

    @Query("SELECT * FROM spots WHERE spotId = :spotId")
    fun observeSpot(spotId: String): Flow<SpotEntity?>

    @Query("SELECT * FROM spots WHERE spotId = :spotId LIMIT 1")
    suspend fun getSpotOnce(spotId: String): SpotEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSpots(spots: List<SpotEntity>)
}
