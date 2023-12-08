package com.example.wishlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast

class UpdateWish : AppCompatActivity() {

    private lateinit var wish: Wishes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_wish_page)

        val wishId = intent.getIntExtra("wish_id", -1) // Get wish ID from intent

        fetchWishDetails(wishId)

        populateFields()

        val updateButton = findViewById<ImageView>(R.id.update)
        updateButton.setOnClickListener {
            updateWishDetails()
        }
    }

    private fun fetchWishDetails(wishId: Int) {
        val databaseHandler = DatabaseHandler(this)
        wish = databaseHandler.getWishByID(wishId)
    }

    private fun populateFields() {
        val wishNameEditText = findViewById<EditText>(R.id.updateName)
        val wishLinkEditText = findViewById<EditText>(R.id.updateLink)
        val wishDescEditText = findViewById<EditText>(R.id.updateDesc)
        val radioGroupCategory = findViewById<RadioGroup>(R.id.rgUpdateCategory)

        wishNameEditText.setText(wish.wishname)
        wishLinkEditText.setText(wish.wishlink)
        wishDescEditText.setText(wish.wishdesc)

        when (wish.category) {
            "SCHOOL" -> radioGroupCategory.check(R.id.rb_school)
            "BIRTHDAY" -> radioGroupCategory.check(R.id.rb_birthday)
            "HOLIDAYS" -> radioGroupCategory.check(R.id.rb_holidays)
            "MISC" -> radioGroupCategory.check(R.id.rb_misc)
        }
    }

    private fun updateWishDetails() {
        val databaseHandler = DatabaseHandler(this)
        val updatedWishName = findViewById<EditText>(R.id.updateName).text.toString()
        val updatedWishLink = findViewById<EditText>(R.id.updateLink).text.toString()
        val updatedWishDesc = findViewById<EditText>(R.id.updateDesc).text.toString()

        val radioGroupCategory = findViewById<RadioGroup>(R.id.rgUpdateCategory)
        val selectedCategoryRadioButtonId = radioGroupCategory.checkedRadioButtonId
        val selectedCategory = when (selectedCategoryRadioButtonId) {
            R.id.rb_school -> "SCHOOL"
            R.id.rb_birthday -> "BIRTHDAY"
            R.id.rb_holidays -> "HOLIDAYS"
            R.id.rb_misc -> "MISC"
            else -> "DefaultCategory" // Set a default category if none is selected
        }

        wish = Wishes(wish.id, updatedWishName, updatedWishLink, updatedWishDesc, selectedCategory)
        databaseHandler.updateWish(wish.id, updatedWishName, updatedWishLink, updatedWishDesc, selectedCategory)

        Toast.makeText(this, "Wish updated successfully", Toast.LENGTH_LONG).show()
        finish()
    }
}

