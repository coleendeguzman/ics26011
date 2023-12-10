package com.example.wishlist

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class HomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)

        val username = intent.getStringExtra("USERNAME")?.uppercase()

        val btnLogout = findViewById<TextView>(R.id.btnLogout)
        val btnAdd = findViewById<ImageView>(R.id.add)
        val tvUsername = findViewById<TextView>(R.id.tvUsername)

        val btnSchool = findViewById<ImageButton>(R.id.btnSchool)
        val btnBirthday = findViewById<ImageButton>(R.id.btnBirthday)
        val btnHolidays = findViewById<ImageButton>(R.id.btnHolidays)
        val btnMisc = findViewById<ImageButton>(R.id.btnMisc)

        tvUsername.text = username

        btnSchool.setOnClickListener {
            val i = Intent(this, SchoolPage::class.java)
            i.putExtra("USERNAME", username) // Pass the username to AddWishActivity
            startActivity(i)
        }

        btnBirthday.setOnClickListener {
            val i = Intent(this, BirthdayPage::class.java)
            i.putExtra("USERNAME", username) // Pass the username to AddWishActivity
            startActivity(i)
        }

        btnHolidays.setOnClickListener {
            val i = Intent(this, HolidaysPage::class.java)
            i.putExtra("USERNAME", username) // Pass the username to AddWishActivity
            startActivity(i)
        }

        btnMisc.setOnClickListener {
            val i = Intent(this, MiscPage::class.java)
            i.putExtra("USERNAME", username) // Pass the username to AddWishActivity
            startActivity(i)
        }

        btnAdd.setOnClickListener{
            val i = Intent (this, AddWishPage::class.java)
            i.putExtra("USERNAME", username) // Pass the username to AddWishActivity
            startActivity(i)        }

        btnLogout.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.logout_dialog)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.setCanceledOnTouchOutside(false)

            val btnCancelLogout = dialog.findViewById<Button>(R.id.btnNo)
            val btnConfirmLogout = dialog.findViewById<Button>(R.id.btnYes)

            btnCancelLogout.setOnClickListener {
                dialog.dismiss()
            }

            btnConfirmLogout.setOnClickListener {
                dialog.dismiss()
                val i = Intent(this, LoginPage::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)
                finish()
            }

            dialog.show()
        }
    }
}