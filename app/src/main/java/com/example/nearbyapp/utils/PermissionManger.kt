package com.example.nearbyapp.utils

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat

class PermissionManger(
    private val activity: ComponentActivity,
    private val permissionCallback: PermissionCallback,
) {
    private val locationPermissionRequest = activity.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        when {
            permissions.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                permissionCallback.permissionGranted()
                Toast.makeText(activity, "Permission granted", Toast.LENGTH_SHORT).show()
            }

            permissions.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                requestLocationPermission()
            }

            else -> {
                if (shouldShowRequestPermissionRationale(
                        activity,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                    )
                ) {
                    requestLocationPermission()
                } else {
                    permissionCallback.handlePermanentDenial()
                }
            }
        }
    }

    fun isLocationPermissionAlreadyGranted(): Boolean {
        val isFineLocationGranted = ContextCompat.checkSelfPermission(
            activity,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED

        val isCoarseLocationGranted = ContextCompat.checkSelfPermission(
            activity,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED
        return isFineLocationGranted && isCoarseLocationGranted
    }

    fun requestLocationPermission() {
        locationPermissionRequest.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
            ),
        )
    }
}
