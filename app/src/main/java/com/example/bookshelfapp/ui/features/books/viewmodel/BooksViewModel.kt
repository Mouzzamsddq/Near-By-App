package com.example.bookshelfapp.ui.features.books.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelfapp.base.Resource
import com.example.bookshelfapp.constants.StringConstant
import com.example.bookshelfapp.data.features.books.repository.BookRepo
import com.example.bookshelfapp.data.features.books.repository.remote.model.BooksItem
import com.example.bookshelfapp.data.features.favourites.repository.FavouriteBooksRepo
import com.example.bookshelfapp.data.features.favourites.repository.local.FavBook
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val bookRepo: BookRepo,
    private val favouriteBooksRepo: FavouriteBooksRepo,
) : ViewModel() {

    private val _booksData = MutableLiveData<BooksDataStatus>()
    val booksDataStatus: LiveData<BooksDataStatus> = _booksData

    init {
        getBooks()
    }

    private fun getBooks() = viewModelScope.launch {
        _booksData.postValue(BooksDataStatus.Loading)
        val result = bookRepo.getBooks()
        when (result.status) {
            Resource.Status.SUCCESS -> {
                val books = result.data ?: emptyList()
                for (book in books) {
                    book.isFav = favouriteBooksRepo.isFavBook(
                        bookId = book.id ?: StringConstant.EMPTY_STRING,
                    )
                }
                _booksData.postValue(BooksDataStatus.Success(result.data ?: emptyList()))
            }

            Resource.Status.ERROR -> {
                _booksData.postValue(
                    BooksDataStatus.Error(
                        result.message ?: StringConstant.COMMON_ERROR_MESSAGE,
                    ),
                )
            }

            Resource.Status.LOADING -> {
                _booksData.postValue(BooksDataStatus.Loading)
            }
        }
    }

    fun refreshFavData() = viewModelScope.launch {
        when (val data = _booksData.value) {
            is BooksDataStatus.Success -> {
                _booksData.postValue(BooksDataStatus.Loading)
                val books = data.books
                for (book in books) {
                    book.isFav = favouriteBooksRepo.isFavBook(
                        bookId = book.id ?: StringConstant.EMPTY_STRING,
                    )
                }
                _booksData.postValue(BooksDataStatus.Success(books))
            }

            else -> Unit
        }
    }

    fun addRemoveFavBook(book: BooksItem, pos: Int) = viewModelScope.launch {
        val result = favouriteBooksRepo.addRemoveFavBooks(
            FavBook(
                bookId = book.id ?: StringConstant.EMPTY_STRING,
                book = book,
                isFavBook = true,
            ),
        )

        when (result.status) {
            Resource.Status.SUCCESS -> {
                val data = _booksData.value
                data?.let {
                    when (it) {
                        is BooksDataStatus.Success -> {
                            val books = it.books
                            books.getOrNull(pos)?.isFav = result.data ?: false
                            _booksData.postValue(BooksDataStatus.Success(books))
                        }

                        else -> Unit
                    }
                }
            }

            else -> Unit
        }
    }

    sealed class BooksDataStatus {
        data class Success(val books: List<BooksItem>) : BooksDataStatus()
        data class Error(val errorMessage: String) : BooksDataStatus()
        object Loading : BooksDataStatus()
    }
}
