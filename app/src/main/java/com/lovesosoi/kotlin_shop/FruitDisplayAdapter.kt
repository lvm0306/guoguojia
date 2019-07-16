
package com.lovesosoi.kotlin_shop

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class FruitDisplayAdapter(var context:Context,var data:MutableList<FruitBean>) : RecyclerView.Adapter<FruitDisplayAdapter.FruitAdapterHolder>(){
    var listener: OnListItemLongClickListener? =null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FruitAdapterHolder {
        var inflate= LayoutInflater.from(context).inflate(R.layout.item_fruit,p0,false)
        return FruitAdapterHolder(inflate)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(p0: FruitAdapterHolder, p1: Int) {
        p0.tv_name.text=data.get(p1).name
        p0.tv_price.text= "单价：" + data.get(p1).price+data.get(p1).unit
        if (listener!=null) {
            p0.itemView.setOnLongClickListener(object :View.OnLongClickListener{
                override fun onLongClick(v: View?): Boolean {
                    listener?.click(p1, p0.itemView, data.get(p1))
                    return true
                }
            })
        }
    }

    fun setOnItemClickListener(click:OnListItemLongClickListener){
        listener=click
    }
    inner class FruitAdapterHolder(view:View):RecyclerView.ViewHolder(view){
        var tv_name: TextView = view.findViewById(R.id.tv_name)
        var tv_price: TextView = view.findViewById(R.id.tv_price)
    }
}