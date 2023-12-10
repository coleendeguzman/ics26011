package com.example.wishlist

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast

class AddWishPage : AppCompatActivity() {

    private lateinit var etWishName: EditText
    private lateinit var etWishDesc: EditText
    private lateinit var etWishLink: EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var dbHandler: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_wish_page)

        dbHandler = DatabaseHandler(this)

        etWishName = findViewById(R.id.et_wish_name)
        etWishLink = findViewById(R.id.et_wish_link)
        etWishDesc = findViewById(R.id.et_wish_description)
        radioGroup = findViewById(R.id.rg_category)


        val btnAddWish = findViewById<ImageView>(R.id.edit)
        val btnBack = findViewById<ImageView>(R.id.back)
        val btnClear = findViewById<TextView>(R.id.clear)

        btnAddWish.setOnClickListener {
            addWish()
        }
        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnClear.setOnClickListener {
            clearFields()
        }
    }

    private fun clearFields() {
        etWishName.text.clear()
        etWishLink.text.clear()
        etWishDesc.text.clear()
        radioGroup.clearCheck()
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

    private fun addWish() {
        val wishname = etWishName.text.toString()
        val wishlink = etWishLink.text.toString()
        val wishdesc = etWishDesc.text.toString()

        val checkedRadioButtonId = radioGroup.checkedRadioButtonId
        if (checkedRadioButtonId != -1) {
            val checkedRadioButton = findViewById<RadioButton>(checkedRadioButtonId)
            val selectedCategory = checkedRadioButton.text.toString()
            if (wishname.trim().isNotEmpty() && wishlink.trim().isNotEmpty() && wishdesc.trim().isNotEmpty()) {
                showConfirmationDialog(wishname, wishlink, wishdesc, selectedCategory)
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
        selectedCategory: String
    ) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.add_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.findViewById<Button>(R.id.btnYes).setOnClickListener {
            dialog.dismiss()
            val wishSuccess = dbHandler.createWish(wishname, wishlink, wishdesc, selectedCategory)

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