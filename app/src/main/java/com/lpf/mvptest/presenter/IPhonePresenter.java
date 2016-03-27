package com.lpf.mvptest.presenter;


import com.lpf.mvptest.base.IBasePresenter;
import com.lpf.mvptest.model.PhoneNumInfo;

/**
 * 当model获取数据完成之后与Presenter交互数据的回调
 */
public interface IPhonePresenter extends IBasePresenter {
    /**
     * 加载成功
     *
     * @param phoneNumInfo 电话号码的信息对象
     */
    void loadSuccess(PhoneNumInfo phoneNumInfo);

    /**
     * 加载失败
     *
     * @param e 失败的原因
     */
    void loadError(Throwable e);
}
