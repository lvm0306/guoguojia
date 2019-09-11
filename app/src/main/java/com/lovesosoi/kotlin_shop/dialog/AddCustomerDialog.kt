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
import android.widget.Toast
import com.lovesosoi.kotlin_shop.interfaces.OnAddCustomer
import com.lovesosoi.kotlin_shop.R

/**
 * 增加商户dialog
 * 2019-7-19 Lovesosoi
 */
class AddCustomerDialog(context: Context, themeResId: Int) : Dialog(context, themeResId) {
    var listener: OnAddCustomer? = null
    var etName: EditText? = null
    var mId: Int = 0
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
        etName = findViewById<EditText>(R.id.et_name)
        var tvCancel = findViewById<Button>(R.id.tv_cancel)
        var tvSure = findViewById<Button>(R.id.tv_sure)

        tvSure.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val tName = etName?.text
                if (tName.toString() == "") {
                    Toast.makeText(scanForActivity(context), "请输入商户名", Toast.LENGTH_SHORT).show()
                    return
                }
                if (mId==0) {
                    listener?.add(tName.toString())
                }else{
                    listener?.update(tName.toString(),mId)
                }
            }
        })
        tvCancel.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                close()
            }
        })
    }

    fun setOnAddCustomerListener(l: OnAddCustomer) {
        listener = l
    }

    fun setDate(name: String, id: Int) {
        etName?.setText(name)
        this.mId = id
    }

    fun show(id: Int) {
        if (id == 0) {
            mId = 0
        } else {
            mId = id
        }
        show()
    }

    fun close() {
        mId = 0
        etName?.setText("")
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