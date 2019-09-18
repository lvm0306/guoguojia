package com.lovesosoi.kotlin_shop

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import butterknife.ButterKnife
import butterknife.OnClick
import com.lovesosoi.kotlin_shop.adapter.EditOrderAdapter
import com.lovesosoi.kotlin_shop.bean.BaseStatus
import com.lovesosoi.kotlin_shop.bean.EditOrderBean
import com.lovesosoi.kotlin_shop.bean.OrderList
import com.lovesosoi.kotlin_shop.dialog.AddFruitDialog
import com.lovesosoi.kotlin_shop.dialog.AddFruitInEditDialog
import com.lovesosoi.kotlin_shop.dialog.DialogNPV
import com.lovesosoi.kotlin_shop.interfaces.*
import com.lovesosoi.kotlin_shop.utils.Util
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_main.*

class EditActivity : AppCompatActivity() {

    lateinit var context: Context
    var util: Utils? = null
    var date: OrderList.DataBean.OrderBean? = null
    var fruitList = mutableListOf<EditOrderBean>()
    var otime:String?=null
    var mAdapter:EditOrderAdapter ?= null
    var fruitAddDialog: AddFruitInEditDialog? = null
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
        fruitAddDialog = AddFruitInEditDialog(context, R.style.DialogTheme)
        //初始化数据
        tv_name.text = "商户名：" + date?.customer_name
        val d = date?.time!!.split("T")
        otime=d[0]
        tv_date.text = "送货日期" + d[0]
        //初始化列表
        for (line in date?.order_info!!.split("^")) {
            if (line==""){
                continue
            }
            var temp = EditOrderBean("", "", "", "","")
            for ((index, value) in line.split("|").withIndex()) {
                if (index == 0) {
                    temp.fruit_name = value //名字
                } else if (index == 1) {
                    temp.fruit_amount = value //数量
                } else if (index == 2) {
                    temp.fruit_unit = value//单价
                } else if (index == 3) {
                    temp.fruit_danwei = value//单位
                } else if (index == 4) {
                    temp.fruit_total = value//总价
                }
            }
            fruitList.add(temp)
        }
        mAdapter = EditOrderAdapter(context, fruitList)
        mAdapter?.setOnItemClickListener(object :IOnPostionClick{
            override fun click(position: Int) {
                util!!.showToast(fruitList.get(position).fruit_name+"已删除")
                fruitList.removeAt(position)
                mAdapter?.notifyDataSetChanged()
            }

        })
        val rv = findViewById<View>(R.id.rv_edit) as RecyclerView
        rv.adapter = mAdapter
        rv.layoutManager = LinearLayoutManager(this, OrientationHelper.VERTICAL, false)

        fruitAddDialog?.setOnAddFruitListener(object : OnAddFruitInEdit {
            override fun add(name: String, price: Double,count:Double ,unit: String) {
                util!!.e(name+"--"+price+"--"+count+"---"+unit)
                val temp = EditOrderBean("", "", "", "","")
                temp.fruit_name=name
                temp.fruit_unit=price.toString()
                temp.fruit_amount=count.toString()
                temp.fruit_danwei=unit
                temp.fruit_total=String.format("%.2f", count*price)
                fruitList.add(temp)
                mAdapter?.notifyDataSetChanged()
                fruitAddDialog?.dismiss()
            }

        })
    }

    private fun initDate() {
        context = this
        util = Utils(context)
        date = intent.getSerializableExtra("date") as OrderList.DataBean.OrderBean
        Log.e("Lovesosoi", date.toString())
    }

    @OnClick(
        R.id.tv_name,
        R.id.tv_add,
        R.id.tv_date,
        R.id.tv_cancel,
        R.id.tv_sure
    )
    fun onClick(view: View) {
        when (view.id) {
            R.id.tv_cancel -> finish()
            R.id.tv_add -> {
                //新增
                fruitAddDialog!!.show()
            }
            R.id.tv_sure -> {
                //提交订单
                var info = ""
                for (i in fruitList) {
                    info += i.fruit_name + "|" + i.fruit_total + "/" + i.fruit_unit + "|" + i.fruit_amount + i.fruit_unit + "|" + String.format(
                        "%.2f", i.fruit_total.toDouble() * i.fruit_amount.toDouble()
                    ) + "^"
                }
                Log.e("lovesosoi", "order---" + info)
            }
            R.id.tv_date ->{
                //选择日期
                val mDialogNPV = DialogNPV(context, "请选择结束的时间")
                mDialogNPV.show()
                mDialogNPV.setListener(object : IPickListener {
                    @SuppressLint("SetTextI18n")
                    override fun pick(year: Int, month: Int, day: Int) {
                        util!!.showToast("选择下单时间--" + year + "-" + month + "-" + day)
                        var temp = ""
                        if (month < 10) {
                            temp += year.toString() + "-0" + month
                        } else {
                            temp += year.toString() + "-" + month
                        }
                        if (day < 10) {
                            temp += "-0" + day
                        } else {
                            temp += "-" + day
                        }
                        tv_date.text = "送货时间：" + temp
                        otime = temp
                    }
                })
            }
        }
    }
}
