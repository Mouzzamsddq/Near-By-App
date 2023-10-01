package com.example.bookshelfapp.data.features.books.repository.remote

import com.example.bookshelfapp.base.BaseDataSource
import com.example.bookshelfapp.data.features.books.repository.remote.api.BooksApiService
import javax.inject.Inject

class BookRemoteDataSource @Inject constructor(
    private val booksApiService: BooksApiService,
) : BaseDataSource() {

    suspend fun getBooks() = getResult {
        booksApiService.getBooks()
    }
}
