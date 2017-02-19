package com.lpf.mvptest.view;

import com.lpf.mvptest.base.IBaseView;
import com.lpf.mvptest.model.PhoneNumInfo;

/**
 * 号码归属地查询的View
 * 如果没有什么特殊的动作，可以不用定义这个view，在activity里面直接实现IBaseView<PhoneNumInfo>接口即可
 *
 */
public interface PhoneNumInfoView extends IBaseView<PhoneNumInfo> {
    //这里这么写的目的是为了明确view的作用，如果这里的view有更多的操作，可以在这里定义更多的接口
}
