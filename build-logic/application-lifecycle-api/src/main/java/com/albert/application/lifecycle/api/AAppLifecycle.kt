package com.albert.application.lifecycle.api

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.annotation.MainThread
import com.albert.application.lifecycle.apt.AApplicationApts
import com.albert.application.lifecycle.common.utils.AApplicationClassLoader

/**
 * //
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//



 */
class AAppLifecycle {
    object Holder {
        val holder = AAppLifecycle()
    }

    private lateinit var application: Application
    private val lifecyclePlugins = mutableListOf<IAApplicationLifecycle>()

    companion object {
        val instance = Holder.holder
            @JvmStatic get
    }

    fun attachBaseContext(base: Context?) {
        val temps = AApplicationApts().plugins
        temps.forEach {
            val plugins = it.getPlugins<IAApplicationLifecycle>()
            plugins.forEach {
                this.lifecyclePlugins.add(it)
                it.appAttachBaseContext(base)
            }
        }
    }

    fun onCreate(application: Application) {
        this.application = application
        this.lifecyclePlugins.forEach {
            it.appOnCreate(application)
        }
    }

    fun onTerminate() {
        this.lifecyclePlugins.forEach {
            it.appOnTerminate(application)
        }
    }

    fun onConfigurationChanged(newConfig: Configuration) {
        this.lifecyclePlugins.forEach {
            it.appOnConfigurationChanged(application, newConfig)
        }
    }

    fun onLowMemory() {
        this.lifecyclePlugins.forEach {
            it.appOnLowMemory(application)
        }
    }

    fun onTrimMemory(level: Int) {
        this.lifecyclePlugins.forEach {
            it.appOnTrimMemory(application, level)
        }
    }
}