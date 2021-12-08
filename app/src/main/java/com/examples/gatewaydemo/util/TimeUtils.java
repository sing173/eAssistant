package com.examples.gatewaydemo.util;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 时间处理工具类
 * @author Administrator
 *
 */
public class TimeUtils {
	/**
	 * 把毫秒，转换成 ：如：03:10
	 * 
	 * @param millisencond
	 * @return
	 */
	public static String convertMilliSecondToMinute2(int millisencond) {
		int oneMinute = 1000 * 60;
		int minutes = millisencond / oneMinute;
		int sencond = (millisencond - minutes * oneMinute) / 1000;
		return getNum(minutes) + ":" + getNum(sencond);
	}

	/**
	 * 把秒，转换成 ：如：03:10
	 * 
	 * @param millisencond
	 * @return
	 */
	public static String convertSecondToMinute2(int seconds) {
		int oneMinute = 60;
		int minutes = seconds / oneMinute;
		int mSecond = seconds - minutes * oneMinute;
		return getNum(minutes) + ":" + getNum(mSecond);
	}

	public static String getNum(int num) {
		if (num >= 10) {
			return "" + num;
		} else {
			return "0" + num;
		}
	}

	public static String getTimestamp() {
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		return String.valueOf(ts.getTime());
	}
	
	public static String getToday()
	{
		java.util.Date localDate = new java.util.Date();
	    String str = new java.sql.Date(localDate.getTime()).toString();
	    String str1=new Date().toString();
	    str =str.replace("-", "/");
		return str1;
	}
	
	public static int getDateValue(String date){
		int value=0;
		//年份后两位
		int a1=Integer.parseInt(date.substring(2, 4));
		//月份
		int a2=Integer.parseInt(date.substring(5, 7));
		//月份所乘单位
		int unit = 0;
		//日期
		int a3=Integer.parseInt(date.substring(8, 10));
		switch (a2-1) {
		case 0:
			unit=31;
		case 1:
			unit=31;
			break;
		case 2:
			unit=60;
			break;
		case 3:
			unit=91;
			break;
		case 4:
			unit=121;
			break;
		case 5:
			unit=152;
			break;
		case 6:
			unit=182;
			break;
		case 7:
			unit=213;
			break;
		case 8:
			unit=244;
			break;
		case 9:
			unit=274;
			break;
		case 10:
			unit=305;
			break;
		case 11:
			unit=335;
			break;
		case 12:
			unit=366;
			break;

		default:
			break;
		}
		value=a1*365+unit+a3;
		return value;
	}
}
