package com.example.nearbyapp.data.features.home.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nearbyapp.data.features.home.local.entity.VenueRemoteKeys

@Dao
interface RemoteKeysDao {
    @Query("SELECT * FROM VENUE_REMOTE_KEYS WHERE id = :id")
    suspend fun getRemoteKeys(id: Long): VenueRemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<VenueRemoteKeys>)

    @Query("DELETE FROM VENUE_REMOTE_KEYS")
    suspend fun deleteAllRemoteKeys()
}
