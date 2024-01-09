package com.example.nearbyapp.utils

import android.content.Context
import android.widget.Toast

object Helper {

    fun showToast(context: Context?, msg: String) {
        context?.let {
            Toast.makeText(it, "$msg", Toast.LENGTH_SHORT).show()
        }
    }
}
