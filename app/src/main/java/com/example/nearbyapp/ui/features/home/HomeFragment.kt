package com.example.nearbyapp.ui.features.home

import QuotePagingAdapter
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nearbyapp.base.BaseFragment
import com.example.nearbyapp.databinding.FragmentHomeBinding
import com.example.nearbyapp.ui.MainActivity
import com.example.nearbyapp.ui.features.home.viewmodel.HomeViewModel
import com.example.nearbyapp.utils.CurrentLocationCallback
import com.example.nearbyapp.utils.LatLng
import com.example.nearbyapp.utils.LocationManager
import com.example.nearbyapp.utils.PermissionCallback
import com.example.nearbyapp.utils.PermissionManger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding>(
        FragmentHomeBinding::inflate,
    ),
    PermissionCallback,
    CurrentLocationCallback {

    private val viewModel: HomeViewModel by viewModels()
    private val permissionManager: PermissionManger by lazy {
        PermissionManger(
            activity = activity as MainActivity,
            permissionCallback = this@HomeFragment,
        )
    }

    private val locationManager: LocationManager by lazy {
        LocationManager(context = context, currLocationCallback = this@HomeFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.venueRv.apply {
            val venueAdapter = QuotePagingAdapter()
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = venueAdapter

            lifecycleScope.launch {
                viewModel.nearByVenueFlow.collectLatest {
                    venueAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                }
            }
        }
    }

    override fun updatedCurrentLocation(latLng: LatLng) {
        Log.d("current location", "lat: ${latLng.lat} lng : ${latLng.lng}")
    }

    override fun handlePermanentDenial() {
        showPermissionSettingsDialog()
    }

    override fun onStart() {
        super.onStart()
        if (!permissionManager.isLocationPermissionAlreadyGranted()) {
            permissionManager.requestLocationPermission()
        } else {
            locationManager.startLocationTracking()
        }
    }

    private fun showPermissionSettingsDialog() {
        AlertDialog.Builder(context)
            .setMessage("Location permission is required for the app to function properly. Please enable it in the app settings.")
            .setPositiveButton("Open Settings") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("Cancel") { _, _ ->
                activity?.finish()
            }
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity?.packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    override fun onStop() {
        super.onStop()
        locationManager.stopLocationTracking()
    }
}
