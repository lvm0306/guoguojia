package com.lovesosoi.kotlin_shop.utils

import android.content.Context
import com.lovesosoi.kotlin_shop.App
import com.lovesosoi.kotlin_shop.R
import com.lovesosoi.kotlin_shop.dialog.NetLoadingDialog

var netloading:NetLoadingDialog?=null
public fun initDialog(conn:Context){
    netloading = NetLoadingDialog(conn, R.style.DialogTheme)
}

public fun showDialog(){
    netloading!!.show()
}
public fun dismissDialog(){
    netloading!!.dismiss()

}
