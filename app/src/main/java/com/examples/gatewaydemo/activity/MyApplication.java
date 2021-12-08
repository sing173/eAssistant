package com.examples.gatewaydemo.activity;

import android.app.Application;

public class MyApplication extends Application{
	
	private static MyApplication mApplication;
	
	public static MyApplication getInstance() {
 
		if (mApplication == null) {
			mApplication = new MyApplication();
		}
		return mApplication;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		CrashHandler crashHandler = new CrashHandler(this,"FKEZSMainAPP");
		Thread.setDefaultUncaughtExceptionHandler(crashHandler);
	}
}
