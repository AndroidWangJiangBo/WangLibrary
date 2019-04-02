package com.android.mylibrary.utils;


import android.widget.Toast;

import com.android.mylibrary.base.BaseActivity;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HttpUtils {

    public static void postHttp(Observable<?> observable, final BaseActivity activity, final HttpCallBack callBack, final boolean isShowLoading) {
        if (isShowLoading) {
            LoadingUtils.show(activity);
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        callBack.cancelHttp(d);
                    }

                    @Override
                    public void onNext(Object value) {
                        callBack.success(value);
                        hide(isShowLoading);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.error(e);
                        if (!NetworkUtils.isNetworkConnected(activity)) {
                            Toast.makeText(activity, "当前网络不可用...请检查是否连接网络", Toast.LENGTH_LONG).show();
                        } else {
                            if (e instanceof SocketTimeoutException) {
                                Toast.makeText(activity, "服务器连接超时...请确保网络通畅", Toast.LENGTH_LONG).show();
                            } else if (e instanceof ConnectException) {
                                Toast.makeText(activity, "服务器连接异常", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(activity, "服务器异常", Toast.LENGTH_LONG).show();
                            }
                        }
                        hide(isShowLoading);
                    }

                    @Override
                    public void onComplete() {
                        callBack.onComplete();
                        hide(isShowLoading);
                    }
                });
    }

    private static void hide(boolean isShow) {
        if (isShow) {
            LoadingUtils.hide();
        }
    }

    public interface HttpCallBack<T> {
        void success(T value);

        void error(Throwable e);

        void onComplete();

        void cancelHttp(Disposable d);
    }

}