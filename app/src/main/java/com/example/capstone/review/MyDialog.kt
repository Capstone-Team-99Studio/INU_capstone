package com.example.capstone.review

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.capstone.AllReviewActivity
import com.example.capstone.R
import com.example.capstone.ReviewActivity

class MyDialog(context: Context) {

    private val dialog = Dialog(context)

    private lateinit var buttonClick: SetButtonListener
    private lateinit var reviewRead : Button
    private lateinit var reviewWrite : Button
    private lateinit var closeBtn : Button

    fun myDig() {
        dialog.setContentView(R.layout.review_dialog)

        reviewRead = dialog.findViewById(R.id.dialog_readReview)
        reviewWrite = dialog.findViewById(R.id.dialog_writeReview)
        closeBtn = dialog.findViewById(R.id.dialog_closeBtn)

        reviewRead.setOnClickListener {
            buttonClick.readOnClicked()
            dialog.dismiss()    // 대화상자를 닫는 함수
        }
        reviewWrite.setOnClickListener {
            buttonClick.writeOnClicked()
            dialog.dismiss()
        }
        closeBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()
    }
    interface SetButtonListener {
        fun readOnClicked()
        fun writeOnClicked()
    }
    fun setButtonListener(listener: SetButtonListener) {
        buttonClick = listener
    }

}
