package com.lovesosoi.kotlin_shop.adapter

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.lovesosoi.kotlin_shop.R
import com.lovesosoi.kotlin_shop.bean.CFruitBean
import com.lovesosoi.kotlin_shop.interfaces.OnItemClick

/**
 * 水果
 * 2019-7-19 Lovesosoi
 */
class FruitAdapter(var context:Context,var data:MutableList<CFruitBean.DataBean.FruitBean>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        val itemViewType = getItemViewType(p1)
        when(itemViewType){
            1->{
                p0 as FruitAdapterHolder
                if (data.get(p1).fruit_cate=="1"){

                }
                p0.tv_name.text=data.get(p1).fruit_name+"("+data.get(p1).fruit_unit+")"
                if (listener!=null) {
                    p0.itemView.setOnClickListener { v: View? -> listener?.click(p1, p0.itemView, data.get(p1)) }
                }
            }
            2->{
                p0 as FruitTypeAdapterHolder
                p0.tv_name.text=data.get(p1).fruit_name
                if (data.get(p1).fruit_name=="常用"){
                    p0.tv_name.setTextColor(Color.parseColor("#ffffff"))
                    p0.tv_name.setBackgroundColor(Color.parseColor("#ff6666"))
                }else if (data.get(p1).fruit_name=="青菜"){
                    p0.tv_name.setTextColor(Color.parseColor("#ffffff"))
                    p0.tv_name.setBackgroundColor(Color.parseColor("#66CD00"))
                }else if (data.get(p1).fruit_name=="豆类"){
                    p0.tv_name.setTextColor(Color.parseColor("#ffffff"))
                    p0.tv_name.setBackgroundColor(Color.parseColor("#EECBAD"))
                }else if (data.get(p1).fruit_name=="菌类"){
                    p0.tv_name.setTextColor(Color.parseColor("#ffffff"))
                    p0.tv_name.setBackgroundColor(Color.parseColor("#FF69B4"))
                }else if (data.get(p1).fruit_name=="其他"){
                    p0.tv_name.setTextColor(Color.parseColor("#ffffff"))
                    p0.tv_name.setBackgroundColor(Color.parseColor("#D1EEEE"))
                }else if (data.get(p1).fruit_name=="肉类"){
                    p0.tv_name.setTextColor(Color.parseColor("#ffffff"))
                    p0.tv_name.setBackgroundColor(Color.parseColor("#EE3B3B"))
                }else if (data.get(p1).fruit_name=="水果"){
                    p0.tv_name.setTextColor(Color.parseColor("#ffffff"))
                    p0.tv_name.setBackgroundColor(Color.parseColor("#98FB98"))
                }else if (data.get(p1).fruit_name=="调料"){
                    p0.tv_name.setTextColor(Color.parseColor("#ffffff"))
                    p0.tv_name.setBackgroundColor(Color.parseColor("#8F8F8F"))
                }
            }
        }
    }

    var listener: OnItemClick? =null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {

        if (p1==1) {
            var inflate = LayoutInflater.from(context).inflate(R.layout.item_fruit, p0, false)
            return FruitAdapterHolder(inflate)
        }else if (p1==2){
            var inflate = LayoutInflater.from(context).inflate(R.layout.item_fruit_type, p0, false)
            return FruitTypeAdapterHolder(inflate)
        }else{
            var inflate = LayoutInflater.from(context).inflate(R.layout.item_fruit, p0, false)
            return FruitAdapterHolder(inflate)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        if (data.get(position).fruit_cate!="100"){
            return 1
        }else{
            return 2
        }
    }

    fun setOnItemClickListener(click: OnItemClick){
        listener=click
    }
    inner class FruitAdapterHolder(view:View):RecyclerView.ViewHolder(view){
        var tv_name: TextView = view.findViewById(R.id.tv_name)
    }
    inner class FruitTypeAdapterHolder(view:View):RecyclerView.ViewHolder(view){
        var tv_name: TextView = view.findViewById(R.id.tv_name)
    }
}