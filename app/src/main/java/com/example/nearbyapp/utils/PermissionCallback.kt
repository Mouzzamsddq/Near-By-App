package com.example.nearbyapp.utils

interface PermissionCallback {
    fun handlePermanentDenial()

    fun permissionGranted()
}