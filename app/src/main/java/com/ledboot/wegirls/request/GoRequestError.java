package com.ledboot.wegirls.request;

import com.android.volley.VolleyError;

public class GoRequestError {
	private Exception mWarpError = null;
	public GoRequestError(Exception err) {
		mWarpError = err;
	}
	
	public Exception toException() {
		return mWarpError;
	}
	
	public VolleyError toVolleyError() {
		return new VolleyError(mWarpError);
	}
	
	public String getMessage() {
		// TODO Auto-generated method stub
		if(mWarpError != null) {
			return mWarpError.getMessage();
		}
		return null;
	}

	public StackTraceElement[] getStackTrace() {
		// TODO Auto-generated method stub
		if(mWarpError != null) {
			return mWarpError.getStackTrace();
		}
		return null;
	}

	public void printStackTrace() {
		// TODO Auto-generated method stub
		if(mWarpError != null) {
			mWarpError.printStackTrace();
		}
	}

	public String toString() {
		// TODO Auto-generated method stub
		if(mWarpError != null) {
			return mWarpError.toString();
		}
		return "";
	}

	public Throwable getCause() {
		// TODO Auto-generated method stub
		if(mWarpError != null) {
			return mWarpError.getCause();
		}
		return null;
	}
	
}
