package com.android.mylibrary.utils;

import android.util.Log;


public class LogUtils {
    public static boolean debug = true;

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
