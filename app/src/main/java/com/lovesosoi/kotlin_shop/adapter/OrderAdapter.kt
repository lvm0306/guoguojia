package com.lovesosoi.kotlin_shop.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.lovesosoi.kotlin_shop.R
import com.lovesosoi.kotlin_shop.bean.OrderBean
import com.lovesosoi.kotlin_shop.interfaces.OnCountClick
import com.lovesosoi.kotlin_shop.interfaces.OnItemClick

/**
 * 下单
 * 2019-7-19 Lovesosoi
 */
class OrderAdapter(var context: Context, var data: MutableList<OrderBean>) :
    RecyclerView.Adapter<OrderAdapter.OrderAdapterHolder>() {
    lateinit var listener: OnItemClick
    lateinit var countListener: OnCountClick
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OrderAdapterHolder {
        var inflate = LayoutInflater.from(context).inflate(R.layout.item_menu, p0, false)
        return OrderAdapterHolder(inflate)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(p0: OrderAdapterHolder, p1: Int) {
//            p0?.tv_name?.text="黄瓜"
//        listener?.click()
        p0.tv_name.text = data.get(p1).name
        p0.tv_unit.text=data.get(p1).price.toString()+"/"+data.get(p1).unit
        p0.tv_count.text = "" + data.get(p1).count + data.get(p1).unit
        p0.itemView.setOnClickListener { v: View? -> listener?.click(p1, p0.itemView, data.get(p1)) }
        p0.tv_sub.setOnClickListener { v: View? -> listener?.sub(p1, p0.itemView, data.get(p1)) }
        p0.tv_add.setOnClickListener { v: View? -> listener?.add(p1, p0.itemView, data.get(p1)) }
        p0.tv_count.setOnClickListener { v: View? -> countListener?.countClick(p1, p0.itemView, data.get(p1)) }

    }

    fun setOnItemClickListener(click: OnItemClick) {
        listener = click
    }

    fun setOnCountClickListener(click: OnCountClick) {
        countListener = click
    }

    inner class OrderAdapterHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_name: TextView = view.findViewById(R.id.tv_name)
        var tv_count: TextView = view.findViewById(R.id.tv_count)
        var tv_sub: TextView = view.findViewById(R.id.tv_sub)
        var tv_add: TextView = view.findViewById(R.id.tv_add)
        var tv_unit: TextView = view.findViewById(R.id.tv_unit)
    }
}