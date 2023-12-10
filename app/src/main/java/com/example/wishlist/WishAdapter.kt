package com.example.wishlist

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
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
        val updateWish: ImageView = itemView.findViewById(R.id.edit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_custom, parent, false)
        return WishViewHolder(view)
    }

    override fun getItemCount(): Int = wishes.size

    override fun onBindViewHolder(holder: WishViewHolder, position: Int) {
        val wish = wishes[position]
        holder.titleTextView.text = wish.wishname
        holder.descTextView.text = wish.wishdesc

        // Make links clickable
        holder.linkTextView.movementMethod = LinkMovementMethod.getInstance()
        holder.linkTextView.text = wish.wishlink?.let { link ->
            val spannableString = android.text.SpannableString(link)
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    handleClickedLink(link, holder.itemView.context)
                }
            }
            spannableString.setSpan(clickableSpan, 0, link.length, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString
        }

        holder.deleteImageView.setOnClickListener {
            val wishId = wishes[position].id
            deleteListener.invoke(wishId) // Call the deleteListener with wish ID
        }

        holder.updateWish.setOnClickListener {
            val i = Intent(holder.itemView.context, UpdateWish::class.java).apply {
                putExtra("wish_id", wish.id)
            }
            holder.itemView.context.startActivity(i)
        }
    }

    fun refreshData(newWishes: List<Wishes>) {
        wishes= newWishes
        notifyDataSetChanged()
    }

    private fun handleClickedLink(link: String, context: Context) {
        // Handle the clicked link here
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        context.startActivity(intent)
    }


}