package com.example.capstone.data

import com.google.gson.annotations.SerializedName

data class StoreData(
    val `data`: List<Data>

){
    data class Data(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name:String,
        @SerializedName("introduce") val introduce: String
    )
}







