package com.lpf.mvptest.view.ui;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lpf.mvptest.R;
import com.lpf.mvptest.base.IBaseView;
import com.lpf.mvptest.model.PhoneNumInfo;
import com.lpf.mvptest.presenter.PhonePresenterImpl;
import com.lpf.mvptest.view.PhoneNumInfoView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, PhoneNumInfoView {
    //获取归属地的请求标识，用回页面多个地方请求的时候，标识是那一个完成并回调了
    private static final int REQUESTMSG = 0;

    //电话号码的EditText
    private EditText phoneNum;
    //获取归属地的按钮
    private Button getPhoneInfo;
    //用于显示最后获取的结果
    private TextView msg;
    //Loading弹框
    private ProgressDialog progressDialog;
    //获取归属地的代理对象
    private PhonePresenterImpl phonePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    private void initView() {
        //初始化控件
        phoneNum = (EditText) findViewById(R.id.phonenum);
        getPhoneInfo = (Button) findViewById(R.id.getphoneinfo);
        msg = (TextView) findViewById(R.id.msg);
        getPhoneInfo.setOnClickListener(this);

        //初始化Loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");

        //初始化代理对象
        phonePresenter = new PhonePresenterImpl(this, this);
    }

    @Override
    public void toast(String msg, int requestTag) {

    }

    @Override
    public void showProgress(int requestTag) {
        if (null != progressDialog && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    public void hideProgress(int requestTag) {
        if (null != progressDialog && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void loadDataSuccess(PhoneNumInfo phoneNumInfo, int requestTag) {
        msg.setText(phoneNumInfo.toString());
    }

    @Override
    public void loadDataError(Throwable e, int requestTag) {
        msg.setText(e.getMessage());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getphoneinfo:
                phonePresenter.getPhoneNumInfo(phoneNum.getText().toString(), REQUESTMSG);
                break;
        }
    }
}
