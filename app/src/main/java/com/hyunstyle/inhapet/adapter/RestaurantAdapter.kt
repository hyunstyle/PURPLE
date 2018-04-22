package com.hyunstyle.inhapet.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import com.hyunstyle.inhapet.R
import com.hyunstyle.inhapet.model.Restaurant
import kotlinx.android.synthetic.main.list_item_restaurant.view.*
import java.util.*

/**
 * Created by sh on 2018-04-06.
 */
class RestaurantAdapter(val context: Context,
                        private val viewId: Int) : BaseAdapter() {

    private var items: List<Restaurant> = Collections.emptyList()

    override fun getItem(position: Int): Any { return items[position] }

    override fun getItemId(p0: Int): Long { return p0.toLong() }

    override fun getCount(): Int {
        Log.e("getcount", "" + items.size)
        return items.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val itemView: View
        val holder: ViewHolder

        if (convertView == null) {
            itemView = LayoutInflater.from(context).inflate(this.viewId, null)
            holder = ViewHolder()
            holder.name = itemView.findViewById(R.id.restaurant_name)
            holder.phone = itemView.findViewById(R.id.restaurant_phone)

            itemView.tag = holder

            Log.e("first", "binded");

        } else {
            holder = convertView.tag as ViewHolder
            itemView = convertView

            Log.e("after", "binded")
        }

        holder.bind(items[position])

        return itemView
    }

    fun setData(i: List<Restaurant>) {

        Log.e("datad", "data received " + i.size)
        this.items = i
        notifyDataSetChanged()
    }

    private class ViewHolder {
        var name: TextView? = null
        var phone: TextView? = null

        fun bind(restaurant: Restaurant) {
            name!!.text = restaurant.name
            phone!!.text = restaurant.phone
        }
    }
}