package com.example.nearbyapp.data.features.auth.repository.local.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "users")
data class Users(
    @PrimaryKey
    val username: String,
    val password: String,
    val country: String,
)
