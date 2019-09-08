package com.lovesosoi.kotlin_shop.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.content.ContextWrapper
import android.view.LayoutInflater
import android.widget.*
import com.lovesosoi.kotlin_shop.R
import com.lovesosoi.kotlin_shop.bean.OrderList
import com.lovesosoi.kotlin_shop.interfaces.IOrderDIalog
import com.lovesosoi.kotlin_shop.interfaces.OnAddFruit

/**
 * 增加水果dialog
 * 2019-7-19 Lovesosoi
 */
class OrderShowDialog(context: Context, themeResId: Int) : Dialog(context, themeResId) {
    var listener: IOrderDIalog? = null
    var data: OrderList.DataBean.OrderBean? = null
    var unit = ""
    var tv_name: TextView? = null
    var tv_time: TextView? = null
    var ll_info: LinearLayout? = null
    var tv_goodsitem: TextView? = null
    var tv_allprice: TextView? = null
    var tv_delete: TextView? = null
    var tv_edit: TextView? = null
    var tv_print: TextView? = null
    var tv_close: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setGravity(Gravity.CENTER);//设置dialog显示居中
        //dialogWindow.setWindowAnimations();设置动画效果
        setContentView(R.layout.dialog_show_order)
        val activity = scanForActivity(context)
        val windowManager = activity!!.windowManager
        val display = windowManager.getDefaultDisplay()
        val lp: WindowManager.LayoutParams = window.attributes
        lp.width = display.getWidth() * 4 / 5// 设置dialog宽度为屏幕的4/5
        window?.setAttributes(lp);
        setCanceledOnTouchOutside(true)//点击外部Dialog消失
//        initView()
        init()
    }

    private fun init() {
        tv_name = findViewById<TextView>(R.id.tv_name)
        tv_time = findViewById<TextView>(R.id.tv_time)
        ll_info = findViewById<LinearLayout>(R.id.ll_info)
        tv_goodsitem = findViewById<TextView>(R.id.tv_goodsitem)
        tv_allprice = findViewById<TextView>(R.id.tv_allprice)
        tv_delete = findViewById<TextView>(R.id.tv_delete)
        tv_edit = findViewById<TextView>(R.id.tv_edit)
        tv_print = findViewById<TextView>(R.id.tv_print)
        tv_close = findViewById<TextView>(R.id.tv_close)
    }

    private fun initView() {


        tv_name?.text = data?.customer_name
        tv_time?.text = data?.time
        var order_info = data!!.order_info
        order_info = order_info!!.substring(0, order_info!!.length - 1)
        ll_info?.removeAllViews()

        var view = LayoutInflater.from(context).inflate(R.layout.item_order_item, null)
        var tv1 = view.findViewById<TextView>(R.id.tv1)
        var tv2 = view.findViewById<TextView>(R.id.tv2)
        var tv3 = view.findViewById<TextView>(R.id.tv3)
        var tv4 = view.findViewById<TextView>(R.id.tv4)
        tv1.text = "名字"
        tv2.text = "单价"
        tv3.text = "数量"
        tv4.text = "价格"
        ll_info?.addView(view)
        for (line in order_info!!.split("^")) {
            var view = LayoutInflater.from(context).inflate(R.layout.item_order_item, null)
            var tv1 = view.findViewById<TextView>(R.id.tv1)
            var tv2 = view.findViewById<TextView>(R.id.tv2)
            var tv3 = view.findViewById<TextView>(R.id.tv3)
            var tv4 = view.findViewById<TextView>(R.id.tv4)
            for ((index, value) in line.split("|").withIndex()) {
                if (index == 0) {
                    tv1.text = value
                } else if (index == 1) {
                    tv2.text = value
                } else if (index == 2) {
                    tv3.text = value
                } else if (index == 3) {
                    tv4.text = value
                }

            }
            ll_info?.addView(view)
        }
        tv_goodsitem?.text = "共" + data!!.all_item + "件货物"
        tv_allprice?.text = "总计:" + data!!.all_price + "元"
        if (listener != null) {
            tv_delete?.setOnClickListener { v: View? -> listener?.delete(0, data!!) }
            tv_print?.setOnClickListener { v: View? -> listener?.print(0, data!!) }
            tv_edit?.setOnClickListener { v: View? -> listener?.edit(0, data!!) }
            tv_close?.setOnClickListener { v: View? -> listener?.close(0, data!!) }
        }


    }

    fun setOnAddFruitListener(l: IOrderDIalog) {
        listener = l
    }

    private fun scanForActivity(cont: Context?): Activity? {
        if (cont == null)
            return null
        else if (cont is Activity)
            return cont
        else if (cont is ContextWrapper)
            return scanForActivity(cont.baseContext)
        return null
    }

    fun refreshData(d: OrderList.DataBean.OrderBean) {
        data = d
        initView()
    }

}