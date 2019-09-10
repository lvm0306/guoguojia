package com.lovesosoi.kotlin_shop.adapter


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.lovesosoi.kotlin_shop.R
import com.lovesosoi.kotlin_shop.bean.CFruitBean
import com.lovesosoi.kotlin_shop.bean.EditOrderBean
import com.lovesosoi.kotlin_shop.bean.OrderList
import com.lovesosoi.kotlin_shop.interfaces.OnItemClick

/**
 * 水果
 * 2019-7-19 Lovesosoi
 */
class EditOrderAdapter(var context:Context,var data:MutableList<EditOrderBean>) : RecyclerView.Adapter<EditOrderAdapter.EditOrderHolder>(){
    var listener: OnItemClick? =null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): EditOrderHolder {
        var inflate= LayoutInflater.from(context).inflate(R.layout.item_edit_order,p0,false)
        return EditOrderHolder(inflate)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(p0: EditOrderHolder, p1: Int) {
        val temp = data.get(p1)
        p0.tv_name.text=temp.fruit_name
        p0.tv_total.text=temp.fruit_total
        p0.et_amount.setText(temp.fruit_amount)
        p0.et_unit.setText(temp.fruit_unit)

    }

    inner class EditOrderHolder(view:View):RecyclerView.ViewHolder(view){
        var tv_name: TextView = view.findViewById(R.id.tv_name)
        var tv_total: TextView = view.findViewById(R.id.tv_total)
        var et_unit: EditText = view.findViewById(R.id.et_unit)
        var et_amount: EditText = view.findViewById(R.id.et_amount)
    }
}