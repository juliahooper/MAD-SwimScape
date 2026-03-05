package com.swimscape.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [SpotEntity::class, FavouriteEntity::class, AlertEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SwimDatabase : RoomDatabase() {
    abstract fun spotDao(): SpotDao
    abstract fun favouriteDao(): FavouriteDao
    abstract fun alertDao(): AlertDao

    companion object {
        @Volatile
        private var INSTANCE: SwimDatabase? = null

        fun getInstance(context: Context): SwimDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    SwimDatabase::class.java,
                    "swimscape_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
