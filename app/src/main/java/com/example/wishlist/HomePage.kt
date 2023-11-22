package com.example.wishlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class HomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val username = intent.getStringExtra("USERNAME")?.uppercase()

        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val tvUsername = findViewById<TextView>(R.id.tvUsername)

        tvUsername.text = username

        btnLogout.setOnClickListener {
            val i = Intent(this, LoginPage::class.java)
            startActivity(i)
            finish()
        }
    }
}