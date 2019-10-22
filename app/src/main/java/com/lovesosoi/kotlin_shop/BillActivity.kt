package com.lovesosoi.kotlin_shop

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.ButterKnife
import butterknife.OnClick
import com.lovesosoi.kotlin_shop.adapter.BillAdapter
import com.lovesosoi.kotlin_shop.bean.OrderList
import com.lovesosoi.kotlin_shop.interfaces.IOnOrderListClick
import kotlinx.android.synthetic.main.activity_bill.*
import kotlin.math.log

class BillActivity : AppCompatActivity() {
    lateinit var context: Context
    var util: Utils? = null
    var order_history_list: ArrayList<OrderList.DataBean.OrderBean>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill)
        ButterKnife.bind(this)
        init()
        initDate()
    }

    private fun init() {
        context = this
        util = Utils(context)
    }

    private fun initDate() {
        var rv = findViewById<RecyclerView>(R.id.rv_bill)
        order_history_list =
                intent.getBundleExtra("bundle").getSerializable("date") as ArrayList<OrderList.DataBean.OrderBean>
        val adapter: BillAdapter = BillAdapter(context, order_history_list!!)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context, OrientationHelper.VERTICAL, false)
        adapter.setOnItemClickListener(object : IOnOrderListClick {
            override fun click(position: Int, view: View, data: Any) {

            }

        })

        tv_name.text = "商户名：" + order_history_list!![0].customer_name
        var totle = 0.0
        for (i in order_history_list!!) {
            totle += i.all_price!!.toDouble()
        }
        tv_count_price.text = "总价：" + String.format("%.2f", totle)
        tv_date.text = "起始日期：" + order_history_list!![0].otime?.split("T")?.get(0) + " --- 结束日期：" +
                order_history_list!![order_history_list!!.size - 1].otime?.split("T")?.get(0)

    }

    /**
     * 点击事件
     */
    @OnClick(
        R.id.tv_sure,
        R.id.tv_name
    )
    fun onClick(view: View) {
        when (view.id) {
            R.id.tv_sure -> {
                util!!.e("点击了返回")
                finish()
            }
        }
    }
}
