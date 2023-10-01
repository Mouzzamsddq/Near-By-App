package com.example.bookshelfapp.ui.features.details.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelfapp.base.Resource
import com.example.bookshelfapp.constants.StringConstant
import com.example.bookshelfapp.data.features.books.repository.remote.model.BooksItem
import com.example.bookshelfapp.data.features.favourites.repository.FavouriteBooksRepo
import com.example.bookshelfapp.data.features.favourites.repository.local.FavBook
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val favouriteBooksRepo: FavouriteBooksRepo,
) : ViewModel() {

    private val _favStatus = MutableLiveData<Boolean>()
    val favStatus: LiveData<Boolean> = _favStatus
    fun addRemoveFavBook(book: BooksItem) = viewModelScope.launch {
        val result = favouriteBooksRepo.addRemoveFavBooks(
            FavBook(
                bookId = book.id ?: StringConstant.EMPTY_STRING,
                book = book,
                isFavBook = true,
            ),
        )

        when (result.status) {
            Resource.Status.SUCCESS -> {
                _favStatus.postValue(result.data ?: false)
            }

            else -> Unit
        }
    }
}
