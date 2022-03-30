package com.example.capstone.Retrofit

import com.example.capstone.data.*
import retrofit2.Call
import retrofit2.http.*

interface RetrofitEmail {

    @POST("email")
    fun postEmail(
        @Body jsonbody: emailkey
    ): Call<emailResponse>

    @POST("verifyCode/{email}")
    fun postVerifyCode(
        @Body jsonbody: VerifyCode, @Path("email") email: String
    ): Call<emailResponse>

    @POST("register")
    fun registerMember(
        @Body jsonbody: register
    ): Call<MemberResponse>
}
