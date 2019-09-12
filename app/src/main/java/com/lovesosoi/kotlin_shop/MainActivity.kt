package com.lovesosoi.kotlin_shop

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.support.annotation.RequiresApi
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
import cn.carbswang.android.numberpickerview.library.NumberPickerView
import com.lovesosoi.kotlin_shop.adapter.*
import com.lovesosoi.kotlin_shop.api.NetUtils
import com.lovesosoi.kotlin_shop.bean.*
import com.lovesosoi.kotlin_shop.dialog.*
import com.lovesosoi.kotlin_shop.interfaces.*
import com.lovesosoi.kotlin_shop.utils.StringUtils
import kotlinx.android.synthetic.main.activity_main.*
import net.posprinter.posprinterface.IMyBinder
import net.posprinter.posprinterface.ProcessData
import net.posprinter.posprinterface.UiExecute
import net.posprinter.service.PosprinterService
import net.posprinter.utils.DataForSendToPrinterPos80
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
    var orderDateList = mutableListOf<String>()//订单的日期列表
    var orderCustomerList = mutableListOf<String>()//订单的商户选择
    var order_history_list = mutableListOf<OrderList.DataBean.OrderBean>()
    lateinit var order_adapter: OrderAdapter
    lateinit var fruit_adapter: FruitAdapter
    lateinit var fruit_control_adapter: FruitDisplayAdapter
    lateinit var custom_control_adapter: CustomControlAdapter
    lateinit var orderHistryAdapter: OrderHistryAdapter2
    lateinit var startAdapter: ArrayAdapter<String>
    lateinit var orderDateAdapter: ArrayAdapter<String>
    lateinit var orderCustomerAdapter: ArrayAdapter<String>
    lateinit var api: NetUtils
    var customers = mutableListOf<CCustomer.DataBean.CustomerBean>()
    var fruitAddDialog: AddFruitDialog? = null
    var customerAddDialog: AddCustomerDialog? = null
    var orderShowDialog: OrderShowDialog? = null
    var fruitAddOrderDialog: FruitAddOrderDialog? = null
    var customername: String? = null//商户的选择
    var orderCustomername: String? = null//商户的选择
    var orderDate: String? = null // 订单的日期选择
    var customerid: Int = 0
    var goodsitem: Int = 0
    var all_price: String? = null
    var util: Utils? = null
    var mAddress = ""//蓝牙 设备地址
    var mName = ""//蓝牙  设备名字
    var CONNECT = false
    var binder: IMyBinder? = null
    var stime: String = ""
    var etime: String = ""
    lateinit var tvETime: TextView
    lateinit var tvSTime: TextView
    //bindService的参数connection
    var conn: ServiceConnection? = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, b: IBinder) {
            //绑定成功
            Log.e("binder", "connected")

            binder = (b as IMyBinder)
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            Log.e("disbinder", "disconnected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        val intent = Intent(this, PosprinterService::class.java)
        bindService(intent, conn, Context.BIND_AUTO_CREATE)
        initData()
        initView()
    }

    private fun initData() {

        context = this
        activity = this as Activity
        api = NetUtils()
        util = Utils(context)

        val time = SimpleDateFormat("yyyy-MM-dd").format(Date(System.currentTimeMillis()))
        stime = time
        etime = time
        orderCustomername = "全部"
        //获取水果
        api.getFruitList(object : IApiListener {
            override fun success(data: Any) {
                if (data is CFruitBean) {
                    fruitList = data.data?.fruit?.toMutableList()!!
                    fruit_adapter = FruitAdapter(context, fruitList)
                    rv_item.adapter = fruit_adapter
                    rv_item.layoutManager = GridLayoutManager(context, 4) as RecyclerView.LayoutManager?

                    fruit_adapter.setOnItemClickListener(object : OnItemClick {
                        override fun add(position: Int, view: View, data: Any) {

                        }

                        override fun sub(position: Int, view: View, data: Any) {

                        }

                        override fun click(position: Int, view: View, data: Any) {

                            if (data is CFruitBean.DataBean.FruitBean) {

                                fruitAddOrderDialog=FruitAddOrderDialog(context,R.style.DialogTheme)
                                fruitAddOrderDialog?.show()
                                fruitAddOrderDialog?.setDate(data.fruit_name!!,data.fruit_unit!!,data.fruit_price!!)
                                fruitAddOrderDialog?.setOnAddCustomerListener(object :IOnAddFruitInOrder{
                                    override fun add(name: String, unit: String, unit_price: String, count: String) {
                                        var flag = false
                                        for ((index, value) in orderList.withIndex()) {
                                            flag = value.name == data.fruit_name
                                            if (flag) {
                                                orderList.get(index).count += count.toDouble()
                                                break
                                            }
                                        }
                                        if (!flag) {
                                            orderList.add(
                                                OrderBean(data.fruit_name!!, count.toDouble(), data.fruit_price!!.toDouble(), data.fruit_unit!!)
                                            )
                                        }
                                        order_adapter.notifyDataSetChanged()
                                        refreshOrder()
                                        fruitAddOrderDialog?.close()
                                    }

                                })

                            }
                        }
                    })
                }
            }

            override fun error(e: Throwable) {
            }

        })
        // 获取商户
        api.getCustomerList(object : IApiListener {
            override fun success(data: Any) {
                if (data is CCustomer) {
                    customers = data.data!!.customer!!.toMutableList()
                    val sp = findViewById<View>(R.id.customer_spinner) as Spinner
                    for (i in customers) {
                        customerList.add(i.customer_name!!)
                        orderCustomerList.add(i.customer_name!!)
                    }
                    orderCustomerList.add(0, "全部")
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
        tvETime = findViewById<View>(R.id.tv_last_time) as TextView
        tvSTime = findViewById<View>(R.id.tv_now_time) as TextView
        val time = SimpleDateFormat("yyyy-MM-dd").format(Date(System.currentTimeMillis()))
        tvSTime.text = "起始时间：" + time
        tvETime.text = "结束时间：" + time

        orderShowDialog = OrderShowDialog(activity, R.style.DialogTheme)
        //rv 初始化

        //列表初始化
        order_adapter = OrderAdapter(context, orderList)
        rv_menu.adapter = order_adapter
        rv_menu.layoutManager = LinearLayoutManager(this, OrientationHelper.VERTICAL, false)

        orderHistryAdapter = OrderHistryAdapter2(context, order_history_list)
        rv_commit_order.adapter = orderHistryAdapter
        rv_commit_order.layoutManager = LinearLayoutManager(this, OrientationHelper.VERTICAL, false)
        orderHistryAdapter.setOnItemClickListener(object : IOnOrderListClick {
            override fun click(position: Int, view: View, data: Any) {
                val data: OrderList.DataBean.OrderBean = order_history_list.get(position)
                orderShowDialog!!.setOnAddFruitListener(object : IOrderDIalog {
                    override fun close(position: Int, data: Any) {
                        orderShowDialog!!.dismiss()
                    }

                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
                    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
                    override fun print(position: Int, data: Any) {
                        //判断是否打开蓝牙
                        if (!CONNECT) {
                            openBlu(data as OrderList.DataBean.OrderBean)
                        } else {
                            BluWrite(data as OrderList.DataBean.OrderBean)

                        }
                    }

                    override fun delete(position: Int, data: Any) {
                        AlertDialog.Builder(context)
                            .setTitle("确认删除" + order_history_list.get(position).time + " 日" + order_history_list.get(position).customer_name + "的订单么" + "?")
                            .setPositiveButton("确定", DialogInterface.OnClickListener { _, _ ->
                                api.deleteOrder(order_history_list.get(position).order_id, object : IApiListener {
                                    override fun success(data: Any) {
                                        util!!.showToast("删除成功")

                                        order_history_list.removeAt(position)
                                        orderHistryAdapter.notifyDataSetChanged()
                                        orderShowDialog!!.dismiss()

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

                    override fun edit(position: Int, data: Any) {
                        util!!.showToast("编辑")
                        orderShowDialog!!.dismiss()
                        val date =order_history_list.get(position)
                        val i=Intent(context, EditActivity::class.java)
                        i.putExtra("date",date)
                        startActivity(i)
                    }

                })
                orderShowDialog!!.show()
                orderShowDialog!!.refreshData(data)

            }
        })

//        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
//        override fun print(position: Int, view: View, data: Any) {

//        }
//
//        override fun delete(position: Int, view: View, data: Any) {
//
//        }
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
            override fun upDate(name: String, price: Double, unit: String, id: Int) {
                api.upDateFruit(name, price, unit,id ,object : IApiListener {
                    override fun success(data: Any) {
                        if (data is BaseStatus) {
                            util!!.showToast("修改成功")
                            if (data.data!!.flag == 1) {
                                refreshFruit()
                            }
                        }
                    }

                    override fun error(e: Throwable) {

                    }
                })
            }

            override fun add(name: String, price: Double, unit: String) {
                api.addFruit(name, price, unit, object : IApiListener {
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
            override fun update(name: String, id: Int) {

                api.updateCustomer(name, id,object : IApiListener {
                    override fun success(data: Any) {
                        if (data is BaseStatus) {
                            if (data.data!!.flag == 1) {
                                util!!.showToast("修改成功")
                                refreshCustomer()

                            }
                        }
                    }

                    override fun error(e: Throwable) {

                    }
                })
            }

            override fun add(name: String) {
                api.addCustomer(name, object : IApiListener {
                    override fun success(data: Any) {
                        if (data is BaseStatus) {
                            if (data.data!!.flag == 1) {
                                util!!.showToast("增加成功")
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
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private fun openBlu(data: OrderList.DataBean.OrderBean) {
        //判断权限
        isHaveQX()
        //打开蓝牙
        val mBluetoothManager: BluetoothManager = this.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val mBluetoothAdapter: BluetoothAdapter = mBluetoothManager.getAdapter()
        if (!mBluetoothAdapter.isEnabled()) {
            var enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            enableBtIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(enableBtIntent)
        }
        if (!mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.startDiscovery()
        }
        val bonded = mBluetoothAdapter.bondedDevices
        for (i in bonded) {
            if (i.name == "Printer001" || i.address == "DC:0D:30:7C:0A:E8") {
                mName = i.name
                mAddress = i.address
                util!!.e(mName + "-- name" + mAddress + "-- adress")
            }
        }
        if (mName.length == 0) {
            util!!.showToast("请先在系统设置中链接打印设备")
        } else {
            util!!.showToast("已找到设备" + mName + "，开始连接")
            binder!!.connectBtPort(mAddress, object : UiExecute {
                override fun onsucess() {
                    util!!.showToast("打印机连接成功")
                    CONNECT = true
                }

                override fun onfailed() {
                    //连接失败后在UI线程中的执行
                    CONNECT = false
                    util!!.showToast("打印机连接失败")
                }
            })
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery()
            }
            BluWrite(data)
        }

    }

    private fun BluWrite(data: OrderList.DataBean.OrderBean) {
        binder!!.writeDataByYouself(object : UiExecute {
            override fun onsucess() {
                util!!.showToast("已打印")
            }

            override fun onfailed() {

            }
        }, ProcessData {
            /**
             *
            list.add(DataForSendToPrinterPos80.selectAlignment(1));//居中
            list.add(DataForSendToPrinterPos80.selectAlignment(0));//居左
            list.add(DataForSendToPrinterPos80.selectAlignment(2));//居右
            list.add(DataForSendToPrinterPos80.printAndFeedForward(2));//走纸
             */
            Log.e("bttext", "bttext1")
            val line = "-----------------------------------------------" + "\n"
            val list = ArrayList<ByteArray>()
            //创建一段我们想打印的文本,转换为byte[]类型，并添加到要发送的数据的集合list中
            list.add(DataForSendToPrinterPos80.initializePrinter())
            list.add(DataForSendToPrinterPos80.selectInternationalCharacterSets(15))
            list.add(DataForSendToPrinterPos80.selectCharacterSize(0))
            list.add(DataForSendToPrinterPos80.selectAlignment(0))//居左
            list.add(StringUtils.strTobytes("客户:" + data.customer_name + "\n"))
//            list.add(StringUtils.strTobytes("单号:XD-2019-08-11-15-58\n"))
            list.add(StringUtils.strTobytes("日期:" + data.time + "\n"))
            list.add(StringUtils.strTobytes(line))
            list.add(StringUtils.strTobytes("商品\t单a\t\t数量\t金额\n"))

            for (line1 in data.order_info!!.split("^")) {
                for ((index, value) in line1.split("|").withIndex()) {
                    var temp = ""
                    if (index == 0) {
                        temp += value + "  \t"
                    } else if (index == 1) {
                        temp += value + "\t\t"
                    } else if (index == 2) {
                        temp += value + "  \t"
                    } else if (index == 3) {
                        temp += value + "\n"
                    }
                    list.add(StringUtils.strTobytes(temp))
                }
            }
//            list.add(StringUtils.strTobytes("鲜蘑\t\t2.0斤\t3.6\t7.2元\n"))
//            list.add(StringUtils.strTobytes("小白菜\t\t2.22斤\t3.60\t7.21元\n"))
//            list.add(StringUtils.strTobytes("大豆油\t\t12.0斤\t3.6\t17.22元\n"))
//            list.add(StringUtils.strTobytes("干豆腐\t\t2.0斤\t3.6\t37.2元\n"))
//            list.add(StringUtils.strTobytes("干将莫邪\t\t2.0斤\t3.6\t47.2元\n"))
            list.add(StringUtils.strTobytes("\n"))
            list.add(StringUtils.strTobytes(line))
            list.add(StringUtils.strTobytes("合计\t\t" + data.all_item + "样\t\t" + data.all_price + "元\n"))
            list.add(DataForSendToPrinterPos80.printAndFeedForward(2))
            list.add(StringUtils.strTobytes("打印日期:" + data.time + "\n"))
            list.add(DataForSendToPrinterPos80.printAndFeedForward(2))
            list.add(DataForSendToPrinterPos80.selectAlignment(1))//居中
            list.add(DataForSendToPrinterPos80.selectCharacterSize(17))
            list.add(StringUtils.strTobytes("老郭菜店"))
            list.add(DataForSendToPrinterPos80.printAndFeedForward(7))
            list.add(DataForSendToPrinterPos80.selectCutPagerModerAndCutPager(0))

            list
        })
    }

    /**
     * 是否有权限
     */
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

    /**
     * 获取订单列表
     */
    private fun getOrderList() {
        api.getOrderList(object : IApiListener {
            override fun error(e: Throwable) {


            }

            override fun success(data: Any) {
                if (data is OrderList) {
                    order_history_list.clear()
                    order_history_list.addAll(data.data?.order?.toMutableList()!!)
                    orderHistryAdapter.notifyDataSetChanged()
                }
            }
        }, stime + " 00:00:00", etime + " 23:59:59", orderCustomername!!)
//        }, stime, etime, orderCustomername!!)
    }


    /**
     * 刷新订单
     */
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

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    @OnClick(
        R.id.tv_menu1,
        R.id.tv_menu2,
        R.id.tv_menu3,
        R.id.tv_menu4,
        R.id.tv_commit,
        R.id.tv_add_fruit,
        R.id.tv_add_custom,
        R.id.tv_order_serch
    )
    fun onClick(view: View) {
        when (view.id) {
            R.id.tv_menu1 -> {
                closeTab()
                ll_order.visibility = View.VISIBLE
                tv_menu1.setTextColor(Color.parseColor("#ffffff"))
                tv_menu1.setBackgroundColor(Color.parseColor("#F95B4F"))
            }
            R.id.tv_menu2 -> {
                closeTab()
                ll_change_fruit.visibility = View.VISIBLE
                tv_menu2.setTextColor(Color.parseColor("#ffffff"))
                tv_menu2.setBackgroundColor(Color.parseColor("#F95B4F"))
                if (rv_change_fruit.adapter == null) {
                    fruit_control_adapter = FruitDisplayAdapter(context, fruitList)
                    rv_change_fruit.adapter = fruit_control_adapter
                    rv_change_fruit.layoutManager = GridLayoutManager(this, 6)
                    fruit_control_adapter.setOnItemClickListener(object :
                        OnListItemLongClickListener {
                        override fun onClick(position: Int, view: View, data: Any) {
                            //单按 编辑
                            val fruit = fruitList.get(position)
                            fruitAddDialog?.show(fruit.fruit_id)
                            fruitAddDialog?.setDate(fruit.fruit_name!!,fruit.fruit_price!!,fruit.fruit_unit!!,fruit.fruit_id)
                        }

                        override fun onLongClick(position: Int, view: View, data: Any) {
                            //长按 删除
                            AlertDialog.Builder(context)
                                .setTitle("确认删除" + fruitList.get(position).fruit_name + "?")
                                .setPositiveButton("确定", DialogInterface.OnClickListener { _, _ ->
                                    api.deleteFruit(fruitList.get(position).fruit_id, object : IApiListener {
                                        override fun success(data: Any) {
                                            if (data is BaseStatus) {
                                                if (data.data!!.flag == 1) {
                                                    util!!.showToast("删除成功")
                                                    fruitList.removeAt(position)
                                                    fruit_control_adapter.notifyDataSetChanged()
                                                    fruit_adapter.notifyDataSetChanged()

                                                }
                                            }
                                        }

                                        override fun error(e: Throwable) {
                                            Log.e("Lovesosoi", e.toString())
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
            R.id.tv_menu3 -> {
                closeTab()
                ll_commit_order.visibility = View.VISIBLE
                tv_menu3.setTextColor(Color.parseColor("#ffffff"))
                tv_menu3.setBackgroundColor(Color.parseColor("#F95B4F"))

                /**
                 * 订单的日期选择
                 */

                tvSTime.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {

                        val mDialogNPV = DialogNPV(context, "请选择开始的时间")
                        mDialogNPV.show()
                        mDialogNPV.setListener(object : IPickListener {
                            @SuppressLint("SetTextI18n")
                            override fun pick(year: Int, month: Int, day: Int) {
                                util!!.showToast("选择的开始时间--" + year + "-" + month + "-" + day)
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
                                tvSTime.text = "起始时间：" + temp
                                stime = temp
                            }
                        })
                    }
                })
                tvETime.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {

                        val mDialogNPV = DialogNPV(context, "请选择结束的时间")
                        mDialogNPV.show()
                        mDialogNPV.setListener(object : IPickListener {
                            @SuppressLint("SetTextI18n")
                            override fun pick(year: Int, month: Int, day: Int) {
                                util!!.showToast("选择的结束时间--" + year + "-" + month + "-" + day)
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
                                tvETime.text = "结束时间：" + temp
                                etime = temp
                            }
                        })
                    }
                })


                /**
                 * 订单的商户选择
                 */
                val customerSp = findViewById<View>(R.id.sp_order_custom) as Spinner
                orderCustomerAdapter =
                    ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, orderCustomerList)
                orderCustomerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
                customerSp.adapter = orderCustomerAdapter
                val customerListener = mCustomClickListener()
                customerSp.onItemSelectedListener = customerListener

            }
            R.id.tv_menu4 -> {
                closeTab()
                ll_customer_control.visibility = View.VISIBLE
                tv_menu4.setTextColor(Color.parseColor("#ffffff"))
                tv_menu4.setBackgroundColor(Color.parseColor("#F95B4F"))
                if (rv_customer_control.adapter == null) {

                    custom_control_adapter = CustomControlAdapter(context, customers)
                    rv_customer_control.adapter = custom_control_adapter
                    rv_customer_control.layoutManager = GridLayoutManager(this, 5)
                    custom_control_adapter.setOnItemClickListener(object :
                        OnListItemLongClickListener {
                        override fun onClick(position: Int, view: View, data: Any) {
                            //单按
                            val customer = customers.get(position)
                            customerAddDialog?.show(customer.customer_id)
                            customerAddDialog?.setDate(customer.customer_name!!,customer.customer_id)
                        }

                        override fun onLongClick(position: Int, view: View, data: Any) {
                            //长按
                            AlertDialog.Builder(context)
                                .setTitle("确认删除" + customers.get(position).customer_name + "?")
                                .setPositiveButton("确定", DialogInterface.OnClickListener { _, _ ->
                                    api.deleteCustomer(customers.get(position).customer_id, object : IApiListener {
                                        override fun success(data: Any) {
                                            if (data is BaseStatus) {
                                                if (data.data!!.flag == 1) {
                                                    util!!.showToast("删除成功")
                                                    customers.removeAt(position)
                                                    customerList.removeAt(position)
                                                    orderCustomerList.removeAt(position)
                                                    custom_control_adapter.notifyDataSetChanged()
                                                    startAdapter.notifyDataSetChanged()
                                                }
                                            }
                                        }

                                        override fun error(e: Throwable) {
                                            util!!.e(e.toString())
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
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm");// HH:mm:ss
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
                    .setTitle("确认提交" + customername + "的订单么" + "?")
                    .setPositiveButton("确定", DialogInterface.OnClickListener { _, _ ->

                        api.addOrder(
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
                                            orderList.clear()
                                            order_adapter.notifyDataSetChanged()
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
                fruitAddDialog?.show(0)
            }
            R.id.tv_add_custom -> {
                // 增加客户
                customerAddDialog?.show()
            }
            R.id.tv_order_serch -> {
                getOrderList()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binder!!.disconnectCurrentPort(object : UiExecute {
            override fun onsucess() {

            }

            override fun onfailed() {

            }
        })
        unbindService(conn)
    }

    /**
     * 增加商户后，刷新商户
     */
    private fun refreshCustomer() {
        api.getCustomerList(object : IApiListener {
            override fun success(data: Any) {
                if (data is CCustomer) {
                    customers.clear()
                    customers.addAll(data.data!!.customer!!.toMutableList())
                    customerList.clear()
                    orderCustomerList.clear()
                    for (i in customers) {
                        customerList.add(i.customer_name!!)
                        orderCustomerList.add(i.customer_name!!)
                    }
                    orderCustomerList.add(0, "全部")
                    startAdapter.notifyDataSetChanged()
                    custom_control_adapter.notifyDataSetChanged()
                    customerAddDialog?.close()
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

        api.getFruitList(object : IApiListener {
            override fun success(data: Any) {
                if (data is CFruitBean) {
                    fruitList.clear()
                    fruitList.addAll(data.data!!.fruit!!.toMutableList())
                    fruit_control_adapter.notifyDataSetChanged()
                    fruit_adapter.notifyDataSetChanged()
                    fruitAddDialog?.close()
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
        tv_menu1.setTextColor(Color.parseColor("#FC9C95"))
        tv_menu2.setTextColor(Color.parseColor("#FC9C95"))
        tv_menu3.setTextColor(Color.parseColor("#FC9C95"))
        tv_menu4.setTextColor(Color.parseColor("#FC9C95"))
        tv_menu1.setBackgroundColor(Color.parseColor("#ffffff"))
        tv_menu2.setBackgroundColor(Color.parseColor("#ffffff"))
        tv_menu3.setBackgroundColor(Color.parseColor("#ffffff"))
        tv_menu4.setBackgroundColor(Color.parseColor("#ffffff"))
    }

    /**
     * Spinner 点击 内部类
     */
    internal inner class myItemClickListener : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            customername = customers.get(position).customer_name
            customerid = customers.get(position).customer_id
        }
    }

    /**
     * 订单日期 下拉列表
     */
    internal inner class mDateClicListener : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (position == 0) {
                orderDate = "今天"
            } else {
                orderDate = orderDateList.get(position)
            }
        }
    }

    /**
     * 订单商户 下拉列表
     */
    internal inner class mCustomClickListener : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (position == 0) {
                orderCustomername = "全部"
            } else {
                orderCustomername = orderCustomerList.get(position)
            }
        }
    }
}

//        var deviceList_found=ArrayList<String>()
//        var myDevice= DeviceReceiver(deviceList_found)
//        //注册蓝牙广播接收者
//        val filterStart = IntentFilter(BluetoothDevice.ACTION_FOUND)
//        val filterEnd = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
//        registerReceiver(myDevice, filterStart)
//        registerReceiver(myDevice, filterEnd)