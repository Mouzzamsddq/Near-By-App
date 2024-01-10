package com.example.nearbyapp.data.features.home.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nearbyapp.data.features.home.local.entity.Venue

@Dao
interface VenueDao {

    @Query("SELECT * FROM VENUE WHERE :query IS NULL OR name LIKE '%' || :query || '%' COLLATE NOCASE")
    fun getVenues(query: String?): PagingSource<Int, Venue>

    @Query("SELECT * FROM VENUE WHERE name LIKE :searchQuery")
    fun getVenuesList(searchQuery: String): LiveData<Venue>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addVenues(venues: List<Venue>)

    @Query("DELETE FROM VENUE")
    suspend fun deleteVenues()
}
