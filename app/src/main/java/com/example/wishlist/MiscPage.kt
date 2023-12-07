package com.example.wishlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wishlist.databinding.HolidaysPageBinding
import com.example.wishlist.databinding.MiscPageBinding

class MiscPage : AppCompatActivity() {
    private lateinit var binding: MiscPageBinding
    private lateinit var db: DatabaseHandler
    private lateinit var wishAdapter: WishAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MiscPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHandler(this)

        wishAdapter = WishAdapter(db.getWishesByCategory("MISC"), this)
        binding.MiscRecycler.layoutManager = LinearLayoutManager(this)
        binding.MiscRecycler.adapter = wishAdapter

        val btnAddWish = findViewById<Button>(R.id.btn_add_wish)
        btnAddWish.setOnClickListener {
            val i = Intent(this, AddWishPage::class.java)
            val username = intent.getStringExtra("USERNAME")?.uppercase()
            i.putExtra("USERNAME", username) // Pass the username to AddWishActivity
            startActivity(i)
        }
    }

    override fun onResume() {
        super.onResume()
        wishAdapter.refreshData(db.getWishesByCategory("MISC"))
    }
}