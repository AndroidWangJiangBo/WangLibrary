package com.android.wangjj.utils;


import android.widget.Toast;

import com.android.wangjj.base.BaseApp;


public class ToastUtils {
    public static void show(String text) {
        Toast.makeText(BaseApp.getContext(), text, Toast.LENGTH_SHORT).show();
    }

}