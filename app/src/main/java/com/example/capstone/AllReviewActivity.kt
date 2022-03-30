package com.example.capstone

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.Retrofit.RetrofitMenu
import com.example.capstone.Retrofit.RetrofitReviewRead
import com.example.capstone.adapter.MenuAdapter
import com.example.capstone.adapter.ReviewAdapter
import com.example.capstone.data.Menu
import com.example.capstone.data.review
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.properties.Delegates

class AllReviewActivity : AppCompatActivity() {

    var reviewValue: review.Data? = null
    private lateinit var recyclerView_review: RecyclerView
    private var foodId by Delegates.notNull<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_review)

        recyclerView_review = findViewById(R.id.recyclerview_review)
        val foodId = intent?.getIntExtra("food_id",0)
        Log.d("food_id", "$foodId")
        if (foodId != null) {
            getReviewList(foodId)
        }
    }

    private fun getReviewList(id: Int) {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://473d-125-180-55-163.ngrok.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitService = retrofit.create(RetrofitReviewRead::class.java)

        retrofitService
            .requestReviewData(id)
            .enqueue(object : Callback<review> {
                override fun onResponse(call: Call<review>, response: Response<review>) {
                    response.takeIf { it.isSuccessful }
                        ?.body()
                        ?.let { it ->
                            // do something
                            reviewValue = response.body()?.data
                            Log.d("리뷰 불러오기 성공!", response!!.body().toString())
                            Log.d("리뷰 불러오기 성공!", reviewValue.toString())
                            //인증한 adapter에 Member 데이터 넣기
                            setReviewAdapter(reviewValue?.reviewList)

                        }
                }

                override fun onFailure(call: Call<review>, t: Throwable) {
                    Log.d("리뷰 통신 실패", "${t.message.toString()}")
                }
            })
    }
    private fun setReviewAdapter(review: List<review.Data.ReviewX>?) {
        val mAdapter = ReviewAdapter(review!!)
        recyclerView_review.adapter = mAdapter
        recyclerView_review.layoutManager = LinearLayoutManager(this)
        mAdapter.notifyDataSetChanged()
        recyclerView_review.setHasFixedSize(true)
    }
}