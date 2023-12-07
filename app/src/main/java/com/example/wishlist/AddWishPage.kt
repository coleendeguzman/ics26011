package com.example.wishlist

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast

class AddWishPage : AppCompatActivity() {

    private lateinit var etWishName: EditText
    private lateinit var etWishDesc: EditText
    private lateinit var etWishLink: EditText
    private lateinit var etImageUrl: EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var dbHandler: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_wish_page)

        dbHandler = DatabaseHandler(this)

        etWishName = findViewById(R.id.et_wish_name)
        etWishDesc = findViewById(R.id.et_wish_description)
        etWishLink = findViewById(R.id.et_wish_link)
        etImageUrl = findViewById(R.id.et_image_url)
        radioGroup = findViewById(R.id.rg_category)

        val btnAddWish = findViewById<Button>(R.id.btn_add_wish)

        btnAddWish.setOnClickListener {
            addWish()
        }
    }

    private fun addWish() {
        val wishname = etWishName.text.toString()
        val wishdesc = etWishDesc.text.toString()
        val wishlink = etWishLink.text.toString()
        val imageurl = etImageUrl.text.toString()

        val checkedRadioButtonId = radioGroup.checkedRadioButtonId

        if (checkedRadioButtonId != -1) {
            val checkedRadioButton = findViewById<RadioButton>(checkedRadioButtonId)
            val selectedCategory = checkedRadioButton.text.toString()

            if (wishname.trim().isNotEmpty() && wishlink.trim().isNotEmpty() && wishdesc.trim().isNotEmpty()) {
                showConfirmationDialog(wishname, wishlink, wishdesc, imageurl, selectedCategory)
            } else {
                showToast("Required Fields left Empty. Please try again.")
            }
        } else {
            showToast("Please select a category")
        }
    }

    private fun showConfirmationDialog(
        wishname: String,
        wishlink: String,
        wishdesc: String,
        imageurl: String,
        selectedCategory: String
    ) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.add_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.findViewById<Button>(R.id.btnYes).setOnClickListener {
            dialog.dismiss()
            val wishSuccess = dbHandler.createWish(wishname, wishlink, wishdesc, imageurl, selectedCategory)

            if (wishSuccess != -1L) {
                showToast("Entry successful!")
                val i = Intent(this, HomePage::class.java)
                val username = intent.getStringExtra("USERNAME")
                i.putExtra("USERNAME", username)
                startActivity(i)

            } else {
                showToast("Entry Failed. Please try again.")
            }
        }

        dialog.findViewById<Button>(R.id.btnNo).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun navigateToHomePage() {
        val intent = Intent(this, HomePage::class.java)
        startActivity(intent)
    }
}
