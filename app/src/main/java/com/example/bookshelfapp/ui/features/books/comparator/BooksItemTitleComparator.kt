package com.example.bookshelfapp.ui.features.books.comparator

import com.example.bookshelfapp.data.features.books.repository.remote.model.BooksItem

class BooksItemTitleComparator : Comparator<BooksItem> {
    override fun compare(book1: BooksItem, book2: BooksItem): Int {
        return book1.title.orEmpty().compareTo(book2.title.orEmpty(), ignoreCase = true)
    }
}
