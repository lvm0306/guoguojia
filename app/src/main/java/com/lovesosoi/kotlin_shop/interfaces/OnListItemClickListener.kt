package com.lovesosoi.kotlin_shop.interfaces

import android.view.View


interface OnListItemLongClickListener{
    fun click(position:Int, view: View, data:Any)
}