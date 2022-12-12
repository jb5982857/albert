package com.albert.application.lifecycle.common.utils;

import android.text.TextUtils;
import android.util.Log;

public class LogUtils {
    private static final String TAG = "urouter_log";
    private static boolean flagVerbose = false;
    private static boolean flagDebug = false;
    private static boolean flagInfo = true;
    private static boolean flagWarn = true;
    private static boolean flagError = true;


    public static void init(boolean debug) {
        if (debug) {
            flagDebug = flagVerbose = true;
        }
    }

    public static void d(String msg) {
        if (flagDebug) {
            Log.d(TAG, msg);
        }
    }

    public static void v(String msg) {
        if (flagVerbose) {
            Log.v(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (flagError) {
            Log.e(TAG, msg);
        }
    }

    public static void e(String msg, Throwable tr) {
        if (flagError) {
            Log.e(TAG, msg, tr);
        }
    }

    public static void i(String msg) {
        if (flagInfo) {
            Log.i(TAG, msg);
        }
    }

    public static void w(String msg) {
        if (flagWarn) {
            Log.w(TAG, msg);
        }
    }

    //------------------------------以下是替换tag的Log，主要是为了一个开关控制--------------------------------
    public static void w(String msg, Throwable tr) {
        if (flagWarn) {
            Log.w(TAG, msg, tr);
        }
    }

    public static void d(String tag, String msg) {
        if (flagDebug && !TextUtils.isEmpty(tag)) {
            Log.d(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (flagVerbose && !TextUtils.isEmpty(tag)) {
            Log.v(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (flagError && !TextUtils.isEmpty(tag)) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (flagError && !TextUtils.isEmpty(tag)) {
            Log.e(tag, msg, tr);
        }
    }

    public static void i(String tag, String msg) {
        if (flagInfo && !TextUtils.isEmpty(tag)) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (flagWarn && !TextUtils.isEmpty(tag)) {
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (flagWarn && !TextUtils.isEmpty(tag)) {
            Log.w(tag, msg, tr);
        }
    }

    public static boolean isDebug() {
        return flagDebug;
    }

    public static void setDebug(boolean debug) {
        LogUtils.flagDebug = debug;
    }

}
