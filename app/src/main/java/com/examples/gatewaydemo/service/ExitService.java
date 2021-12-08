package com.examples.gatewaydemo.service;

import com.examples.gatewaydemo.util.LogUtils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ExitService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//Toast.makeText(getApplicationContext(), "登陆成功！", 2000).show();
		//util.info(ExitService.class, "登陆成功");
		LogUtils.infoLog("123456", "登录成功","FKEZSMainApp");
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		LogUtils.infoLog("123456", "登录成功","FKEZSMainApp");
		//util.info(ExitService.class, "登陆成功");
		//Toast.makeText(getApplicationContext(), "登陆成功！", 2000).show();
	}
		
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//util.info(ExitService.class, "退出程序");
		LogUtils.infoLog("123456", "登录成功","FKEZSMainApp");
		Log.i("---ondestory---", "---ondestory---");
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Log.i("---onUnbind---", "---onUnbind---");
		LogUtils.infoLog("123456", "登录成功","FKEZSMainApp");
		//util.info(ExitService.class, "退出程序");
		return super.onUnbind(intent);
	}
}
