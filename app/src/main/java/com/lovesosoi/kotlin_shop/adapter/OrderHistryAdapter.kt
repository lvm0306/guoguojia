package com.lovesosoi.kotlin_shop.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        p0.tv_info.text = "土豆  1.5/斤  2.2斤\n" +
                "土豆  1.5/斤  2.2斤\n" +
                "土豆  1.5/斤  2.2斤\n" +
                "土豆  1.5/斤  2.2斤"
        p0.tv_goodsitem.text = "共" + data.get(p1).all_item + "件货物"
        p0.tv_allprice.text = "总计:" + data.get(p1).all_price + "元"
        p0.tv_delete.setOnClickListener { v: View? -> listener?.delete(p1, p0.itemView, data.get(p1)) }
        p0.tv_print.setOnClickListener { v: View? -> listener?.print(p1, p0.itemView, data.get(p1)) }

    }

    fun setOnItemClickListener(click: OrderHistoryListener) {
        listener = click
    }


    inner class OrderHistoryAdapterHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_name: TextView = view.findViewById(R.id.tv_name)
        var tv_time: TextView = view.findViewById(R.id.tv_time)
        var tv_info: TextView = view.findViewById(R.id.tv_info)
        var tv_delete: TextView = view.findViewById(R.id.tv_delete)
        var tv_print: TextView = view.findViewById(R.id.tv_print)
        var tv_goodsitem: TextView = view.findViewById(R.id.tv_goodsitem)
        var tv_allprice: TextView = view.findViewById(R.id.tv_allprice)
    }
}