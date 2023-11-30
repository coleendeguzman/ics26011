package com.example.wishlist

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterPage : AppCompatActivity() {

    private lateinit var dbHandler: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_page)

        dbHandler = DatabaseHandler(this)

        val tvLogin = findViewById<TextView>(R.id.tvLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        tvLogin.setOnClickListener {
            val i = Intent(this, LoginPage::class.java)
            startActivity(i)
        }

        btnRegister.setOnClickListener {

            fun isValidPassword(password: String): Boolean {
                val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")
                return passwordRegex.matches(password)
            }

            val etUsername = findViewById<EditText>(R.id.etUsername)
            val etPassword = findViewById<EditText>(R.id.etPassword)
            val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)

            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.trim().isNotEmpty() && password.trim().isNotEmpty()) {

                if (isValidPassword(password)) {

                    if (etPassword.text.toString() == etConfirmPassword.text.toString()) {

                        val registrationSuccess = dbHandler.registerUser(username, password)

                        if (registrationSuccess != -1L) {
                            Toast.makeText(this, "Registration successful!", Toast.LENGTH_LONG)
                                .show()

                            val i = Intent(this, LoginPage::class.java)
                            i.putExtra("USERNAME", username)
                            startActivity(i)

                            etUsername.text.clear()
                            etPassword.text.clear()
                        }
                    } else {
                            Toast.makeText(
                                this,
                                "Passwords do not match. Please Try again.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {

                        Toast.makeText(
                            this,
                            "Registration Failed. Please try again.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }


                else {

                    Toast.makeText(this, "Password needs 8 characters at least 1 symbol and a mix of upper and lower case letters .", Toast.LENGTH_LONG).show()
                }

            }
        }
    }