package com.example.capstone.Oner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone.Login.App
import com.example.capstone.Oner.CustomDialogStore
import com.example.capstone.Oner.FoodRegisterActivity
import com.example.capstone.Oner.StoreSettingActivity
import com.example.capstone.Retrofit.RetrofitMenu
import com.example.capstone.adapter.FoodAdapter
import com.example.capstone.data.Menu
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.capstone.databinding.ActivityOnerMainBinding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OnerMainActivity : AppCompatActivity() {
    val binding by lazy { ActivityOnerMainBinding.inflate(layoutInflater)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val adapter = FoodAdapter(this)
        binding.flist.adapter = adapter
        binding.flist.layoutManager =  LinearLayoutManager(this)
        Log.d("로그인 정보", "${App.memberInfo}, ${App.nowLogin}")
        binding.menuSingBtn1.setOnClickListener{
            val menuintent = Intent(this, FoodRegisterActivity::class.java)
            startActivity(menuintent)
        }
        val retrofit = Retrofit.Builder()
            .baseUrl("https://473d-125-180-55-163.ngrok.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitService = retrofit.create(RetrofitMenu::class.java)

        retrofitService.requestFoodData(1).enqueue(object : Callback<Menu>{
            override fun onResponse(call: Call<Menu>, response: Response<Menu>) {
                adapter.menuList = response.body()?.data?.foodListDtoList
                adapter.notifyDataSetChanged()
                Log.d("foodlist", "${response.body()?.data?.foodListDtoList}")
            }

            override fun onFailure(call: Call<Menu>, t: Throwable) {
                Log.d("foodlist", "통신 에러")
            }

        })

        binding.myStoreLayout.setOnClickListener(){
            val storeDialog = CustomDialogStore(this)
            storeDialog.showDialog()
            storeDialog.setButtonListener(object: CustomDialogStore.OnButtonClickListener {
            override fun okBtnClicked() {
                val intent = Intent(this@OnerMainActivity, StoreSettingActivity::class.java)
                startActivity(intent)
            }

            }) //화면 넘김
        }
    }
}