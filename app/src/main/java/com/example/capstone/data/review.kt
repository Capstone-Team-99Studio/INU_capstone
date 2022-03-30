package com.example.capstone.data

data class review(
    val `data`: Data
) {
    data class Data(
        val id: Int,
        val name: String,
        val photoId: Any,
        val price: String,
        val rateAverage: Double,
        val rateNum: Int,
        val reviewList: List<ReviewX>,
        val status: Any,
        val storeName: String,
        val text: String
    ) {
        data class ReviewX(
            val createTime: Any,
            val nickname: String,
            val photoIds: Any,
            val retext: Any,
            val star: Int,
            val text: String,
            val updateTime: Any
        )
    }
}