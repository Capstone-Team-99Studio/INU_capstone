package com.example.capstone.Oner
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import com.example.capstone.R


class CustomDialog(context: Context) {

    private val dialog = Dialog(context)

    private lateinit var buttonClick: OnButtonClickListener
    private lateinit var storesetting : Button
    private lateinit var reviewRead : Button
    private lateinit var closeBtn : Button
    private lateinit var foodDeleteBtn : Button

    fun showDialog(){

        dialog.setContentView(R.layout.dialog_btn)

        storesetting = dialog.findViewById(R.id.foodsettingBtn)
        reviewRead = dialog.findViewById(R.id.reviewBtn)
        closeBtn = dialog.findViewById(R.id.cancelBtn)
        foodDeleteBtn = dialog.findViewById(R.id.foodDeleteBtn)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        storesetting.setOnClickListener {
            buttonClick.storesettingClicked()
            dialog.dismiss()
        }
        reviewRead.setOnClickListener {
            buttonClick.reviewReadClicked()
            dialog.dismiss()    // 대화상자를 닫는 함수
        }

        foodDeleteBtn.setOnClickListener{
            buttonClick.foodDeleteClicked()
            dialog.dismiss()
        }

        closeBtn.setOnClickListener {
            dialog.dismiss()
        }
    }


    interface OnButtonClickListener {
        fun storesettingClicked()
        fun reviewReadClicked()
        fun foodDeleteClicked()
    }

    fun setButtonListener(listener: OnButtonClickListener) {
        buttonClick = listener
    }
}