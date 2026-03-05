package com.swimscape.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alerts")
data class AlertEntity(
    @PrimaryKey val alertId: String,
    val spotId: String,
    val title: String,
    val message: String,
    val severity: String,
    val timestampEpoch: Long
)
