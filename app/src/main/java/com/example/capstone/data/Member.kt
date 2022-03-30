package com.example.capstone.data

import com.google.gson.annotations.SerializedName

data class Member(
    @SerializedName("data") val `data` : MemberInfo
)