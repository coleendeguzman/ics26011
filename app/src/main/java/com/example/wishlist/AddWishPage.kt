package com.example.wishlist

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class AddWishPage : AppCompatActivity() {
    private lateinit var dbHandler: DatabaseHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_wish_page)

        dbHandler = DatabaseHandler(this)

        val btnAddWish = findViewById<Button>(R.id.btn_add_wish)

        btnAddWish.setOnClickListener {
            val etWishName = findViewById<EditText>(R.id.et_wish_name)
            val etWishDesc = findViewById<EditText>(R.id.et_wish_description)
            val etWishLink = findViewById<EditText>(R.id.et_wish_link)
            val etImageUrl = findViewById<EditText>(R.id.et_image_url)
            val radioGroup = findViewById<RadioGroup>(R.id.rg_category)

            val wishname = etWishName.text.toString()
            val wishdesc = etWishDesc.text.toString()
            val wishlink = etWishLink.text.toString()
            val imageurl = etImageUrl.text.toString()

            val checkedRadioButtonId = radioGroup.checkedRadioButtonId

            if (checkedRadioButtonId != -1) {
                val checkedRadioButton = findViewById<RadioButton>(checkedRadioButtonId)
                val selectedCategory = checkedRadioButton.text.toString()

                if (wishname.trim().isNotEmpty() && wishlink.trim().isNotEmpty() && wishdesc.trim().isNotEmpty()) {

                    val dialog = Dialog(this)
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setContentView(R.layout.add_dialog)
                    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.findViewById<Button>(R.id.btnYes).setOnClickListener {
                        dialog.dismiss()

                        val wishSuccess = dbHandler.createWish(wishname, wishdesc, wishlink, imageurl, selectedCategory)

                        if (wishSuccess != -1L) {
                            Toast.makeText(this, "Entry successful!", Toast.LENGTH_LONG).show()
                            val i = Intent(this, HomePage::class.java)
                            startActivity(i)
                        } else {
                            Toast.makeText(this, "Entry Failed. Please try again.", Toast.LENGTH_LONG).show()
                        }
                    }

                    dialog.findViewById<Button>(R.id.btnNo).setOnClickListener {
                        dialog.dismiss()
                    }

                    dialog.show()

                } else {
                    Toast.makeText(this, "Required Fields left Empty. Please try again.", Toast.LENGTH_LONG).show()
                }

            } else {
                Toast.makeText(this, "Please select a category", Toast.LENGTH_LONG).show()
            }
        }
    }
}