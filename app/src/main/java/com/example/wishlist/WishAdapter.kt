package com.example.wishlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView

class WishAdapter(private var wishes: List<Wishes>, context: Context) : RecyclerView.Adapter<WishAdapter.WishViewHolder>() {

    class WishViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.schoolWishTitle)
        val descTextView: TextView = itemView.findViewById(R.id.schoolWishDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.school_wish, parent, false)
            return WishViewHolder(view)
    }

    override fun getItemCount(): Int = wishes.size

    override fun onBindViewHolder(holder: WishViewHolder, position: Int) {
        val wish = wishes[position]
        holder.titleTextView.text = wish.wishname
        holder.descTextView.text = wish.wishdesc
    }

    fun refreshData(newWishes: List<Wishes>) {
        wishes= newWishes
        notifyDataSetChanged()
    }

}