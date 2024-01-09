package com.example.nearbyapp.data.features.main.repository

import com.example.nearbyapp.data.features.main.repository.local.MainLocalDataSource
import javax.inject.Inject

class MainRepo @Inject constructor(
    private val mainLocalDataSource: MainLocalDataSource,
) {
}
