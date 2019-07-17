package com.lovesosoi.kotlin_shop

import android.app.Activity
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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import butterknife.ButterKnife
import butterknife.OnClick
import com.colin.doumovie.api.BuildApi
import com.lovesosoi.kotlin_shop.adapter.CustomControlAdapter
import com.lovesosoi.kotlin_shop.adapter.FruitAdapter
import com.lovesosoi.kotlin_shop.adapter.FruitDisplayAdapter
import com.lovesosoi.kotlin_shop.adapter.OrderAdapter
import com.lovesosoi.kotlin_shop.api.ApiResponse
import com.lovesosoi.kotlin_shop.api.BaseObserver
import com.lovesosoi.kotlin_shop.api.NetUtils
import com.lovesosoi.kotlin_shop.bean.*
import com.lovesosoi.kotlin_shop.dialog.AddCustomerDialog
import com.lovesosoi.kotlin_shop.dialog.AddFruitDialog
import com.lovesosoi.kotlin_shop.interfaces.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.*

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
    lateinit var activity: Activity
    var orderList = mutableListOf<OrderBean>()
    var  fruitList=mutableListOf<CFruitBean.DataBean.FruitBean>()
    lateinit var order_adapter: OrderAdapter
    lateinit var fruit_adapter: FruitAdapter
    lateinit var fruit_control_adapter: FruitDisplayAdapter
    lateinit var custom_control_adapter: CustomControlAdapter
    lateinit var startAdapter: ArrayAdapter<CCustomer.DataBean.CustomerBean>
    lateinit var net:NetUtils
    var customers = mutableListOf<CCustomer.DataBean.CustomerBean>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        initData()
        initView()
    }

    private fun initData() {

        context = this
        activity = this as Activity
        net= NetUtils()
//        orderList.add(OrderBean("黄瓜",1,1.00))
//        orderList.add(OrderBean("茄子",2,1.50))
//        orderList.add(OrderBean("豆角",3,2.00))
//        orderList.add(OrderBean("豆腐",4,2.30))\
        val parm = HashMap<String, String>()
        parm.put("city", "深圳")
        parm.put("start", "0")
        parm.put("count", "5")
        parm.put("client", "")
        //获取水果
        net.getFruitList(object :IApiListener{
            override fun success(data: Any) {
                if (data is CFruitBean){
                    fruitList = data.data?.fruit?.toMutableList()!!
                    fruit_adapter = FruitAdapter(context, fruitList)
                    fruit_adapter.notifyDataSetChanged()
                    rv_item.adapter = fruit_adapter
                    rv_item.layoutManager = GridLayoutManager(context, 3) as RecyclerView.LayoutManager?

                    fruit_adapter.setOnItemClickListener(object : OnItemClick {
                        override fun add(position: Int, view: View, data: Any) {

                        }

                        override fun sub(position: Int, view: View, data: Any) {

                        }

                        override fun click(position: Int, view: View, data: Any) {
                            if (data is CFruitBean.DataBean.FruitBean) {
                                var flag = false
                                for ((index, value) in orderList.withIndex()) {
                                    flag = value.name == data.fruit_name
                                    if (flag) {
                                        orderList.get(index).count += 1
                                        break
                                    }
                                }
                                if (!flag) {
                                    orderList.add(OrderBean(data.fruit_name!!, 1.00, data.fruit_price!!.toDouble(), data.fruit_unit!!))
                                }
                                order_adapter.notifyDataSetChanged()
                                refreshOrder()
                            }
                        }
                    })
                }
            }

            override fun error(e: Throwable) {
            }

        })
    // 获取商户
        net.getCustomerList(object :IApiListener{
            override fun success(data: Any) {
                if (data is CCustomer){
                    customers=data.data!!.customer!!.toMutableList()
                    val sp = findViewById<View>(R.id.customer_spinner) as Spinner
                    startAdapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, customers)
                    startAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
                    sp.adapter = startAdapter
                    val listen = myItemClickListener()
                    sp.onItemSelectedListener = listen
                }
            }

            override fun error(e: Throwable) {

            }

        })

    }

    private fun initView() {
        //rv 初始化

        //列表初始化
        order_adapter = OrderAdapter(context, orderList)
        rv_menu.adapter = order_adapter
        rv_menu.layoutManager = LinearLayoutManager(this, OrientationHelper.VERTICAL, false)

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

    @OnClick(
        R.id.tv_menu1,
        R.id.tv_menu2,
        R.id.tv_menu3,
        R.id.tv_menu4,
        R.id.tv_commit,
        R.id.tv_add_fruit,
        R.id.tv_add_custom
    )
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
                    fruit_control_adapter = FruitDisplayAdapter(context, fruitList)
                    rv_change_fruit.adapter = fruit_control_adapter
                    rv_change_fruit.layoutManager = GridLayoutManager(this, 6)
                    fruit_control_adapter.setOnItemClickListener(object :
                        OnListItemLongClickListener {
                        override fun click(position: Int, view: View, data: Any) {

                            AlertDialog.Builder(context)
                                .setTitle("确认删除" + fruitList.get(position).fruit_name + "?")
                                .setPositiveButton("确定", DialogInterface.OnClickListener { _, _ ->
                                    net.deleteFruit(fruitList.get(position).fruit_id,object :IApiListener{
                                        override fun success(data: Any) {
                                            if (data is BaseStatus){
                                                if (data.data!!.flag==1){
                                                    Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        }

                                        override fun error(e: Throwable) {
                                            Log.e("Lovesosoi",e.toString())
                                        }
                                    })
                                    fruitList.removeAt(position)
                                    fruit_control_adapter.notifyDataSetChanged()
                                    fruit_adapter.notifyDataSetChanged()
                                })
                                .setNeutralButton("取消", null)
                                .create()
                                .show()

                        }
                    })
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

                    custom_control_adapter = CustomControlAdapter(context, customers)
                    rv_customer_control.adapter = custom_control_adapter
                    rv_customer_control.layoutManager = GridLayoutManager(this, 5)
                    custom_control_adapter.setOnItemClickListener(object :
                        OnListItemLongClickListener {
                        override fun click(position: Int, view: View, data: Any) {
                            AlertDialog.Builder(context)
                                .setTitle("确认删除" + customers.get(position) + "?")
                                .setPositiveButton("确定", DialogInterface.OnClickListener { _, _ ->
                                    net.deleteCustomer(customers.get(position).customer_id,object :IApiListener{
                                        override fun success(data: Any) {
                                            if (data is BaseStatus){
                                                if (data.data!!.flag==1){
                                                    Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        }

                                        override fun error(e: Throwable) {
                                            Log.e("Lovesosoi",e.toString())
                                        }
                                    })
                                    customers.removeAt(position)
                                    custom_control_adapter.notifyDataSetChanged()
                                })
                                .setNeutralButton("取消", null)
                                .create()
                                .show()
                        }

                    })
                }
            }
            R.id.tv_commit -> {
                //提交订单
            }
            R.id.tv_add_fruit -> {
                // 增加菜品
                var dialog = AddFruitDialog(activity, R.style.DialogTheme)
                dialog.setOnAddFruitListener(object : OnAddFruit {
                    override fun add(name: String, price: Double, unit: String) {
//                        Toast.makeText(context, name + " " + price + " " + unit, Toast.LENGTH_SHORT).show()
                        var fruit = CFruitBean.DataBean.FruitBean()
                        fruit.fruit_name=name
                        fruit.fruit_unit=unit
                        fruit.fruit_price=price.toString()
                        fruitList.add(fruit)
                        fruit_control_adapter.notifyDataSetChanged()
                        fruit_adapter.notifyDataSetChanged()
                        net.addFruit(name,price,unit,object :IApiListener{
                            override fun success(data: Any) {
                                if (data is BaseStatus){
                                    if (data.data!!.flag==1){
                                        Toast.makeText(context,"增加成功",Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }

                            override fun error(e: Throwable) {

                            }
                        })
                        dialog.dismiss()
                    }
                })
                dialog.show()
            }
            R.id.tv_add_custom -> {
                // 增加客户
                var dialog = AddCustomerDialog(activity, R.style.DialogTheme)
                dialog.setOnAddCustomerListener(object : OnAddCustomer {
                    override fun add(name: String) {
//                        Toast.makeText(context, name + " ", Toast.LENGTH_SHORT).show()
                        var c:CCustomer.DataBean.CustomerBean= CCustomer.DataBean.CustomerBean()
                        c.customer_name=name
                        customers.add(c)
                        startAdapter.notifyDataSetChanged()
                        custom_control_adapter.notifyDataSetChanged()
                        Toast.makeText(context,"增加成功",Toast.LENGTH_SHORT).show()

                        net.addCustomer(name,object :IApiListener{
                            override fun success(data: Any) {
                                if (data is BaseStatus){
                                    if (data.data!!.flag==1){
                                        refreshCustomer()

                                    }
                                }
                            }

                            override fun error(e: Throwable) {

                            }
                        })
                        dialog.dismiss()
                    }
                })
                dialog.show()
            }
        }
    }

    private fun refreshCustomer() {
        net.getCustomerList(object :IApiListener{
            override fun success(data: Any) {
                if (data is CCustomer){
                    customers=data.data!!.customer!!.toMutableList()
                    startAdapter.notifyDataSetChanged()
                    custom_control_adapter.notifyDataSetChanged()
                }
            }

            override fun error(e: Throwable) {

            }

        })
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
            if (position != 0) {
                Toast.makeText(context, "你的选择是：${customers[position]}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
