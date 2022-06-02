package com.example.capstone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.esafirm.imagepicker.model.Image
import com.example.capstone.Image.OnPreviewImageClick
import com.example.capstone.Image.SlideImageViewer
import com.example.capstone.R
import com.example.capstone.data.ImageUrlItem

class FoodReviewImageAdapter(val photoId: Int) : RecyclerView.Adapter<FoodReviewImageAdapter.FoodImageHolder>() {

    private val items = mutableListOf<ImageUrlItem>()
    inner class FoodImageHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        private val imagePreView = itemView?.findViewById<ImageView>(R.id.food_preview)


        fun bind(data: ImageUrlItem, position: Int) {

            if (imagePreView != null) {
                Glide.with(itemView.context)
                    .load(data.imageUrl)
                    .centerCrop()
                    .into(imagePreView)
            }
            imagePreView?.setOnClickListener {
                val imageList = ArrayList<String>()
                items.forEach {
                    imageList.add(it.imageUrl)
                }
                SlideImageViewer.start(itemView.context, imageList, position)
            }

        }

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FoodReviewImageAdapter.FoodImageHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_food_image, parent, false)
        return FoodImageHolder(view)
    }
    fun addImage(image: Image){
        this.items.add(ImageUrlItem(image.uri.toString()))
        notifyDataSetChanged()
    }

    fun addImageUrl(imagePath : String){
        this.items.add(ImageUrlItem(imagePath))
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: FoodReviewImageAdapter.FoodImageHolder, position: Int) {
        val data = items[position]
        holder.bind(data, position)
    }

    override fun getItemCount(): Int = 1
}