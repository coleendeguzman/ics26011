package com.example.wishlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

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

            val wishname = etWishName.text.toString()
            val wishdesc = etWishDesc.text.toString()
            val wishlink = etWishLink.text.toString()
            val imageurl = etImageUrl.text.toString()


            if (wishname.trim().isNotEmpty() && wishlink.trim().isNotEmpty() && wishdesc.trim()
                    .isNotEmpty()
            ) {
                val wishSuccess = dbHandler.createWish(wishname, wishdesc, wishlink, imageurl)

                if (wishSuccess != -1L) {

                    Toast.makeText(this, "Entry successful!", Toast.LENGTH_LONG)
                        .show()
                    val i = Intent(this, HomePage::class.java)
                    startActivity(i)
                } else {

                    Toast.makeText(
                        this,
                        "Entry Failed. Please try again.",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } else {

                Toast.makeText(
                    this,
                    "Required Fields Left Empty. Please try again.",
                    Toast.LENGTH_LONG
                ).show()
            }


        }
    }
}