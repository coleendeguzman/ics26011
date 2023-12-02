package com.example.wishlist

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ListAdapter(private val context: Activity, private val wishname: Array<String>, private val wishlink: Array<String>,
                  private val wishdesc: Array<String>)
    :ArrayAdapter<String>(context, R.layout.custom_list){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_list, null, true)

        val wishText = rowView.findViewById<TextView>(R.id.tvWishName)
        val linkText = rowView.findViewById<TextView>(R.id.tvWishLink)
        val descText = rowView.findViewById<TextView>(R.id.tvDescription)

        wishText.text = "${wishname[position]}"
        linkText.text = "${wishlink[position]}"
        descText.text= "${wishdesc[position]}"

        return rowView
    }
}