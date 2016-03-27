package com.lpf.mvptest.service;

import com.lpf.mvptest.helper.RetrofitManager;
import com.lpf.mvptest.model.PhoneNumInfo;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 归属地请求的服务
 */
public interface PhoneNunInfoService {
    //http://api.k780.com:88/?app=phone.get&phone={phoneNum}&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json
    @Headers(RetrofitManager.CACHE_CONTROL_AGE + RetrofitManager.CACHE_STALE_LONG)
    @GET("/")
    Observable<PhoneNumInfo> getBeforeNews(@Query("app") String app
            , @Query("phone") String phone
            , @Query("appkey") String appkey
            , @Query("sign") String sign
            , @Query("format") String format);

}
