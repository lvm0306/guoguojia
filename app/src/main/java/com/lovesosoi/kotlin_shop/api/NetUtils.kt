package com.lovesosoi.kotlin_shop.api

import android.util.Log
import com.colin.doumovie.api.BuildApi
import com.lovesosoi.kotlin_shop.bean.BaseStatus
import com.lovesosoi.kotlin_shop.bean.CCustomer
import com.lovesosoi.kotlin_shop.bean.CFruitBean
import com.lovesosoi.kotlin_shop.bean.OrderList
import com.lovesosoi.kotlin_shop.interfaces.IApiListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.HashMap

class NetUtils{
    fun getFruitList(listener:IApiListener){
        BuildApi.buildApiServers()
            ?.getFruitList()
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : ApiResponse<CFruitBean>() {
                override fun success(data: CFruitBean) {
                    listener.success(data)
                }

                override fun failure(e: Throwable) {
                    listener.error(e)
                }
            })
    }
    fun getCustomerList(listener:IApiListener){
        BuildApi.buildApiServers()
            ?.getCustomerList()
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : ApiResponse<CCustomer>() {
                override fun success(data: CCustomer) {
                    listener.success(data)
                }

                override fun failure(e: Throwable) {
                    listener.error(e)
                }
            })
    }
    fun getOrderList(listener:IApiListener){

        BuildApi.buildApiServers()
            ?.getOrderHistory()
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : ApiResponse<OrderList>() {
                override fun success(data: OrderList) {
                    listener.success(data)
                }

                override fun failure(e: Throwable) {
                    listener.error(e)
                }
            })
    }
    fun addFruit(name:String,price:Double,unit:String,listener:IApiListener){
        val parm = HashMap<String, String>()
        parm.put("fruit_name", name)
        parm.put("fruit_price", price.toString())
        parm.put("fruit_unit", unit)
        BuildApi.buildApiServers()
            ?.addFruit(parm)
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : ApiResponse<BaseStatus>() {
                override fun success(data: BaseStatus) {
                    listener.success(data)
                }

                override fun failure(e: Throwable) {
                    listener.error(e)
                }
            })
    }
    fun deleteFruit(id:Int,listener:IApiListener){
        val parm = HashMap<String, String>()
        parm.put("fruit_id", id.toString())
        BuildApi.buildApiServers()
            ?.deleteFruit(parm)
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : ApiResponse<BaseStatus>() {
                override fun success(data: BaseStatus) {
                    listener.success(data)
                }

                override fun failure(e: Throwable) {
                    listener.error(e)
                }
            })
    }
    fun addCustomer(name:String,listener:IApiListener){
        val parm = HashMap<String, String>()
        parm.put("customer_name", name)
        BuildApi.buildApiServers()
            ?.addCustomer(parm)
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : ApiResponse<BaseStatus>() {
                override fun success(data: BaseStatus) {
                    listener.success(data)
                }

                override fun failure(e: Throwable) {
                    listener.error(e)
                }
            })
    }
    fun deleteCustomer(id:Int,listener:IApiListener){
        val parm = HashMap<String, String>()
        parm.put("customer_id", id.toString())
        BuildApi.buildApiServers()
            ?.deleteCustomer(parm)
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : ApiResponse<BaseStatus>() {
                override fun success(data: BaseStatus) {
                    listener.success(data)
                }

                override fun failure(e: Throwable) {
                    listener.error(e)
                }
            })
    }
    fun deleteOrder(id:Int,listener:IApiListener){
        val parm = HashMap<String, String>()
        parm.put("order_id", id.toString())
        BuildApi.buildApiServers()
            ?.deleteOrder(parm)
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : ApiResponse<BaseStatus>() {
                override fun success(data: BaseStatus) {
                    listener.success(data)
                }

                override fun failure(e: Throwable) {
                    listener.error(e)
                }
            })
    }

    fun addOrder(customer_name:String,
                 cusomerid:String,
                 time:String,
                 all_price:String,
                 all_item:String,
                 order_info:String,
                 listener:IApiListener){
        val parm = HashMap<String, String>()
        parm.put("customer_id", cusomerid)
        parm.put("customer_name", customer_name)
        parm.put("time", time)
        parm.put("all_price", all_price)
        parm.put("all_item", all_item)
        parm.put("order_info", order_info)
        BuildApi.buildApiServers()
            ?.addOrder(parm)
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : ApiResponse<BaseStatus>() {
                override fun success(data: BaseStatus) {
                    listener.success(data)
                }

                override fun failure(e: Throwable) {
                    listener.error(e)
                }
            })
    }
}