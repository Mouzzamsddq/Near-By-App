package com.smrize.app.data.auth.model

import com.google.gson.annotations.SerializedName

data class Error(
    @SerializedName("code")
    val code: String,
    @SerializedName("datetime")
    val datetime: Datetime,
    @SerializedName("reason")
    val reason: String,
)
