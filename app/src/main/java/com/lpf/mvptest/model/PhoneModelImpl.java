package com.lpf.mvptest.model;

import android.content.Context;

import com.lpf.mvptest.base.BaseModel;
import com.lpf.mvptest.base.IBasePresenter;
import com.lpf.mvptest.service.PhoneNunInfoService;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 获取号码归属地的具体Model实现
 */
public class PhoneModelImpl extends BaseModel {
    private Context mContext;
    private PhoneNunInfoService phoneNunInfoService;

    public PhoneModelImpl(Context context) {
        super();
        this.mContext = context;
        phoneNunInfoService = retrofitManager.getService();
    }

    public void loadPhoneNumInfo(String phoneNum, final IBasePresenter<PhoneNumInfo> callBack, final int requestTag) {
        phoneNunInfoService.getBeforeNews("phone.get", phoneNum, "10003", "b59bc3ef6191eb9f747dd4e83c99f2a4", "json")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PhoneNumInfo>() {
                    @Override
                    public void onCompleted() {
                        callBack.requestComplete(requestTag);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.requestError(e, requestTag);
                    }

                    @Override
                    public void onNext(PhoneNumInfo phoneNumInfo) {
                        if (null != phoneNumInfo && phoneNumInfo.getSuccess().equals("1"))
                            callBack.retuestSuccess(phoneNumInfo, requestTag);
                        else if (null != phoneNumInfo && phoneNumInfo.getSuccess().equals("0"))
                            callBack.requestError(new Exception(phoneNumInfo.getMsg()), requestTag);
                        else
                            callBack.requestError(new Exception("获取数据错误，请重试！"), requestTag);
                    }
                });
    }
}
