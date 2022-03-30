package com.example.capstone.Login

import android.app.Application
import com.example.capstone.data.MemberInfo

class App : Application(){

    companion object{
        lateinit var prefs: Preference
        var nowLogin :Boolean = false
        var memberInfo : MemberInfo? = null
    }

    override fun onCreate() {
        prefs = Preference(applicationContext)
        val autoLogin = prefs.autoLogin
        if(autoLogin == true){
            val token = prefs.token
            if(token != null){
                nowLogin = true
            }
        }
        super.onCreate()
    }
}