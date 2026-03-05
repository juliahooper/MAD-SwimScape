package com.swimscape.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {
    @Query("SELECT * FROM favourites ORDER BY favouritedAtEpoch DESC")
    fun observeFavourites(): Flow<List<FavouriteEntity>>

    @Query("SELECT spotId FROM favourites")
    fun observeFavouriteSpotIds(): Flow<List<String>>

    @Query("""
        SELECT s.* FROM spots s
        INNER JOIN favourites f ON s.spotId = f.spotId
        ORDER BY f.favouritedAtEpoch DESC
    """)
    fun observeFavouriteSpotSummaries(): Flow<List<SpotEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(favourite: FavouriteEntity)

    @Query("DELETE FROM favourites WHERE spotId = :spotId")
    suspend fun removeFavourite(spotId: String)

    @Query("SELECT COUNT(*) > 0 FROM favourites WHERE spotId = :spotId")
    suspend fun isFavourite(spotId: String): Boolean
}
