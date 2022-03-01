package org.linphone.activities.main

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import org.linphone.R

class RegisterUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)

        val myWebView: WebView = findViewById(R.id.webview)

        myWebView.loadUrl("http://voip.llamadasvenezuela.com/mbilling/index.php/signup/add")
        // this will enable the javascript settings
        myWebView.settings.javaScriptEnabled = true

        // if you want to enable zoom feature
        myWebView.settings.setSupportZoom(true)
    }
}
