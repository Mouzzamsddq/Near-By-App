package com.example.nearbyapp.base

import org.json.JSONObject
import retrofit2.Response

abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let { return Resource.success(it) }
                if (body == null) {
                    return Resource.success(null)
                }
            }
            var jObjError: JSONObject
            var errorMessage = ""
            try {
                jObjError = response.errorBody()?.string()?.let { JSONObject(it) } ?: JSONObject("")
                val errors = jObjError.getJSONArray("errors").getJSONObject(0)
                errorMessage = errors.getString("reason")
            } catch (e: java.lang.Exception) {
                errorMessage = response.errorBody()?.string() ?: response.message()
            } finally {
                if (errorMessage.isEmpty()) errorMessage = "Oops! Something Went Wrong"
            }
            return error(errorMessage)
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Resource<T> {
        return Resource.error(message)
    }
}
