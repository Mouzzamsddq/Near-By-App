package com.example.bookshelfapp.data.features.books.repository.remote.model


import com.google.gson.annotations.SerializedName

data class BooksItem(
    @SerializedName("alias")
    val alias: String?,
    @SerializedName("hits")
    val hits: Int?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("lastChapterDate")
    val lastChapterDate: Int?,
    @SerializedName("title")
    val title: String?
)