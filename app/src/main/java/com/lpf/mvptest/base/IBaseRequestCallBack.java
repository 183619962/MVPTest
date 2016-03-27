package com.lpf.mvptest.base;

/**
 * 请求数据的回调<br>
 * Presenter用于接受model获取（加载）数据后的回调
 * Created by Administrator on 2016/3/23.
 */
public interface IBaseRequestCallBack<T> {
    /**
     * 开始请求之前
     */
    void beforeRequest(int requestTag);

    /**
     * 请求失败
     *
     * @param e 失败的原因
     */
    void requestError(Throwable e, int requestTag);

    /**
     * 请求结束
     */
    void requestComplete(int requestTag);

    /**
     * 请求成功
     *
     * @param callBack 根据业务返回相应的数据
     */
    void retuestSuccess(T callBack, int requestTag);
}
