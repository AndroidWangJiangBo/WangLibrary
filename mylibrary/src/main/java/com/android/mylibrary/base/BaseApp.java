package com.android.mylibrary.base;

import android.app.Application;
import android.content.Context;


public class BaseApp extends Application {
    private static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // 程序终止的时候执行
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // 低内存的时候执行
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        // 程序在内存清理的时候执行
    }

    public static Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}




