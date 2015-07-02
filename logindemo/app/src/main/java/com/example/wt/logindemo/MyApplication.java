package com.example.wt.logindemo;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by wt on 15-6-12.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this, "dtzr0r9zbb1n2dmsspbg2e28cn85atl609e8ud8prn0cqr6q", "ayjb9lw82khfz1tc6t1vp7x7eykg34ruu30ba0ox4yrx69ni");
    }
}
