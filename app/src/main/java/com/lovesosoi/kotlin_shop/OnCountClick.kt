package com.lovesosoi.kotlin_shop

import android.view.View

interface OnCountClick{
    fun countClick(position:Int, view: View, data:Any)
}