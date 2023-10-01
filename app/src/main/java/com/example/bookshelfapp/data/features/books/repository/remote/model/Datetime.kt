package com.smrize.app.data.auth.model

import com.google.gson.annotations.SerializedName

data class Datetime(
    @SerializedName("timezone")
    val timezone: String,
    @SerializedName("value")
    val value: String,
)
