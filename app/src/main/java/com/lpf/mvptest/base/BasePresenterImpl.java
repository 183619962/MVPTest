package com.lpf.mvptest.base;

/**
 * 代理对象的基础实现
 *
 * @param <T> 视图接口对象(view) 具体业务各自继承自IBaseView
 * @param <V> 业务请求返回的具体对象
 */
public class BasePresenterImpl<T extends IBaseView, V> implements IBaseRequestCallBack<V> {
    public IBaseView iView;

    /**
     * 构造方法
     *
     * @param view 具体业务的接口对象
     */
    public BasePresenterImpl(T view) {
        this.iView = view;
    }

    @Override
    public void beforeRequest(int requestTag) {
        //显示LOading
        iView.showProgress(requestTag);
    }

    @Override
    public void requestError(Throwable e, int requestTag) {
        //通知UI具体的错误信息
        iView.loadDataError(e,requestTag);
    }

    @Override
    public void requestComplete(int requestTag) {
        //隐藏Loading
        iView.hideProgress(requestTag);
    }

    @Override
    public void retuestSuccess(V callBack, int requestTag) {
        //将获取的数据回调给UI（activity或者fragment）
        iView.loadDataSuccess(callBack, requestTag);
    }
}
