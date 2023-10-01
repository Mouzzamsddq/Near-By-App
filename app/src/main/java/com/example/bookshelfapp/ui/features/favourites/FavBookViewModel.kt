package com.example.bookshelfapp.ui.features.favourites

import androidx.lifecycle.ViewModel
import com.example.bookshelfapp.data.features.favourites.repository.FavouriteBooksRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavBookViewModel @Inject constructor(
    private val favouriteBooksRepo: FavouriteBooksRepo,
) : ViewModel() {

    fun getFavBooks() = favouriteBooksRepo.getFavBooks()
}
