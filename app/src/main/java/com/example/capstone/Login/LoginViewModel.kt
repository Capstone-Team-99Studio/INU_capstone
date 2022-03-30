package com.example.capstone.Login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.capstone.data.MemberInfo

class LoginViewModel : ViewModel() {
    val client : LoginServiceCreator
    lateinit var token : LiveData<String>

    val memberClient : MemberServiceCreator
    lateinit var memberInfo : LiveData<MemberInfo>

    val oner : OnerLoginServiceCreator
    lateinit var onertoken : LiveData<String>


    init {
        client = LoginServiceCreator()
        memberClient = MemberServiceCreator()
        oner = OnerLoginServiceCreator()
    }

    fun requestLogin(email:String,password:String){
        token = client.requestLogin(email,password)
    }

    fun requestMemberInfo(token:String){
        memberInfo = memberClient.requestMemberInfo(token)
    }

    fun requestOnerLogin(email:String,password:String){
        onertoken = oner.requestOnerLogin(email,password)
    }
}
