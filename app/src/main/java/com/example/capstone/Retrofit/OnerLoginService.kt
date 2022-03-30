package com.example.capstone.Retrofit

import com.example.capstone.data.LoginBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface OnerLoginService : LoginService {

    @POST("login/business")
    fun requestBusinessLogin(@Body jsonbody: LoginBody) : Call<String>
}