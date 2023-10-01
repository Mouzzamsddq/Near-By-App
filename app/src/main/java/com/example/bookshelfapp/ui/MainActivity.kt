package com.example.bookshelfapp.ui

import android.os.Bundle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.bookshelfapp.R
import com.example.bookshelfapp.base.BaseActivity
import com.example.bookshelfapp.databinding.ActivityMainBinding
import com.example.bookshelfapp.utils.gone
import com.example.bookshelfapp.utils.show

class MainActivity : BaseActivity<ActivityMainBinding>(
    ActivityMainBinding::inflate,
) {

    private val navController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.bottomNavView.setupWithNavController(navController)
        addDestinationChangeListener()
        if(true) {
            navigationToFragment(id = R.id.booksFragment, arguments = null)
        }

    }

    private fun addDestinationChangeListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.label) {
                getString(R.string.home),
                getString(R.string.favourites),
                getString(R.string.profile) -> binding.bottomNavView.show()

                else -> binding.bottomNavView.gone()
            }
        }
    }
    private fun navigationToFragment(id: Int, arguments: Bundle?) {
        navController.navigate(
            resId = id,
            args = arguments,
            navOptions = NavOptions.Builder().apply {
                setEnterAnim(R.anim.slide_in_right)
                setExitAnim(R.anim.slide_out_left)
                setPopEnterAnim(R.anim.slide_in_left)
                setPopExitAnim(R.anim.slide_out_right)
            }.build(),
            navigatorExtras = null,
        )
    }
}
