package com.example.wishlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wishlist.databinding.HolidaysPageBinding

class HolidaysPage : AppCompatActivity() {
    private lateinit var binding: HolidaysPageBinding
    private lateinit var db: DatabaseHandler
    private lateinit var wishAdapter: WishAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HolidaysPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHandler(this)

        wishAdapter = WishAdapter(db.getWishesByCategory("HOLIDAYS"), this)
        binding.HolidaysRecycler.layoutManager = LinearLayoutManager(this)
        binding.HolidaysRecycler.adapter = wishAdapter

        val btnAddWish = findViewById<Button>(R.id.addWish)

        btnAddWish.setOnClickListener {
            val i = Intent(this, AddWishPage::class.java)
            startActivity(i)
        }
    }

    override fun onResume() {
        super.onResume()
        wishAdapter.refreshData(db.getWishesByCategory("HOLIDAYS"))
    }
}

