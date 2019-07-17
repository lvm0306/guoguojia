package com.lovesosoi.kotlin_shop.api

import android.content.Context
import com.google.gson.Gson
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.HttpException

abstract class ApiResponse<T>: Observer<T> {
    abstract fun success(data: T)
    abstract fun failure(e: Throwable)

    override fun onSubscribe(d: Disposable) {
    }

    override fun onNext(t: T) {
        success(t)
    }

    override fun onComplete() {
    }

    override fun onError(e: Throwable) {
        failure(e)
    }

}