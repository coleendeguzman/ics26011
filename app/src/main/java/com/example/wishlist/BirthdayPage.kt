package com.example.wishlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wishlist.databinding.BirthdayPageBinding
import com.example.wishlist.databinding.SchoolPageBinding

class BirthdayPage : AppCompatActivity() {
    private lateinit var binding: BirthdayPageBinding
    private lateinit var db: DatabaseHandler
    private lateinit var wishAdapter: BirthdayWishAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BirthdayPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHandler(this)

        wishAdapter = BirthdayWishAdapter(db.getWishesByCategory("BIRTHDAY"), this)
        binding.BirthdayRecycler.layoutManager = LinearLayoutManager(this)
        binding.BirthdayRecycler.adapter = wishAdapter

        val btnAddWish = findViewById<Button>(R.id.btn_add_wish)
        btnAddWish.setOnClickListener {
            val i = Intent(this, AddWishPage::class.java)
            startActivity(i)
        }
    }

    override fun onResume() {
        super.onResume()
        wishAdapter.refreshData(db.getWishesByCategory("BIRTHDAY"))
    }
}