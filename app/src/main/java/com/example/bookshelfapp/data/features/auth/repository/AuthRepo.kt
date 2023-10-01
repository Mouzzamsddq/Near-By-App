package com.example.bookshelfapp.data.features.auth.repository

import com.example.bookshelfapp.data.features.auth.repository.local.AuthLocalDataSource
import com.example.bookshelfapp.data.features.auth.repository.local.entity.Users
import javax.inject.Inject

class AuthRepo @Inject constructor(
    private val authLocalDataSource: AuthLocalDataSource,
) {
    suspend fun performSignup(users: Users) = authLocalDataSource.performSignUp(users = users)

    suspend fun performSignIn(name: String, password: String) =
        authLocalDataSource.performSignIn(name = name, password = password)

    fun saveUserAuthenticated() = authLocalDataSource.saveUserAuthenticated()

    fun logout() = authLocalDataSource.logout()
}
