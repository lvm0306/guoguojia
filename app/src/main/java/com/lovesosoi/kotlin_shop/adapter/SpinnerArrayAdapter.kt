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

class SpinnerArrayAdapter(var context: Context,var list:MutableList<CCustomer.DataBean.CustomerBean>) : BaseAdapter() {

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any? {

        return null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View? {
//        var inflate = LayoutInflater.from(context).inflate(R.layout.spinner_fruit, p0, false)

        return null
    }

    inner class OrderAdapterHolder(view: View) {
        var tv_name: TextView = view.findViewById(R.id.tv_name)
        var tv_count: TextView = view.findViewById(R.id.tv_count)
        var tv_sub: TextView = view.findViewById(R.id.tv_sub)
        var tv_add: TextView = view.findViewById(R.id.tv_add)
        var tv_unit: TextView = view.findViewById(R.id.tv_unit)
    }
}
