package com.example.bookshelfapp.ui.features.signup.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookshelfapp.R
import com.example.bookshelfapp.utils.PatternUtils

class SignUpViewModel : ViewModel() {

    private val _fieldValidationStatus = MutableLiveData<FieldsValidationStatus>()
    val fieldsValidationStatus: LiveData<FieldsValidationStatus> = _fieldValidationStatus

    fun checkValidation(name: String?, password: String?, context: Context) {
        if (name.isNullOrBlank()) {
            _fieldValidationStatus.postValue(FieldsValidationStatus.NameError(context.getString(R.string.please_enter_valid_name)))
            return
        }
        if(password.isNullOrBlank()) {
            _fieldValidationStatus.postValue(FieldsValidationStatus.PasswordError(context.getString(R.string.please_enter_valid_password)))
            return
        }
        if(PatternUtils.isValidPassword(password)) {
            _fieldValidationStatus.postValue(FieldsValidationStatus.Success)
        } else {
            _fieldValidationStatus.postValue(FieldsValidationStatus.PasswordError(context.getString(R.string.password_validity_error)))
        }
    }

    sealed class FieldsValidationStatus {
        data class NameError(val errorMessage: String) : FieldsValidationStatus()
        data class PasswordError(val errorMessage: String) : FieldsValidationStatus()
        object Success : FieldsValidationStatus()
    }

}
