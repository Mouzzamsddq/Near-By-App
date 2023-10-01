package com.example.bookshelfapp.data.features.favourites.repository

import com.example.bookshelfapp.data.features.favourites.repository.local.FavBook
import com.example.bookshelfapp.data.features.favourites.repository.local.FavBooksLocalDataSource
import javax.inject.Inject

class FavouriteBooksRepo @Inject constructor(
    private val favBooksLocalDataSource: FavBooksLocalDataSource,
) {

    suspend fun addRemoveFavBooks(favBook: FavBook) = favBooksLocalDataSource.addRemoveFavBooks(favBook)

    suspend fun isFavBook(bookId: String) = favBooksLocalDataSource.isFavBook(bookId = bookId)

    fun getFavBooks() = favBooksLocalDataSource.getFavBooks()
}
