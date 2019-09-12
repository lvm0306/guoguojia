package com.lovesosoi.kotlin_shop.interfaces

/**
 * 商户增加接口
 * 2019-7-19 Lovesosoi
 */
interface IOnAddFruitInOrder{
    fun add(name:String,unit:String,unit_price:String,count:String)
}