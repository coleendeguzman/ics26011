package com.example.wishlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wishlist.databinding.SchoolPageBinding

class SchoolPage : AppCompatActivity() {

    private lateinit var binding: SchoolPageBinding
    private lateinit var db: DatabaseHandler
    private lateinit var wishAdapter: SchoolWishAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SchoolPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHandler(this)

        wishAdapter = SchoolWishAdapter(db.getWishesByCategory("SCHOOL"), this)
        binding.SchoolRecycler.layoutManager = LinearLayoutManager(this)
        binding.SchoolRecycler.adapter = wishAdapter

        val btnAddWish = findViewById<Button>(R.id.btn_add_wish)
        btnAddWish.setOnClickListener {
            val i = Intent(this, AddWishPage::class.java)
            startActivity(i)
        }
    }

    override fun onResume() {
        super.onResume()
        wishAdapter.refreshData(db.getWishesByCategory("SCHOOL"))
    }
}
