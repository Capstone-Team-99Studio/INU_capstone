package com.example.capstone.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.capstone.MenuActivity
import com.example.capstone.R
import com.example.capstone.data.StoreData

class StoreAdapter(private var stores: List<StoreData.Data>?) : RecyclerView.Adapter<StoreAdapter.ViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.store_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item: StoreData.Data = stores!!.get(position)

        holder.storeName.text = stores?.get(position)?.name
        holder.storeDetail.text = stores?.get(position)?.introduce

        if (item.photoId != null) {

            val requestOptions = RequestOptions()
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.ic_launcher_background)

            Glide.with(holder.storeImage)
                .load("https://473d-125-180-55-163.ngrok.io/circles/view/photo/${item.photoId}")
                .fitCenter()
                .apply(requestOptions)
                .override(200,200)
                .into(holder.storeImage)
        } else {
            holder.storeImage.setImageResource(R.drawable.ic_launcher_background)
        }

        holder.itemView.setOnClickListener {
            var intent = Intent(holder.itemView?.context, MenuActivity::class.java)
            intent.putExtra("id", stores?.get(position)?.id)
            Log.d("스토어 아이디 = ", "${stores?.get(position)?.id}")
            startActivity(holder.itemView.context, intent, null)
        }
    }

    override fun getItemCount(): Int {
        Log.d("리턴 사이즈", "${stores?.size}")
        return stores!!.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        var storeName: TextView = view.findViewById(R.id.store_name)
        var storeDetail: TextView = view.findViewById(R.id.store_detail)
        var storeImage: ImageView = view.findViewById(R.id.store_img)

    }
}