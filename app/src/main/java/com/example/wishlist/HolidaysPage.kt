package com.example.wishlist

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wishlist.databinding.HolidaysPageBinding

class HolidaysPage : AppCompatActivity() {
    private lateinit var binding: HolidaysPageBinding
    private lateinit var db: DatabaseHandler
    private lateinit var wishAdapter: WishAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HolidaysPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHandler(this)

        wishAdapter = WishAdapter(db.getWishesByCategory("HOLIDAYS"), this) { wishId ->
            showDeleteConfirmationDialog(wishId)
        }
        binding.HolidaysRecycler.layoutManager = LinearLayoutManager(this)
        binding.HolidaysRecycler.adapter = wishAdapter

        val btnAddWish = findViewById<ImageView>(R.id.add)
        val btnBack = findViewById<ImageView>(R.id.back)

        btnAddWish.setOnClickListener {
            val i = Intent(this, AddWishPage::class.java)
            val username = intent.getStringExtra("USERNAME")?.uppercase()
            i.putExtra("USERNAME", username) // Pass the username to AddWishActivity
            startActivity(i)
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        wishAdapter.refreshData(db.getWishesByCategory("HOLIDAYS"))
    }

    private fun showDeleteConfirmationDialog(wishId: Int) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.delete_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.findViewById<Button>(R.id.btnYes).setOnClickListener {
            dialog.dismiss()
            val deletedRows = db.deleteWishById(wishId)
            if (deletedRows > 0) {
                Toast.makeText(this, "Wish successfully deleted.", Toast.LENGTH_LONG).show()
                wishAdapter.refreshData(db.getWishesByCategory("HOLIDAYS"))
            } else {
                Toast.makeText(this, "Delete failed. Try again.", Toast.LENGTH_LONG).show()
            }
        }

        dialog.findViewById<Button>(R.id.btnNo).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}

