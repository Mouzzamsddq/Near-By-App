package com.example.nearbyapp.data.features.home.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "venue")
data class Venue(
    @PrimaryKey
    val id: Long,
    val name: String,
    val url: String,
    val city: String,
    val address: String,
    val displayLocation: String,
)
