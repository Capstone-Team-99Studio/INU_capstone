package com.example.capstone.data

import com.google.gson.annotations.SerializedName

data class ReviewData (
    @SerializedName("reviewStar") val reviewStar: String,
    @SerializedName("text")val text: String,
)