package com.albert.application.lifecycle.api

import android.app.Application
import android.content.Context

interface IAApplicationLifecycle {
    fun appAttachBaseContext(base: Context?) {
    }

    fun appOnCreate(application: Application) {

    }

    fun appOnTerminate(application: Application) {
    }

    fun appOnConfigurationChanged(
        application: Application,
        newConfig: android.content.res.Configuration
    ) {

    }

    fun appOnLowMemory(application: Application) {
    }

    fun appOnTrimMemory(application: Application, level: Int) {
    }
}