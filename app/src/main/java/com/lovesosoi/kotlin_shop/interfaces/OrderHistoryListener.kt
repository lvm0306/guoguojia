package com.lovesosoi.kotlin_shop.interfaces

import android.view.View

interface OrderHistoryListener{

    fun print(position:Int, view: View, data:Any)
    fun delete(position:Int, view: View, data:Any)
}