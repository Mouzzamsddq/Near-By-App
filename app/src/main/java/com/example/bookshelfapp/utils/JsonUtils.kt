package com.example.bookshelfapp.utils

import android.content.Context
import org.json.JSONObject
import java.io.IOException

object JsonUtils {
    fun loadJSONFromAsset(context: Context, fileName: String): JSONObject? {
        return try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val json = String(buffer, Charsets.UTF_8)
            JSONObject(json)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
