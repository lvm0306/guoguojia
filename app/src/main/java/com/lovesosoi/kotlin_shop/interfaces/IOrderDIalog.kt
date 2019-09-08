package com.lovesosoi.kotlin_shop.interfaces

import android.view.View

interface IOrderDIalog {

    fun print(position:Int,  data:Any)
    fun delete(position:Int, data:Any)
    fun edit(position:Int, data:Any)
    fun close(position:Int, data:Any)
}