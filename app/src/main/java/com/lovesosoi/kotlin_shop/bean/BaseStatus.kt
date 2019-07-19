package com.lovesosoi.kotlin_shop.bean

import java.io.Serializable

/**
 * base 请求信息
 * 2019-7-19 Lovesosoi
 */
class BaseStatus :Serializable{

    /**
     * data : {"flag":1}
     * error :
     * status : 1
     */

    var data: DataBean? = null
    var error: String? = null
    var status: Int = 0

    class DataBean  :Serializable{
        /**
         * flag : 1
         */

        var flag: Int = 0
    }

    override fun toString(): String {
        return "BaseStatus(data=$data, error=$error, status=$status)"
    }

}
