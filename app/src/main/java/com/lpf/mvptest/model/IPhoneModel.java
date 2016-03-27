package com.lpf.mvptest.model;


import com.lpf.mvptest.presenter.IPhonePresenter;

/**
 * model业务类的接口，就是具体好做些什么事情
 * Created by Administrator on 2016/3/23.
 */
public interface IPhoneModel {
    /**
     * 通过网络请求加载电话号码信息
     *
     * @param phoneNum        电话号码
     * @param iphonePresenter 与presenter交互的回调
     */
    void loadPhoneNumInfo(String phoneNum, IPhonePresenter iphonePresenter);
}
