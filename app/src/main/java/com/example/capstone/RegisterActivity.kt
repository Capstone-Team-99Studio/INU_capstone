package com.example.capstone

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.example.capstone.Register.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private val viewModel by viewModels<RegisterViewModel>()

    private lateinit var nickname : EditText
    private lateinit var email : EditText
    private lateinit var pw : EditText
    private lateinit var repw : EditText
    private lateinit var key : EditText
    private val correct = "inu.ac.kr"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        pw = findViewById(R.id.regi_pw)
        repw = findViewById(R.id.regi_repw)
        email = findViewById(R.id.regi_id)
        nickname = findViewById(R.id.regi_nickname)
        key = findViewById(R.id.regi_key)
        val registerButton: Button = findViewById(R.id.regi_signupbutton)
        var check: TextView = findViewById(R.id.regi_pwok)
        var codeButton: Button = findViewById(R.id.regi_emailButton)

        // 인증번호 전송
        codeButton.setOnClickListener {
            Utility.outFocusEditText(this, email)
            sendCode()
        }

        repw.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (pw.text.toString() == repw.text.toString()) {
                    check.text = "비밀번호가 일치합니다."
                    check.setTextColor(Color.BLACK)
                    registerButton.isEnabled = true
                }
                else {
                    check.text = "비밀번호가 일치하지 않습니다."
                    check.setTextColor(Color.RED)
                    registerButton.isEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (pw.text.toString() == repw.text.toString()) {
                    check.text = "비밀번호가 일치합니다."
                    check.setTextColor(Color.BLACK)
                    registerButton.isEnabled = true
                }
                else {
                    check.text = "비밀번호가 일치하지 않습니다."
                    check.setTextColor(Color.RED)
                    registerButton.isEnabled = false
                }
            }
        })
        registerButton.setOnClickListener {
            signUp()
        }

    }
    private fun sendCode(){
        if (!Utility.istNetworkConnected(this)){
            showToastMsg(getString(R.string.internet_not_connected))
        }
        else if (isCorrectEmail()) {
            emailCountDown.start()
            viewModel.postEmail(email.text.toString())
            viewModel.correctEmail.observe(
                this,
                {
                    emailCountDown.cancel()
                    if (it == email.text.toString()){
                        Utility.focusEditText(this,key)
                        showToastMsg(getString(R.string.msg_code_send))
                    }else if(it == "registerer email"){
                        showToastMsg(getString(R.string.msg_registered_email))
                        Utility.focusEditText(this,email)
                    }
                })
        }else{
            showToastMsg(getString(R.string.msg_wrong_email))
            Utility.focusEditText(this,email)
            Log.d("메일 인증", "잘못된 주소 입니다.")
        }
    }

    fun showToastMsg(msg:String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
    private val emailCountDown: CountDownTimer = object : CountDownTimer(9250, 500) {
        override fun onTick(millisUntilFinished: Long) {}
        override fun onFinish() {
            showToastMsg(getString(R.string.msg_code_not_sended))
            Utility.focusEditText(this@RegisterActivity,email)
        }
    }
    private fun isCorrectEmail(): Boolean{ //형식상 일치하는지 판단.(inu.ac.kr로 끝나는지 확인)
        val inputEmail = email.text.toString()
        if(!inputEmail.contains('@')){ return false }

        val divEmail = inputEmail.split("@")
        Log.d("email", divEmail[1])
        return divEmail[1] == correct
    }
    private fun isEmptyNickName(): Boolean = nickname.text.isEmpty()

    private fun isEmptyEmail(): Boolean = email.text.isEmpty()

    private fun isEmptyPassword(): Boolean = pw.text.isEmpty()

    private fun isCorrectPassword(): Boolean = pw.text.toString() == repw.text.toString()

    private fun isRegisteredEmail() : Boolean = viewModel.correctEmail == email.text

    private fun signUp(){

        if(!Utility.istNetworkConnected(this)){
            showToastMsg(getString(R.string.internet_not_connected))
        }
        else if(isEmptyNickName()){
            showToastMsg(getString(R.string.msg_empty_nickname)) //닉네임을 입력해주세요.
            Utility.focusEditText(this,nickname)
        }else if(isEmptyEmail()){
            showToastMsg(getString(R.string.msg_empty_email)) //이메일을 입력해주세요.
            Utility.focusEditText(this,email)
        }else if(!isCorrectEmail()){
            showToastMsg(getString(R.string.msg_wrong_email)) // 올바르지 않은 이메일 주소입니다.
            Utility.focusEditText(this,email)
        }else if(isEmptyPassword()){
            showToastMsg(getString(R.string.msg_empty_password))
            Utility.focusEditText(this,pw)
        }
        else if(!isCorrectPassword()) {
            showToastMsg(getString(R.string.msg_incorrect_password)) //비밀번호가 일치하지 않습니다!
            Utility.focusEditText(this,repw)
        }else if(isRegisteredEmail()){
            showToastMsg(getString(R.string.msg_registered_email))
            Utility.focusEditText(this,email)
        }
        else {
            singUpCountDown.start()
            viewModel.verifiedEmailResponse(email.text.toString(),key.text.toString())
            viewModel.verifiedCode.observe(
                this,
                {
                    singUpCountDown.cancel()
                    if(it == key.text.toString()){
                        // 회원가입 성공, 닉네임, 이메일, 패스워드 데이터 전송
                        viewModel.registerMember(email.text.toString(),nickname.text.toString(),pw.text.toString())
                        showToastMsg("회원가입 성공")
                        finish()
                    }else if(it == "code is incorrect"){
                        showToastMsg(getString(R.string.msg_wrong_code))
                        Utility.focusEditText(this,key)
                    }
                })
        }
    }
    private val singUpCountDown: CountDownTimer = object : CountDownTimer(5250, 500) {
        override fun onTick(millisUntilFinished: Long) {}
        override fun onFinish() {
            showToastMsg(getString(R.string.msg_wrong_code))
            Utility.focusEditText(this@RegisterActivity,key)
        }
    }
}