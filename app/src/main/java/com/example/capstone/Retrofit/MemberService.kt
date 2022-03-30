package com.example.capstone.Retrofit


import com.example.capstone.data.Member
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface MemberService {

    @GET("member")
    fun requestMemberInfo(@Header("X-AUTH-TOKEN") token : String) : Call<Member>

}