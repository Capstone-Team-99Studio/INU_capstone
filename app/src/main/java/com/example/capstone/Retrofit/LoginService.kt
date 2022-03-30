package com.example.capstone.Retrofit

import com.example.capstone.data.LoginBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    @POST("login")
    fun requestLogin(@Body jsonbody: LoginBody) : Call<String>


}