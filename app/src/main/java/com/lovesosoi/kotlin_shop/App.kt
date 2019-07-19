package com.lovesosoi.kotlin_shop

import android.app.Application

/**
 * application
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    /**
     * 双重校验
     */
    companion object {
        val instance: App by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            App()
        }
    }

    /**
     * 线程安全的懒汉
     */
//    companion object {
//        private var instance: App? = null
//
//            get() {
//                if (field == null) {
//                    field = App()
//                }
//                return field
//            }
//
//        @Synchronized
//        fun get(): App {
//            return instance!!
//        }
//    }

}