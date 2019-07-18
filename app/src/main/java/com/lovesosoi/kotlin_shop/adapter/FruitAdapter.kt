package com.lovesosoi.kotlin_shop.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.lovesosoi.kotlin_shop.R
import com.lovesosoi.kotlin_shop.bean.CFruitBean
import com.lovesosoi.kotlin_shop.bean.FruitBean
import com.lovesosoi.kotlin_shop.interfaces.OnItemClick

class FruitAdapter(var context:Context,var data:MutableList<CFruitBean.DataBean.FruitBean>) : RecyclerView.Adapter<FruitAdapter.FruitAdapterHolder>(){
    var listener: OnItemClick? =null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FruitAdapterHolder {
        var inflate= LayoutInflater.from(context).inflate(R.layout.item_fruit,p0,false)
        return FruitAdapterHolder(inflate)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(p0: FruitAdapterHolder, p1: Int) {
        p0.tv_name.text=data.get(p1).fruit_name
        p0.tv_price.text= "单价：" + data.get(p1).fruit_price+data.get(p1).fruit_unit
        if (listener!=null) {
            p0.itemView.setOnClickListener { v: View? -> listener?.click(p1, p0.itemView, data.get(p1)) }
        }
    }

    fun setOnItemClickListener(click: OnItemClick){
        listener=click
    }
    inner class FruitAdapterHolder(view:View):RecyclerView.ViewHolder(view){
        var tv_name: TextView = view.findViewById(R.id.tv_name)
        var tv_price: TextView = view.findViewById(R.id.tv_price)
    }
}