package com.lovesosoi.kotlin_shop.interfaces

import android.view.View

/**
 * 订单点击接口
 */
interface OnItemClick{
    fun click(position:Int,view:View,data:Any)
    fun add(position:Int,view:View,data:Any)
    fun sub(position:Int,view:View,data:Any)
    fun subLong(position:Int,view:View,data:Any)
}