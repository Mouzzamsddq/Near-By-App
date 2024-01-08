package com.example.nearbyapp.ui

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import com.example.nearbyapp.base.BaseActivity
import com.example.nearbyapp.databinding.ActivityMainBinding
import com.example.nearbyapp.utils.PermissionCallback
import com.example.nearbyapp.utils.PermissionManger
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity :
    BaseActivity<ActivityMainBinding>(
        ActivityMainBinding::inflate,
    ),
    PermissionCallback {

    private val viewModel: MainViewModel by viewModels()
    private val navController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navHostFragment.navController
    }
    private val permissionManager: PermissionManger by lazy {
        PermissionManger(activity = this@MainActivity, permissionCallback = this@MainActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        if (!permissionManager.isLocationPermissionAlreadyGranted()) {
            permissionManager.requestLocationPermission()
        }
    }

    override fun handlePermanentDenial() {
        showPermissionSettingsDialog()
    }

    private fun showPermissionSettingsDialog() {
        AlertDialog.Builder(this)
            .setMessage("Location permission is required for the app to function properly. Please enable it in the app settings.")
            .setPositiveButton("Open Settings") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("Cancel") { _, _ ->
                finish()
            }
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }
}
