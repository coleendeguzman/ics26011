package com.example.wishlist

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
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
        val btnRegister = findViewById<TextView>(R.id.btnRegister)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val btnTogglePassword1 = findViewById<ImageView>(R.id.btnTogglePassword1)
        val btnTogglePassword2 = findViewById<ImageView>(R.id.btnTogglePassword2)

        etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
        etConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()

        btnTogglePassword1.setOnClickListener {
            if (etPassword.transformationMethod == PasswordTransformationMethod.getInstance()) {
                etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                btnTogglePassword1.setImageResource(R.drawable.show)
            } else {
                etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                btnTogglePassword1.setImageResource(R.drawable.hide)
            }
            etPassword.setSelection(etPassword.text.length)
        }

        btnTogglePassword2.setOnClickListener {
            if (etConfirmPassword.transformationMethod == PasswordTransformationMethod.getInstance()) {
                etConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                btnTogglePassword2.setImageResource(R.drawable.show)
            } else {
                etConfirmPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                btnTogglePassword2.setImageResource(R.drawable.hide)
            }
            etConfirmPassword.setSelection(etPassword.text.length)
        }

        tvLogin.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.cancel_dialog)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.findViewById<Button>(R.id.btnYes).setOnClickListener {
                val i = Intent(this, LoginPage::class.java)
                startActivity(i)
            }

            dialog.findViewById<Button>(R.id.btnNo).setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
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