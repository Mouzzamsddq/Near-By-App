package com.example.nearbyapp.ui.features.home

import VenuePagingAdapter
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
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

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding>(
        FragmentHomeBinding::inflate,
    ),
    PermissionCallback,
    CurrentLocationCallback {

    private val viewModel: HomeViewModel by viewModels()
    private val venueAdapter by lazy {
        VenuePagingAdapter()
    }
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
        binding.apply {
            loaderView.root.isVisible = true
            venueRv.apply {
                setHasFixedSize(true)
                adapter = venueAdapter
            }
            viewModel.getSavedUserLocation()?.let {
                viewModel.changeDetailUrl(it)
            }
            viewModel.nearByVenue.observe(viewLifecycleOwner) {
                venueAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }

            venueAdapter.addLoadStateListener { loadState ->
                loaderView.root.isVisible = loadState.refresh == LoadState.Loading
                venueRv.isVisible = loadState.source.refresh is LoadState.NotLoading
                retryBtn.isVisible = loadState.source.refresh is LoadState.Error
                errorTextMessage.isVisible = loadState.source.refresh is LoadState.Error
                if (loadState.source.refresh is LoadState.NotLoading &&
                    venueAdapter.itemCount == 0
                ) {
                    venueRv.isVisible = false
                    noVenuesIv.isVisible = true
                    noVenuesTv.isVisible = true
                } else {
                    Log.d("kkk","called")
                    venueRv.isVisible = true
                    noVenuesIv.isVisible = false
                    noVenuesTv.isVisible = false
                }
            }
        }
    }

    override fun updatedCurrentLocation(latLng: LatLng) {
        Log.d("current location", "lat: ${latLng.lat} lng : ${latLng.lng}")
        viewModel.changeDetailUrl(latLng)
    }

    override fun handlePermanentDenial() {
        showPermissionSettingsDialog()
    }

    override fun permissionGranted() {
        locationManager.startLocationTracking()
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
