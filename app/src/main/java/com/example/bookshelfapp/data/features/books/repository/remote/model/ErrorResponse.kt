package com.example.bookshelfapp.data.features.books.repository.remote.model

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("errors")
    val errors: List<Error>,
)
