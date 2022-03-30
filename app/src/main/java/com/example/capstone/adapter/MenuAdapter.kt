package com.example.capstone.adapter
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.*
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.AllReviewActivity
import com.example.capstone.data.Menu
import com.example.capstone.R
import com.example.capstone.ReviewActivity
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
        val menuDetail: TextView = itemView.findViewById(R.id.menu_detail)
        val menuImg: ImageView = itemView.findViewById(R.id.menu_img)
        val menuRating: TextView = itemView.findViewById(R.id.rating)
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
        holder.menuDetail.text = items?.introduce
        holder.menuRating.text = 3.0.toString()
        holder.menuImg.setImageResource(R.drawable.ic_launcher_background)

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
                    val intent = Intent(holder.itemView?.context, ReviewActivity::class.java)
                    intent.putExtra("storeName", items?.name)
                    intent.putExtra("food_id", items?.id)
                    startActivity(holder.itemView.context, intent, null)
                }
            })
        }
    }
}