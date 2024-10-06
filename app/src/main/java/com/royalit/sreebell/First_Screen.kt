package com.royalit.sreebell

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.royalit.sreebell.databinding.FirstScreenBinding
import com.royalit.sreebell.utils.Constants

class First_Screen : Activity() {
    private lateinit var binding: FirstScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FirstScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Constants.changeNotificationBarColor(this, ContextCompat.getColor(this, R.color.black), false)

        binding.signinText.setOnClickListener {

            val intent = Intent(
                this,
                com.royalit.sreebell.SignIn_Screen::class.java
            )
            startActivity(intent)
            finish()
        }
        binding.signupText.setOnClickListener {


            val intent = Intent(
                this,
                com.royalit.sreebell.Signup_Screen::class.java
            )
            startActivity(intent)
            finish()
        }

    }
}