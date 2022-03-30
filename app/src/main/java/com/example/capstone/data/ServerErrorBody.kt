package com.example.capstone.data

import com.google.gson.annotations.SerializedName

data class ServerErrorBody(
    @SerializedName("timestamp") val time :String,
    @SerializedName("status") val code:String,
    @SerializedName("error") val message:String,
    @SerializedName("path") val path:String
)
