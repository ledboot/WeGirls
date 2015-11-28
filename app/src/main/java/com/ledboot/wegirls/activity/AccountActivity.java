package com.ledboot.wegirls.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.ledboot.wegirls.R;
import com.ledboot.wegirls.fragment.BaseFragment;
import com.ledboot.wegirls.fragment.LoginFrg;
import com.ledboot.wegirls.fragment.RegisterFrg;

import java.util.List;

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

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
//        frameLayout = (FrameLayout) findViewById(R.id.frame);
        initData();
    }


    private void initData() {
        int aim = getIntent().getIntExtra(AIM, EXTRA_LOGIN);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        BaseFragment fragment = null;
        switch (aim) {
            case EXTRA_LOGIN:
                fragment = new LoginFrg();
                break;
            case EXTRA_REGIST:
                fragment = new RegisterFrg();
                break;
        }
        transaction.replace(R.id.frame, fragment).commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            List<Fragment> fragmentList = fragmentManager.getFragments();
            if (fragmentList != null) {
                int size = fragmentList.size();
                BaseFragment fragment = null;
                for (int i = 0; i < size; i++) {
                    fragment = (BaseFragment) fragmentList.get(i);
                    fragment.onKeyDown(keyCode,event);
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
