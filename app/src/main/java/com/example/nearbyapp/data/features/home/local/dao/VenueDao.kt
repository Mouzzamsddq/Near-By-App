package com.example.nearbyapp.data.features.home.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nearbyapp.data.features.home.local.entity.Venue

@Dao
interface VenueDao {

    @Query("SELECT * FROM VENUE")
    fun getVenues(): PagingSource<Int, Venue>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addVenues(venues: List<Venue>)

    @Query("DELETE FROM VENUE")
    suspend fun deleteVenues()
}
