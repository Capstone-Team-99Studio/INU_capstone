package com.example.capstone

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import com.example.capstone.Oner.OnerMainActivity
import com.example.capstone.Login.App
import com.example.capstone.Login.LoginViewModel
import com.example.capstone.Retrofit.RetrofitLogin
import com.example.capstone.data.LoginBody
import com.example.capstone.data.MemberInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel>()

    private lateinit var loginId : EditText
    private lateinit var loginPw : EditText
    private lateinit var onerbtn: Button
    private lateinit var onerCheckBox: CheckBox
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        loginId = findViewById(R.id.login_id)
        loginPw = findViewById(R.id.login_pw)

        val loginButton: Button = findViewById(R.id.login_loginbutton)
        val registerButton: Button = findViewById(R.id.login_signupbutton)
        onerCheckBox = findViewById(R.id.onerCheck)

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }


        loginButton.setOnClickListener {

            if(Utility.istNetworkConnected(this)) {
                when {
                    loginId.text.isEmpty() -> {
                        showToastMsg(getString(R.string.login_empty_email))
                        Utility.focusEditText(this,loginId)
                    }
                    loginPw.text.isEmpty() -> {
                        showToastMsg(getString(R.string.login_empty_password))
                        Utility.focusEditText(this,loginPw)
                    }
                    else -> {
                        if (!onerCheckBox.isChecked){
                            loginProcess()
                            showToastMsg("로그인 되었습니다.")
                        }
                        else{
                            OnerloginProcess()
                            val intent = Intent(this, OnerMainActivity::class.java)
                            startActivity(intent)
                        }

                    }
                }
            }

        }
    }
    private fun loginProcess(){
        viewModel.requestLogin(loginId.text.toString(), loginPw.text.toString())
        viewModel.token.observe(this,
            {
                when (it) {
                    "not registered email" -> {
                        showToastMsg(getString(R.string.login_not_registered_email))
                        Utility.focusEditText(this,loginId)
                    }
                    "incorrect password" -> {
                        showToastMsg(getString(R.string.login_incorrect_password))
                        Utility.focusEditText(this,loginPw)
                    }
                    "server error" -> {
                        showToastMsg(getString(R.string.login_server_error))
                    }
                    else -> { //토큰을 정상적으로 받았을 때.
                        App.prefs.token = it
                        Log.d("로그인", "${App.prefs.token}")
                        requestMemberInfo(it)
                    }
                }
            })
    }

    private fun OnerloginProcess(){
        viewModel.requestOnerLogin(loginId.text.toString(), loginPw.text.toString())
        viewModel.onertoken.observe(this,
            {
                when (it) {
                    "not registered email" -> {
                        showToastMsg(getString(R.string.login_not_registered_email))
                        Utility.focusEditText(this,loginId)
                    }
                    "incorrect password" -> {
                        showToastMsg(getString(R.string.login_incorrect_password))
                        Utility.focusEditText(this,loginPw)
                    }
                    "server error" -> {
                        showToastMsg(getString(R.string.login_server_error))
                    }
                    else -> { //토큰을 정상적으로 받았을 때.
                        App.prefs.token = it
                        requestMemberInfo(it)
                    }
                }
            })
    }
    private fun requestMemberInfo(token:String){
        viewModel.requestMemberInfo(token)
        viewModel.memberInfo.observe(
            this,
            {
                App.memberInfo = it
                App.nowLogin = true
                showToastMsg("로그인 성공")
                finish()
            }
        )
    }
    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }
    private fun showToastMsg(msg:String){ Toast.makeText(this,msg,Toast.LENGTH_SHORT).show() }

}