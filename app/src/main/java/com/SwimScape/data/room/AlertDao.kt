package com.swimscape.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {
    @Query("""
        SELECT * FROM alerts
        WHERE spotId IN (:spotIds)
        ORDER BY timestampEpoch DESC
    """)
    fun observeAlertsForSpotIds(spotIds: List<String>): Flow<List<AlertEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAlerts(alerts: List<AlertEntity>)

    @Query("DELETE FROM alerts WHERE timestampEpoch < :beforeEpoch")
    suspend fun clearOldAlerts(beforeEpoch: Long)
}
