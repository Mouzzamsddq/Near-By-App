package com.example.bookshelfapp.ui.features.books.comparator

import com.example.bookshelfapp.data.features.books.repository.remote.model.BooksItem

class BooksItemIsFavComparator : Comparator<BooksItem> {
    override fun compare(book1: BooksItem, book2: BooksItem): Int {
        return when {
            book1.isFav == book2.isFav -> 0
            book1.isFav == true -> -1
            book2.isFav == true -> 1
            else -> 0
        }
    }
}
