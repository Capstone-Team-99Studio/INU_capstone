package com.example.capstone.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.AllReviewActivity
import com.example.capstone.Oner.CustomDialog
import com.example.capstone.Oner.StoreSettingActivity
import com.example.capstone.R
import com.example.capstone.Retrofit.RetrofitMenu
import com.example.capstone.data.Menu
import com.example.capstone.databinding.FoodListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FoodAdapter(private val context: Context): RecyclerView.Adapter<foodHolder>(){

    var menuList : Array<Menu.Data.FoodListDto>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): foodHolder {
        val binding = FoodListBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return foodHolder(binding)
    }

    override fun onBindViewHolder(holder: foodHolder, position: Int) {
        val menu = menuList?.get(position)
        holder.setMenu(menu)

        holder.itemView.setOnClickListener(){
            val dialog =  CustomDialog(context)
            dialog.showDialog()
            dialog.setButtonListener(object: CustomDialog.OnButtonClickListener {
                override fun storesettingClicked() {
                    val intent = Intent(holder.itemView?.context, StoreSettingActivity::class.java)
                    intent.putExtra("storeName", menu?.name)
                    ContextCompat.startActivity(holder.itemView.context, intent, null)
                }

                override fun reviewReadClicked() {
                    val intent = Intent(holder.itemView?.context, AllReviewActivity::class.java)
                    intent.putExtra("food_id", menu?.id)
                    Log.d("food id", "${menu?.id}")
                    ContextCompat.startActivity(holder.itemView.context, intent, null)
                }

                override fun foodDeleteClicked() {
                    val retrofit = Retrofit.Builder()
                        .baseUrl("https://473d-125-180-55-163.ngrok.io/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                    val retrofitService = retrofit.create(RetrofitMenu::class.java)
                    val id = menu?.id
                    if (id != null) {
                        retrofitService.deleteFood(id).enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                Log.d("음식항목 삭제", "")
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                Log.d("음식항목 삭제", "통신 에러")
                            }

                        })
                    }

                }
            })
        }


    }

    override fun getItemCount(): Int {
        return menuList?.size?: 0
    }
}

class foodHolder(val binding: FoodListBinding): RecyclerView.ViewHolder(binding.root)
{


    init {
        itemView.setOnClickListener(){

        }
    }
    fun setMenu(menu: Menu.Data.FoodListDto?){
        menu?.let{
            binding.menuName.text = menu.name
            binding.menuPrice.text = menu.price
            binding.menuDetail.text = menu.introduce
            binding.rating.text = "3.0"
            binding.ratingImg.setImageResource(R.drawable.star)

        }
    }

}