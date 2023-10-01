package com.example.bookshelfapp.data.features.auth.repository.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.bookshelfapp.data.features.auth.repository.local.entity.Users

@Dao
interface UsersDao {

    @Insert
    suspend fun insertUser(users: Users)

    @Query("Select Exists(SELECT * FROm users WHERE username = :username)")
    suspend fun isUserAlreadyExist(username: String): Boolean

    @Query("SELECT COUNT(*) FROM users WHERE username = :username AND password = :password")
    suspend fun performSignIn(username: String, password: String): Int
}
