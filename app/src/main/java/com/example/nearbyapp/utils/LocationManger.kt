package com.example.nearbyapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class LocationManager(
    private val context: Context?,
    private val currLocationCallback: CurrentLocationCallback,
    private var timeInterval: Long = 10000,
    private var minimalDistance: Float = 100f,
) {

    private var request: LocationRequest
    private var locationClient: FusedLocationProviderClient? =
        context?.let { LocationServices.getFusedLocationProviderClient(it) }
    private var locationCallback: LocationCallback

    init {
        request = createRequest()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let {
                    currLocationCallback.updatedCurrentLocation(
                        latLng = LatLng(
                            lat = it.latitude,
                            lng = it.longitude,
                        ),
                    )
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocationCallBack() {
        locationClient?.lastLocation?.addOnSuccessListener {
            it?.let {
                currLocationCallback.updatedCurrentLocation(
                    latLng = LatLng(
                        it.longitude,
                        it.latitude,
                    ),
                )
            }
        }
    }

    private fun createRequest(): LocationRequest =
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, timeInterval).apply {
            setMinUpdateDistanceMeters(minimalDistance)
            setWaitForAccurateLocation(true)
        }.build()

    @SuppressLint("MissingPermission")
    fun startLocationTracking() {
        locationClient?.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())
    }

    fun stopLocationTracking() {
        locationClient?.flushLocations()
        locationClient?.removeLocationUpdates(locationCallback)
    }

    fun isGpsEnabled(): Boolean {
        val locationManager: LocationManager =
            context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}
