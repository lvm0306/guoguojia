package com.lovesosoi.kotlin_shop.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import com.lovesosoi.kotlin_shop.R
import com.lovesosoi.kotlin_shop.bean.CCustomer


class SpinnerArrayAdapter(var context: Context, var list: MutableList<CCustomer.DataBean.CustomerBean>) :
    BaseAdapter() {

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any? {

        return list.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View? {
        var holder: SpinnerHolder
        var view=convertView
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.spinner_fruit, parent, false)
            holder = SpinnerHolder(view)
            view.tag = holder

        } else {
            view = convertView
            holder=view?.tag as SpinnerHolder
        }
        holder.tv.text = list.get(position).customer_name

        return convertView
    }

    inner class SpinnerHolder(view: View) {
        var tv: TextView = view.findViewById(R.id.tv)
    }
}
