package com.ledboot.wegirls.request;

import android.os.Handler;
import android.os.Looper;

import com.ledboot.wegirls.utils.Debuger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;


public abstract class GoJsonRequest extends GoRequest{
	public static final String TAG = "GoJsonRequest";
	
	public static boolean sTrigger_logout = false;
	
	private JSONObject mJsonParams = null;
	
    public GoJsonRequest(String url) {
		this(url, false);
		// TODO Auto-generated constructor stub
	}
    
    public GoJsonRequest(String url, boolean needCache) {
		super(url, needCache);
		// TODO Auto-generated constructor stub
	}
    
	@Override
	protected String genParams() {
		// TODO Auto-generated method stub
		if(mJsonParams == null) {
			mJsonParams = genJsonParams();
		}
		return mJsonParams.toString();
	}
	
	@Override
	protected void onError(GoRequestError error) {
		// TODO Auto-generated method stub
		onJsonError(error);
	}

	@Override
	protected void onResponse(String response) {
		// TODO Auto-generated method stub
		try {
			JSONObject repObj = new JSONObject(response);
			
			//10035为token错误，如果出现token错误，让用户重新登录
			if(!sTrigger_logout && repObj.optInt("status",-128) == 10035) {
				sTrigger_logout = true;
				new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						sTrigger_logout = false;
					}
				}, 8 * 1000);
				Debuger.logW(TAG, "出现用户token错误，重启应用！");
//				Tools.exitApp(WorldGo.getInstance(), Monitor.LOGOUT_CAUSE_ACCOUNT_EXCEPTION);
			}
			onJsonResponse(repObj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			onJsonError(new GoRequestError(e));
		}
	}
	
	public final void setJsonParams(JSONObject params) {
		mJsonParams = params;
	}
	
	protected JSONObject genJsonParams() {
		/*String macAddr = SystemUtil.getMacAddr();
		String time = SystemClock.uptimeMillis()+"";
		JSONObject obj = new JSONObject();
		try {
			obj.put("mac", macAddr);
			obj.put("time", time);
			obj.put("version", SimpleEncrypter.getKeyVersion());
			obj.put("sign", SimpleEncrypter.generateSignCode(macAddr+time));
			obj.put("appVersion", SystemUtil.getSystemVersionCode());
		} catch (Exception e1) {}
		mParams.put("checkSign", obj);
		mParams.put("publishChannel", Config.PUBLISH_CHANNEL);*/
		mJsonParams = new JSONObject();
    	try {
            for (Map.Entry<String, Object> entry : mParams.entrySet()) {
            	if(entry.getValue() instanceof List) {
            		mJsonParams.put(entry.getKey(), new JSONArray((List)entry.getValue()));
            	}else if(entry.getValue() == null){
            		mJsonParams.put(entry.getKey(), "null");
            	}else{
            		mJsonParams.put(entry.getKey(), entry.getValue());
            	}
            }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Debuger.logException(TAG, e);
		}

        return mJsonParams;
	}
	
    protected abstract void onJsonResponse(JSONObject response);
    protected abstract void onJsonError(GoRequestError err);
}
