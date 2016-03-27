package com.lpf.mvptest.base;

/**
 * 代理基类
 */
public interface IBasePresenter {

    /**
     * 开始<br>
     * 用于做一些初始化的操作
     */
    void onResume();

    /**
     * 销毁<br>
     * 用于做一些销毁、回收等类型的操作
     */
    void onDestroy();
}
