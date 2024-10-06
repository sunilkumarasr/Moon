package com.royalit.sreebell

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.erepairs.app.api.Api
import com.erepairs.app.models.AppMaintananceResponseModel
import com.royalit.sreebell.api.APIClient
import com.royalit.sreebell.databinding.ActivityGetStartedScreenBinding
import com.royalit.sreebell.databinding.LoginScreenBinding
import com.royalit.sreebell.utils.Constants
import com.royalit.sreebell.utils.NetWorkConection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetStartedScreen : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityGetStartedScreenBinding
    private var isLogined: Boolean = false
    var isActive=true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetStartedScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Constants.changeNotificationBarColor(this, ContextCompat.getColor(this, R.color.splashgreen), false)
        sharedPreferences =
            getSharedPreferences(
                "loginprefs",
                Context.MODE_PRIVATE
            )
        binding.cvEnterNow.setOnClickListener {
            /*val editor = sharedPreferences.edit()
            editor.putBoolean("is_get_started", true)
            editor.commit()
            val intent = Intent(this@GetStartedScreen, GetStartedScreen::class.java)
            startActivity(intent)
            finish()*/
            val editor = sharedPreferences.edit()
            editor.putBoolean("is_get_started", true)
            editor.commit()
            checkAppMaintanance()
        }
    }

    private fun checkAppMaintanance() {

        if (NetWorkConection.isNEtworkConnected(this@GetStartedScreen)) {

            val apiServices = APIClient.client.create(Api::class.java)
            var call=  apiServices.checkAppMaintanance(getString(R.string.api_key))


            call?.enqueue(object : Callback<AppMaintananceResponseModel> {
                @SuppressLint("WrongConstant")
                override fun onResponse(call: Call<AppMaintananceResponseModel>, response: Response<AppMaintananceResponseModel>) {
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {
                        try {
                            var message=""
                            val list= response.body()?.response
                            if(list!=null&&list.size>0)
                            {
                                if(list.get(0).message!="online")
                                {
                                    isActive=false
                                }
                                message= list.get(0).reason.toString()
                            }

                            if(isActive)
                            {
                                isLogined = sharedPreferences.getBoolean("islogin", false)
                                Log.d("isLogined","isLogined $isLogined")
                                if (isLogined) {
                                    val intent = Intent(this@GetStartedScreen, HomeScreen::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    val intent = Intent(this@GetStartedScreen, com.royalit.sreebell.First_Screen::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }else
                            {
                                val intent = Intent(this@GetStartedScreen, com.royalit.sreebell.AppMaintananceActivity::class.java)
                                intent.putExtra("message",message)
                                startActivity(intent)
                                finish()
                            }


                        } catch (e: java.lang.NullPointerException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<AppMaintananceResponseModel>, t: Throwable) {
                    Log.e(ContentValues.TAG, t.toString())

                    isLogined = sharedPreferences.getBoolean("islogin", false)
                    Log.d("isLogined","isLogined $isLogined")
                    if (isLogined) {
                        val intent = Intent(this@GetStartedScreen, HomeScreen::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(this@GetStartedScreen, com.royalit.sreebell.First_Screen::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            })
        } else {
            Toast.makeText(
                this@GetStartedScreen,
                "Please Check your internet",
                Toast.LENGTH_LONG
            ).show()
        }


    }
}