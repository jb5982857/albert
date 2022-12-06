package com.albert.app_utils

import android.os.Looper

fun mainThreadAssert() {
    if (Looper.getMainLooper() != Looper.myLooper()) {
        throw MainAssertException()
    }
}