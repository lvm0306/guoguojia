package com.lovesosoi.kotlin_shop.bean

import java.io.Serializable

/**
 * 水果
 * 2019-7-19 Lovesosoi
 */
class CFruitBean : Serializable {

    /**
     * data : {"fruit":[{"fruit_id":1,"fruit_name":"土豆","fruit_price":"2.50","fruit_unit":"斤"}]}
     * error :
     * status : 1
     */

    var data: DataBean? = null
    var error: String? = null
    var status: Int = 0

    class DataBean : Serializable {
        var fruit: List<FruitBean>? = null

        class FruitBean : Serializable {
            /**
             * fruit_id : 1
             * fruit_name : 土豆
             * fruit_price : 2.50
             * fruit_unit : 斤
             */
            var fruit_id: Int = 0
            var fruit_name: String? = null
            var fruit_price: String? = null
            var fruit_unit: String? = null

        }
    }
}
