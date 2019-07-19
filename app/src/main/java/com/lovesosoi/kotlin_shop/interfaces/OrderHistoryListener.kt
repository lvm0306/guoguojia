package com.lovesosoi.kotlin_shop.interfaces

import android.view.View

/**
 * 历史记录点击
 * 2019-7-19 Lovesosoi
 */
interface OrderHistoryListener{

    fun print(position:Int, view: View, data:Any)
    fun delete(position:Int, view: View, data:Any)
}