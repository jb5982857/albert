package com.albert.application.lifecycle.common.utils

import android.os.Handler
import android.os.Looper

val mMainHandler = Handler(Looper.getMainLooper())

fun Looper.isMainLooper(): Boolean {
    return this == Looper.getMainLooper();
}