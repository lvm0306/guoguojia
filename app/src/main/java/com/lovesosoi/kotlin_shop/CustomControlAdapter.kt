package com.lovesosoi.kotlin_shop

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class CustomControlAdapter(var context: Context, var data:MutableList<String>) : RecyclerView.Adapter<CustomControlAdapter.CustomControlViewHoler>(){
    var listener: OnListItemLongClickListener? =null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomControlViewHoler {
        var inflate= LayoutInflater.from(context).inflate(R.layout.item_custom_control,p0,false)
        return CustomControlViewHoler(inflate)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(p0: CustomControlViewHoler, p1: Int) {
        p0.tv_name.text=data.get(p1)
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
    inner class CustomControlViewHoler(view: View): RecyclerView.ViewHolder(view){
        var tv_name: TextView = view.findViewById(R.id.tv_name)
    }
}