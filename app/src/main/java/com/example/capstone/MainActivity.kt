package com.example.capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.Login.App
import com.example.capstone.Login.LoginViewModel
import com.example.capstone.adapter.StoreAdapter
import com.example.capstone.data.StoreData
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {


    private lateinit var drawerLayout: DrawerLayout
    var data: StoreData? = null

    private val viewModel by viewModels<LoginViewModel>()

    private lateinit var recyclerView_store: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView_store = findViewById(R.id.recyclerview_main)
        drawerLayout = findViewById(R.id.main_drawer_layout)
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.navi_menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(false) // 뒤로가기 버튼 활성화 여부
        supportActionBar?.setDisplayShowTitleEnabled(false)

        getStoreList()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_login, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        //로그인 여부에 따른 메뉴 표시
        loginCheck()
        if(App.nowLogin) {
            menu?.getItem(0)?.isEnabled = false
            menu?.getItem(1)?.isEnabled = true
            menu?.getItem(2)?.isEnabled = true
            menu?.getItem(3)?.isEnabled = true
            Log.d("로그인 정보", "${App.memberInfo}, ${App.nowLogin}")
        } else {
            menu?.getItem(0)?.isEnabled = true
            menu?.getItem(1)?.isEnabled = false
            menu?.getItem(2)?.isEnabled = false
            menu?.getItem(3)?.isEnabled = false
            Log.d("로그인 정보", "${App.memberInfo}, ${App.nowLogin}")
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId) {
            R.id.menu_login -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_logout -> {
                App.memberInfo = null
                App.nowLogin = false
                Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()

            }
            /*
            R.id.menu_memberInfo -> {

            }
            R.id.menu_review -> {

            }*/
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getStoreList() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://473d-125-180-55-163.ngrok.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitService = retrofit.create(RetrofitAPI1::class.java)


        retrofitService
            .requestStoreData()
            .enqueue(object : Callback<StoreData> {
                override fun onResponse(call: Call<StoreData>, response: Response<StoreData>) {
                    response.takeIf { it.isSuccessful }
                        ?.body()
                        ?.let { it ->
                            // do something
                            data = response.body()
                            Log.d("성공성공!", response!!.body().toString())
                            //인증한 adapter에 Member 데이터 넣기
                            setStoreAdapter(it.data)

                        }
                }

                override fun onFailure(call: Call<StoreData>, t: Throwable) {
                    Log.d("통신 실패", "${t.message.toString()}")
                }
            })
    }
    private fun setStoreAdapter(storeList: List<StoreData.Data>) {
        val mAdapter = StoreAdapter(storeList)
        recyclerView_store.adapter = mAdapter
        recyclerView_store.layoutManager = LinearLayoutManager(this)
        mAdapter.notifyDataSetChanged()
        recyclerView_store.setHasFixedSize(true)
    }

    private fun loginCheck() {
        if(App.nowLogin) {
            if(App.memberInfo == null) {
                viewModel.requestMemberInfo(App.prefs.token!!)
                viewModel.memberInfo.observe(this, {
                    App.memberInfo = it
                })
                Log.d("로그인 정보", "${App.memberInfo}, ${App.nowLogin}")
            }
        }
    }
}