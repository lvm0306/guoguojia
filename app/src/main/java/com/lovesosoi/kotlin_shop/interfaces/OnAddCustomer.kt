package com.lovesosoi.kotlin_shop.interfaces

/**
 * 商户增加接口
 * 2019-7-19 Lovesosoi
 */
interface OnAddCustomer{
    fun add(name:String)
    fun update(name:String,id:Int)
}