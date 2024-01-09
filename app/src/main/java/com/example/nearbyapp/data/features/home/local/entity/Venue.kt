package com.example.nearbyapp.data.features.home.local.entity

import androidx.room.Entity

@Entity(tableName = "venue")
data class Venue(
    val id: Long,
    val name: String,
    val url: String,
    val displayLocation: String,
)
