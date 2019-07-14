package com.lovesosoi.kotlin_shop

import android.view.View


interface OnListItemClickListener{
    fun click(position:Int, view: View, data:Any)
}