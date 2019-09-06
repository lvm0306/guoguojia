package com.lovesosoi.kotlin_shop.interfaces

import android.view.View

/**
 * 订单点击接口
 * 2019-7-19 Lovesosoi
 */
interface IOnOrderListClick{
    fun click(position:Int, view: View, data:Any)
}