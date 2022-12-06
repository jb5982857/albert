package com.albert.log.local

import android.util.Log

object ALog {
    private const val TAG = "albert"

    fun d(message: String, tag: String = TAG) {
        Log.d(tag, message)
    }

    fun i(message: String, tag: String = TAG) {
        Log.i(tag, message)
    }

    fun v(message: String, tag: String = TAG) {
        Log.v(tag, message)
    }

    fun w(message: String, tag: String = TAG) {
        Log.w(tag, message)
    }

    fun e(message: String, tag: String = TAG) {
        Log.e(tag, message)
    }
}