package com.android.wangjj.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;


public class ActivityUtils {

    private List<Activity> activityList = new ArrayList<>();
    private static ActivityUtils instance;

    // 单例模式中获取唯一的ExitApplication实例
    public static synchronized ActivityUtils getInstance() {
        if (null == instance) {
            instance = new ActivityUtils();
        }
        return instance;
    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        if (activityList == null) {
            activityList = new ArrayList<>();
        }
        activityList.add(activity);
        LogUtils.dMyLog("add " + getTopActivityName(activity));
    }

    // 移除Activity
    public void removeActivity(Activity activity) {
        if (activityList != null) {
            activityList.remove(activity);
            LogUtils.dMyLog("remove " + getTopActivityName(activity));
        }

    }

    // 遍历所有Activity并finish
    public void exitSystem(Context context) {
//        if (null != activityList) {
//            for (Activity activity : activityList) {
//                if (activity != null) activity.finish();
//            }
//        }
        // 退出进程
//        android.os.Process.killProcess(android.os.Process.myPid());
//        System.exit(0);
    }


    //获取最上层activityName
    public static String getTopActivityName(Activity activity) {
        try {
            String topActivityName = activity.getClass().getSimpleName();
            return topActivityName;
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean isServiceExisted(Context context, String className) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(Integer.MAX_VALUE);

        if (!(serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            ActivityManager.RunningServiceInfo serviceInfo = serviceList.get(i);
            ComponentName serviceName = serviceInfo.service;
            if (serviceName.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }
}
