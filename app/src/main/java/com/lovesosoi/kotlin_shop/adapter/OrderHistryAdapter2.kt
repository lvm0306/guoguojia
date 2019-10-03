package com.lovesosoi.kotlin_shop.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.lovesosoi.kotlin_shop.R
import com.lovesosoi.kotlin_shop.bean.OrderList
import com.lovesosoi.kotlin_shop.interfaces.IOnOrderListClick
import com.lovesosoi.kotlin_shop.interfaces.OrderHistoryListener
import java.text.SimpleDateFormat

/**
 * 历史订单
 * 2019-7-19 Lovesosoi
 */
class OrderHistryAdapter2(var context: Context, var data: MutableList<OrderList.DataBean.OrderBean>) :
    RecyclerView.Adapter<OrderHistryAdapter2.OrderHistoryAdapterHolder>() {
    lateinit var listener: IOnOrderListClick
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OrderHistoryAdapterHolder {
        var inflate = LayoutInflater.from(context).inflate(R.layout.item_order2, p0, false)
        return OrderHistoryAdapterHolder(inflate)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(p0: OrderHistoryAdapterHolder, p1: Int) {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
        val date = simpleDateFormat.parse(data.get(p1).otime!!.replace("T"," "))
        val time = simpleDateFormat.format(date)
        p0.tv_name.text = data.get(p1).customer_name
        p0.tv_time.text = time
        p0.tv_money.text = "总价:"+data.get(p1).all_price+"元"
        if (listener != null) {
            p0.ll.setOnClickListener { v: View? -> listener?.click(p1, p0.itemView, data.get(p1)) }
        }
    }

    fun setOnItemClickListener(click: IOnOrderListClick) {
        listener = click
    }


    inner class OrderHistoryAdapterHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_name: TextView = view.findViewById(R.id.tv_name)
        var tv_time: TextView = view.findViewById(R.id.tv_time)
        var tv_money: TextView = view.findViewById(R.id.tv_money)
        var ll: LinearLayout = view.findViewById(R.id.ll)
    }
}