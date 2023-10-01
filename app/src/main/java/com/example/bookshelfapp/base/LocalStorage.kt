package com.example.bookshelfapp.base

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
}
