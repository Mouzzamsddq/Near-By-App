package com.example.nearbyapp.data.features.home.remote.api

import com.example.nearbyapp.data.features.home.remote.response.VenueResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeApiService {
    @GET("venues")
    suspend fun getVenues(
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
        @Query("client_id") clientId: String,
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("range") range: String,
    ): VenueResponse
}
