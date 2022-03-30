package com.example.capstone.Retrofit

import com.example.capstone.data.MemberResponse
import com.example.capstone.data.ReviewData
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitReview {

    @POST("review/{foodid}")
    fun postReview(
        @Header("X-AUTH-TOKEN") token: String,
        @Path("foodid") foodid: Int,
        @Body jsonBody: ReviewData) : Call<MemberResponse>

    @Multipart
    @POST("user/review/{reviewid}/photos")
    fun postReviewPhoto(
        @Header("X-AUTH-TOKEN") token: String,
        @Path("reviewid") reviewid: Int,
        @Part photo: List<MultipartBody.Part>) : Call<List<Int>>
}