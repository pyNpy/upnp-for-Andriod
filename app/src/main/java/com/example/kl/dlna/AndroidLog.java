package com.example.kl.dlna;

import android.util.Log;

public class AndroidLog {
	private static String mlogTag="dlna";
	public static  void setLogTag(String logTag)
	{
		mlogTag = logTag;
	}
	public static  String getLogTag() {
		return mlogTag;
	}
	public static void log(String msg) 
	{
		Log.i( mlogTag  , msg);
	
	}
	public static void logE(String msg)
	{
		Log.e(mlogTag, msg);
	}
	public static void reSet()
	{
		mlogTag="dlna";
	}

	
}
