package com.android.wangjj.utils;

import android.util.Log;

import com.android.wangjj.BuildConfig;


public class LogUtils {
    private static boolean debug = BuildConfig.isDebug;

    public static void d(String name, String msg) {
        if (debug) {
            Log.d(name, msg);
        }
    }

    public static void dMyLog(String msg) {
        d("mylog", msg);
    }

    public static void dHttp(String msg) {
        d("http", msg);
    }
}
