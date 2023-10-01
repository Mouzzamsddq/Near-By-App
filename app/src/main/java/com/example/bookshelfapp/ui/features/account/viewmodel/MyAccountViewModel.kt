package com.example.bookshelfapp.ui.features.account.viewmodel

import androidx.lifecycle.ViewModel
import com.example.bookshelfapp.data.features.auth.repository.AuthRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyAccountViewModel @Inject constructor(
    private val authRepo: AuthRepo,
) : ViewModel() {

    fun logout() {
        authRepo.logout()
    }
}
