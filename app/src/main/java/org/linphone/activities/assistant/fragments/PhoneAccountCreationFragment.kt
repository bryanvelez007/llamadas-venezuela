/*
 * Copyright (c) 2010-2020 Belledonne Communications SARL.
 *
 * This file is part of linphone-android
 * (see https://www.linphone.org).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.linphone.activities.assistant.fragments

import android.app.Activity
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import org.linphone.R
import org.linphone.activities.assistant.AssistantActivity
import org.linphone.activities.assistant.viewmodels.PhoneAccountCreationViewModel
import org.linphone.activities.assistant.viewmodels.PhoneAccountCreationViewModelFactory
import org.linphone.activities.assistant.viewmodels.SharedAssistantViewModel
import org.linphone.activities.navigateToGenericLogin
import org.linphone.activities.navigateToPhoneAccountValidation
import org.linphone.databinding.AssistantPhoneAccountCreationFragmentBinding
import org.linphone.mediastream.Version

class PhoneAccountCreationFragment :
    AbstractPhoneFragment<AssistantPhoneAccountCreationFragmentBinding>() {
    private lateinit var sharedViewModel: SharedAssistantViewModel
    override lateinit var viewModel: PhoneAccountCreationViewModel

    override fun getLayoutId(): Int = R.layout.assistant_phone_account_creation_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("registerComplete", "no")
        editor.apply()
        editor.commit()

        val myHandler = Handler(Looper.getMainLooper())

        myHandler.post(object : Runnable {
            override fun run() {
                val prefs = PreferenceManager.getDefaultSharedPreferences(activity)
                val isRegister = prefs.getString("registerComplete", "")

                myHandler.postDelayed(this, 5000 /*5 segundos*/)

                if (isRegister == "yes") {
                    myHandler.removeCallbacks(this)
                    myHandler.removeCallbacksAndMessages(null)
                    navigateToPhoneAccountValidation()
                }
            }
        })

        // navigateToPhoneAccountValidation()

        val btnFinalRegister = view.findViewById(R.id.btnGoToSignIn) as Button
        btnFinalRegister.visibility = View.INVISIBLE

        val myWebView: WebView = view.findViewById(R.id.WebView1)

        myWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                url: String,

            ): Boolean {
                view.loadUrl(url)
                return true
            }
        }
        myWebView.webViewClient = MyWebViewClient(requireActivity())
        myWebView.loadUrl("http://voip.llamadasvenezuela.com/mbilling/index.php/signup/add")
        myWebView.settings.javaScriptEnabled = true
        myWebView.settings.allowContentAccess = true
        myWebView.settings.domStorageEnabled = true
        myWebView.settings.useWideViewPort = true

        sharedViewModel = requireActivity().run {
            ViewModelProvider(this)[SharedAssistantViewModel::class.java]
        }

        viewModel = ViewModelProvider(
            this,
            PhoneAccountCreationViewModelFactory(sharedViewModel.getAccountCreator())
        )[PhoneAccountCreationViewModel::class.java]
        binding.viewModel = viewModel

        // navigateToPhoneAccountValidation()

        binding.setInfoClickListener {
            navigateToPhoneAccountValidation()
        }

        binding.setSelectCountryClickListener {
            CountryPickerFragment(viewModel).show(childFragmentManager, "CountryPicker")
        }

        viewModel.goToSmsValidationEvent.observe(
            viewLifecycleOwner
        ) {
            it.consume {
                val args = Bundle()
                args.putBoolean("IsCreation", true)
                args.putString("PhoneNumber", viewModel.accountCreator.phoneNumber)
                navigateToGenericLogin()
            }
        }

        viewModel.onErrorEvent.observe(
            viewLifecycleOwner
        ) {
            it.consume { message ->
                (requireActivity() as AssistantActivity).showSnackBar(message)
            }
        }

        if (Version.sdkAboveOrEqual(Version.API23_MARSHMALLOW_60)) {
            checkPermissions()
        }
    }
}

class MyWebViewClient internal constructor(private val activity: Activity) : WebViewClient() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url: String = request?.url.toString()
        if (url.contains("username")) {
            val userName = url.split("username=")[1].split("&")[0]
            val password = url.split("password=")[1].split("&")[0]
            val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("username", userName)
            editor.putString("password", password)
            editor.putString("registerComplete", "yes")
            editor.apply()
            editor.commit()

            // context.startActivity(intent)
        }
        view?.loadUrl(url)
        return true
    }

    override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
        webView.loadUrl(url)
        return true
    }

    override fun onReceivedError(
        view: WebView,
        request: WebResourceRequest,
        error: WebResourceError
    ) {
    }
}
