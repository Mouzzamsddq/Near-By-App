import android.annotation.SuppressLint
import android.app.Activity
import android.os.Looper
import com.example.nearbyapp.utils.CurrentLocationCallback
import com.example.nearbyapp.utils.LatLng
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class LocationManager(
    activity: Activity,
    currLocationCallback: CurrentLocationCallback,
    private var timeInterval: Long = 60,
    private var minimalDistance: Float = 500f,
) {

    private var request: LocationRequest
    private var locationClient: FusedLocationProviderClient
    private var locationCallback: LocationCallback

    init {
        locationClient = LocationServices.getFusedLocationProviderClient(activity)
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

    private fun createRequest(): LocationRequest =
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, timeInterval).apply {
            setMinUpdateDistanceMeters(minimalDistance)
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            setWaitForAccurateLocation(true)
        }.build()

    @SuppressLint("MissingPermission")
    fun startLocationTracking() {
        locationClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())
    }

    fun stopLocationTracking() {
        locationClient.flushLocations()
        locationClient.removeLocationUpdates(locationCallback)
    }
}
