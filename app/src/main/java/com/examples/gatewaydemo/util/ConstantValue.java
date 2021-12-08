package com.examples.gatewaydemo.util;

import android.os.Environment;

import com.examples.gatewaydemo.model.RelationInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hutian on 2018/3/21.
 * 静态常量池
 */

public class ConstantValue {
	public static final int PROCESSING = 1;
	public static final int FAILURE = -1;
	public static final int REQUEST_SUCCESS=0;
	public static final int REQUEST_FAILURE=3;
	public static final int  UploadFileSuccess=200;
	public static final int  UploadFilefailed=100;
	public static final int  ZipFolderSuccess=120;
	public static final int ZipFolderFail=121;
	public static final int UploadTransLogFileSuccess=130;
	public static final int UploadTransLogFilefailed=131;
	public static String FkezsCrashLogdir= Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "crash"+ File.separator+"FkezsOpenCard";
	public static String LogTransdir= Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "fkezslog"+ File.separator+"Trans";
	public static List<RelationInfo> relationInfos = new ArrayList<RelationInfo>();
}
