package com.albert.application.lifecycle.api


interface IAApplicationLifecycleTemp {
    fun <T> getPlugins(): List<T>
}