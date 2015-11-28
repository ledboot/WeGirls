package com.ledboot.wegirls.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Random;

/**
 * Created by Administrator on 2015/11/23 0023.
 */
public class EUtils {
    public static String TAG = EUtils.class.getSimpleName();

    private static Context mContex;

    public static void initialize(Application application) {
        mContex = application.getApplicationContext();
    }

    public static void Toast(String text) {
        android.widget.Toast.makeText(mContex, text, android.widget.Toast.LENGTH_SHORT).show();
    }

    public static void ToastL(String text) {
        android.widget.Toast.makeText(mContex, text, android.widget.Toast.LENGTH_LONG).show();
    }

    /**
     * 复制文本到剪贴板
     *
     * @param text
     */
    public static void copyToClipboard(String text) {
        ClipboardManager cbm = (ClipboardManager) mContex.getSystemService(Activity.CLIPBOARD_SERVICE);
        cbm.setPrimaryClip(ClipData.newPlainText(mContex.getPackageName(), text));
    }

    /**
     * 经纬度测距
     * @param longitude1
     * @param latitude1
     * @param longitude2
     * @param latitude2
     * @return
     */
    public static double distance(double longitude1, double latitude1, double longitude2, double latitude2) {
        double a, b, R;
        R = 6378137; // 地球半径
        latitude1 = latitude1 * Math.PI / 180.0;
        latitude2 = latitude2 * Math.PI / 180.0;
        a = latitude1 - latitude2;
        b = (longitude1 - longitude2) * Math.PI / 180.0;
        double d;
        double sa2, sb2;
        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(latitude1) * Math.cos(latitude2) * sb2 * sb2));
        return d;
    }

    /**
     * 是否有网络
     * @return
     */
    public static boolean isNetWorkAvilable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContex
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 取APP版本号
     * @return
     */
    public static int getAppVersionCode(){
        try {
            PackageManager mPackageManager = mContex.getPackageManager();
            PackageInfo _info = mPackageManager.getPackageInfo(mContex.getPackageName(),0);
            return _info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    /**
     * 取APP版本名
     * @return
     */
    public static String getAppVersionName(){
        try {
            PackageManager mPackageManager = mContex.getPackageManager();
            PackageInfo _info = mPackageManager.getPackageInfo(mContex.getPackageName(),0);
            return _info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    /**
     * 判断是否是空字符串 null和"" 都返回 true
     *
     * @param str 判断的字符串
     * @return 是否有效
     */
    public static boolean isEmpty(String str) {
        return str == null || str.equals("");
    }


    /**
     * 生成随机的字符串
     * @param length
     * @return
     */
    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }


}
