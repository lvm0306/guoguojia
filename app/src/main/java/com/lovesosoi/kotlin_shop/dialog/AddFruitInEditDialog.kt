package com.lovesosoi.kotlin_shop.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.content.ContextWrapper
import android.widget.*
import com.lovesosoi.kotlin_shop.R
import com.lovesosoi.kotlin_shop.interfaces.OnAddFruit
import com.lovesosoi.kotlin_shop.interfaces.OnAddFruitInEdit

/**
 * 增加水果dialog
 * 2019-7-19 Lovesosoi
 */
class AddFruitInEditDialog(context: Context, themeResId: Int) : Dialog(context, themeResId) {
    var listener: OnAddFruitInEdit? = null
    var stringArray: List<String>? = null
    var etName: EditText? = null
    var etPrice: EditText? = null
    var et_unit: EditText? = null
    var etCount: EditText? = null
    var tvCancel: Button? = null
    var tvSure: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setGravity(Gravity.CENTER);//设置dialog显示居中
        //dialogWindow.setWindowAnimations();设置动画效果
        setContentView(R.layout.dialog_add_fruit_in_edit)
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
        etName = findViewById<EditText>(R.id.et_name)
        etPrice = findViewById<EditText>(R.id.et_price)
        etCount = findViewById<EditText>(R.id.et_count)
        et_unit = findViewById<EditText>(R.id.et_unit)
        tvCancel = findViewById<Button>(R.id.tv_cancel)
        tvSure = findViewById<Button>(R.id.tv_sure)

        stringArray = (context.resources.getStringArray(R.array.unit)).toList()
        val startAdapter = ArrayAdapter(context, R.layout.spinner_fruit, stringArray)
        startAdapter.setDropDownViewResource(R.layout.spinner_fruit)


        tvSure?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val tName = etName?.text
                val tPrice = etPrice?.text
                val tUnit = et_unit?.text
                val tCount = etCount?.text
                var price = 0.0
                var name = ""
                var unit = ""
                var count = 0.0
                if (tName.toString() != "") {
                    name = tName.toString()
                } else {
                    Toast.makeText(scanForActivity(context), "请输入名字", Toast.LENGTH_SHORT).show()
                    return
                }
                if (tPrice.toString() != "") {
                    price = tPrice.toString().toDouble()
                }else{
                    Toast.makeText(scanForActivity(context), "请输入价格", Toast.LENGTH_SHORT).show()
                    return
                }

                if (tCount.toString() != "") {
                    count = tCount.toString().toDouble()
                }else{
                    Toast.makeText(scanForActivity(context), "请输入数量", Toast.LENGTH_SHORT).show()
                    return
                }

                if (tUnit.toString() == "") {
                    Toast.makeText(scanForActivity(context), "请输入计量单位", Toast.LENGTH_SHORT).show()
                    return
                } else {
                    unit = tUnit.toString()
                }
                Log.e("Lovesosoi", name + " " + price + " " + unit)
                listener?.add(name, price, count,unit)

            }
        })
        tvCancel?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                close()
            }
        })
    }

    fun setOnAddFruitListener(l: OnAddFruitInEdit) {
        listener = l
    }

    fun setDate(name: String, price: String, unit: String, id: Int) {
        etName?.setText(name)
        etPrice?.setText(price)
        et_unit?.setText(unit)
    }

    fun close() {
        etName?.setText("")
        etPrice?.setText("")
        et_unit?.setText("")
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