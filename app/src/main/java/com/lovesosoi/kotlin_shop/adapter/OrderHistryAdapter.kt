package com.lovesosoi.kotlin_shop.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.lovesosoi.kotlin_shop.R
import com.lovesosoi.kotlin_shop.bean.OrderBean
import com.lovesosoi.kotlin_shop.bean.OrderList
import com.lovesosoi.kotlin_shop.interfaces.OnCountClick
import com.lovesosoi.kotlin_shop.interfaces.OnItemClick
import com.lovesosoi.kotlin_shop.interfaces.OrderHistoryListener

class OrderHistryAdapter(var context: Context, var data: MutableList<OrderList.DataBean.OrderBean>) :
    RecyclerView.Adapter<OrderHistryAdapter.OrderHistoryAdapterHolder>() {
    lateinit var listener: OrderHistoryListener
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OrderHistoryAdapterHolder {
        var inflate = LayoutInflater.from(context).inflate(R.layout.item_order, p0, false)
        return OrderHistoryAdapterHolder(inflate)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(p0: OrderHistoryAdapterHolder, p1: Int) {
//            p0?.tv_name?.text="黄瓜"
//        listener?.click()
        p0.tv_name.text = data.get(p1).customer_name
        p0.tv_time.text = data.get(p1).time
        val order_info = data.get(p1).order_info
//        var s=order_info!!.replace("^","\n")
//        var s1=s!!.replace("|","\t\t\t\t")
        p0.ll_info.removeAllViews()
        for (line in order_info!!.split("^")) {
            var view=LayoutInflater.from(context).inflate(R.layout.item_order_item,null)
            var tv1=view.findViewById<TextView>(R.id.tv1)
            var tv2=view.findViewById<TextView>(R.id.tv2)
            var tv3=view.findViewById<TextView>(R.id.tv3)
            var tv4=view.findViewById<TextView>(R.id.tv4)
            for ((index, value) in line.split("|").withIndex()) {
                if (index==0){
                    tv1.text=value
                }else if (index==1){
                    tv2.text=value
                }else if (index==2){
                    tv3.text=value
                }else if (index==3){
                    tv4.text=value
                }

            }
            p0.ll_info.addView(view)
        }
        p0.tv_goodsitem.text = "共" + data.get(p1).all_item + "件货物"
        p0.tv_allprice.text = "总计:" + data.get(p1).all_price + "元"
        if (listener != null) {
            p0.tv_delete.setOnClickListener { v: View? -> listener?.delete(p1, p0.itemView, data.get(p1)) }
            p0.tv_print.setOnClickListener { v: View? -> listener?.print(p1, p0.itemView, data.get(p1)) }
        }

    }

    fun setOnItemClickListener(click: OrderHistoryListener) {
        listener = click
    }


    inner class OrderHistoryAdapterHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_name: TextView = view.findViewById(R.id.tv_name)
        var tv_time: TextView = view.findViewById(R.id.tv_time)
        var ll_info: LinearLayout = view.findViewById(R.id.ll_info)
        var tv_delete: TextView = view.findViewById(R.id.tv_delete)
        var tv_print: TextView = view.findViewById(R.id.tv_print)
        var tv_goodsitem: TextView = view.findViewById(R.id.tv_goodsitem)
        var tv_allprice: TextView = view.findViewById(R.id.tv_allprice)
    }
}