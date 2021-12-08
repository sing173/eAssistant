package com.examples.gatewaydemo.util;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by 胡田on 2018/7/6.
 * 系统动态申请权限类
 */

public class PermissionUtils {
	private static final int REQUEST_EXTERANL_STORAGE=1;
	private static String[] PERMISSIONS_STORAGE={
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
	};
    public static void verrifyStoragePermissions(Activity activity){
    	int permission= ActivityCompat.checkSelfPermission(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE);
    	if(permission!= PackageManager.PERMISSION_GRANTED){
    		ActivityCompat.requestPermissions(activity,PERMISSIONS_STORAGE,REQUEST_EXTERANL_STORAGE);
	    }
    }
}
