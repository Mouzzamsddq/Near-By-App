package com.example.nearbyapp.data.features.books.repository

import com.example.nearbyapp.data.features.books.repository.remote.BookRemoteDataSource
import javax.inject.Inject

class BookRepo @Inject constructor(
    private val bookRemoteDataSource: BookRemoteDataSource,
) {

    suspend fun getBooks() = bookRemoteDataSource.getBooks()
}
