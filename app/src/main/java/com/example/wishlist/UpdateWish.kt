package com.example.wishlist

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wishlist.databinding.UpdateWishPageBinding

class UpdateWish : AppCompatActivity() {

    private lateinit var binding: UpdateWishPageBinding
    private lateinit var wish: Wishes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UpdateWishPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val wishId = intent.getIntExtra("wish_id", -1) // Get wish ID from intent

        fetchWishDetails(wishId)

        populateFields()

        val btnUpdate = binding.update
        val btnBack = binding.back
        val btnClear = binding.clear


        btnUpdate.setOnClickListener {
            showUpdateConfirmationDialog()
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnClear.setOnClickListener {
            clearFields()
        }
    }

    private fun clearFields() {
        binding.updateName.text.clear()
        binding.updateLink.text.clear()
        binding.updateDesc.text.clear()
        binding.rgUpdateCategory.clearCheck()
    }

    override fun onBackPressed() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.cancel_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.setOnKeyListener { _, keyCode, _ ->
            keyCode == KeyEvent.KEYCODE_BACK
        }

        val btnCancel = dialog.findViewById<Button>(R.id.btnNo)
        val btnConfirm = dialog.findViewById<Button>(R.id.btnYes)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            dialog.dismiss()
            super.onBackPressed() // Go back only if confirmed
        }

        dialog.show()
    }

    private fun fetchWishDetails(wishId: Int) {
        val databaseHandler = DatabaseHandler(this)
        wish = databaseHandler.getWishByID(wishId)
    }

    private fun populateFields() {
        val wishNameEditText = binding.updateName
        val wishLinkEditText = binding.updateLink
        val wishDescEditText = binding.updateDesc
        val radioGroupCategory = binding.rgUpdateCategory

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

    private fun showUpdateConfirmationDialog() {
        val updatedWishName = binding.updateName.text.toString()
        val updatedWishLink = binding.updateLink.text.toString()
        val updatedWishDesc = binding.updateDesc.text.toString()

        val radioGroupCategory = binding.rgUpdateCategory
        val selectedCategoryRadioButtonId = radioGroupCategory.checkedRadioButtonId
        val selectedCategory = when (selectedCategoryRadioButtonId) {
            R.id.rb_school -> "SCHOOL"
            R.id.rb_birthday -> "BIRTHDAY"
            R.id.rb_holidays -> "HOLIDAYS"
            R.id.rb_misc -> "MISC"
            else -> "DefaultCategory" // Set a default category if none is selected
        }

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.update_dialog)
        dialog.setContentView(R.layout.update_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnYes = dialog.findViewById<Button>(R.id.btnYes)
        val btnNo = dialog.findViewById<Button>(R.id.btnNo)

        btnYes.setOnClickListener {
            dialog.dismiss()
            updateWish(updatedWishName, updatedWishLink, updatedWishDesc, selectedCategory)
        }

        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun updateWish(
        updatedWishName: String,
        updatedWishLink: String,
        updatedWishDesc: String,
        selectedCategory: String
    ) {
        val databaseHandler = DatabaseHandler(this)
        wish = Wishes(wish.id, updatedWishName, updatedWishLink, updatedWishDesc, selectedCategory)
        databaseHandler.updateWish(wish.id, updatedWishName, updatedWishLink, updatedWishDesc, selectedCategory)

        Toast.makeText(this, "Wish updated successfully", Toast.LENGTH_LONG).show()
        finish()
    }
}
