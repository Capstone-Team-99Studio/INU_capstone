package com.example.capstone

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.*
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.Image.OnPreviewImageClick
import com.example.capstone.adapter.ReviewImageAdapter
import com.example.capstone.review.ReviewViewModel
import okhttp3.MultipartBody
import com.esafirm.imagepicker.features.ImagePickerConfig
import com.esafirm.imagepicker.features.ImagePickerSavePath
import com.example.capstone.Image.SlideImageViewer
import com.esafirm.imagepicker.features.registerImagePicker
import com.esafirm.imagepicker.model.Image
import com.example.capstone.Login.App
import com.example.capstone.data.ReviewData
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream


class ReviewActivity : AppCompatActivity(), OnPreviewImageClick {

    private val viewModel by viewModels<ReviewViewModel>()
    private var photo = ArrayList<MultipartBody.Part>()

    private var reviewImage = arrayListOf<Image>()
    private var reviewImageUrls = arrayListOf<String>()

    private lateinit var foodName: TextView
    private lateinit var foodRatingBar: RatingBar
    private lateinit var foodGallery : TextView
    private lateinit var requestBtn : Button
    private lateinit var foodReview : EditText

    private lateinit var reviewImageRecyclerView : RecyclerView
    private lateinit var reviewImageAdapter : ReviewImageAdapter



    private val postedPhotos = ArrayList<Int>()
    private val postedReviewId: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        requestBtn = findViewById(R.id.finish_Button)
        foodName = findViewById(R.id.food_title)
        foodReview = findViewById(R.id.review_detail)
        foodRatingBar = findViewById(R.id.review_rating)
        foodGallery = findViewById(R.id.review_upload)
        reviewImageRecyclerView = findViewById(R.id.recycler_review_image)
        foodName.text = intent.getStringExtra("storeName")

        val foodNum = intent?.getIntExtra("food_id", 0)
        var foodRating: String = foodRatingBar.rating.toString()

        foodGallery.setOnClickListener {
            if(reviewImageAdapter.itemCount ==0){
                reviewPickerLauncher.launch(reviewPickerConfig)
            }
        }
        requestBtn.setOnClickListener {
            requestReview(foodNum!!)
        }
        reviewImageRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        reviewImageAdapter = ReviewImageAdapter(0, this)
        reviewImageRecyclerView.adapter = reviewImageAdapter
    }

    private val reviewPickerLauncher = registerImagePicker { result: List<Image> ->
        reviewImageUrls.clear()
        result.forEach { image ->
            println(image)

            reviewImageAdapter.addImage(image)
            reviewImage.clear()
            reviewImage.addAll(result)
        }
    }

    private val reviewPickerConfig = ImagePickerConfig {
        isShowCamera = false
        isFolderMode = true
        savePath = ImagePickerSavePath("Camera")
        savePath = ImagePickerSavePath(Environment.getExternalStorageDirectory().path, isRelative = false)
    }

    override fun startReviewSlideImageView(curIndex: Int) {
        SlideImageViewer.start(this, reviewImageUrls)
    }

    private fun requestReview(food_Id: Int) {
        val body = getReviewData()
        viewModel.postReview(App.prefs.token!!, food_Id, body)
        viewModel.postedReviewId.observe(
            this,
            {
                if(it == -1) { //서버 응답 실패
                    showToastMsg("리뷰 등록 실패")
                } else {
                    postPhoto(it)
                }
            }
        )
    }

    fun showToastMsg(msg:String){ Toast.makeText(this,msg, Toast.LENGTH_SHORT).show() }

    private fun getReviewData() : ReviewData {
        return ReviewData(foodRatingBar.rating, foodReview.text.toString())
    }

    private fun postPhoto(reviewId: Int) {
        if(reviewImage.isEmpty()) {
            showToastMsg("리뷰가 등록되었습니다.")
            setResult(RESULT_OK)
            finish()
        } else {
            addImagesToFiles(reviewImage)
            viewModel.postPhoto(App.prefs.token!!, reviewId, photo)
            viewModel.postedImageId.observe(
                this, {
                    if(it[0] == -1) {
                        showToastMsg("사진을 업로드하지 못했습니다.")
                    } else {
                        showToastMsg("리뷰가 등록되었습니다.")
                        setResult(RESULT_OK)
                        finish()
                    }
                }
            )
        }
    }

    private fun addImagesToFiles(images: ArrayList<Image>) {
        images.forEach {
            photo.add(getCompressedFile(it.path))
        }
    }

    private fun getCompressedFile(photoPath: String) : MultipartBody.Part{
        val bitmap = getScaledBitmap(photoPath,this)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
        val byteArray = stream.toByteArray()
        val bitmapBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(),byteArray)
        val body = MultipartBody.Part.createFormData("files",photoPath,bitmapBody)
        return body
    }
    fun getScaledBitmap(path: String, activity: Activity): Bitmap {
        val size = Point()

        @Suppress("DEPRECATION")
        activity.windowManager.defaultDisplay.getSize(size)

        return getScaledBitmap(path, size.x, size.y)
    }

    fun getScaledBitmap(path: String, destWidth: Int, destHeight: Int): Bitmap {
        // 이미지 파일의 크기를 읽는다
        var options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)

        val srcWidth = options.outWidth.toFloat()
        val srcHeight = options.outHeight.toFloat()

        // 크기를 얼마나 줄일지 파악한다
        var inSampleSize = 1
        if (srcHeight > destHeight || srcWidth > destWidth) {
            val heightScale = srcHeight / destHeight
            val widthScale = srcWidth / destWidth
            val sampleScale = if (heightScale > widthScale) {
                heightScale
            } else {
                widthScale
            }
            inSampleSize = Math.round(sampleScale)
        }

        options = BitmapFactory.Options()
        options.inSampleSize = inSampleSize

        // 최종 Bitmap을 생성한다
        return BitmapFactory.decodeFile(path, options)
    }
}