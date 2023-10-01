package com.example.bookshelfapp.base

import androidx.room.TypeConverter
import com.example.bookshelfapp.data.features.books.repository.remote.model.BooksItem
import com.google.gson.Gson

object Converter {
    @TypeConverter
    fun toBookString(book: BooksItem): String {
        val gson = Gson()
        return gson.toJson(book)
    }

    @TypeConverter
    fun toBookObject(bookString: String): BooksItem {
        return Gson().fromJson(bookString, BooksItem::class.java)
    }
}
