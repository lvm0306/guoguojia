package com.lovesosoi.kotlin_shop

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //    @BindView(R.id.tv_title)
//    lateinit var tv_title: TextView
//    @BindView(R.id.tv_weather)
//    lateinit var tv_weather: TextView
//    @BindView(R.id.tv_menu1)
//    lateinit var tv_menu1: TextView
//    @BindView(R.id.tv_menu2)
//    lateinit var tv_menu2: TextView
//    @BindView(R.id.tv_menu3)
//    lateinit var tv_menu3: TextView
//    @BindView(R.id.tv_menu4)
//    lateinit var tv_menu4: TextView
//    @BindView(R.id.rv_menu)
//    lateinit var rv_menu: RecyclerView
//    @BindView(R.id.rv_item)
//    lateinit var rv_item: RecyclerView
    lateinit var context: Context
    var orderList = mutableListOf<OrderBean>()
    var fruitList = mutableListOf<FruitBean>()
    var customList = mutableListOf<CustomBean>()
    lateinit var order_adapter: OrderAdapter
    lateinit var fruit_adapter: FruitAdapter
    lateinit var fruit_control_adapter: FruitAdapter
    lateinit var custom_control_adapter: CustomControlAdapter
    val strs = arrayOf("请选择商户", "学府三", "学府四", "哈西包子","学府三", "学府四", "哈西包子","学府三", "学府四", "哈西包子","学府三", "学府四", "哈西包子")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        initData()
        initView()
    }

    private fun initData() {
//        orderList.add(OrderBean("黄瓜",1,1.00))
//        orderList.add(OrderBean("茄子",2,1.50))
//        orderList.add(OrderBean("豆角",3,2.00))
//        orderList.add(OrderBean("豆腐",4,2.30))
        fruitList.add(FruitBean("黄瓜", 1.00))
        fruitList.add(FruitBean("茄子", 1.30))
        fruitList.add(FruitBean("豆角", 1.80))
        fruitList.add(FruitBean("豆腐", 2.20))
        fruitList.add(FruitBean("土豆", 1.66))
        fruitList.add(FruitBean("金针菇", 2.50))
        fruitList.add(FruitBean("拉皮", 1.50))
        fruitList.add(FruitBean("肥羊粉", 2.50))
        fruitList.add(FruitBean("鲜蘑", 4.50))
        fruitList.add(FruitBean("鸡蛋", 4.20))
        fruitList.add(FruitBean("西瓜", 1.50))
        customList.add(CustomBean("哈西包子"))
        customList.add(CustomBean("烧烤"))
        customList.add(CustomBean("学府四"))
        customList.add(CustomBean("学府三"))
        customList.add(CustomBean("哈西包子"))
        customList.add(CustomBean("烧烤"))
        customList.add(CustomBean("学府四"))
        customList.add(CustomBean("学府三"))

    }

    private fun initView() {
        context = this
        //rv 初始化
        //spinner 初始化
        var sp = findViewById<View>(R.id.customer_spinner) as Spinner
        val startAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, strs)
        startAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
