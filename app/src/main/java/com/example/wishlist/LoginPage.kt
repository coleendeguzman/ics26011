package com.example.wishlist

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
class LoginPage : AppCompatActivity() {
    private lateinit var dbHandler: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        dbHandler = DatabaseHandler(this)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegister = findViewById<TextView>(R.id.tvSignUp)

        btnLogin.setOnClickListener {
            val etUsername = findViewById<EditText>(R.id.etUsername)
            val etPassword = findViewById<EditText>(R.id.etPassword)

            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.trim().isNotEmpty() && password.trim().isNotEmpty()) {
                val success = dbHandler.loginUser(username, password)

                if (success) {
                    val user = dbHandler.getUserInfo(username)

                    if (user != null) {
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show()
                        val i = Intent(this, HomePage::class.java)
                        i.putExtra("USERNAME", username)
                        startActivity(i)
                        etUsername.text.clear()
                        etPassword.text.clear()
                    }

                    else {
                        Toast.makeText(this, "User does not exist.", Toast.LENGTH_LONG).show()
                    }
                }

                else {
                    Toast.makeText(this, "Invalid information.", Toast.LENGTH_LONG).show()
                }
            }

            else {
                Toast.makeText(this, "No fields may be left blank", Toast.LENGTH_LONG).show()
            }
        }

        tvRegister.setOnClickListener {

            val i = Intent(this, RegisterPage::class.java)
            startActivity(i)
        }
    }
}