package com.lpf.mvptest.presenter;

import android.content.Context;

import com.lpf.mvptest.base.BasePresenterImpl;
import com.lpf.mvptest.model.PhoneModelImpl;
import com.lpf.mvptest.model.PhoneNumInfo;
import com.lpf.mvptest.view.PhoneNumInfoView;


/**
 * Presenter的实现，协调model去加载数据，获取model加载完成时候的回调，控制界面加载框的显示与隐藏
 */
public class PhonePresenterImpl extends BasePresenterImpl<PhoneNumInfoView, PhoneNumInfo> {
    private PhoneModelImpl phoneModel;
    private Context mContext;


    public PhonePresenterImpl(PhoneNumInfoView phoneNumInfoView, Context context) {
        super(phoneNumInfoView);
        this.mContext = context;
        phoneModel = new PhoneModelImpl(mContext);
    }

    /**
     * 获取归属地信息
     *
     * @param phoneNum   电话号码
     * @param requestTag 请求标识
     */
    public void getPhoneNumInfo(String phoneNum, int requestTag) {
        onResume();
        beforeRequest(requestTag);
        phoneModel.loadPhoneNumInfo(phoneNum, this, requestTag);
    }
}
