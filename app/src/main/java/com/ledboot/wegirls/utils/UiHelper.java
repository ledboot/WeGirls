package com.ledboot.wegirls.utils;

import android.content.Context;
import android.content.Intent;

import com.ledboot.wegirls.activity.AccountActivity;

/**
 * Created by Administrator on 2015/11/21.
 */
public class UiHelper {

    /**
     * 跳转到登陆界面
     * @param context
     */
    public static void showLogin(Context context,int aim){
        Intent intent = new Intent(context, AccountActivity.class);
        intent.putExtra(AccountActivity.AIM,aim);
        context.startActivity(intent);
    }
}
