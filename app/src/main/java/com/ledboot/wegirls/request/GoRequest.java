package com.ledboot.wegirls.request;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.ledboot.wegirls.utils.Debuger;
import com.ledboot.wegirls.utils.GZIPUtil;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public abstract class GoRequest {
	public static final String TAG = "GoRequest";
	
	public static final int COMPRESS_NONE = 0;
	public static final int COMPRESS_GZIP = 1;
	
	protected HashMap<String, Object> mParams = new HashMap<String, Object>();
	protected HashMap<String, String> mHeaders = new HashMap<String, String>();
	private int mCompress = COMPRESS_NONE;
	private String mContent = null;
	
    private WrapRequest mRequest = null;
    private String mUrl = null;

	public GoRequest(String url,int method) {
		this(url,method,false);
	}

	public GoRequest(String url) {
		this(url,Request.Method.POST,false);
	}

	public GoRequest(String url,boolean needCache) {
		this(url,Request.Method.POST,needCache);
	}

	public GoRequest(String url,int method,boolean needCache) {
		mUrl = url;
		
		ErrorListener listener = new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Debuger.logException(TAG, error);
				onError(new GoRequestError(error));
			}
			
		};
		
		mRequest = new WrapRequest(url,method,needCache,listener);
	}
	
	public void perform() {
		GoVolley.getRequestQueue().add(mRequest);
	}
	
	public final void addParam(String key , Object value) {
		mParams.put(key, value);
	}
	
	public final void setParams(HashMap<String, Object> params) {
		mParams = params;
	}
	
    public final void addHeader(String key , String value) {
    	mHeaders.put(key, value);
    }
    
    public final void setCompress(int compress) {
    	mCompress = compress;
    }
	
    protected abstract String genParams();
    protected abstract void onResponse(String response);
    protected abstract void onError(GoRequestError error);
    
	class WrapRequest extends Request<String> {
		
		public WrapRequest(String url ,int method ,boolean needCache,ErrorListener listener) {
			this(method, url,listener);
			this.setShouldCache(needCache);
			// TODO Auto-generated constructor stub
		}
		
		private WrapRequest(int method, String url,ErrorListener listener) {
			super(method, url, listener);
		}

	    @Override
	    protected void deliverResponse(String response) {
	    	onResponse(response);
	    }
	    
	    private void checkNeedCompress() {
	    	mContent = genParams();
	    	/*if(mContent.length() > Const.MIN_COMPRESS_SIZE) {
	    		mCompress = COMPRESS_GZIP;
	    	}*/
	    }
	    
	    @Override
	    protected Response<String> parseNetworkResponse(NetworkResponse response) {
	    	
	    	Map<String, String> headers = response.headers;
	    	String result = null;
	    	
	    	String encoding = headers.get("Content-Encoding");
	    	boolean isCompress = (encoding != null && encoding.contains("gzip"));
	    	if(isCompress) {
	    		result = new String(GZIPUtil.decompress(response.data),Charset.forName("utf-8"));
	    	} else {
	    		result = new String(response.data,Charset.forName("utf-8"));
	    	}

	    	Debuger.logD(TAG, "respond url="+mUrl+" isCompress="+isCompress+" result="+result);
			return Response.success(result,HttpHeaderParser.parseCacheHeaders(response));
	    }
	    
	    public String getBodyContentType() {
	        return "text/html;charset=utf-8";
	    }
		
		@Override
		public Map<String, String> getHeaders() throws AuthFailureError {
			// TODO Auto-generated method stub
			checkNeedCompress();
			mHeaders.put("Accept-Encoding", "gzip");
			if(mCompress == COMPRESS_GZIP) {
				mHeaders.put("Content-Encoding", "gzip");
			}
			//mHeaders.put("WorldGo-Compressed", "gzip");
			return mHeaders;
		}
		
		@Override
		public byte[] getBody() {
			// TODO Auto-generated method stub
			if(mContent == null) {
				mContent = genParams();
			}
			
			Debuger.logD(TAG,"post url="+mUrl+" params:"+mContent);
			
			byte[] theBody = null;
			if(mCompress == COMPRESS_GZIP) {
				theBody = GZIPUtil.compress(mContent.getBytes(Charset.forName("utf-8")));
			} else {
				theBody = mContent.getBytes(Charset.forName("utf-8"));
			}
			
			return theBody;
		}
	} 	
}
