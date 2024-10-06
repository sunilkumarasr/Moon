package com.royalit.sreebell

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView

class UrlLoading : Activity() {
    var common_web_view: View? = null
    lateinit var wvCommon: WebView
    var close: ImageView? = null
    var toolbar_title: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_url_loading)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        wvCommon = findViewById(R.id.wvCommon)
        close = findViewById(R.id.close)
        toolbar_title = findViewById(R.id.toolbar_title)


        close?.setOnClickListener(View.OnClickListener { view: View? -> finish() })


        wvCommon?.webViewClient = WebViewClient()

        // this will load the url of the website
        wvCommon?.loadUrl(getIntent().getStringExtra("url")!!)

        // this will enable the javascript settings, it can also allow xss vulnerabilities
        wvCommon?.settings?.javaScriptEnabled = true

        // if you want to enable zoom feature
        wvCommon?.settings?.setSupportZoom(true)


    }
    override fun onBackPressed() {
        // if your webview can go back it will go back
        if (wvCommon?.canGoBack()!!)
            wvCommon?.goBack()
        // if your webview cannot go back
        // it will exit the application
        else
            super.onBackPressed()
    }



}