package com.example.capstone.data

import com.google.gson.annotations.SerializedName

data class ClientErrorBody(
    @SerializedName("code")val code:String,
    @SerializedName("message") val message:String
)
