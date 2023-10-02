package com.example.bookshelfapp.ui.features.books.comparator

import com.example.bookshelfapp.data.features.books.repository.remote.model.BooksItem

class BooksItemHitsComparator : Comparator<BooksItem> {
    override fun compare(book1: BooksItem, book2: BooksItem): Int {
        return book1.hits?.compareTo(book2.hits ?: 0) ?: 0
    }
}