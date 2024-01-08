package com.example.nearbyapp.utils

import android.content.Context
import android.widget.Toast

fun Context?.showToast(message: String) {
    this?.let {
        Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
    }
}
