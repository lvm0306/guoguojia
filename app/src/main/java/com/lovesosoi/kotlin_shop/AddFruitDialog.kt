package com.lovesosoi.kotlin_shop

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager

class AddFruitDialog(context: Context, themeResId: Int) : Dialog(context, themeResId) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setGravity(Gravity.CENTER);//设置dialog显示居中
        //dialogWindow.setWindowAnimations();设置动画效果
        setContentView(R.layout.abc_action_menu_item_layout)
        var activity=context as Activity
        var windowManager=activity.windowManager
        var  display = windowManager.getDefaultDisplay()
        var lp:WindowManager.LayoutParams= window.attributes
        lp.width = display.getWidth()*4/5;// 设置dialog宽度为屏幕的4/5
        window.setAttributes(lp);
        setCanceledOnTouchOutside(true);//点击外部Dialog消失
    }
}