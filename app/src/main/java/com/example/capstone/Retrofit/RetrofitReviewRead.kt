package com.example.capstone.Retrofit

import com.example.capstone.data.StoreData
import com.example.capstone.data.review
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitReviewRead {

    @GET("foods/{id}")
    fun requestReviewData(@Path("id") id: Int): Call<review>
}