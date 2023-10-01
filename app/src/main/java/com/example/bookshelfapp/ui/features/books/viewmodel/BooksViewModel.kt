package com.example.bookshelfapp.ui.features.books.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelfapp.base.Resource
import com.example.bookshelfapp.constants.StringConstant
import com.example.bookshelfapp.data.features.books.repository.BookRepo
import com.example.bookshelfapp.data.features.books.repository.remote.model.BooksItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val bookRepo: BookRepo,
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

    sealed class BooksDataStatus {
        data class Success(val books: List<BooksItem>) : BooksDataStatus()
        data class Error(val errorMessage: String) : BooksDataStatus()
        object Loading : BooksDataStatus()
    }
}
