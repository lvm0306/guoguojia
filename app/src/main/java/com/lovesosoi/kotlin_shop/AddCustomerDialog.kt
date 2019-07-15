package com.lovesosoi.kotlin_shop

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.content.ContextWrapper
import android.widget.Toast


class AddCustomerDialog(context: Context, themeResId: Int) : Dialog(context, themeResId) {
    var listener: OnAddCustomer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setGravity(Gravity.CENTER);//设置dialog显示居中
        //dialogWindow.setWindowAnimations();设置动画效果
        setContentView(R.layout.dialog_add_customer)
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
        var tvCancel = findViewById<Button>(R.id.tv_cancel)
        var tvSure = findViewById<Button>(R.id.tv_sure)

        tvSure.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val name = etName.text.toString()
                if (name.length > 0 ) {
                    listener?.add(etName.text.toString())
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

    fun setOnAddCustomerListener(l: OnAddCustomer) {
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

}