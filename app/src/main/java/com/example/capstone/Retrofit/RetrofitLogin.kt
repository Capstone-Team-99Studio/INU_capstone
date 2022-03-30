package com.example.capstone.Retrofit

import com.example.capstone.data.LoginBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitLogin {
    @FormUrlEncoded
    @POST("login")
    fun requestLogin(
        @Body jsonBody: LoginBody): Call<String>
}