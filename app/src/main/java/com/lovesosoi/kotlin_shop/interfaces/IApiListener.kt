package com.lovesosoi.kotlin_shop.interfaces

interface IApiListener{
    fun success(data:Any)
    fun error(e:Throwable)
}