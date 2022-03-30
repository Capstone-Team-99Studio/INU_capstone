package com.example.capstone.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.AllReviewActivity
import com.example.capstone.R
import com.example.capstone.ReviewActivity
import com.example.capstone.data.review
import com.example.capstone.review.MyDialog

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
        val reviewImg: ImageView = itemView.findViewById(R.id.food_image)
        val reviewDetail: TextView = itemView.findViewById(R.id.food_review)
        val reviewStore: TextView = itemView.findViewById(R.id.food_store)
        val reviewStoreReview:TextView = itemView.findViewById(R.id.food_storereview)
    }

    override fun getItemCount(): Int {
        Log.d("리턴 사이즈", "${reviews?.size}")
        return reviews!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var items = reviews!!.get(position)
        Log.d("recyclerview ", "${items}")
        holder.reviewNickName.text = items?.nickname
        holder.reviewDate.text = items?.createTime as CharSequence?
        holder.reviewRating.text = items?.star.toString()
        holder.reviewImg.setImageResource(R.drawable.ic_launcher_background)
        holder.reviewDetail.text = items?.text

        if (items?.retext != null) {
            holder.reviewStore.text = "사장님 답변"
            holder.reviewStoreReview.text = items?.retext as CharSequence?
        }

    }
}