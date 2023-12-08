package com.example.wishlist

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.wishlist.databinding.ActivityUpdateWishBinding

class UpdateWish : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateWishBinding
    private lateinit var db: DatabaseHandler
    private var wishId: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateWishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHandler(this)

        wishId = intent.getIntExtra("wish_id", -1)
        if (wishId == -1){
            finish()
            return
        }

        val wish = db.getWishByID(wishId)
        binding.etUpdateWishName.setText(wish.wishname)
        binding.etUpdateWishDescription.setText(wish.wishdesc)
        binding.etUpdateWishLink.setText(wish.wishdesc)

        binding.update.setOnClickListener {
            val newName = binding.etUpdateWishName.text.toString()
            val newDesc = binding.etUpdateWishDescription.text.toString()
            val newLink = binding.etUpdateWishLink.text.toString()
            val newCat = binding.rgUpdateCategory.toString()
            val updatedWish = Wishes(wishId, newName, newDesc, newLink, newCat)

            db.updateWish(updatedWish)
            finish()
            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show()
        }
    }
}