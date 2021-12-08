package com.examples.gatewaydemo.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	//��ʾshort��toast
	public static void showShortToast (Context context, String toastMsg){
		Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show();
	}
	//��ʾlong��toast
	public static void showLongToast (Context context, String toastMsg){
		Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show();
	}
}
