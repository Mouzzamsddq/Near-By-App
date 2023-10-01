package com.example.bookshelfapp.data.features.auth.repository.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.bookshelfapp.data.features.auth.repository.local.entity.Users

@Dao
interface UsersDao {

    @Insert
    suspend fun insertUser(users: Users)

    @Query("SELECT * FROM USERS")
    suspend fun getUsersList(): List<Users>

    @Query("Select Exists(SELECT * FROm users WHERE username = :username)")
    suspend fun isUserAlreadyExist(username: String): Boolean
}
