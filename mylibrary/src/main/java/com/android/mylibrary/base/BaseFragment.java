package com.android.mylibrary.base;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class BaseFragment extends Fragment {
    protected Context context;
    private boolean isViewVisible;

    private boolean isPrepared;
    /**
     * 第一次onResume中的调用onUserVisible避免操作与onFirstUserVisible操作重复
     */
    private boolean isFirstResume = true;

    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(setLayout(), container, false);
    }

    protected abstract int setLayout();

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        isViewVisible = true;
    }

    protected abstract void initView(View view);

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initPrepare();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstResume) {
            isFirstResume = false;
            return;
        }
        if (getUserVisibleHint()) {
            onUserVisible();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onUserInvisible();
        }
    }

    protected abstract void initData();

    /**
     * 用户可见时 加载的方法
     * 不可加载页面 只能加载数据
     *
     * @param isVisibleToUser 是否用户可见
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            } else {
                onUserVisible();
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false;
                onFirstUserInvisible();
            } else {
                onUserInvisible();
            }
        }
    }

    public synchronized void initPrepare() {
        if (isPrepared) {
            onFirstUserVisible();
        } else {
            isPrepared = true;
        }
    }

    /**
     * 第一次fragment可见（进行初始化工作）
     */
    public void onFirstUserVisible() {

    }

    /**
     * fragment可见（切换回来或者onResume）
     */
    public void onUserVisible() {

    }

    /**
     * 第一次fragment不可见（不建议在此处理事件）
     */
    public void onFirstUserInvisible() {

    }

    /**
     * fragment不可见（切换掉或者onPause）
     */
    public void onUserInvisible() {

    }

    protected abstract void initWhenUserIsVisble();

    protected <T extends View> T bindView(int id) {
        return (T) getView().findViewById(id);
    }

    protected <T extends View> T findViewById(int id) {
        return (T) getView().findViewById(id);
    }
}