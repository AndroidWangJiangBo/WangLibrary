package com.android.wangjj.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import com.android.wangjj.bean.EventMessage;
import com.android.wangjj.utils.ActivityUtils;
import com.android.wangjj.utils.EventBusUtils;
import com.android.wangjj.utils.NetworkUtils;
import com.android.wangjj.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public abstract class BaseActivity extends AppCompatActivity {

    //全屏开关
    private boolean openFullWindows = false;
    //沉浸式开关
    private boolean openStatusBar = false;
    //屏幕竖向开关
    private boolean openScreenPortrait = true;
    //修改状态栏颜色开关
    private boolean openChangeTitleColor = false;
    //eventbus开关
    private boolean openEventBus = false;

    private List<Disposable> disList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtils.getInstance().addActivity(this);
        setContentViewBefore();
        changeAppStyle();
        setContentView(setContentView());
        if (isOpenEventBus()) {
            EventBusUtils.register(this);
        }
        setContentViewAfter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!NetworkUtils.isNetworkConnected(this)) {
            ToastUtils.show("当前网络不可用...请检查是否连接网络");
        }

    }

    private void changeAppStyle() {
        openFullWindows();
        openChangeTitleColor();
        openStatusBar();
        openScreenPortrait();
    }

    private void openChangeTitleColor() {
        if (isOpenChangeTitleColor()) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    getWindow().setStatusBarColor(getResources().getColor(android.R.color.holo_blue_dark));
                    //底部导航栏
                    //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void openScreenPortrait() {
        if (isOpenScreenPortrait()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private void openStatusBar() {
        if (isOpenStatusBar()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // 透明状态栏
                getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                // 透明导航栏
                getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
    }

    private void openFullWindows() {
        if (isOpenFullWindows()) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        }
    }


    /**
     * 接收到分发的事件
     *
     * @param event 事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveEvent(EventMessage event) {
    }

    /**
     * 接受到分发的粘性事件
     *
     * @param event 粘性事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceiveStickyEvent(EventMessage event) {
    }

    @Override
    public void finish() {
        removeHttp();
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtils.getInstance().removeActivity(this);
        if (isOpenEventBus()) {
            EventBusUtils.unregister(this);
        }
    }

    /**
     * 点击空白区域隐藏键盘.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {  //把操作放在用户点击的时候
            View v = getCurrentFocus();      //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (isShouldHideKeyboard(v, ev)) { //判断用户点击的是否是输入框以外的区域
                hideKeyboard(v.getWindowToken());   //收起键盘
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {  //判断得到的焦点控件是否包含EditText
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],    //得到输入框在屏幕中上下左右的位置
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击位置如果是EditText的区域，忽略它，不收起键盘。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void removeHttp() {
        if (null == disList || disList.size() == 0) {
            return;
        }
        for (int i = 0; i < disList.size(); i++) {
            disList.get(i).dispose();
        }
        disList.clear();
    }

    public abstract void setContentViewBefore();

    public abstract int setContentView();

    public abstract void setContentViewAfter();

    public boolean isOpenFullWindows() {
        return openFullWindows;
    }

    public void setOpenFullWindows(boolean openFullWindows) {
        this.openFullWindows = openFullWindows;
    }

    public boolean isOpenStatusBar() {
        return openStatusBar;
    }

    public void setOpenStatusBar(boolean openStatusBar) {
        this.openStatusBar = openStatusBar;
    }

    public boolean isOpenScreenPortrait() {
        return openScreenPortrait;
    }

    public void setOpenScreenPortrait(boolean openScreenPortrait) {
        this.openScreenPortrait = openScreenPortrait;
    }

    public boolean isOpenChangeTitleColor() {
        return openChangeTitleColor;
    }

    public void setOpenChangeTitleColor(boolean openChangeTitleColor) {
        this.openChangeTitleColor = openChangeTitleColor;
    }

    public boolean isOpenEventBus() {
        return openEventBus;
    }

    public void setOpenEventBus(boolean openEventBus) {
        this.openEventBus = openEventBus;
    }

    public List<Disposable> getDisList() {
        return disList;
    }

    public void setDisList(List<Disposable> disList) {
        this.disList = disList;
    }
}
