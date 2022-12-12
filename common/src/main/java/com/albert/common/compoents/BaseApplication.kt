package com.albert.common.compoents

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.albert.application.lifecycle.api.AAppLifecycle
import com.albert.common.BuildConfig
import com.alibaba.android.arouter.launcher.ARouter

lateinit var application: Application

class BaseApplication : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        AAppLifecycle.instance.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        if (BuildConfig.DEBUG) {
            ARouter.openLog()     // 打印日志
            ARouter.openDebug()   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this)
        AAppLifecycle.instance.onCreate(application)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        AAppLifecycle.instance.onLowMemory()
    }

    override fun onTerminate() {
        super.onTerminate()
        AAppLifecycle.instance.onTerminate()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        AAppLifecycle.instance.onConfigurationChanged(newConfig)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        AAppLifecycle.instance.onTrimMemory(level)
    }
}