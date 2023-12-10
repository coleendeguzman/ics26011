package com.example.wishlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
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

        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnTogglePassword = findViewById<ImageView>(R.id.btnTogglePassword)

        etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()

        btnTogglePassword.setOnClickListener {
            if (etPassword.transformationMethod == PasswordTransformationMethod.getInstance()) {
                etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                btnTogglePassword.setImageResource(R.drawable.show)
            } else {
                etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                btnTogglePassword.setImageResource(R.drawable.hide)
            }
            etPassword.setSelection(etPassword.text.length)
        }

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
                        // Store username in shared preferences
                        dbHandler.setLoggedInUser(user.username)
                        val sharedPref = getSharedPreferences("USER_PREFERENCES", Context.MODE_PRIVATE)
                        val editor = sharedPref.edit()
                        editor.putString("USERNAME", username)
                        editor.apply()

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