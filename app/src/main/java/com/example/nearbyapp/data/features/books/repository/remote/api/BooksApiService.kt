package com.example.nearbyapp.data.features.books.repository.remote.api

import com.example.nearbyapp.data.features.books.repository.remote.model.BooksItem
import retrofit2.Response
import retrofit2.http.GET

interface BooksApiService {
    @GET("b/ZEDF")
    suspend fun getBooks(): Response<List<BooksItem>>
}
