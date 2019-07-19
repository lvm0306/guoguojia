package com.lovesosoi.kotlin_shop.bean

/**
 * 订单
 * 2019-7-19 Lovesosoi
 */
data class OrderBean(var name:String,var count:Double=0.00,var price:Double,var unit:String)