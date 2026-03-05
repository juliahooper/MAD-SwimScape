package com.swimscape.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
data class FavouriteEntity(
    @PrimaryKey val spotId: String,
    val favouritedAtEpoch: Long
)
