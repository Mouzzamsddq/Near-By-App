package com.example.nearbyapp.data.features.home

import com.example.nearbyapp.data.features.home.local.LocalHomeDataSource
import com.example.nearbyapp.utils.LatLng
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val localHomeDataSource: LocalHomeDataSource,
) {
    fun saveUserLocation(userLocation: LatLng) =
        localHomeDataSource.saveUserCurrentLocation(userLocation)

    fun getSavedUserLocation() = localHomeDataSource.getSavedUserLocation()
}
