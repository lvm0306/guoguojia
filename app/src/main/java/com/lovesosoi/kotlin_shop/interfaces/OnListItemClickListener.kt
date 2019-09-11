package com.lovesosoi.kotlin_shop.interfaces

import android.view.View

/**
 * 删除长安点击
 * 2019-7-19 Lovesosoi
 */
interface OnListItemLongClickListener{
    fun onLongClick(position:Int, view: View, data:Any)
    fun onClick(position:Int, view: View, data:Any)
}