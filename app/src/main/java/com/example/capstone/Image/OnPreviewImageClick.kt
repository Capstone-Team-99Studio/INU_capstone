package com.example.capstone.Image

import java.io.File

interface OnPreviewImageClick {

    fun startReviewSlideImageView(curIndex: Int)
    abstract fun getExternalStoragePublicDirectory(): File?
}