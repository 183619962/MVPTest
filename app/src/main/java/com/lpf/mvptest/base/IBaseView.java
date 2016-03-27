package com.lpf.mvptest.base;

/**
 * 视图基类
 */
public interface IBaseView<T> {
    /**
     * 通过toast提示用户
     *
     * @param msg        提示的信息
     * @param requestTag 请求标识
     */
    void toast(String msg, int requestTag);

    /**
     * 显示进度
     *
     * @param requestTag 请求标识
     */
    void showProgress(int requestTag);

    /**
     * 隐藏进度
     *
     * @param requestTag 请求标识
     */
    void hideProgress(int requestTag);

    /**
     * 基础的请求的返回
     *
     * @param data
     * @param requestTag 请求标识
     */
    void loadDataSuccess(T data, int requestTag);

    /**
     * 基础请求的错误
     *
     * @param e
     * @param requestTag 请求标识
     */
    void loadDataError(Throwable e, int requestTag);
}
