package com.lovesosoi.kotlin_shop

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import butterknife.ButterKnife
import butterknife.OnClick
import com.lovesosoi.kotlin_shop.adapter.EditOrderAdapter
import com.lovesosoi.kotlin_shop.bean.EditOrderBean
import com.lovesosoi.kotlin_shop.bean.OrderList
import com.lovesosoi.kotlin_shop.utils.Util
import kotlinx.android.synthetic.main.activity_main.*

class EditActivity : AppCompatActivity() {

    lateinit var context: Context
    var util: Utils? = null
    var date: OrderList.DataBean.OrderBean? = null
    var fruitList = mutableListOf<EditOrderBean>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        ButterKnife.bind(this)
        initDate()
        initView()
    }

    private fun initView() {
        val tv_name = findViewById<View>(R.id.tv_name) as TextView
        val tv_date = findViewById<View>(R.id.tv_date) as TextView
        tv_name.text = "商户名：" + date?.customer_name
        tv_date.text = date?.time!!.replace("T", " ")

        for (line in date?.order_info!!.split("^")) {
            var temp = EditOrderBean("", "", "", "")
            for ((index, value) in line.split("|").withIndex()) {
                if (index == 0) {
                    temp.fruit_name = value
                } else if (index == 1) {
                    temp.fruit_unit = value
                } else if (index == 2) {
                    temp.fruit_amount = value
                } else if (index == 3) {
                    temp.fruit_total = value
                }
            }
            fruitList.add(temp)
        }
        val mAdapter = EditOrderAdapter(context, fruitList)
        val rv = findViewById<View>(R.id.rv_edit) as RecyclerView
        rv.adapter = mAdapter
        rv.layoutManager = LinearLayoutManager(this, OrientationHelper.VERTICAL, false)

    }

    private fun initDate() {
        context = this
        util = Utils(context)
        date = intent.getSerializableExtra("date") as OrderList.DataBean.OrderBean
    }

    @OnClick(
        R.id.tv_name,
        R.id.tv_date,
        R.id.tv_cancel,
        R.id.tv_sure
    )
    fun onClick(view: View) {
        when (view.id) {
            R.id.tv_cancel -> finish()
            R.id.tv_sure -> util!!.showToast("确认订单")
        }
    }
}
