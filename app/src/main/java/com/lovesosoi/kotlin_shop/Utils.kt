package com.lovesosoi.kotlin_shop

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast

/**
 * 工具类
 * 2019-7-19 Lovesosoi
 */
class Utils(var c: Context) {

    val TAG = "Lovesosoi"
    fun showToast(context: String) {
        Toast.makeText(c, context, Toast.LENGTH_SHORT).show()
    }

    fun showDevToast() {
        Toast.makeText(c, "暂未开放", Toast.LENGTH_SHORT).show()
    }

    fun e(context: String) {
        Log.e(TAG, context)
    }

    fun d(context: String) {
        Log.d(TAG, context)
    }

    fun v(context: String) {
        Log.v(TAG, context)
    }

    fun w(context: String) {
        Log.w(TAG, context)
    }

    fun i(context: String) {
        Log.i(TAG, context)
    }

}