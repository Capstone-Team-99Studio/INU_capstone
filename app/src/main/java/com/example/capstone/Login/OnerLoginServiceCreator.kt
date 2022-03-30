package com.example.capstone.Login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.capstone.Retrofit.LoginService
import com.example.capstone.Retrofit.OnerLoginService
import com.example.capstone.data.ClientErrorBody
import com.example.capstone.data.LoginBody
import com.example.capstone.data.ServerErrorBody
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OnerLoginServiceCreator {

    private val BASE_URL = "https://473d-125-180-55-163.ngrok.io/"
    private val client : OnerLoginService

    val gson = GsonBuilder().setLenient().create()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    init {
        client = retrofit.create(OnerLoginService::class.java)
    }

    fun requestOnerLogin(email:String, password:String) : LiveData<String> {

        val livedata = MutableLiveData<String>()
        val body = LoginBody(email,password)
        val call = client.requestBusinessLogin(body)
        call.enqueue(object: Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("이메일 error", "${t.message}")
            }

            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                if(response.isSuccessful){
                    Log.d("요청 성공!", "onResponse is Successful!")
                    val body = response.body()
                    livedata.value = body!!
                    Log.d("Login response","$body")
                }
                else {
                    Log.e("요청 실패", "response is not Successful")
                    // Log.e("errorBody() is ", response.errorBody()!!.string())

                    try {
                        val errorBody = response.errorBody() //response.errorBody()는 딱 한 번만 실행되어야 한다.
                        val loginErrorConverter = //로그인 오류(이메일, 비밀번호)
                            retrofit.responseBodyConverter<ClientErrorBody>(
                                ClientErrorBody::class.java,
                                ClientErrorBody::class.java.annotations)
                        /* //  서버가 꺼져있을 때
                        val serverErrorConverter =
                        retrofit.responseBodyConverter<ServerErrorBody>(
                            ServerErrorBody::class.java,
                            ServerErrorBody::class.java.annotations)
                        val convertedBody = serverErrorConverter.convert(errorBody) */
                        val convertedBody = loginErrorConverter.convert(errorBody)
                        Log.e("errorBody() message is ", "${convertedBody?.message}" )
                          //if (convertedBody?.message == "가입되지 않은 E-MAIL 입니다."){
                          //    livedata.value = "not registered email"
                          //}else if (convertedBody?.message == "잘못된 비밀번호입니다."){
                          //    livedata.value = "incorrect password"
                        //}
                        when(convertedBody?.message){
                            "가입되지 않은 E-MAIL 입니다." -> {
                                livedata.value = "not registered email"
                            }
                            "잘못된 비밀번호입니다." -> {
                                livedata.value = "incorrect password"
                            }else ->{
                            livedata.value = "server error"
                        }
                        }
                    }catch (e:Exception){
                        livedata.value = "server error"
                        e.printStackTrace()
                    }
                }
            }
        })
        return livedata
    }
}