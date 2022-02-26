package org.linphone.activities.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import org.linphone.R
import org.linphone.activities.GenericActivity

class LoginUserActivity : GenericActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_user)

        val buttonSignIn = findViewById<Button>(R.id.btn_signIn)
        val buttonSignUp = findViewById<Button>(R.id.btn_signUp)

        buttonSignIn.setOnClickListener {
            Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@LoginUserActivity, MainActivity::class.java))
        }

        buttonSignUp.setOnClickListener {
            Toast.makeText(this, "Register", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@LoginUserActivity, RegisterUserActivity::class.java))
        }
    }
}
