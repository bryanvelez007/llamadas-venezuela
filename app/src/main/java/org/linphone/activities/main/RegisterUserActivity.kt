package org.linphone.activities.main

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import org.linphone.R

class RegisterUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)

        val myWebView: WebView = findViewById(R.id.webview)

        myWebView.webViewClient = MyWebViewClient(this)
        myWebView.loadUrl("http://voip.llamadasvenezuela.com/mbilling/index.php/signup/add")
        // this will enable the javascript settings
        myWebView.settings.javaScriptEnabled = true

        // if you want to enable zoom feature
        myWebView.settings.setSupportZoom(true)
    }

    class MyWebViewClient internal constructor(private val activity: Activity) : WebViewClient() {

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            val url: String = request?.url.toString()
            Toast.makeText(activity, "" + url, Toast.LENGTH_SHORT).show()
            view?.loadUrl(url)
            return true
        }

        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
            Toast.makeText(activity, "" + url, Toast.LENGTH_SHORT).show()
            webView.loadUrl(url)
            return true
        }

        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceError
        ) {
            Toast.makeText(activity, "Got Error! $error", Toast.LENGTH_SHORT).show()
        }
    }
}
