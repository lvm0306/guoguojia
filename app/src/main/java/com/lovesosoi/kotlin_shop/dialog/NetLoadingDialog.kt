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
import com.lovesosoi.kotlin_shop.App
import com.lovesosoi.kotlin_shop.interfaces.OnAddCustomer
import com.lovesosoi.kotlin_shop.R
import com.lovesosoi.kotlin_shop.interfaces.IOnAddFruitInOrder

/**
 * 增加商户dialog
 * 2019-7-19 Lovesosoi
 */
class NetLoadingDialog(context: Context, themeResId: Int) : Dialog(context, themeResId) {
    var listener: IOnAddFruitInOrder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setGravity(Gravity.CENTER);//设置dialog显示居中
        //dialogWindow.setWindowAnimations();设置动画效果
        setContentView(R.layout.dialog_loading)
        val activity = scanForActivity(context)
        val windowManager = activity!!.windowManager
        val display = windowManager.getDefaultDisplay()
        val lp: WindowManager.LayoutParams = window.attributes
        lp.width = display.getWidth() * 1 / 5// 设置dialog宽度为屏幕的4/5
        window?.setAttributes(lp);
        setCanceledOnTouchOutside(true)//点击外部Dialog消失
        initView()
    }

    private fun initView() {
    }

    fun setOnAddCustomerListener(l: IOnAddFruitInOrder) {
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