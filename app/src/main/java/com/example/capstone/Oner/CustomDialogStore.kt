package com.example.capstone.Oner

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import com.example.capstone.R

class CustomDialogStore(context: Context) {
    private val dialog = Dialog(context)

    private lateinit var buttonClick: OnButtonClickListener
    private lateinit var okBtn : Button
    private lateinit var closeBtn : Button

    fun showDialog(){

        dialog.setContentView(R.layout.dialog_btn_store)

        okBtn = dialog.findViewById(R.id.yesBtn)
        closeBtn = dialog.findViewById(R.id.noBtn)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        okBtn.setOnClickListener {
            buttonClick.okBtnClicked()
            dialog.dismiss()
        }
        closeBtn.setOnClickListener {
            dialog.dismiss()    // 대화상자를 닫는 함수
        }

    }


    interface OnButtonClickListener {
        fun okBtnClicked()

    }

    fun setButtonListener(listener: OnButtonClickListener) {
        buttonClick = listener
    }
}