package com.example.capstone.Register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.capstone.Register.RegisterServiceCreator

class RegisterViewModel : ViewModel(){
    val client : RegisterServiceCreator
    lateinit var correctEmail : LiveData<String>
    lateinit var verifiedCode : LiveData<String>

    init {
        client = RegisterServiceCreator()
    }

    fun postEmail(email:String) {
        correctEmail = client.postEmail(email)
    }

    fun verifiedEmailResponse(email: String, code:String){
        verifiedCode = client.isEmailVerified(email, code)
    }

    fun registerMember(email: String, nickName: String, password: String) = client.registerMember(email, nickName, password)
}
