package com.example.wishlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WishAdapter(private var wishes: List<Wishes>,
                  context: Context,
                  private val deleteListener: (Int) -> Unit)
    : RecyclerView.Adapter<WishAdapter.WishViewHolder>() {

    class WishViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.wishTitle)
        val linkTextView: TextView = itemView.findViewById(R.id.wishLink)
        val descTextView: TextView = itemView.findViewById(R.id.wishDesc)
        val deleteImageView: ImageView = itemView.findViewById(R.id.delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_custom, parent, false)
        return WishViewHolder(view)

    }

    override fun getItemCount(): Int = wishes.size

    override fun onBindViewHolder(holder: WishViewHolder, position: Int) {
        val wish = wishes[position]
        holder.titleTextView.text = wish.wishname
        holder.linkTextView.text = wish.wishlink
        holder.descTextView.text = wish.wishdesc
        holder.deleteImageView.setOnClickListener {
            val wishId = wishes[position].id
            deleteListener.invoke(wishId) // Call the deleteListener with wish ID
        }
    }

    fun refreshData(newWishes: List<Wishes>) {
        wishes= newWishes
        notifyDataSetChanged()
    }


}