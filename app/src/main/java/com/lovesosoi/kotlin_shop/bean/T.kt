package com.lovesosoi.kotlin_shop.bean

class T {


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
        }
    }
}
