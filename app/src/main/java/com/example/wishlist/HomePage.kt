package com.example.wishlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

class HomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)

        val username = intent.getStringExtra("USERNAME")?.uppercase()

        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val tvUsername = findViewById<TextView>(R.id.tvUsername)

        val btnSchool = findViewById<ImageButton>(R.id.btnSchool)
        val btnBirthday = findViewById<ImageButton>(R.id.btnBirthday)
        val btnHolidays = findViewById<ImageButton>(R.id.btnHolidays)
        val btnMisc = findViewById<ImageButton>(R.id.btnMisc)

        tvUsername.text = username


        btnSchool.setOnClickListener {
            val i = Intent(this, SchoolPage::class.java)
            i.putExtra("USERNAME", username) // Pass the username to AddWishActivity
            startActivity(i)
        }

        btnBirthday.setOnClickListener {
            val i = Intent(this, BirthdayPage::class.java)
            i.putExtra("USERNAME", username) // Pass the username to AddWishActivity
            startActivity(i)
        }

        btnHolidays.setOnClickListener {
            val i = Intent(this, HolidaysPage::class.java)
            i.putExtra("USERNAME", username) // Pass the username to AddWishActivity
            startActivity(i)
        }

        btnMisc.setOnClickListener {
            val i = Intent(this, MiscPage::class.java)
            i.putExtra("USERNAME", username) // Pass the username to AddWishActivity
            startActivity(i)
        }

        btnLogout.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }


    }
}