//        sp.prompt = "选择商户"
        sp.adapter = startAdapter
        sp.setSelection(0)
        var listen = myItemClickListener()
        sp.onItemSelectedListener = listen

        //列表初始化
        order_adapter = OrderAdapter(context, orderList)
        fruit_adapter = FruitAdapter(context, fruitList)
        rv_menu.adapter = order_adapter
        rv_menu.layoutManager = LinearLayoutManager(this, OrientationHelper.VERTICAL, false)
        rv_item.adapter = fruit_adapter
        rv_item.layoutManager = GridLayoutManager(this, 3)

        order_adapter.setOnItemClickListener(object : OnItemClick {
            override fun sub(position: Int, view: View, data: Any) {
                var count = orderList.get(position).count
                if (count > 1) {
                    orderList.get(position).count = String.format("%.2f", count - 1).toDouble()
                } else {
                    orderList.removeAt(position)
                }
                order_adapter.notifyDataSetChanged()
                refreshOrder()
            }

            override fun add(position: Int, view: View, data: Any) {
                var count_t = orderList.get(position).count
                count_t = String.format("%.2f", count_t + 1).toDouble()
                orderList.get(position).count = count_t
                order_adapter.notifyDataSetChanged()
                refreshOrder()
            }

            override fun click(position: Int, view: View, data: Any) {
                if (data is OrderBean) {

                }
            }
        })

        order_adapter.setOnCountClickListener(object : OnCountClick {
            override fun countClick(position: Int, view: View, data: Any) {
                if (data is OrderBean) {
                    val msg = "请输入斤数"
                    var inflater: LayoutInflater =
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    var et: EditText = inflater.inflate(R.layout.item_et, null) as EditText
                    et.setText(orderList.get(position).count.toString())
                    AlertDialog.Builder(context)
                        .setTitle(msg)
                        .setView(et)
                        .setPositiveButton("确定", DialogInterface.OnClickListener { _, _ ->
                            Toast.makeText(context, et.text.toString(), Toast.LENGTH_SHORT).show()
                            orderList.get(position).count = et.text.toString().toDouble()
                            refreshOrder()
                            order_adapter.notifyDataSetChanged()
                        })
                        .setNeutralButton("取消", null)
                        .create()
                        .show()
                }

            }

        })

        fruit_adapter.setOnItemClickListener(object : OnItemClick {
            override fun add(position: Int, view: View, data: Any) {

            }

            override fun sub(position: Int, view: View, data: Any) {

            }

            override fun click(position: Int, view: View, data: Any) {
                if (data is FruitBean) {
                    var flag = false
                    for ((index, value) in orderList.withIndex()) {
                        flag = value.name == data.name
                        if (flag) {
                            orderList.get(index).count += 1
                            break
                        }
                    }
                    if (!flag) {
                        orderList.add(OrderBean(data.name, 1.00, data.price, "斤"))
                    }
                    order_adapter.notifyDataSetChanged()
                    refreshOrder()
                }
            }
        })

    }

    fun refreshOrder() {
        var pay = 0.00
        var count = 0
        for ((index, value) in orderList.withIndex()) {
            pay += value.count * value.price
            count++
        }
        tv_pay.text = "总计:" + String.format("%.2f", pay) + "元"
        tv_goodnum.text = "共计" + count + "件货物"
    }

    @OnClick(R.id.tv_menu1, R.id.tv_menu2, R.id.tv_menu3, R.id.tv_menu4)
    fun onClick(view: View) {
        when (view.id) {
            R.id.tv_menu1 -> {
                closeTab()
                ll_order.visibility = View.VISIBLE
                tv_menu1.setTextColor(Color.parseColor("#aa464447"))
                tv_menu1.setBackgroundColor(Color.parseColor("#aaffffff"))
            }
            R.id.tv_menu2 -> {
                closeTab()
                ll_change_fruit.visibility = View.VISIBLE
                tv_menu2.setTextColor(Color.parseColor("#aa464447"))
                tv_menu2.setBackgroundColor(Color.parseColor("#aaffffff"))
                if (rv_change_fruit.adapter == null) {

                    fruit_control_adapter = FruitAdapter(context, fruitList)
                    rv_change_fruit.adapter = fruit_control_adapter
                    rv_change_fruit.layoutManager = GridLayoutManager(this, 6)
                }


            }
            R.id.tv_menu3 -> {
                closeTab()
                ll_commit_order.visibility = View.VISIBLE
                tv_menu3.setTextColor(Color.parseColor("#aa464447"))
                tv_menu3.setBackgroundColor(Color.parseColor("#aaffffff"))


            }
            R.id.tv_menu4 -> {
                closeTab()
                ll_customer_control.visibility = View.VISIBLE
                tv_menu4.setTextColor(Color.parseColor("#aa464447"))
                tv_menu4.setBackgroundColor(Color.parseColor("#aaffffff"))
                if (rv_customer_control.adapter == null) {

                    custom_control_adapter = CustomControlAdapter(context, customList)
                    rv_customer_control.adapter = custom_control_adapter
                    rv_customer_control.layoutManager = GridLayoutManager(this, 5)
                }
            }
        }
    }

    fun closeTab() {
        ll_change_fruit.visibility = View.GONE
        ll_order.visibility = View.GONE
        ll_commit_order.visibility = View.GONE
        ll_customer_control.visibility = View.GONE
        tv_menu1.setTextColor(Color.parseColor("#aaffffff"))
        tv_menu2.setTextColor(Color.parseColor("#aaffffff"))
        tv_menu3.setTextColor(Color.parseColor("#aaffffff"))
        tv_menu4.setTextColor(Color.parseColor("#aaffffff"))
        tv_menu1.setBackgroundColor(Color.parseColor("#aa464447"))
        tv_menu2.setBackgroundColor(Color.parseColor("#aa464447"))
        tv_menu3.setBackgroundColor(Color.parseColor("#aa464447"))
        tv_menu4.setBackgroundColor(Color.parseColor("#aa464447"))
    }

    internal inner class myItemClickListener : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            Toast.makeText(context, "你的选择是：${strs[position]}", Toast.LENGTH_SHORT).show()
        }
    }
}
