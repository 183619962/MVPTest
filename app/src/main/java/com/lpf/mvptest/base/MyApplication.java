package com.lpf.mvptest.base;

import android.app.Application;
import android.content.Context;

import com.lpf.mvptest.utils.AppContextUtil;

/**
 * Created by laucherish on 16/3/17.
 */
public class MyApplication extends Application {

    private static Context mApplicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        AppContextUtil.init(this);
        mApplicationContext = this;
    }

    // 获取ApplicationContext
    public static Context getContext() {
        return mApplicationContext;
    }
}
