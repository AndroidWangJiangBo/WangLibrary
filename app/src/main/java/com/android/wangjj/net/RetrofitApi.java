package com.android.wangjj.net;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApi {

    public static final String JAVA_IP = "https://wxpay.wxutil.com/";

    private static RetrofitApi instance;
    private RetrofitService service;

    private RetrofitApi() {
        // 创建 OKHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(15, TimeUnit.SECONDS);//连接超时时间
        builder.writeTimeout(60, TimeUnit.SECONDS);//写操作 超时时间
        builder.readTimeout(60, TimeUnit.SECONDS);//读操作超时时间

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(JAVA_IP)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        service = retrofit.create(RetrofitService.class);
    }

    public static RetrofitApi getInstance() {
        if (null == instance) {
            instance = new RetrofitApi();
        }
        return instance;
    }

//    public Observable<WXResult> getWXResult() {
//        return service.getWXResult();
//    }


}
