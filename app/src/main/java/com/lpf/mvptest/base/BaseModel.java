package com.lpf.mvptest.base;


import com.lpf.mvptest.helper.RetrofitManager;

/**
 * 业务对象的基类
 */
public class BaseModel {
    //retrofit请求数据的管理类
    public RetrofitManager retrofitManager;

    public BaseModel() {
        retrofitManager = RetrofitManager.builder();
    }

}
