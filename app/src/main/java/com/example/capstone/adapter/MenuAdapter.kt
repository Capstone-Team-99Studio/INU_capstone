package com.example.capstone.adapter
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.capstone.*
import com.example.capstone.Login.App
import com.example.capstone.data.Menu
import com.example.capstone.review.MyDialog

class MenuAdapter(private val foods: Array<Menu.Data.FoodListDto>, private val context: Context) : RecyclerView.Adapter<MenuAdapter.ViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.food_list, parent, false)
        return ViewHolder(view)
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val menuName: TextView = itemView.findViewById(R.id.menu_name)
        val menuImg: ImageView = itemView.findViewById(R.id.menu_img)
        val menuRating: TextView = itemView.findViewById(R.id.rating)
        val menuStatus: ImageView = itemView.findViewById(R.id.soldout)
        val menuPrice: TextView = itemView.findViewById(R.id.menu_price)
    }

    override fun getItemCount(): Int {
        Log.d("리턴 사이즈", "${foods?.size}")
        return foods!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var items = foods!!.get(position)
        Log.d("recyclerview ", "${items.name}/${items.price}/${items.status}")
        holder.menuName.text = items?.name
        holder.menuPrice.text = items?.price

        if (items.rateAverage == null) {
            holder.menuRating.text = "0.0"
        } else {
            holder.menuRating.text = items?.rateAverage.toString()
        }
        if (items.status == "SOLDOUT") {
            holder.menuStatus.setImageResource(R.drawable.soldout)
        }
        else {
            holder.menuStatus.setImageResource(R.drawable.onsale)
        }
        if (items.photoId != null) {

            val requestOptions = RequestOptions()
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.ic_launcher_background)

            Glide.with(holder.menuImg)
                .load("https://473d-125-180-55-163.ngrok.io/circles/view/photo/${items.photoId}")
                .fitCenter()
                .apply(requestOptions)
                .override(200,200)
                .into(holder.menuImg)
        } else {
            holder.menuImg.setImageResource(R.drawable.ic_launcher_background)
        }


        holder.itemView.setOnClickListener {
            val dialog = MyDialog(context)
            dialog.myDig()
            dialog.setButtonListener(object: MyDialog.SetButtonListener {
                override fun readOnClicked() {
                    val intent = Intent(holder.itemView?.context, AllReviewActivity::class.java)
                    intent.putExtra("food_id", items?.id)
                    Log.d("food id", "${items?.id}")
                    startActivity(holder.itemView.context, intent, null)
                }

                override fun writeOnClicked() {
                    if (App.nowLogin) {
                        val intent = Intent(holder.itemView?.context, ReviewActivity::class.java)
                        intent.putExtra("storeName", items?.name)
                        intent.putExtra("food_id", items?.id)
                        startActivity(holder.itemView.context, intent, null)
                    } else {
                        val builder = AlertDialog.Builder(holder.itemView?.context)
                        builder.setTitle("로그인 필요")
                            .setMessage("로그인을 하시겠습니까?")
                            .setPositiveButton("예", DialogInterface.OnClickListener { dialog, id ->
                                val intent =
                                    Intent(holder.itemView?.context, LoginActivity::class.java)
                                startActivity(holder.itemView.context, intent, null)
                            })
                            .setNegativeButton(
                                "아니요",
                                DialogInterface.OnClickListener { dialog, id ->
                                })
                            .show()
                    }
                }
            })
        }
    }
}