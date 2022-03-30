package com.example.capstone

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.Retrofit.RetrofitMenu
import com.example.capstone.adapter.MenuAdapter
import com.example.capstone.data.Menu
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.properties.Delegates

class MenuActivity : AppCompatActivity() {

    var menuList: Menu? = null
    var foodValue: Menu.Data? = null

    private lateinit var recyclerView_menu: RecyclerView
    private lateinit var storeName: TextView
    private lateinit var storeNumber: TextView
    private lateinit var foodOrigin: TextView
    private var storeId by Delegates.notNull<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        recyclerView_menu = findViewById(R.id.recyclerview_food)
        storeName = findViewById(R.id.store_name2)
        storeNumber = findViewById(R.id.store_number)
        foodOrigin = findViewById(R.id.food_origin)

        val storeId = intent?.getStringExtra("id")?.toInt()
        if (storeId != null) {
            getMenuList(storeId)
        }


    }
    private fun getMenuList(id: Int) {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://473d-125-180-55-163.ngrok.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitService = retrofit.create(RetrofitMenu::class.java)

        retrofitService
            .requestFoodData(id)
            .enqueue(object : Callback<Menu> {
                override fun onResponse(call: Call<Menu>, response: Response<Menu>) {
                    response.takeIf { it.isSuccessful }
                        ?.body()
                        ?.let { it ->
                            // do something
                            menuList = response.body()
                            foodValue = response.body()?.data
                            Log.d("메뉴 불러오기 성공!", response!!.body().toString())
                            Log.d("메뉴 불러오기 성공!", foodValue.toString())
                            //인증한 adapter에 Member 데이터 넣기
                            setMenuAdapter(foodValue?.foodListDtoList)
                            storeName.text = foodValue?.name
                            storeNumber.text = foodValue?.phoneNumber
                            foodOrigin.text = foodValue?.foodOrigin
                            val callNumber: Int = replaceNumber(foodValue!!.phoneNumber)
                            storeNumber.setOnClickListener {
                                val myUri = Uri.parse("tel:0$callNumber")
                                val intent = Intent(Intent.ACTION_DIAL, myUri)
                                startActivity(intent)
                            }

                        }
                }

                override fun onFailure(call: Call<Menu>, t: Throwable) {
                    Log.d("메뉴 통신 실패", "${t.message.toString()}")
                }
            })
    }
    private fun setMenuAdapter(menu: Array<Menu.Data.FoodListDto>?) {
        val mAdapter = MenuAdapter(menu!!, this)
        recyclerView_menu.adapter = mAdapter
        recyclerView_menu.layoutManager = LinearLayoutManager(this)
        mAdapter.notifyDataSetChanged()
        recyclerView_menu.setHasFixedSize(true)
    }
    private fun replaceNumber(number: String) : Int{
        val callNumber = number.replace("-","")
        return callNumber.toInt()
    }
}