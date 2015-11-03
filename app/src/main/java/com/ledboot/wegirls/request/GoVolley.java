package com.ledboot.wegirls.request;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.ledboot.wegirls.Boot;

import android.net.http.AndroidHttpClient;

import java.io.File;

/**
 * Created by Administrator on 2015/11/3 0003.
 */
public class GoVolley {

    private static final String CACHE_DIR = "volley";

    /** Number of network request dispatcher threads to start. */
    private static final int DEFAULT_NETWORK_THREAD_POOL_SIZE = 3;

    public RequestQueue mRequestQueue;

    private Context mContext;

    public static GoVolley sInstance;

    private GoVolley(Context context){
        mContext = context;
        mRequestQueue = newRequestQueue(mContext,null);
        mRequestQueue.start();
    }

    private RequestQueue newRequestQueue(Context context,HttpStack stack){
        File cacheDir = new File(context.getCacheDir(),CACHE_DIR);
        String userAgent = "volley/0";
        try {
            String packageName = context.getPackageName();
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            userAgent = packageName + "/" + info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }

        if (stack == null) {
            if (Build.VERSION.SDK_INT >= 9) {
                stack = new HurlStack();
            } else {
                // Prior to Gingerbread, HttpUrlConnection was unreliable.
                // See: http://android-developers.blogspot.com/2011/09/androids-http-clients.html
                stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
            }
        }
        Network network = new BasicNetwork(stack);

        RequestQueue requestQueue = new RequestQueue(new DiskBasedCache(cacheDir),network,DEFAULT_NETWORK_THREAD_POOL_SIZE);
        return requestQueue;
    }

    public static GoVolley getInstance(Context context){
        if(sInstance == null){
            synchronized (GoVolley.class){
                if(sInstance == null){
                    sInstance = new GoVolley(context);
                }
            }
        }
        return sInstance;
    }

    public static RequestQueue getRequestQueue(){
        getInstance(Boot.getInstance());
        if(sInstance !=null){
            return sInstance.getThisRequestQueue();
        }
        return  null;
    }

    private RequestQueue getThisRequestQueue() {
        return mRequestQueue;
    }

}
