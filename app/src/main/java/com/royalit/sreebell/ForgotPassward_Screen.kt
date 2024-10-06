package com.royalit.sreebell

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.erepairs.app.api.Api
import com.erepairs.app.models.Common_Response
import com.royalit.sreebell.api.APIClient
import com.royalit.sreebell.databinding.ForgotpasswordScreenBinding
import com.royalit.sreebell.utils.NetWorkConection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *  Created by Sucharitha Peddinti on 27/11/21.
 */
class ForgotPassward_Screen : AppCompatActivity() {
    private lateinit var binding: ForgotpasswordScreenBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var customerid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ForgotpasswordScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences =
            getSharedPreferences(
                "loginprefs",
                Context.MODE_PRIVATE
            )


        customerid = sharedPreferences.getString("userid", "").toString()

        binding.signin.setOnClickListener {


            val intent = Intent(
                this@ForgotPassward_Screen,
                SignIn_Screen::class.java
            )
            startActivity(intent)
            finish()
        }

        binding.sendmailBtn.setOnClickListener {
            val email = binding.emailForgot.text.toString().trim()

            if (email.isEmpty()) {
                binding.emailForgot.error = "Please Enter Your Email"
            } else {
                postforgotpassword(email)

            }

        }
    }

    private fun postforgotpassword(email: String) {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)

            val call = apiServices.userforgot_passwrd(getString(R.string.api_key), email)



            binding.progrssForgot.visibility = View.VISIBLE
            call.enqueue(object : Callback<Common_Response> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<Common_Response>,
                    response: Response<Common_Response>
                ) {

                    Log.e(ContentValues.TAG, response.body().toString())
                    binding.progrssForgot.visibility = View.GONE

                    if (response.isSuccessful) {

                        try {
                            Log.e("djs", "" + response.body()?.response)


                            val intent = Intent(
                                this@ForgotPassward_Screen,
                                SignIn_Screen::class.java
                            )
                            startActivity(intent)
                            finish()

                            Toast.makeText(
                                this@ForgotPassward_Screen,
                                "" + response.body()?.response,
                                Toast.LENGTH_LONG
                            ).show()
                        } catch (e: java.lang.NullPointerException) {
                            e.printStackTrace()
                        }

                    }


                }

                override fun onFailure(call: Call<Common_Response>, t: Throwable) {
                    Log.e(ContentValues.TAG, t.toString())
                    binding.progrssForgot.visibility = View.GONE

                }

            })


        } else {

            Toast.makeText(
                this,
                "Please Check your internet",
                Toast.LENGTH_LONG
            ).show()
        }

    }


}