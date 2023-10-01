package com.example.bookshelfapp.ui.features.signup.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelfapp.R
import com.example.bookshelfapp.base.Resource
import com.example.bookshelfapp.constants.StringConstant
import com.example.bookshelfapp.data.features.auth.AuthRepo
import com.example.bookshelfapp.data.features.auth.repository.local.entity.Users
import com.example.bookshelfapp.utils.PatternUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepo: AuthRepo,
) : ViewModel() {

    private val _fieldValidationStatus = MutableLiveData<FieldsValidationStatus>()
    val fieldsValidationStatus: LiveData<FieldsValidationStatus> = _fieldValidationStatus

    private val _signUpStatus = MutableLiveData<SignUpStatus>()
    val signUpStatus: LiveData<SignUpStatus> = _signUpStatus

    private var selectedCountry = StringConstant.EMPTY_STRING

    fun performSignUp(name: String, password: String) = viewModelScope.launch {
        _signUpStatus.postValue(SignUpStatus.Loading)
        delay(1000)
        val result = authRepo.performSignup(
            Users(
                username = name,
                password = password,
                country = selectedCountry,
            ),
        )
        when (result.status) {
            Resource.Status.SUCCESS -> {
                _signUpStatus.postValue(SignUpStatus.Success)
            }

            Resource.Status.ERROR -> {
                _signUpStatus.postValue(
                    SignUpStatus.Error(
                        errorMessage = result.message ?: StringConstant.EMPTY_STRING,
                    ),
                )
            }

            Resource.Status.LOADING -> {
                _signUpStatus.postValue(SignUpStatus.Loading)
            }
        }
    }

    fun updateSelectedCountry(selectedCountry: String) {
        this.selectedCountry = selectedCountry
    }

    fun checkValidation(name: String?, password: String?, context: Context) {
        if (name.isNullOrBlank()) {
            _fieldValidationStatus.postValue(FieldsValidationStatus.NameError(context.getString(R.string.please_enter_valid_name)))
            return
        }
        if (password.isNullOrBlank()) {
            _fieldValidationStatus.postValue(
                FieldsValidationStatus.PasswordError(
                    context.getString(
                        R.string.please_enter_valid_password,
                    ),
                ),
            )
            return
        }
        if (PatternUtils.isValidPassword(password)) {
            _fieldValidationStatus.postValue(FieldsValidationStatus.Success)
        } else {
            _fieldValidationStatus.postValue(
                FieldsValidationStatus.PasswordError(
                    context.getString(
                        R.string.password_validity_error,
                    ),
                ),
            )
        }
    }

    sealed class SignUpStatus {
        object Success : SignUpStatus()
        data class Error(val errorMessage: String) : SignUpStatus()
        object Loading : SignUpStatus()
    }

    sealed class FieldsValidationStatus {
        data class NameError(val errorMessage: String) : FieldsValidationStatus()
        data class PasswordError(val errorMessage: String) : FieldsValidationStatus()
        object Success : FieldsValidationStatus()
    }
}
