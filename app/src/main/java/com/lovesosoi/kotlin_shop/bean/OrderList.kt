package com.lovesosoi.kotlin_shop.bean

import java.io.Serializable

/**
 * 历史订单列表
 * 2019-7-19 Lovesosoi
 */
class OrderList : Serializable {

    /**
     * data : {"order":[{"order_id":1,"customer_id":1,"customer_name":"学府三","time":"2019-7-17","all_price":"39.8","all_item":"4","order_info":"茄子|3斤^豆角|5斤"}]}
     * error :
     * status : 1
     */

    var data: DataBean? = null
    var error: String? = null
    var status: Int = 0

    class DataBean : Serializable {
        var order: List<OrderBean>? = null

        class OrderBean : Serializable {
            /**
             * order_id : 1
             * customer_id : 1
             * customer_name : 学府三
             * time : 2019-7-17
             * all_price : 39.8
             * all_item : 4
             * order_info : 茄子|3斤^豆角|5斤
             */

            var order_id: Int = 0
            var customer_id: Int = 0
            var customer_name: String? = null
            var time: String? = null
            var all_price: String? = null
            var all_item: String? = null
            var order_info: String? = null
            override fun toString(): String {
                return "OrderBean(order_id=$order_id, customer_id=$customer_id, customer_name=$customer_name, time=$time, all_price=$all_price, all_item=$all_item, order_info=$order_info)"
            }

        }

        override fun toString(): String {
            return "DataBean(order=$order)"
        }

    }

    override fun toString(): String {
        return "OrderList(data=$data, error=$error, status=$status)"
    }

}
