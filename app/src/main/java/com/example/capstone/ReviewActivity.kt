package com.example.capstone

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.net.Uri
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getExternalFilesDirs
import androidx.lifecycle.MutableLiveData
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
import com.example.capstone.Retrofit.RetrofitMenu
import com.example.capstone.Retrofit.RetrofitReview
import com.example.capstone.data.MemberResponse
import com.example.capstone.data.ReviewData
import com.example.capstone.data.review
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.*
import kotlin.math.floor

class ReviewActivity : AppCompatActivity(), OnPreviewImageClick {

    private val viewModel by viewModels<ReviewViewModel>()
    private var photo : MultipartBody.Part? = null

    private var reviewImage = arrayListOf<Image>()
    private var reviewImageUrls = arrayListOf<String>()

    private lateinit var foodName: TextView
    private lateinit var foodRatingBar: RatingBar
    private lateinit var foodGallery : TextView
    private lateinit var requestBtn : Button
    private lateinit var foodReview : EditText
    private lateinit var foodImage : ImageView

    lateinit var storagePermission: ActivityResultLauncher<String>
    lateinit var galleryLauncher: ActivityResultLauncher<String>
    private var photoUri : Uri? = null
    private val postedReviewId: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        var body : MultipartBody.Part? = null
        requestBtn = findViewById(R.id.finish_Button)
        foodName = findViewById(R.id.food_title)
        foodReview = findViewById(R.id.review_detail)
        foodRatingBar = findViewById(R.id.review_rating)
        foodGallery = findViewById(R.id.review_upload)
        foodImage = findViewById(R.id.review_Image)
        foodName.text = intent.getStringExtra("storeName")

        val foodNum = intent?.getIntExtra("food_id", 0)
        var foodRating:String? = null
        val ratingBarListener = RatingBar.OnRatingBarChangeListener {
                _ , fl: Float, _ ->
            foodRating = fl.toInt().toString()
            Log.d("Review Rating", "$foodRating")
            Log.d("로그인 토큰", "${App.prefs.token}")
        }
        foodRatingBar.onRatingBarChangeListener = ratingBarListener
        foodGallery.setOnClickListener {
            setViews()
        }
        requestBtn.setOnClickListener {
            requestReview(foodNum!!)
        }


        storagePermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if(isGranted) {
                setViews()
            } else {
                Toast.makeText(baseContext, "외부저장소 권한을 승인해야 앱을 사용할 수 있습니다.", Toast.LENGTH_LONG).show()
                finish()
            }
        }
        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            foodImage.setImageURI(uri)
            photoUri = uri
            Log.d("uri", photoUri.toString())
            val res = createCopyAndReturnRealPath(this,photoUri!!)
            val file = File(res)
            val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
            photo = MultipartBody.Part.createFormData("file", file.name, requestFile)
            Log.d("file.absolutePath", file.toString())
            Log.d("file.absolutePath", requestFile.toString())
            Log.d("file.absolutePath", res!!)
            Log.d("body", photo.toString())
        }
    }

    override fun startReviewSlideImageView(curIndex: Int) {
        SlideImageViewer.start(this, reviewImageUrls)
    }

    override fun getExternalStoragePublicDirectory(): File? {
        TODO("Not yet implemented")
    }

    private fun requestReview(food_Id: Int) {

        val body = getReviewData()
        viewModel.postReview(App.prefs.token!!, food_Id, body)
        viewModel.postedReviewId.observe(
            this,
            {
                if(it == -1) { //서버 응답 실패
                    showToastMsg("리뷰 등록 실패")
                    Log.d("리뷰 등록", "실패 $")
                } else {
                    postPhoto(it)
                }
            }
        )
    }

    fun showToastMsg(msg:String){ Toast.makeText(this,msg, Toast.LENGTH_SHORT).show() }

    private fun postPhoto(reviewId: Int) {
        Log.d("Review Photo","${reviewId}")
        if(photoUri == null) {
            showToastMsg("리뷰가 등록되었습니다.")
            Log.d("Review Photo","텍스트만 등록")
            setResult(RESULT_OK)
            finish()
        } else {
            viewModel.postPhoto(App.prefs.token!!, reviewId, photo)
            viewModel.postedImageId.observe(
                this, {
                    if(it == -1) {
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

    private fun getReviewData() : ReviewData {

        var foodRatingString: String? = null
        var reviewRating: String? = null
        foodRatingString = foodRatingBar.rating.toInt().toString()
        if (foodRatingString == "1") {
            reviewRating = "ONE"
        }
        else if (foodRatingString == "2") {
            reviewRating = "TWO"
        }
        else if (foodRatingString == "3") {
            reviewRating = "THREE"
        }
        else if (foodRatingString == "4") {
            reviewRating = "FOUR"
        }
        else if (foodRatingString == "5") {
            reviewRating = "FIVE"
        }
        Log.d("Review Data","${reviewRating}, ${foodReview.text}")
        return ReviewData(reviewRating!!, foodReview.text.toString())
    }

    fun setViews() {

        foodGallery.setOnClickListener {
            openGallery()
        }
    }
    fun openGallery() {
        galleryLauncher.launch("image/*")
    }
    @Nullable
    fun createCopyAndReturnRealPath(context: Context, uri: Uri): String? {
        val contentResolver: ContentResolver = context.getContentResolver() ?: return null

        // 파일 경로를 만듬
        val filePath: String = (context.getApplicationInfo().dataDir + File.separator
                + System.currentTimeMillis())
        val file = File(filePath)
        try {
            // 매개변수로 받은 uri 를 통해  이미지에 필요한 데이터를 불러 들인다.
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            // 이미지 데이터를 다시 내보내면서 file 객체에  만들었던 경로를 이용한다.
            val outputStream: OutputStream = FileOutputStream(file)
            val buf = ByteArray(1024)
            var len: Int
            while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
            outputStream.close()
            inputStream.close()
        } catch (ignore: IOException) {
            return null
        }
        return file.getAbsolutePath()
    }


}