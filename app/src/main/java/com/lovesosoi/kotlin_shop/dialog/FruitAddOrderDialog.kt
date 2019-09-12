package com.lovesosoi.kotlin_shop.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.content.ContextWrapper
import android.widget.TextView
import android.widget.Toast
import com.lovesosoi.kotlin_shop.interfaces.OnAddCustomer
import com.lovesosoi.kotlin_shop.R
import com.lovesosoi.kotlin_shop.interfaces.IOnAddFruitInOrder

/**
 * 增加商户dialog
 * 2019-7-19 Lovesosoi
 */
class FruitAddOrderDialog(context: Context, themeResId: Int) : Dialog(context, themeResId) {
    var listener: IOnAddFruitInOrder? = null
    var etUnit: EditText? = null
    var etUnitPrice: EditText? = null
    var etCount: EditText? = null
    var tvName: TextView? = null
    var mId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setGravity(Gravity.CENTER);//设置dialog显示居中
        //dialogWindow.setWindowAnimations();设置动画效果
        setContentView(R.layout.dialog_add_fruit_in_order)
        val activity = scanForActivity(context)
        val windowManager = activity!!.windowManager
        val display = windowManager.getDefaultDisplay()
        val lp: WindowManager.LayoutParams = window.attributes
        lp.width = display.getWidth() * 4 / 5// 设置dialog宽度为屏幕的4/5
        window?.setAttributes(lp);
        setCanceledOnTouchOutside(true)//点击外部Dialog消失
        initView()
    }

    private fun initView() {
        tvName = findViewById<TextView>(R.id.tv_name)
        etUnit = findViewById<EditText>(R.id.et_unit)
        etUnitPrice = findViewById<EditText>(R.id.et_unit_price)
        etCount = findViewById<EditText>(R.id.et_count)
        var tvCancel = findViewById<Button>(R.id.tv_cancel)
        var tvSure = findViewById<Button>(R.id.tv_sure)

        tvSure.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val tUnit = etUnit?.text
                val tUnitPrice = etUnitPrice?.text
                val tCount = etCount?.text
                var unitPrice=""
                if (tUnit.toString() == "") {
                    Toast.makeText(scanForActivity(context), "请输入单位", Toast.LENGTH_SHORT).show()
                    return
                }
                if (tCount.toString() == "") {
                    Toast.makeText(scanForActivity(context), "请输入数量", Toast.LENGTH_SHORT).show()
                    return
                }
                if (tUnitPrice.toString() == "") {
                    unitPrice="0.00"
                }else{
                    unitPrice=tUnitPrice.toString()
                }
                if (listener!=null){
                    listener?.add(tvName?.text.toString(),tUnit.toString(),unitPrice,tCount.toString())
                }
            }
        })
        tvCancel.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                close()
            }
        })
    }

    fun setOnAddCustomerListener(l: IOnAddFruitInOrder) {
        listener = l
    }

    fun setDate(name: String, unit:String,unitPrice:String) {
        etUnit?.setText(unit)
        etUnitPrice?.setText(unitPrice)
        tvName?.setText(name)
    }

    fun close() {
        mId = 0
        etUnit?.setText("")
        etUnitPrice?.setText("")
        tvName?.setText("")
        dismiss()
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

}