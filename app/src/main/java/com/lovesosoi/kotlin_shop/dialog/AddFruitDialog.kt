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

/**
 * 增加水果dialog
 * 2019-7-19 Lovesosoi
 */
class AddFruitDialog(context: Context, themeResId: Int) : Dialog(context, themeResId) {
    var listener: OnAddFruit? = null
    var stringArray:List<String>?=null
    var unit=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setGravity(Gravity.CENTER);//设置dialog显示居中
        //dialogWindow.setWindowAnimations();设置动画效果
        setContentView(R.layout.dialog_add_fruit)
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
        var etName = findViewById<EditText>(R.id.et_name)
        var etPrice = findViewById<EditText>(R.id.et_price)
        var sp = findViewById<Spinner>(R.id.et_weight)
        var tvCancel = findViewById<Button>(R.id.tv_cancel)
        var tvSure = findViewById<Button>(R.id.tv_sure)

        stringArray =(context.resources.getStringArray(R.array.unit)).toList()
        val startAdapter = ArrayAdapter(context, R.layout.spinner_fruit, stringArray)
        startAdapter.setDropDownViewResource(R.layout.spinner_fruit)
        sp.adapter = startAdapter
        sp.onItemSelectedListener = myItemClickListener()


        tvSure.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val name = etName.text.toString()
                val price = etPrice.text.toString().toDouble()
                Log.e("Lovesosoi",name+" "+price.toString()+" "+unit)
                if (name.length > 0 && price > 0 && unit!="选择计量单位") {
                    listener?.add(etName.text.toString(), etPrice.text.toString().toDouble(),unit)
                } else {
                    Toast.makeText(scanForActivity(context),"请输入具体内容",Toast.LENGTH_SHORT).show()
                }
            }
        })
        tvCancel.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                dismiss()
            }
        })
    }

    fun setOnAddFruitListener(l: OnAddFruit) {
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

    internal inner class myItemClickListener : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            unit = stringArray!![position]
            if (position!=0) {
                Toast.makeText(context, "你的选择是：${stringArray!![position]}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}