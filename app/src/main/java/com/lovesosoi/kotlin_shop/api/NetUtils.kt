package com.lovesosoi.kotlin_shop.api

import android.content.Context
import android.util.Log
import com.colin.doumovie.api.BuildApi
import com.lovesosoi.kotlin_shop.bean.BaseStatus
import com.lovesosoi.kotlin_shop.bean.CCustomer
import com.lovesosoi.kotlin_shop.bean.CFruitBean
import com.lovesosoi.kotlin_shop.bean.OrderList
import com.lovesosoi.kotlin_shop.interfaces.IApiListener
import com.lovesosoi.kotlin_shop.utils.dismissDialog
import com.lovesosoi.kotlin_shop.utils.initDialog
import com.lovesosoi.kotlin_shop.utils.showDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.HashMap

/**
 * 网络请求
 * 2019-7-19 Lovesosoi
 */
class NetUtils {
    var con:Context?=null
    var boolean:Boolean?=null

    constructor(con: Context?,boolean:Boolean?) {
        if (boolean!!) {
            this.con = con
            initDialog(con!!)
        }
    }

    /**
     * 获取水果列表
     */
    fun getFruitList(listener: IApiListener) {
        showDialog()
        BuildApi.buildApiServers()
            ?.getFruitList()
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : ApiResponse<CFruitBean>() {
                override fun success(data: CFruitBean) {
                    listener.success(data)
                    dismissDialog()
                }

                override fun failure(e: Throwable) {
                        listener.error(e)
                    dismissDialog()
                }
            })
    }

    /**
     * 获取商户列表
     */
    fun getCustomerList(listener: IApiListener) {
        showDialog()
        BuildApi.buildApiServers()
            ?.getCustomerList()
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : ApiResponse<CCustomer>() {
                override fun success(data: CCustomer) {
                    listener.success(data)
                    dismissDialog()
                }

                override fun failure(e: Throwable) {
                    listener.error(e)
                    dismissDialog()
                }
            })
    }

    /**
     * 获取订单列表
     */
    fun getOrderList(listener: IApiListener,stime :String,etime :String,customer_name :String) {
        showDialog()
        val parm = HashMap<String, String>()
        parm.put("customer_name", customer_name)
        parm.put("stime",stime)
        parm.put("etime", etime)
        BuildApi.buildApiServers()
            ?.getOrderHistory(parm)
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : ApiResponse<OrderList>() {
                override fun success(data: OrderList) {
                    listener.success(data)
                    dismissDialog()
                }

                override fun failure(e: Throwable) {
                    listener.error(e)
                    dismissDialog()
                }
            })
    }

    /**
     * 增加水果
     */
    fun addFruit(name: String, price: Double, unit: String,cate:Int, listener: IApiListener) {
        showDialog()
        val parm = HashMap<String, String>()
        parm.put("fruit_name", name)
        parm.put("fruit_price", price.toString())
        parm.put("fruit_unit", unit)
        parm.put("fruit_cate", cate.toString())
        BuildApi.buildApiServers()
            ?.addFruit(parm)
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : ApiResponse<BaseStatus>() {
                override fun success(data: BaseStatus) {
                    listener.success(data)
                    dismissDialog()
                }

                override fun failure(e: Throwable) {
                    listener.error(e)
                    dismissDialog()
                }
            })
    }

    /**
     * 修改水果
     */
    fun upDateFruit(name: String, price: Double, unit: String, id:Int,cate:Int,listener: IApiListener) {
        showDialog()
        val parm = HashMap<String, String>()
        parm.put("fruit_name", name)
        parm.put("fruit_price", price.toString())
        parm.put("fruit_unit", unit)
        parm.put("fruit_id", id.toString())
        parm.put("fruit_cate", cate.toString())
        BuildApi.buildApiServers()
            ?.updateFruit(parm)
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : ApiResponse<BaseStatus>() {
                override fun success(data: BaseStatus) {
                    listener.success(data)
                    dismissDialog()
                }

                override fun failure(e: Throwable) {
                    listener.error(e)
                    dismissDialog()
                }
            })
    }

    /**
     * 删除水果
     */
    fun deleteFruit(id: Int, listener: IApiListener) {
        showDialog()
        val parm = HashMap<String, String>()
        parm.put("fruit_id", id.toString())
        BuildApi.buildApiServers()
            ?.deleteFruit(parm)
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : ApiResponse<BaseStatus>() {
                override fun success(data: BaseStatus) {
                    listener.success(data)
                    dismissDialog()
                }

                override fun failure(e: Throwable) {
                    listener.error(e)
                    dismissDialog()
                }
            })
    }

    /**
     * 增加商户
     */
    fun updateCustomer(name: String,id:Int, listener: IApiListener) {
        showDialog()
        val parm = HashMap<String, String>()
        parm.put("customer_name", name)
        parm.put("customer_id", id.toString())
        BuildApi.buildApiServers()
            ?.updateCustomer(parm)
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : ApiResponse<BaseStatus>() {
                override fun success(data: BaseStatus) {
                    listener.success(data)
                    dismissDialog()
                }

                override fun failure(e: Throwable) {
                    listener.error(e)
                    dismissDialog()
                }
            })
    }

    /**
     * 增加商户
     */
    fun addCustomer(name: String, listener: IApiListener) {
        showDialog()
        val parm = HashMap<String, String>()
        parm.put("customer_name", name)
        BuildApi.buildApiServers()
            ?.addCustomer(parm)
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : ApiResponse<BaseStatus>() {
                override fun success(data: BaseStatus) {
                    listener.success(data)
                    dismissDialog()
                }

                override fun failure(e: Throwable) {
                    listener.error(e)
                    dismissDialog()
                }
            })
    }

    /**
     * 删除商户
     */
    fun deleteCustomer(id: Int, listener: IApiListener) {
        showDialog()
        val parm = HashMap<String, String>()
        parm.put("customer_id", id.toString())
        BuildApi.buildApiServers()
            ?.deleteCustomer(parm)
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : ApiResponse<BaseStatus>() {
                override fun success(data: BaseStatus) {
                    listener.success(data)
                    dismissDialog()
                }

                override fun failure(e: Throwable) {
                    listener.error(e)
                    dismissDialog()
                }
            })
    }

    /**
     * 删除订单
     */
    fun deleteOrder(id: Int, listener: IApiListener) {
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

    /**
     * 增加订单
     */
    fun addOrder(
        customer_name: String,
        cusomerid: String,
        time: String,
        otime: String,
        all_price: String,
        all_item: String,
        order_info: String,
        listener: IApiListener
    ) {
        showDialog()
        val parm = HashMap<String, String>()
        parm.put("customer_id", cusomerid)
        parm.put("customer_name", customer_name)
        parm.put("time", time)
        parm.put("otime", otime)
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
                    dismissDialog()
                }

                override fun failure(e: Throwable) {
                    listener.error(e)
                    dismissDialog()
                }
            })
    }

    /**
     * 修改订单
     */
    fun upDateOrder(
        customer_name: String,
        cusomerid: String,
        time: String,
        order_id: String,
        otime: String,
        all_price: String,
        all_item: String,
        order_info: String,
        listener: IApiListener
    ) {
        val parm = HashMap<String, String>()
        parm.put("customer_id", cusomerid)
        parm.put("customer_name", customer_name)
        parm.put("order_id", order_id)
        parm.put("time", time)
        parm.put("otime", otime)
        parm.put("all_price", all_price)
        parm.put("all_item", all_item)
        parm.put("order_info", order_info)
        BuildApi.buildApiServers()
            ?.updateOrder(parm)
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