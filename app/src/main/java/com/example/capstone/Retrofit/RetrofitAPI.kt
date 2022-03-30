package com.example.capstone

import com.example.capstone.data.StoreData
import retrofit2.Call
import retrofit2.http.*


interface RetrofitAPI1 {
        @GET("stores")
        fun requestStoreData(): Call<StoreData>
}