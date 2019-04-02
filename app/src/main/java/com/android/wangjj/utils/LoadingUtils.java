package com.android.wangjj.utils;

import android.view.View;
import android.view.ViewGroup;

import com.android.wangjj.base.BaseActivity;
import com.android.wangjj.view.LoadView;


public class LoadingUtils {

    private static LoadView loadView;


    public static void show(BaseActivity activity) {
        loadView = new LoadView(activity);
        loadView.setTag("2019");
        activity.addContentView(loadView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public static void hide() {
        if (loadView != null) {
            ViewGroup vp = (ViewGroup) loadView.getParent();
            if (vp != null) {
                ((ViewGroup) loadView.getParent()).removeView(loadView);
            }
        }
    }

    public static boolean isShowing() {
        if (loadView != null) {
            return getChildA();
        } else {
            return false;
        }
    }

    private static Boolean getChildA() {
        boolean a = false;
        ViewGroup vp = ((ViewGroup) loadView.getParent());
        if (vp == null) {
            return a;
        }
        for (int i = 0; i < vp.getChildCount(); i++) {
            View viewchild = vp.getChildAt(i);
            if (viewchild.getTag() != null && String.valueOf(viewchild.getTag()).equals("2019")) {
                LogUtils.dMyLog("2019");
                a = true;
                break;
            }
        }
        return a;
    }
}
