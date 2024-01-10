package com.example.nearbyapp.ui.features.home

import VenuePagingAdapter
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.SeekBar
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.example.nearbyapp.R
import com.example.nearbyapp.base.BaseFragment
import com.example.nearbyapp.databinding.FragmentHomeBinding
import com.example.nearbyapp.ui.MainActivity
import com.example.nearbyapp.ui.features.home.adapter.LoadStateFooterAdapter
import com.example.nearbyapp.ui.features.home.viewmodel.HomeViewModel
import com.example.nearbyapp.utils.CurrentLocationCallback
import com.example.nearbyapp.utils.Helper
import com.example.nearbyapp.utils.LatLng
import com.example.nearbyapp.utils.LocationManager
import com.example.nearbyapp.utils.PermissionCallback
import com.example.nearbyapp.utils.PermissionManger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding>(
        FragmentHomeBinding::inflate,
    ),
    PermissionCallback,
    CurrentLocationCallback {
    private val debouncePeriod = 1000L
    private var lastSeekBarValue: Int = 0
    private var debounceJob: Job? = null
    private val viewModel: HomeViewModel by viewModels()
    private val venueAdapter by lazy {
        VenuePagingAdapter {
            openUrl(url = it)
        }
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
    private var alertDialog: AlertDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            loaderView.root.isVisible = true
            venueRv.apply {
                setHasFixedSize(true)
                adapter = venueAdapter.withLoadStateFooter(
                    footer = LoadStateFooterAdapter { venueAdapter.retry() },
                )
            }
            viewModel.nearByVenue.observe(viewLifecycleOwner) {
                venueAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }

            venueAdapter.addLoadStateListener { loadState ->
                loaderView.root.isVisible = loadState.mediator?.refresh is LoadState.Loading
                retryBtn.isVisible =
                    loadState.mediator?.refresh is LoadState.Error && venueAdapter.itemCount == 0
                errorTextMessage.isVisible =
                    loadState.mediator?.refresh is LoadState.Error && venueAdapter.itemCount == 0
                noVenuesIv.isVisible =
                    loadState.refresh is LoadState.NotLoading && venueAdapter.itemCount == 0
                noVenuesTv.isVisible =
                    loadState.refresh is LoadState.NotLoading && venueAdapter.itemCount == 0
            }
            retryBtn.setOnClickListener {
                venueAdapter.retry()
            }

            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    debounceJob?.cancel()
                    debounceJob = CoroutineScope(Dispatchers.Main).launch {
                        delay(debouncePeriod)
                        if (progress != lastSeekBarValue) {
                            lastSeekBarValue = progress
                            viewModel.changeDistanceFilter(progress)
                        }
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                }
            })

            searchEt.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(
                    query: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int,
                ) {
                    query?.toString().let { q ->
                        crossIv.isVisible = !q.isNullOrBlank()
                        viewModel.searchByVenueName(if (q.isNullOrBlank()) null else q.trim())
                    }
                }
            })
            crossIv.setOnClickListener {
                searchEt.text.clear()
            }
        }
    }

    override fun updatedCurrentLocation(latLng: LatLng) {
        loadUpdatedDataBasedOnLocation(latLng = latLng, permissionCheckRequired = true)
    }

    private fun loadUpdatedDataBasedOnLocation(
        latLng: LatLng,
        permissionCheckRequired: Boolean =
            false,
    ) {
        if (permissionCheckRequired) {
            if (!permissionManager.isLocationPermissionAlreadyGranted()) {
                Helper.showToast(
                    context,
                    msg = "Please enable location permission from settings...!",
                )
                return
            }
            if (!locationManager.isGpsEnabled()) {
                Helper.showToast(context, msg = "Please enable gps to see the updated venues...!")
                return
            }
        }
        viewModel.updatedUserLocation(latLng)
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
            dismissAlertDialog()
            locationManager.getCurrentLocationCallBack()
            locationManager.startLocationTracking()
        }
    }

    private fun showPermissionSettingsDialog() {
        alertDialog = AlertDialog.Builder(context)
            .setCancelable(false)
            .setMessage(getString(R.string.location_permission_msg))
            .setPositiveButton(getString(R.string.open_settings)) { _, _ ->
                openAppSettings()
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                activity?.finish()
            }
            .show()
    }

    private fun dismissAlertDialog() {
        alertDialog?.dismiss()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts(getString(R.string.package_scheme), activity?.packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    override fun onStop() {
        super.onStop()
        locationManager.stopLocationTracking()
    }

    private fun openUrl(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }
}
