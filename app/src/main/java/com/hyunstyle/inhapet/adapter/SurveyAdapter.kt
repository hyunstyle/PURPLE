package com.hyunstyle.inhapet.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.hyunstyle.inhapet.R
import com.hyunstyle.inhapet.interfaces.SurveyResponse
import com.hyunstyle.inhapet.model.Restaurant
import org.jetbrains.anko.find

/**
 * Created by sh on 2018-04-17.
 */

class SurveyAdapter(val context: Context, private val menuType: Int, private val viewId: Int, private val types: Array<String>,
                    private val delegate: SurveyResponse) : BaseAdapter() {


    override fun getCount(): Int {
        return types.size
    }

    override fun getItem(i: Int): Any {
        return types[i]
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View? {
        val itemView: View
        val holder: ViewHolder

        if (view == null) {
            itemView = LayoutInflater.from(context).inflate(this.viewId, null)
            holder = ViewHolder()
            holder.name = itemView.findViewById(R.id.type_text)
            holder.layout = itemView.findViewById(R.id.type_layout)
            holder.layout!!.setOnClickListener{v -> delegate.clicked(menuType, i, holder.layout!!, holder.name!!)}

            itemView.tag = holder

            Log.e("first", "binded")

        } else {
            holder = view.tag as ViewHolder
            itemView = view

            Log.e("after", "binded")
        }

        holder.bind(types[i])

        return itemView
    }

    private class ViewHolder {
        var name: TextView? = null
        var layout: LinearLayout? = null

        fun bind(typeName: String) {
            name!!.text = typeName
        }
    }


}
