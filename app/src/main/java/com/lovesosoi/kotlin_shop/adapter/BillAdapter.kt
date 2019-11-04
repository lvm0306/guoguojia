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
import java.text.SimpleDateFormat


class BillAdapter(var context: Context, var data: ArrayList<OrderList.DataBean.OrderBean>) :
    RecyclerView.Adapter<BillAdapter.BillAdapterHolder>() {
    lateinit var listener: IOnOrderListClick
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BillAdapterHolder {
        var inflate = LayoutInflater.from(context).inflate(R.layout.item_bill, p0, false)
        return BillAdapterHolder(inflate)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(p0: BillAdapterHolder, p1: Int) {
//        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
//        val date = simpleDateFormat.parse(data.get(p1).otime!!.replace("T"," "))
//        val time = simpleDateFormat.format(date)
        p0.tv_time.text = data.get(p1).otime?.split("T")?.get(0)
        p0.tv_money.text = data.get(p1).all_price+"元"
        p0.tv_name.text = data.get(p1).customer_name
        if (listener != null) {
            p0.ll.setOnClickListener { v: View? -> listener?.click(p1, p0.itemView, data.get(p1)) }
        }
    }

    fun setOnItemClickListener(click: IOnOrderListClick) {
        listener = click
    }


    inner class BillAdapterHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_name: TextView = view.findViewById(R.id.tv_name)
        var tv_time: TextView = view.findViewById(R.id.tv_time)
        var tv_money: TextView = view.findViewById(R.id.tv_money)
        var ll: LinearLayout = view.findViewById(R.id.ll)
    }
}