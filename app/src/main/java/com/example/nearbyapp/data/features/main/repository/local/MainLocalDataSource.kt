package com.example.nearbyapp.data.features.main.repository.local

import com.example.nearbyapp.base.LocalStorage
import com.example.nearbyapp.constants.LocalStorageConstants
import javax.inject.Inject

class MainLocalDataSource @Inject constructor(
    private val localStorage: LocalStorage,
) {
    fun isUserAuthenticated() =
        localStorage.getBoolean(LocalStorageConstants.IS_USER_AUTHENTICATED)
}
