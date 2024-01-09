package com.example.nearbyapp.data.features.auth.repository.local

import com.example.nearbyapp.base.LocalStorage
import com.example.nearbyapp.base.Resource
import com.example.nearbyapp.constants.LocalStorageConstants
import com.example.nearbyapp.constants.StringConstant
import com.example.nearbyapp.data.features.auth.repository.local.dao.UsersDao
import com.example.nearbyapp.data.features.auth.repository.local.entity.Users
import javax.inject.Inject

class AuthLocalDataSource @Inject constructor(
    private val usersDao: UsersDao,
    private val localStorage: LocalStorage,
) {

    suspend fun performSignUp(users: Users): Resource<Boolean> {
        if (usersDao.isUserAlreadyExist(users.username)) {
            return Resource.error(message = StringConstant.USER_ALREADY_EXIST)
        }
        return try {
            usersDao.insertUser(users = users)
            Resource.success(data = true)
        } catch (e: Exception) {
            Resource.error(message = StringConstant.COMMON_ERROR_MESSAGE)
        }
    }

    suspend fun performSignIn(name: String, password: String): Resource<Boolean> {
        if (!usersDao.isUserAlreadyExist(name)) {
            return Resource.error(message = StringConstant.USER_NOT_REGISTERED)
        }
        val isValid = usersDao.performSignIn(username = name, password = password)
        return if (isValid == 0) {
            Resource.error(message = StringConstant.ENTER_VALID_PASSWORD)
        } else {
            Resource.success(data = true)
        }
    }

    fun saveUserAuthenticated() {

    }

    fun logout() {
    }
}
