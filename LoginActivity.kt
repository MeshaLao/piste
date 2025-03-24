package com.android.wrapitup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : Activity() {
    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextUsername = findViewById(R.id.usernameEditText)
        editTextPassword = findViewById(R.id.passwordEditText)
        val buttonLogin = findViewById<Button>(R.id.loginButton)
        val buttonSignup = findViewById<Button>(R.id.signupButton)

        buttonSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivityForResult(intent, 1)
        }

        buttonLogin.setOnClickListener {
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username or Password cannot be empty.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LandingActivity::class.java))
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            data?.let {
                editTextUsername.setText(it.getStringExtra("username"))
                editTextPassword.setText(it.getStringExtra("password"))
            }
        }
    }

}