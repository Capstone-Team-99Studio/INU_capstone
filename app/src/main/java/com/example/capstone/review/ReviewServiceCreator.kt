package com.example.capstone.review

import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.capstone.Retrofit.MemberService
import com.example.capstone.Retrofit.RetrofitReview
import com.example.capstone.data.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class ReviewServiceCreator {
    private val BASE_URL = "https://473d-125-180-55-163.ngrok.io/"
    private val client : RetrofitReview

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    init {
        client = retrofit.create(RetrofitReview::class.java)
    }
    fun requestReview(token:String, foodId: Int, reviewBody:ReviewData) : LiveData<Int> {

        val livedata = MutableLiveData<Int>()
        val call = client.postReview(token, foodId, reviewBody)

        call.enqueue(object: Callback<MemberResponse> {
            override fun onFailure(call: Call<MemberResponse>, t: Throwable) {
                Log.e("Review Data 전송 error", "${t.message}")
            }

            override fun onResponse(
                call: Call<MemberResponse>,
                response: Response<MemberResponse>
            ) {
                if(response.isSuccessful){
                    Log.d("Review Data 전송", "성공")
                    val body = response.body()
                    livedata.value = body?.id
                } else {
                    livedata.value = -1
                }
            }

        })
        return livedata
    }
    fun requestReviewPhoto(token:String, id:Int, photo:List<MultipartBody.Part>) : LiveData<List<Int>> {
        val livedata = MutableLiveData<List<Int>>()
        val call = client.postReviewPhoto(token, id, photo)

        call.enqueue(object: Callback<List<Int>> {
            override fun onResponse(call: Call<List<Int>>, response: Response<List<Int>>) {
                if (response.isSuccessful) {
                    Log.d("Review Photo 전송", "성공")

                    val body = response.body()
                    livedata.value = body!!
                }
                else {
                    Log.e("Review Data 전송", "실패")
                    Log.e("errorBody() is ",response.errorBody()!!.string())
                    livedata.value = listOf(-1)
                }
            }

            override fun onFailure(call: Call<List<Int>>, t: Throwable) {
                Log.e("Review Photo 전송 에러", "${t.message}")
            }
        })
        return livedata
    }
}


