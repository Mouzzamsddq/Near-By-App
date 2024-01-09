package com.example.nearbyapp.data.features.home.local

import com.example.nearbyapp.base.LocalStorage
import com.example.nearbyapp.constants.LocalStorageConstants
import com.example.nearbyapp.utils.LatLng
import javax.inject.Inject

class LocalHomeDataSource @Inject constructor(
    private val localStorage: LocalStorage,
) {

    fun saveUserCurrentLocation(userLocation: LatLng) {
        localStorage.setString(LocalStorageConstants.LAT, userLocation.lat.toString())
        localStorage.setString(LocalStorageConstants.LNG, userLocation.lng.toString())
    }

    fun getSavedUserLocation(): LatLng? {
        return try {
            val lat = localStorage.getString(LocalStorageConstants.LAT)?.toDouble()
            val lng = localStorage.getString(LocalStorageConstants.LNG)?.toDouble()
            if (lat == null || lng == null) {
                null
            } else {
                LatLng(lat, lng)
            }
        } catch (e: Exception) {
            null
        }
    }
}
