package com.example.bookshelfapp.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.bookshelfapp.R
import com.example.bookshelfapp.base.BaseActivity
import com.example.bookshelfapp.databinding.ActivityMainBinding
import com.example.bookshelfapp.ui.features.signin.SignInFragment
import com.example.bookshelfapp.utils.gone
import com.example.bookshelfapp.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(
    ActivityMainBinding::inflate,
) {

    private val viewModel: MainViewModel by viewModels()
    private val navController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding.bottomNavView.setupWithNavController(navController)
        addDestinationChangeListener()
        setObservers()
        viewModel.checkIsUserAuthenticated()
        binding.bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.booksFragment -> {
                    navController.navigate(R.id.booksFragment)
                    return@setOnItemSelectedListener true
                }

                R.id.favouritesBookFragment -> {
                    navController.navigate(R.id.favouritesBookFragment)
                    return@setOnItemSelectedListener true
                }
                // Add more cases for other menu items
                R.id.accountFragment -> {
                    navController.navigate(R.id.accountFragment)
                    return@setOnItemSelectedListener true
                }

                else -> return@setOnItemSelectedListener true
            }
        }
    }

    private fun setObservers() {
        viewModel.isUserAuthenticated.observe(this@MainActivity) { isAuthenticated ->
            val inflater = navController.navInflater
            val graph = inflater.inflate(R.navigation.nav_graph)
            if (isAuthenticated) {
                graph.setStartDestination(R.id.booksFragment)
            } else {
                graph.setStartDestination(R.id.signInFragment)
            }
            navController.graph = graph
        }
    }

    private fun addDestinationChangeListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            showHideLoaderView(show = false)
            when (destination.label) {
                getString(R.string.home),
                getString(R.string.favourites),
                getString(R.string.profile),
                -> binding.bottomNavView.show()

                else -> binding.bottomNavView.gone()
            }
        }
    }

    fun showHideLoaderView(show: Boolean) {
        binding.loaderView.container.isVisible = show
    }

    override fun onBackPressed() {
        val navHostFragment: Fragment? =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        val currentFragment = navHostFragment?.childFragmentManager?.fragments?.get(0)
        if (navHostFragment != null && currentFragment is SignInFragment) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}
