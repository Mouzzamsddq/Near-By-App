package com.example.nearbyapp.base

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.nearbyapp.constants.StringConstant
import com.example.nearbyapp.data.features.home.local.dao.RemoteKeysDao
import com.example.nearbyapp.data.features.home.local.dao.VenueDao
import com.example.nearbyapp.data.features.home.local.entity.Venue
import com.example.nearbyapp.data.features.home.local.entity.VenueRemoteKeys

@Database(
    version = 1,
    entities = [Venue::class, VenueRemoteKeys::class],
    exportSchema = false,
)
@TypeConverters(Converter::class)
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
                    StringConstant.DB_NAME,
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
