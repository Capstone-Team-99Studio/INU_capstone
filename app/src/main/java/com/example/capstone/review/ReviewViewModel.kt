package com.example.capstone.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.capstone.data.ReviewData
import okhttp3.MultipartBody

class ReviewViewModel: ViewModel() {

    val client : ReviewServiceCreator

    lateinit var postedImageId : LiveData<List<Int>>
    lateinit var postedReviewId : LiveData<Int>

    init {
        client = ReviewServiceCreator()
    }
    fun postReview(token: String, foodId: Int, reviewBody: ReviewData) {
        postedReviewId = client.requestReview(token, foodId, reviewBody)
    }
    fun postPhoto(token:String, id:Int, files:List<MultipartBody.Part>) {
        postedImageId = client.requestReviewPhoto(token, id, files)
    }

}


