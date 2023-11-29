package com.example.wishlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class HolidaysPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holidays_page)
    }

    fun toWishCreation(view: View) {
        val i = Intent(this, AddWishPage::class.java);
        startActivity(i)
    }
}