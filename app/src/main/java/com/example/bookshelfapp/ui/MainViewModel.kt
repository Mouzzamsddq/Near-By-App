package com.example.bookshelfapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookshelfapp.data.features.main.repository.MainRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepo: MainRepo,
) : ViewModel() {

    private val _isUserAuthenticated = MutableLiveData<Boolean>()
    val isUserAuthenticated: LiveData<Boolean> = _isUserAuthenticated

    fun checkIsUserAuthenticated() {
        _isUserAuthenticated.postValue(mainRepo.isUserAuthenticated())
    }
}
