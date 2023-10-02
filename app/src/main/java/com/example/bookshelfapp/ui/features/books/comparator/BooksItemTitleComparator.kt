package com.example.bookshelfapp.ui.features.books.comparator

import com.example.bookshelfapp.data.features.books.repository.remote.model.BooksItem

class BooksItemTitleComparator(private val ascendingOrder: Boolean = true) : Comparator<BooksItem> {
    override fun compare(book1: BooksItem, book2: BooksItem): Int {
        val order = if (ascendingOrder) 1 else -1
        return book1.title.orEmpty().compareTo(book2.title.orEmpty(), ignoreCase = true) * order
    }
}
