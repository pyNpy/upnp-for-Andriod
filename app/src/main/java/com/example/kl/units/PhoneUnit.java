package com.example.kl.units;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class PhoneUnit {

	private Display PhoneDisplay;
	private Context AppContext;
	
	 
	public Display getPhoneDisplay() {
		return PhoneDisplay;
	}

	public void setPhoneDisplay(Display phoneDisplay) {
		PhoneDisplay = phoneDisplay;
	}

	public Context getAppContext() {
		return AppContext;
	}

	public void setAppContext(Context appContext) {
		AppContext = appContext;
	}

	public PhoneUnit(Context context)
	{
		super();
		this.AppContext =context;
		init(); 
	}
	
	private void init()
	{
		if(AppContext==null) 
			return ;
		WindowManager manager = (WindowManager) AppContext.getSystemService(Context.WINDOW_SERVICE);
		this.PhoneDisplay = manager.getDefaultDisplay();
	}

	@SuppressLint("NewApi")
	public int getScreenWidth() {
		Point outSize =new Point();
		this.PhoneDisplay.getRealSize(outSize);
		return outSize.x;
	}
	@SuppressLint("NewApi")
	public int getScreenHeight() {
		Point outSize =new Point();
		this.PhoneDisplay.getRealSize(outSize);
		return outSize.y;
	}
	
	
}
