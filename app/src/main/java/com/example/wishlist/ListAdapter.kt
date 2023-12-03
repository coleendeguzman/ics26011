package com.example.wishlist
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ListAdapter(
    private val context: Activity,
    private val wishname: Array<String>,
    private val wishlink: Array<String>,
    private val wishdesc: Array<String>
) : ArrayAdapter<String>(context, R.layout.custom_list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var rowView = convertView
        val viewHolder: ViewHolder

        if (rowView == null) {
            val inflater = context.layoutInflater
            rowView = inflater.inflate(R.layout.custom_list, parent, false)

            viewHolder = ViewHolder()
            viewHolder.wishText = rowView.findViewById(R.id.tvWishName)
            viewHolder.linkText = rowView.findViewById(R.id.tvWishLink)
            viewHolder.descText = rowView.findViewById(R.id.tvDescription)

            rowView.tag = viewHolder
        } else {
            viewHolder = rowView.tag as ViewHolder
        }

        viewHolder.wishText?.text = wishname[position]
        viewHolder.linkText?.text = wishlink[position]
        viewHolder.descText?.text = wishdesc[position]

        return rowView!!
    }

    private class ViewHolder {
        var wishText: TextView? = null
        var linkText: TextView? = null
        var descText: TextView? = null
    }
}
