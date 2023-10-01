package com.example.bookshelfapp.data.features.favourites.repository.local

import com.example.bookshelfapp.base.Resource
import com.example.bookshelfapp.data.features.favourites.repository.local.dao.FavBooksDao
import javax.inject.Inject

class FavBooksLocalDataSource @Inject constructor(
    private val favBooksDao: FavBooksDao,
) {
    suspend fun addRemoveFavBooks(favBook: FavBook): Resource<Boolean> {
        return if (favBooksDao.isFavBook(favBook.bookId)) {
            favBooksDao.delete(favBook)
            Resource.success(data = false)
        } else {
            favBooksDao.insert(favBook = favBook)
            Resource.success(data = true)
        }
    }

    suspend fun isFavBook(bookId: String) = favBooksDao.isFavBook(bookId = bookId)
}
