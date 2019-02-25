package org.fourthline.cling.android;

import android.util.Log;

public  class AndroidLog {
	private static String mlogTag="dlna";
	static public void setLogTag(String logTag)
	{
		mlogTag = logTag;
	}
	static public String getLogTag() {
		return mlogTag;
	}
	static public  void log(String msg) 
	{
		Log.i( mlogTag  , msg);
	
	}
	static  public void logE(String msg)
	{
		Log.e(mlogTag, msg);
	}
	
}
