package com.example.bookshelfapp.data.features.favourites.repository.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.bookshelfapp.data.features.favourites.repository.local.FavBook

@Dao
interface FavBooksDao {
    @Insert
    suspend fun insert(favBook: FavBook)

    @Delete
    suspend fun delete(favBook: FavBook)

    @Query("Select Exists(SELECT * FROm FAV_BOOKS WHERE bookId = :bookId)")
    suspend fun isFavBook(bookId: String): Boolean

    @Query("DELETE FROM fav_books WHERE bookId =:bookId")
    suspend fun removeFromFav(bookId: String)

    @Query("SELECT * FROM fav_books")
    fun getFavBooks(): LiveData<List<FavBook>>
}
