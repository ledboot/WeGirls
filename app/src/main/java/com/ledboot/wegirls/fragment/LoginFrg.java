package com.ledboot.wegirls.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.ledboot.wegirls.R;
import com.ledboot.wegirls.event.LoginEvent;
import com.ledboot.wegirls.utils.EUtils;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/11/21.
 */
public class LoginFrg extends BaseFragment implements View.OnClickListener{

    EditText accountTv;
    EditText passTv;
    Button loginBtn;
    TextView registTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,null);
        initView(view);
        return view;
    }

    private void initView(View view){
        accountTv = (EditText) view.findViewById(R.id.account);
        passTv = (EditText) view.findViewById(R.id.password);
        registTv = (TextView) view.findViewById(R.id.tv_regist);
        loginBtn = (Button) view.findViewById(R.id.btn_login);

        loginBtn.setOnClickListener(this);
        registTv.setOnClickListener(this);
    }

    private void login(){
        String account = accountTv.getText().toString();
        String pass = passTv.getText().toString();
        if(EUtils.isEmpty(account) || EUtils.isEmpty(pass)){
            EUtils.Toast("请输入完整的信息");
           return;
        }
        AVUser.loginByMobilePhoneNumberInBackground(account, pass, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser user, AVException e) {
                if(e != null){
                    EUtils.Toast("帐号或密码错误");
                }else{
                    EventBus.getDefault().post(new LoginEvent(user));
                }
            }
        });
    }

    private void goRegist(){
        RegisterFrg registerFrg = new RegisterFrg();
        repace(R.id.frame,registerFrg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_regist:
                goRegist();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        getActivity().finish();
        return super.onKeyDown(keyCode, event);
    }
}
