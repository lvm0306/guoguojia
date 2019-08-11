package com.lovesosoi.kotlin_shop

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
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
import com.lovesosoi.kotlin_shop.adapter.*
import com.lovesosoi.kotlin_shop.api.ApiResponse
import com.lovesosoi.kotlin_shop.api.BaseObserver
import com.lovesosoi.kotlin_shop.api.NetUtils
import com.lovesosoi.kotlin_shop.bean.*
import com.lovesosoi.kotlin_shop.dialog.AddCustomerDialog
import com.lovesosoi.kotlin_shop.dialog.AddFruitDialog
import com.lovesosoi.kotlin_shop.interfaces.*
import com.lovesosoi.kotlin_shop.utils.DeviceReceiver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.internal.Util
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.text.SimpleDateFormat
import java.util.*

/**
 * 首页
 * 2019-7-19 Lovesosoi
 */
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
    var fruitList = mutableListOf<CFruitBean.DataBean.FruitBean>()
    var customerList = mutableListOf<String>()
    var order_history_list = mutableListOf<OrderList.DataBean.OrderBean>()
    lateinit var order_adapter: OrderAdapter
    lateinit var fruit_adapter: FruitAdapter
    lateinit var fruit_control_adapter: FruitDisplayAdapter
    lateinit var custom_control_adapter: CustomControlAdapter
    lateinit var orderHistryAdapter: OrderHistryAdapter
    lateinit var startAdapter: ArrayAdapter<String>
    lateinit var net: NetUtils
    var customers = mutableListOf<CCustomer.DataBean.CustomerBean>()
    var fruitAddDialog: AddFruitDialog? = null
    var customerAddDialog: AddCustomerDialog? = null
    var customername: String? = null
    var customerid: Int = 0
    var goodsitem: Int = 0
    var all_price: String? = null
    var util:Utils? =null
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
        net = NetUtils()
        util= Utils(context)
        //获取水果
        net.getFruitList(object : IApiListener {
            override fun success(data: Any) {
                if (data is CFruitBean) {
                    fruitList = data.data?.fruit?.toMutableList()!!
                    fruit_adapter = FruitAdapter(context, fruitList)
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
                                    orderList.add(
                                        OrderBean(
                                            data.fruit_name!!,
                                            1.00,
                                            data.fruit_price!!.toDouble(),
                                            data.fruit_unit!!
                                        )
                                    )
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
        net.getCustomerList(object : IApiListener {
            override fun success(data: Any) {
                if (data is CCustomer) {
                    customers = data.data!!.customer!!.toMutableList()
                    val sp = findViewById<View>(R.id.customer_spinner) as Spinner
                    for (i in customers) {
                        customerList.add(i.customer_name!!)
                    }
                    startAdapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, customerList)
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

        orderHistryAdapter = OrderHistryAdapter(context, order_history_list)
        rv_commit_order.adapter = orderHistryAdapter
        rv_commit_order.layoutManager = LinearLayoutManager(this, OrientationHelper.VERTICAL, false)
        orderHistryAdapter.setOnItemClickListener(object : OrderHistoryListener {
            override fun print(position: Int, view: View, data: Any) {
               //判断是否打开蓝牙
                openBlu()
            }

            override fun delete(position: Int, view: View, data: Any) {
                AlertDialog.Builder(context)
                    .setTitle("确认删除" + order_history_list.get(position).time + " 日" + order_history_list.get(position).customer_name + "的订单么" + "?")
                    .setPositiveButton("确定", DialogInterface.OnClickListener { _, _ ->
                        net.deleteOrder(order_history_list.get(position).order_id, object : IApiListener {
                            override fun success(data: Any) {
                                util!!.showToast("删除成功")
                                order_history_list.removeAt(position)
                                orderHistryAdapter.notifyDataSetChanged()
                            }

                            override fun error(e: Throwable) {
                                Log.e("lovesosoi", e.toString())
                            }

                        })
                    })
                    .setNeutralButton("取消", null)
                    .create()
                    .show()

            }
        })


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
                            util!!.showToast(et.text.toString())
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


        fruitAddDialog = AddFruitDialog(activity, R.style.DialogTheme)
        customerAddDialog = AddCustomerDialog(activity, R.style.DialogTheme)

        fruitAddDialog?.setOnAddFruitListener(object : OnAddFruit {
            override fun add(name: String, price: Double, unit: String) {
//                        Toast.makeText(context, name + " " + price + " " + unit, Toast.LENGTH_SHORT).show()
                var fruit = CFruitBean.DataBean.FruitBean()
                fruit.fruit_name = name
                fruit.fruit_unit = unit
                fruit.fruit_price = price.toString()
                fruitList.add(fruit)
                fruit_control_adapter.notifyDataSetChanged()
                fruit_adapter.notifyDataSetChanged()
                net.addFruit(name, price, unit, object : IApiListener {
                    override fun success(data: Any) {
                        if (data is BaseStatus) {
                            if (data.data!!.flag == 1) {
                                refreshFruit()
                            }
                        }
                    }

                    override fun error(e: Throwable) {

                    }
                })
            }
        })

        customerAddDialog = AddCustomerDialog(activity, R.style.DialogTheme)
        customerAddDialog?.setOnAddCustomerListener(object : OnAddCustomer {
            override fun add(name: String) {
                var c: CCustomer.DataBean.CustomerBean = CCustomer.DataBean.CustomerBean()
                c.customer_name = name
                customers.add(c)
                customerList.add(name)
                util!!.showToast("增加成功")


                net.addCustomer(name, object : IApiListener {
                    override fun success(data: Any) {
                        if (data is BaseStatus) {
                            if (data.data!!.flag == 1) {
                                refreshCustomer()

                            }
                        }
                    }

                    override fun error(e: Throwable) {

                    }
                })
            }
        })
        getOrderList()
    }

    /**
     * 打开蓝牙
     */
    private fun openBlu() {
        //判断权限
        isHaveQX()
        //打开蓝牙
        var mBluetoothManager: BluetoothManager = this.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager;
        var mBluetoothAdapter: BluetoothAdapter = mBluetoothManager.getAdapter()
        if (!mBluetoothAdapter.isEnabled()) {
            var enableBtIntent: Intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            enableBtIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(enableBtIntent)
        }

        mBluetoothAdapter.startDiscovery()
//        var myDevice: DeviceReceiver = DeviceReceiver( deviceList_found, adapter2, lv2)

//        //注册蓝牙广播接收者
//        val filterStart = IntentFilter(BluetoothDevice.ACTION_FOUND)
//        val filterEnd = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
//        registerReceiver(myDevice, filterStart)
//        registerReceiver(myDevice, filterEnd)
    }

    private fun isHaveQX() {
        val permissions = arrayOf(
            Manifest.permission.BLUETOOTH_PRIVILEGED,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH
        )

        val mPermissionList = ArrayList<String>()

        //逐个判断你要的权限是否已经通过
        for (i in permissions.indices) {
            if (ContextCompat.checkSelfPermission(context, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i])//添加还未授予的权限
            }
        }
        //申请权限
        if (mPermissionList.size > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(activity, permissions, 1)
        } else {
            //说明权限都已经通过，可以做你想做的事情去
            Log.e(ContentValues.TAG, "请随意发挥")
        }
    }

    private fun getOrderList() {
        net.getOrderList(object : IApiListener {
            override fun error(e: Throwable) {


            }

            override fun success(data: Any) {
                if (data is OrderList) {
                    order_history_list.clear()
                    order_history_list.addAll(data.data?.order?.toMutableList()!!)
                    orderHistryAdapter.notifyDataSetChanged()
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
        goodsitem = count
        all_price = String.format("%.2f", pay)
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
                                    net.deleteFruit(fruitList.get(position).fruit_id, object : IApiListener {
                                        override fun success(data: Any) {
                                            if (data is BaseStatus) {
                                                if (data.data!!.flag == 1) {
                                                    util!!.showToast("删除成功")

                                                }
                                            }
                                        }

                                        override fun error(e: Throwable) {
                                            Log.e("Lovesosoi", e.toString())
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
                                .setTitle("确认删除" + customers.get(position).customer_name + "?")
                                .setPositiveButton("确定", DialogInterface.OnClickListener { _, _ ->
                                    net.deleteCustomer(customers.get(position).customer_id, object : IApiListener {
                                        override fun success(data: Any) {
                                            if (data is BaseStatus) {
                                                if (data.data!!.flag == 1) {
                                                    util!!.showToast("删除成功")
                                                    customers.removeAt(position)
                                                    customerList.removeAt(position)
                                                    custom_control_adapter.notifyDataSetChanged()
                                                    startAdapter.notifyDataSetChanged()
                                                }
                                            }
                                        }

                                        override fun error(e: Throwable) {
                                            util!!.e( e.toString())
                                        }
                                    })
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
                if (customername == null) {
                    util!!.showToast("请选择商户")
                    return
                }
                if (goodsitem == 0) {
                    util!!.showToast("请增加商品")
                    return
                }
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
                val date = Date(System.currentTimeMillis())
                val time = simpleDateFormat.format(date)
                var info = ""
                for (i in orderList) {
                    info += i.name + "|" + i.price + "/" + i.unit + "|" + i.count + i.unit + "|" + String.format(
                        "%.2f",
                        i.price * i.count
                    ) + "元 " + "^"
                }
                Log.e(
                    "Lovesosoi", "customerName=" + customername
                            + " customerid= " + customerid
                            + " time= " + time
                            + " all_price= " + all_price
                            + " all_item= " + goodsitem
                            + " order_info= " + info
                )


                AlertDialog.Builder(context)
                    .setTitle("确认提交"+customername+"的订单么" + "?")
                    .setPositiveButton("确定", DialogInterface.OnClickListener { _, _ ->

                        net.addOrder(
                            customername!!,
                            customerid.toString(),
                            time,
                            all_price.toString(),
                            goodsitem.toString(),
                            info,
                            object : IApiListener {
                                override fun success(data: Any) {
                                    if (data is BaseStatus) {
                                        if (data.data!!.flag == 1) {
                                            util!!.showToast("提交成功")

                                            getOrderList()
                                        }
                                    }
                                }

                                override fun error(e: Throwable) {

                                }
                            })
                    })
                    .setNeutralButton("取消", null)
                    .create()
                    .show()


            }
            R.id.tv_add_fruit -> {
                // 增加菜品
                fruitAddDialog?.show()
            }
            R.id.tv_add_custom -> {
                // 增加客户
                customerAddDialog?.show()
            }
        }
    }

    /**
     * 增加商户后，刷新商户
     */
    private fun refreshCustomer() {
        net.getCustomerList(object : IApiListener {
            override fun success(data: Any) {
                if (data is CCustomer) {
                    customers.clear()
                    customers.addAll(data.data!!.customer!!.toMutableList())
                    customerList.clear()
                    for (i in customers) {
                        customerList.add(i.customer_name!!)
                    }
                    startAdapter.notifyDataSetChanged()
                    custom_control_adapter.notifyDataSetChanged()
                    customerAddDialog?.dismiss()
                }
            }

            override fun error(e: Throwable) {

            }

        })
    }


    /**
     *
     * 增加水果后，刷新水果
     */
    private fun refreshFruit() {

        net.getFruitList(object : IApiListener {
            override fun success(data: Any) {
                util!!.showToast("增加成功")

                if (data is CFruitBean) {
                    fruitList.clear()
                    fruitList.addAll(data.data!!.fruit!!.toMutableList())
                    fruit_control_adapter.notifyDataSetChanged()
                    fruit_adapter.notifyDataSetChanged()
                    fruitAddDialog?.dismiss()
                }
            }

            override fun error(e: Throwable) {

            }

        })

    }

    /**
     * 切换标签
     */
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

    /**
     * Spinner 点击 内部类
     */
    internal inner class myItemClickListener : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//            if (position != 0) {
//                Toast.makeText(context, "你的选择是：${customers[position].customer_name}", Toast.LENGTH_SHORT).show()
            customername = customers.get(position).customer_name
            customerid = customers.get(position).customer_id
//            }
        }
    }
}
