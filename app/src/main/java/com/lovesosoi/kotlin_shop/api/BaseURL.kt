package com.colin.doumovie.api

/**
 * 网络请求地址
 * 2019-7-19 Lovesosoi
 */
class BaseURL {
    companion object {
        val SERVER_URL = "http://149.28.158.28:8000/api/fruit/"

        const val FRUIT_LIST="fruit_all" //水果列表
        const val FRUIT_ADD="fruit_add" //增加水果
        const val FRUIT_DELETE="fruit_delete" //删除水果
        const val CUSTOMER_LIST="customer_all" //商户列表
        const val CUSTOMER_ADD="customer_add" //商户增加
        const val CUSTOMER_DELETE="customer_delete" //商户删除
        const val ORDER_LIST="order_all" //订单列表
        const val ORDER_ADD="order_add" //订单增加
        const val ORDER_DELETE="order_delete" //订单增删除
    }
}