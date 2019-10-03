package com.lovesosoi.kotlin_shop.bean

import java.io.Serializable

data class EditOrderBean (var fruit_name: String,var fruit_unit: String,var fruit_amount: String,var fruit_total: String,var fruit_danwei:String){
    override fun toString(): String {
        return "EditOrderBean(fruit_name='$fruit_name', fruit_unit='$fruit_unit', fruit_amount='$fruit_amount', fruit_total='$fruit_total', fruit_danwei='$fruit_danwei')"
    }
}