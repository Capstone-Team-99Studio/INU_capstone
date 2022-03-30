package com.example.capstone.data

import com.google.gson.annotations.SerializedName

data class register(
    @SerializedName("email") val email: String,
    @SerializedName("nickName") val nickName: String,
    @SerializedName("password") val password: String,
)

data class emailResponse(
    @SerializedName("response") val response: String
)

data class emailkey(
    @SerializedName("email") val email: String
)

data class VerifyCode(
    @SerializedName("code") val code: String
)

data class MemberResponse(
    @SerializedName("id") val id: Int
    )
