package com.example.bookshelfapp.data.features.books.repository.remote.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
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
    val lastChapterDate: Long?,
    @SerializedName("title")
    val title: String?,
    var isFav: Boolean?
) : Parcelable
