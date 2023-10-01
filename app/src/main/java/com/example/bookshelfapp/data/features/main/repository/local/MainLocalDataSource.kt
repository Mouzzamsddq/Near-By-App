package com.example.bookshelfapp.data.features.main.repository.local

import com.example.bookshelfapp.base.LocalStorage
import com.example.bookshelfapp.constants.LocalStorageConstants
import javax.inject.Inject

class MainLocalDataSource @Inject constructor(
    private val localStorage: LocalStorage,
) {
    fun isUserAuthenticated() =
        localStorage.getBoolean(LocalStorageConstants.IS_USER_AUTHENTICATED)
}
