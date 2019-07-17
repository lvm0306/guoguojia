package com.lovesosoi.kotlin_shop.interfaces

import android.view.View

interface OnItemClick{
    fun click(position:Int,view:View,data:Any)
    fun add(position:Int,view:View,data:Any)
    fun sub(position:Int,view:View,data:Any)
}