package com.lovesosoi.kotlin_shop.api

import org.reactivestreams.Subscriber

/**
 * observer 扩展
 *
 * 2019-7-19 Lovesosoi
 */
abstract class BaseObserver<T> : Subscriber<T> {
    override fun onError(e: Throwable?) {
        onFail(e)
        onEnd()
    }

    override fun onNext(t: T) {
        onSuccess(t)
    }


    /**
     * 结束回调
     */
    abstract fun onEnd()

    /**
     * 请求成功回调
     */
    abstract fun onSuccess(data: T)

    /**
     * 请求失败回调
     */
    abstract fun onFail(error: Throwable?)
}