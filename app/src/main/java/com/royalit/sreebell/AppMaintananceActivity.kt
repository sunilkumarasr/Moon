package com.royalit.sreebell

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text

class AppMaintananceActivity : AppCompatActivity() {
    var message=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_app_maintanance)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txt_message=findViewById<TextView>(R.id.txt_message)

        try {
            message = intent.getStringExtra("message").toString()
            if (message != null && message.isNotEmpty())
                txt_message.setText(message)
        }catch (e:Exception)
        {

        }
    }
}