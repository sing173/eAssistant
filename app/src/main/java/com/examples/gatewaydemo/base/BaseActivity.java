package com.examples.gatewaydemo.base;

import com.examples.gatewaydemo.activity.MyApplication; 

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.Window;

public abstract class BaseActivity extends Activity{
	private MyApplication application;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView();
		Listener();
		application=MyApplication.getInstance();
	}
	
	public abstract void initView();
	
	public abstract void Listener();
}
