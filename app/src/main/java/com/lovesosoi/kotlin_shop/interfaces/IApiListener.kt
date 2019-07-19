package com.lovesosoi.kotlin_shop.interfaces

/**
 * 网络回调接口
 * 2019-7-19 Lovesosoi
 */
interface IApiListener{
    fun success(data:Any)
    fun error(e:Throwable)
}