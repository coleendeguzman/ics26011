package com.example.wishlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView


class HolidaysPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.holidays_page)

        val btnAddWish = findViewById<Button>(R.id.addWish)

        btnAddWish.setOnClickListener {
            val i = Intent(this, AddWishPage::class.java)
            startActivity(i)
        }
    }
}
