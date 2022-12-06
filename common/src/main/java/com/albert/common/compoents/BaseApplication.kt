package com.albert.common.compoents

import android.app.Application
import android.content.Context
import com.albert.common.BuildConfig
import com.alibaba.android.arouter.launcher.ARouter

lateinit var application: Application

class BaseApplication : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        if (BuildConfig.DEBUG) {
            ARouter.openLog()     // 打印日志
            ARouter.openDebug()   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this)
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }
}