package org.linphone.activities.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import org.linphone.R
import org.linphone.activities.GenericActivity

class LoginUserActivity : GenericActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_user)

        val buttonSignIn = findViewById<Button>(R.id.btn_signIn)
        val buttonSignUp = findViewById<Button>(R.id.btn_signUp)
        val txtUser = findViewById<EditText>(R.id.txtUsername)
        val txtPassword = findViewById<EditText>(R.id.txtPassword)

        val sharedPrefFile = packageName + "_preferences"
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("username", "defaultUser")
        val password = sharedPreferences.getString("password", "defaultPass")
        val isLoged = sharedPreferences.getString("isLoged", "default")

        if (isLoged == "yes") {
            startActivity(Intent(this@LoginUserActivity, MainActivity::class.java))
        }

        if (userName != "defaultUser" && password != "defaultPass") {
            txtUser.setText(userName)
            txtPassword.setText(password)
        }

        buttonSignIn.setOnClickListener {
            startActivity(Intent(this@LoginUserActivity, MainActivity::class.java))
        }

        buttonSignUp.setOnClickListener {
            startActivity(Intent(this@LoginUserActivity, RegisterUserActivity::class.java))
        }
    }
}
