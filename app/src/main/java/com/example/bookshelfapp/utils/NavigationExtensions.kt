package com.example.bookshelfapp.utils

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

fun NavController.navigateSafe(
    @IdRes action: Int? = null,
    args: Bundle? = null,
    isNavigateUP: Boolean = false
) = try {
    if (isNavigateUP) {
        navigateUp()
    } else if (action != null) {
        navigate(action, args)
    }
    true
} catch (e: Exception) {
    e.printStackTrace()
    false
}

fun Fragment.findNavControllerSafely(): NavController? = if (isAdded) {
    findNavController()
} else {
    null
}
