package com.ledboot.wegirls.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ledboot.wegirls.R;

/**
 * Created by Administrator on 2015/11/21.
 */
public class LoginFrg extends BaseFragment {

    TextView account;
    TextView pass;
    Button loginBtn;
    TextView regist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container);
        initView(view);
        setListener();
        return  view;
    }

    private void setListener() {
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFrg registerFrg = new RegisterFrg();
                repace(R.id.frame,registerFrg);
            }
        });
    }

    private void initView(View view){
        account = (TextView) view.findViewById(R.id.account);
        pass = (TextView) view.findViewById(R.id.password);
        regist = (TextView) view.findViewById(R.id.tv_regist);
        loginBtn = (Button) view.findViewById(R.id.login_btn);
    }

}
