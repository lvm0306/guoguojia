package com.lovesosoi.kotlin_shop

import android.view.View


interface OnListItemLongClickListener{
    fun click(position:Int, view: View, data:Any)
}