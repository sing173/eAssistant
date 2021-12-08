package com.examples.gatewaydemo.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import android.icu.text.DateFormat;
import android.os.Environment;
import android.util.Log;

import com.examples.gatewaydemo.util.DateFormatUtil;
import com.examples.gatewaydemo.util.DateFormatUtil.FormatType;

/**
 * 日志打印工具类
 */
public class LogUtils {
	
	private static final boolean LOG_DEBUG = true;
	private static final boolean LOG_ERROR = true;
	private static final boolean LOG_INFO = true;
	private static final boolean LOG_VERBOSE = true;
	private static final boolean LOG_WARN = true;
	/**
	 * 开发阶段
	 */
	private static final int DEVELOP = 0;
	/**
	 * 内部测试阶段
	 */
	private static final int DEBUG = 1;
	/**
	 * 公开测试
	 */
	private static final int BATE = 2;
	/**
	 * 投产阶段
	 */
	private static final int RELEASE = 3;

	/**
	 * 当前阶段
	 */
	private static int currentStage = BATE;

	private static String path;
	private static String userid;
	private static File file;
	private static FileOutputStream outputStream;
	private static String pattern = "yyyy-MM-dd HH:mm:ss";
	
	public LogUtils(String userid){
		this.userid=userid;
	}

	public static void d(String content) {
		if (!LOG_DEBUG)
			return;
		StackTraceElement caller = getCallerMethodName();
		String tag = generateTag(caller);
		Log.d(tag, content);
	}

	public static void d(String tag, String content) {
		if (!LOG_DEBUG)
			return;
		Log.d(tag, content);
	}

	public static void e(String content) {
		if (!LOG_ERROR)
			return;
		StackTraceElement caller = getCallerMethodName();
		String tag = generateTag(caller);
		Log.e(tag, content);
	}

	public static void e(String tag, String content) {
		if (!LOG_ERROR)
			return;
		Log.e(tag, content);
	}

	public static void i(String content) {
		if (!LOG_INFO)
			return;
		StackTraceElement caller = getCallerMethodName();
		String tag = generateTag(caller);
		Log.i(tag, content);
	}

	public static void i(String tag, String content) {
		if (!LOG_INFO)
			return;
		Log.i(tag, content);
	}

	public static void v(String content) {
		if (!LOG_VERBOSE)
			return;
		StackTraceElement caller = getCallerMethodName();
		String tag = generateTag(caller);
		Log.v(tag, content);
	}

	public static void v(String tag, String content) {
		if (!LOG_VERBOSE)
			return;
		Log.v(tag, content);
	}

	public static void w(String content) {
		if (!LOG_WARN)
			return;
		StackTraceElement caller = getCallerMethodName();
		String tag = generateTag(caller);
		Log.w(tag, content);
	}

	public static void w(String tag, String content) {
		if (!LOG_WARN)
			return;
		Log.w(tag, content);
	}

	private static String generateTag(StackTraceElement caller) {
		String tag = "%s[%s, %d]";
		String callerClazzName = caller.getClassName();
		callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
		tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
		return tag;
	}

	public static StackTraceElement getCallerMethodName() {
		StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
		return stacks[4];
	}
	
	public static void log4Info(String logINFO){
        //Logger log = Logger.getLogger(MyApplication.class);  
        //log.info(logINFO);  
	}
	
	public static void log4Debug(String logDEBUG){
        //Logger log = Logger.getLogger(MyApplication.class);  
        //log.debug(logDEBUG);  
	}
	
	public static void log4Error(String logERROR){
        //Logger log = Logger.getLogger(MyApplication.class);  
        //log.error(logERROR);  
	}
	
	static {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			Date date = new Date();
			String time = DateFormatUtil.DateToString(date, FormatType.DateShot);
			File externalStorageDirectory = Environment.getExternalStorageDirectory();
			path = externalStorageDirectory.getAbsolutePath() + "/fklog/";
			File directory = new File(path);
			if (!directory.exists()) {
				directory.mkdirs();
			}
			file = new File(new File(path), "fklog_"+userid+"_"+time+".txt");
			try {
				outputStream = new FileOutputStream(file, true);
			} catch (FileNotFoundException e) {

			}
		} else {

		}
	}
	
	public static void infoLog(String userid,String msg,String appName){
		Date date = new Date();
		String time = DateFormatUtil.DateToString(date, FormatType.DateShot);
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File externalStorageDirectory = Environment.getExternalStorageDirectory();
			path = externalStorageDirectory.getAbsolutePath() + "/fklog/"+appName+"/";
			File directory = new File(path);
			if (!directory.exists()) {
				directory.mkdirs();
			}
			file = new File(new File(path), "fklog_"+userid+"_"+time+".txt");
			try {
				outputStream = new FileOutputStream(file, true);
			} catch (FileNotFoundException e) {

			}
		} 
		//String time = DateFormatUtils.format(date, pattern);
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			if (outputStream != null) {
				try {
					//outputStream.write(time.getBytes());
					String className = "";
					/*if (clazz != null) {
						className = clazz.getSimpleName();
					}*/
					//outputStream.write(("    " + className + "\r\n").getBytes());
					outputStream.write((time+"   ").getBytes());
					outputStream.write(msg.getBytes());
					outputStream.write("\r\n".getBytes());
					outputStream.flush();
				} catch (IOException e) {

				}
			} else {
				android.util.Log.i("SDCAEDTAG", "file is null");
			}
		}
		
	}

	public static void info(String msg) {
		info(LogUtils.class, msg);
	}

	public static void info(@SuppressWarnings("rawtypes") Class clazz, String msg) {
		switch (currentStage) {
			case DEVELOP:
				// 控制台输出
				Log.i(clazz.getSimpleName(), msg);
				break;
			case DEBUG:
				// 在应用下面创建目录存放日志

//				break;
			case BATE:
				// 写日志到sdcard
				Date date = new Date();
				String time=TimeUtils.getToday();
				//String time = DateFormatUtils.format(date, pattern);
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					if (outputStream != null) {
						try {
							outputStream.write(time.getBytes());
							String className = "";
							if (clazz != null) {
								className = clazz.getSimpleName();
							}
							outputStream.write(("    " + className + "\r\n").getBytes());

							outputStream.write(msg.getBytes("UTF-8"));
							outputStream.write("\r\n".getBytes());
							outputStream.flush();
						} catch (IOException e) {

						}
					} else {
						android.util.Log.i("SDCAEDTAG", "file is null");
					}
				}
				break;
			case RELEASE:
				// 不做日志记录
				break;
		}
	}
}

 