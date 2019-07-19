package com.lovesosoi.kotlin_shop.bean

import java.io.Serializable

/**
 * 商户
 * 2019-7-19 Lovesosoi
 */
class CCustomer :Serializable{

    /**
     * data : {"customer":[{"customer_id":1,"customer_name":"学府三"}]}
     * error :
     * status : 1
     */

    var data: DataBean? = null
    var error: String? = null
    var status: Int = 0

    class DataBean :Serializable{
        var customer: List<CustomerBean>? = null

        class CustomerBean :Serializable{
            /**
             * customer_id : 1
             * customer_name : 学府三
             */

            var customer_id: Int = 0
            var customer_name: String? = null
        }
    }
}
