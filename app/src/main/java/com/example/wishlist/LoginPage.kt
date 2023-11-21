package com.example.wishlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class LoginPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.log_in_activity)

        val tvSignUp = findViewById<TextView>(R.id.tvSignUp)

        tvSignUp.setOnClickListener {
            val i = Intent(this, RegisterPage::class.java)
            startActivity(i)
        }
    }
}
