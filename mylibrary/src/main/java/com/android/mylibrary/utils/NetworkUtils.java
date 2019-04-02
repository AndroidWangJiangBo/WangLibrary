package com.android.mylibrary.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
    public static final int TYPE_NONE = -1;
    public static final int TYPE_MOBILE = 0;
    public static final int TYPE_WIFI = 1;


    /**
     * 检测网络是否可用
     */
    @SuppressLint("MissingPermission")
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = null;
        if (cm != null) {
            ni = cm.getActiveNetworkInfo();
        }
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
     */
    public static int getNetworkType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            return TYPE_NONE;//没网
        }

        int type = activeNetworkInfo.getType();
        switch (type) {
            case ConnectivityManager.TYPE_MOBILE:
                return TYPE_MOBILE;//移动数据
            case ConnectivityManager.TYPE_WIFI:
                return TYPE_WIFI;//WIFI
            default:
                break;
        }
        return TYPE_NONE;
    }
}
