package com.chencha.citysselectordemo;

import android.app.Application;

import com.chencha.citysselectordemo.utils.SPUtils;

/**
 * Desc:
 * Author: jianglei
 * Date: 17/11/13
 */

public class App extends Application {
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        SPUtils.contains(this, "cha");
    }

    public static App getInstance() {
        return instance;
    }
}
