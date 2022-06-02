package com.example.capstone.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.capstone.AllReviewActivity
import com.example.capstone.R
import com.example.capstone.data.review

class ReviewAdapter(private val reviews: List<review.Data.ReviewX>) : RecyclerView.Adapter<ReviewAdapter.ViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.review_list, parent, false)
        return ViewHolder(view)
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val reviewNickName: TextView = itemView.findViewById(R.id.review_nickname)
        val reviewDate: TextView = itemView.findViewById(R.id.food_date)
        val reviewRating: TextView = itemView.findViewById(R.id.food_rating)

        val reviewDetail: TextView = itemView.findViewById(R.id.food_review)
        val reviewStore: TextView = itemView.findViewById(R.id.food_store)
        val reviewStoreReview:TextView = itemView.findViewById(R.id.food_storereview)
        val reviewImage: ImageView = itemView.findViewById(R.id.food_Image)
    }

    override fun getItemCount(): Int {
        Log.d("리턴 사이즈", "${reviews?.size}")
        return reviews!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var items = reviews!!.get(position)

        Log.d("recyclerview ", "$items")
        holder.reviewNickName.text = items?.nickname
        if (items?.updateTime != null) {
            var reviewDate = items?.updateTime.toString()
            var reviewDateToken = reviewDate.split("T")
            holder.reviewDate.text = reviewDateToken[0]
        }
        else {
            var reviewDate =items?.createTime.toString()
            var reviewDateToken = reviewDate.split("T")
            holder.reviewDate.text = reviewDateToken[0]
        }

        holder.reviewRating.text = items?.star.toString()
        holder.reviewDetail.text = items?.text

        if (items?.photoIds.isNotEmpty()) {
            val photoId = items?.photoIds[0]
            val requestOptions = RequestOptions()
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.ic_launcher_background)

            Glide.with(holder.reviewImage)
                .load("https://473d-125-180-55-163.ngrok.io/circles/view/photo/${photoId}")
                .fitCenter()
                .apply(requestOptions)
                .override(800,600)
                .into(holder.reviewImage)
        }
        else {
            holder.reviewImage.setImageResource(0)
        }

        if (items?.retext != null) {
            holder.reviewStore.text = "사장님 답변"
            holder.reviewStoreReview.text = items?.retext as CharSequence?
        }
    }
}

