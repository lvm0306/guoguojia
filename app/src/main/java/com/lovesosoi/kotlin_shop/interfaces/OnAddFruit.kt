package com.lovesosoi.kotlin_shop.interfaces

/**
 * 水果增加接口
 * 2019-7-19 Lovesosoi
 */
interface OnAddFruit{
    fun add(name:String,price:Double,unit:String,cate:String)
    fun upDate(name:String,price:Double,unit:String,id:Int,cate:String)
}