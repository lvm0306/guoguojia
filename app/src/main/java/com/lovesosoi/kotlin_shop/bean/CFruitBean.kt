package com.lovesosoi.kotlin_shop.bean

import java.io.Serializable

/**
 * 水果
 * 2019-7-19 Lovesosoi
 */
class CFruitBean : Serializable {
    var data: DataBean? = null
    var error: String? = null
    var status: Int = 0

    class DataBean {
        var fruit: List<FruitBean>? = null

        class FruitBean {
            /**
             * fruit_id : 22
             * fruit_name : 黄瓜
             * fruit_price : 2.0
             * fruit_unit : 斤
             * fruit_cate : 1
             */

            var fruit_id: Int = 0
            var fruit_name: String? = null
            var fruit_price: String? = null
            var fruit_unit: String? = null
            var fruit_cate: String? = null

            constructor(
                fruit_id: Int,
                fruit_name: String?,
                fruit_price: String?,
                fruit_unit: String?,
                fruit_cate: String?
            ) {
                this.fruit_id = fruit_id
                this.fruit_name = fruit_name
                this.fruit_price = fruit_price
                this.fruit_unit = fruit_unit
                this.fruit_cate = fruit_cate
            }
        }
    }
}
