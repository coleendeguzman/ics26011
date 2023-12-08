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
import com.example.wishlist.databinding.MiscPageBinding

class MiscPage : AppCompatActivity() {
    private lateinit var binding: MiscPageBinding
    private lateinit var db: DatabaseHandler
    private lateinit var wishAdapter: WishAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MiscPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHandler(this)

        wishAdapter = WishAdapter(db.getWishesByCategory("MISC"), this) { wishId ->
            // Delete action with confirmation
            showDeleteConfirmationDialog(wishId)
        }
        binding.MiscRecycler.layoutManager = LinearLayoutManager(this)
        binding.MiscRecycler.adapter = wishAdapter

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
        wishAdapter.refreshData(db.getWishesByCategory("MISC"))
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
                wishAdapter.refreshData(db.getWishesByCategory("MISC"))
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