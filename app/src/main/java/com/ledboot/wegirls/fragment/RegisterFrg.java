package com.ledboot.wegirls.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.SignUpCallback;
import com.ledboot.wegirls.R;
import com.ledboot.wegirls.event.LoginEvent;
import com.ledboot.wegirls.utils.Debuger;
import com.ledboot.wegirls.utils.EUtils;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/11/21.
 */
public class RegisterFrg extends BaseFragment implements View.OnClickListener {

    public static String TAG = RegisterFrg.class.getSimpleName();

    EditText phoneNumEt;
    EditText smsCodeEt;
    EditText passEt;
    Button sendSmsCodeBtn;
    Button registBtn;

    AVUser user = null;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        phoneNumEt = (EditText) view.findViewById(R.id.phone_num);
        passEt = (EditText) view.findViewById(R.id.password);
        smsCodeEt = (EditText) view.findViewById(R.id.smscode);
        sendSmsCodeBtn = (Button) view.findViewById(R.id.send_smscode);
        registBtn = (Button) view.findViewById(R.id.regist_btn);
        sendSmsCodeBtn.setOnClickListener(this);
        registBtn.setOnClickListener(this);
    }


    private void regist(){
        String smsCode = smsCodeEt.getText().toString();
        final String phone = phoneNumEt.getText().toString();
        final String pass = passEt.getText().toString();
        if(EUtils.isEmpty(phone)){
            EUtils.Toast("请输入手机号码");
            return;
        }
        if(EUtils.isEmpty(pass)){
            EUtils.Toast("请输入密码");
            return;
        }
        if(EUtils.isEmpty(smsCode)){
            EUtils.isEmpty("请输入验证码");
            return;
        }
        AVUser.verifyMobilePhoneInBackground(smsCode, new AVMobilePhoneVerifyCallback() {
            @Override
            public void done(AVException e) {
                if (e != null) {
                    Debuger.logD(TAG, e.getMessage());
                    EUtils.Toast("验证码错误");
                } else {
                    EUtils.Toast("注册成功");
                    login(phone,pass);
                }
            }
        });

    }

    private void login(String phone,String pass){
        AVUser.loginByMobilePhoneNumberInBackground(phone, pass, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                EventBus.getDefault().post(new LoginEvent(avUser));
            }
        });
    }

    private void sendSmsCode(){
        String phone = phoneNumEt.getText().toString();
        String pass = passEt.getText().toString();
        if(EUtils.isEmpty(phone)){
            EUtils.Toast("请输入手机号码");
            return;
        }
        if(EUtils.isEmpty(pass)){
            EUtils.Toast("请输入密码");
            return;
        }

        user = new AVUser();
        user.setMobilePhoneNumber(phone);
        user.setPassword(pass);
        user.setUsername(EUtils.getRandomString(7));

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Debuger.logD(TAG, "成功注册");
                } else {
                    Debuger.logD(TAG, "成功失败,e=" + e.getMessage());
                }
            }
        });

        AVUser.requestMobilePhoneVerifyInBackground(phone, new RequestMobileCodeCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    sendSmsCodeBtn.setEnabled(false);
                    mHandler.post(timeRunnable);
                    EUtils.Toast("验证码已发送");
                } else {
                    Debuger.logD(TAG, e.getMessage());
                }
            }
        });
    }

    private android.os.Handler mHandler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    mHandler.removeCallbacks(timeRunnable);
                    sendSmsCodeBtn.setEnabled(true);
                    sendSmsCodeBtn.setText("发送验证码");
                    break;
                case 2:
                    String str = String.format("(%s)",msg.arg1);
                    sendSmsCodeBtn.setText(str);
                    break;
            }
        }
    };

    Runnable timeRunnable = new Runnable() {
        int i = 30;
        @Override
        public void run() {
            Debuger.logD(TAG,"timeRunnable,i="+i);
            i-=1;
            if(i == 0){
                i = 30;
                mHandler.sendEmptyMessage(1);
            }else{
                Message msg = mHandler.obtainMessage();
                msg.what = 2;
                msg.arg1 = i;
                mHandler.sendMessage(msg);
            }
            mHandler.postDelayed(timeRunnable,1000);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_smscode:
                sendSmsCode();
                break;
            case R.id.regist_btn:
                regist();
                break;
        }
    }

}
