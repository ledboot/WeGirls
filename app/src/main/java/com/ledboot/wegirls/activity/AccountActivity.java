package com.ledboot.wegirls.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.ledboot.wegirls.R;
import com.ledboot.wegirls.fragment.LoginFrg;
import com.ledboot.wegirls.fragment.RegisterFrg;

/**
 * Created by Administrator on 2015/11/21.
 */
public class AccountActivity extends BaseActivity {

    public static final String AIM = "_aim";

    /**
     * 登录
     */
    public static final int EXTRA_LOGIN = 1;

    /**
     * 注册
     */
    public static final int EXTRA_REGIST = 2;


    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
//        frameLayout = (FrameLayout) findViewById(R.id.frame);
        initData();
    }


    private void initData(){
        int aim = getIntent().getIntExtra(AIM,EXTRA_LOGIN);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;
        switch (aim){
            case EXTRA_LOGIN:
                fragment = new LoginFrg();
                break;
            case EXTRA_REGIST:
                fragment = new RegisterFrg();
                break;
        }
        transaction.replace(R.id.frame,fragment).commit();
    }


}
