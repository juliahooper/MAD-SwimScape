package com.swimscape.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "spots")
data class SpotEntity(
    @PrimaryKey val spotId: String,
    val name: String,
    val county: String,
    val lat: Double?,
    val lng: Double?,
    val notes: String?,
    val waterTempC: Double?,
    val riskStatus: String?,
    val lastUpdatedEpoch: Long?
)
