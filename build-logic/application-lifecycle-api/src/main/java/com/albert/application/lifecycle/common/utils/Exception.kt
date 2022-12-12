package com.albert.application.lifecycle.common.utils

fun String.illegalArgumentException() {
    throw IllegalArgumentException(this)
}