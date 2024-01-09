package com.example.nearbyapp.base

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class LocalStorage(application: Application) {
    private val sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(application)

    fun setString(key: String, value: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun setBoolean(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getString(key: String?): String? {
        return sharedPreferences.getString(key, null)
    }

    fun getBoolean(key: String): Boolean = sharedPreferences.getBoolean(key, false)

    fun setDouble(key: String, value: Double) {
        val editor = sharedPreferences.edit()
        editor.putLong(key, java.lang.Double.doubleToRawLongBits(value))
        editor.apply()
    }

    fun getDouble(key: String): Double {
        val rawValue = sharedPreferences.getLong(key, 0)
        return java.lang.Double.longBitsToDouble(rawValue)
    }
}
