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
import com.lovesosoi.kotlin_shop.api.NetUtils
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
import java.text.SimpleDateFormat
import java.util.*

class EditActivity : AppCompatActivity() {

    lateinit var context: Context
    var util: Utils? = null
    var date: OrderList.DataBean.OrderBean? = null
    var fruitList = mutableListOf<EditOrderBean>()
    var otime: String? = null
    var mAdapter: EditOrderAdapter? = null
    var fruitAddDialog: AddFruitInEditDialog? = null
    lateinit var api: NetUtils
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
        val tv_count_price = findViewById<View>(R.id.tv_count_price) as TextView
        fruitAddDialog = AddFruitInEditDialog(context, R.style.DialogTheme)
        //初始化数据
        tv_name.text = "商户名：" + date?.customer_name
        val d = date?.time!!.split("T")
        otime = d[0]
        tv_date.text = "送货日期: " + d[0]
        //初始化列表
        var t_count=0.0
        var t_price=0.0
        for (line in date?.order_info!!.split("^")) {
            if (line == "") {
                continue
            }
            var temp = EditOrderBean("", "", "", "", "")
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
            t_count+=1
            t_price+=temp.fruit_total.toDouble()

            fruitList.add(temp)
        }
        mAdapter = EditOrderAdapter(context, fruitList)
        mAdapter?.setOnItemClickListener(object : IOnPostionClick {
            override fun click(position: Int) {
                util!!.showToast(fruitList.get(position).fruit_name + "已删除")
                fruitList.removeAt(position)
                mAdapter?.notifyDataSetChanged()
                reCalc()
            }

        })
        val rv = findViewById<View>(R.id.rv_edit) as RecyclerView
        rv.adapter = mAdapter
        rv.layoutManager = LinearLayoutManager(this, OrientationHelper.VERTICAL, false)

        fruitAddDialog?.setOnAddFruitListener(object : OnAddFruitInEdit {
            override fun add(name: String, price: Double, count: Double, unit: String) {
                util!!.e(name + "--" + price + "--" + count + "---" + unit)
                val temp = EditOrderBean("", "", "", "", "")
                temp.fruit_name = name
                temp.fruit_unit = price.toString()
                temp.fruit_amount = count.toString()
                temp.fruit_danwei = unit
                temp.fruit_total = String.format("%.2f", count * price)
                fruitList.add(temp)
                mAdapter?.notifyDataSetChanged()
                fruitAddDialog?.dismiss()
            }

        })
    }

    /**
     * 重新计算价格
     */
    private fun reCalc() {


    }

    /**
     * 初始化数据
     */
    private fun initDate() {
        context = this
        api = NetUtils(context,false)
        util = Utils(context)
        date = intent.getSerializableExtra("date") as OrderList.DataBean.OrderBean
        Log.e("Lovesosoi", date.toString())
    }

    /**
     * 点击事件
     */
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
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm");// HH:mm:ss
                val _date = Date(System.currentTimeMillis())
                val time = simpleDateFormat.format(_date)
                var info = ""
                var all_conut = 0.0
                var all_price = 0.0
                for (i in fruitList) {
                    Log.e("lovesosoi",i.toString());
                    all_conut +=1
                    all_price += i.fruit_unit.toDouble() * i.fruit_amount.toDouble()
                    info += i.fruit_name + "|" + i.fruit_amount + "|" + String.format("%.2f", i.fruit_unit.toDouble()
                    ) + "|" + i.fruit_danwei + "|" + String.format("%.2f", i.fruit_unit.toDouble() * i.fruit_amount.toDouble()
                    ) + "^"
                }
                Log.e("lovesosoi", "order---" + info)

                api.upDateOrder(
                    customer_name = date?.customer_name.toString(),
                    cusomerid = date?.customer_id.toString(),
                    time = time,
                    order_id = date?.order_id.toString(),
                    otime = otime.toString(),
                    all_price = all_price.toString(),
                    all_item = all_conut.toString(),
                    order_info = info,
                    listener = object : IApiListener {
                        override fun error(e: Throwable) {

                        }

                        override fun success(data: Any) {

                            if (data is BaseStatus) {
                                if (data.data!!.flag == 1) {
                                    util!!.showToast("提交成功")
                                    setResult(1)
                                    finish()

                                }
                            }
                        }

                    }
                )

            }
            R.id.tv_date -> {
                //选择日期
                val mDialogNPV = DialogNPV(context, "请选择结束的时间")
                mDialogNPV.show()
                mDialogNPV.setListener(object : IPickListener {
                    @SuppressLint("SetTextI18n")
                    override fun pick(year: Int, month: Int, day: Int) {
//                        util!!.showToast("选择下单时间--" + year + "-" + month + "-" + day)
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
                        tv_date.text = "送货日期：" + temp
                        otime = temp
                    }
                })
            }
        }
    }
}
