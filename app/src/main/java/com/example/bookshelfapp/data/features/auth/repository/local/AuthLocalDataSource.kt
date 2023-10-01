package com.example.bookshelfapp.data.features.auth.repository.local

import com.example.bookshelfapp.base.LocalStorage
import com.example.bookshelfapp.base.Resource
import com.example.bookshelfapp.constants.LocalStorageConstants
import com.example.bookshelfapp.constants.StringConstant
import com.example.bookshelfapp.data.features.auth.repository.local.dao.UsersDao
import com.example.bookshelfapp.data.features.auth.repository.local.entity.Users
import javax.inject.Inject

class AuthLocalDataSource @Inject constructor(
    private val usersDao: UsersDao,
    private val localStorage: LocalStorage
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
            return Resource.error(message = StringConstant.ENTER_VALID_NAME)
        }
        val isValid = usersDao.performSignIn(username = name, password = password)
        return if (isValid == 0) {
            Resource.error(message = StringConstant.ENTER_VALID_PASSWORD)
        } else {
            Resource.success(data = true)
        }
    }

    fun saveUserAuthenticated() {
        localStorage.setBoolean(LocalStorageConstants.IS_USER_AUTHENTICATED, true)
    }
}
