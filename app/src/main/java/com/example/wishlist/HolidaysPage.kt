package com.example.wishlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView


class HolidaysPage : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var wishAdapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.holidays_page)

        listView = findViewById(R.id.listViewHolidays)
        val dbHandler = DatabaseHandler(this)
        val category = "HOLIDAYS"

        val wishes = dbHandler.getWishesByCategory(category)

        val wishnames = wishes.map { it.wishname }.toTypedArray()
        val wishlinks = wishes.map { it.wishlink }.toTypedArray()
        val wishdescs = wishes.map { it.wishdesc }.toTypedArray()

        wishAdapter = ListAdapter(this, wishnames, wishlinks, wishdescs)
        listView.adapter = wishAdapter

        val btnAddWish = findViewById<Button>(R.id.addWish)

        btnAddWish.setOnClickListener {
            val i = Intent(this, AddWishPage::class.java)
            startActivity(i)
        }
    }

}
