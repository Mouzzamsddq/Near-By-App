package com.example.nearbyapp.data.features.home.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "venue_remote_keys")
data class VenueRemoteKeys(
    @PrimaryKey
    val id: Long,
    val prevKey: Int?,
    val nextKey: Int?,
)
