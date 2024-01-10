package com.example.nearbyapp.base

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nearbyapp.data.features.home.local.dao.RemoteKeysDao
import com.example.nearbyapp.data.features.home.local.dao.VenueDao
import com.example.nearbyapp.data.features.home.local.entity.Venue
import com.example.nearbyapp.data.features.home.local.entity.VenueRemoteKeys

const val DB_NAME = "near_by_db"
@Database(
    version = 1,
    entities = [Venue::class, VenueRemoteKeys::class],
    exportSchema = false,
)
abstract class NearByDb : RoomDatabase() {

    abstract fun venueDao(): VenueDao
    abstract fun remoteKeyDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: NearByDb? = null
        fun getDatabase(context: Context): NearByDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NearByDb::class.java,
                    DB_NAME,
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
