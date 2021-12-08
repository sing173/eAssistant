package com.examples.gatewaydemo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期格式化管理工具类
 * 
 * @type DateFormatUtil TODO
 * @author shaoting.chen
 * @time 2016年3月31日上午11:19:26
 */
public class DateFormatUtil {
	
	/**
	 * 格式类型
	 * @type FormatType
	 * TODO
	 * @author jiazhi.cao
	 * @time 2015-3-9上午11:36:38
	 */
	public enum FormatType{
		/**
		 * yyyy-MM-dd HH:mm:ss
		 */
		DateLong,
		/**
		 * yyyyMMdd
		 */
		DateShot,
		/**
		 * yyyy-MM-dd
		 */
		DateWithUnderline,
		/**
		 * yyyy/MM/dd 
		 */
		DateWithDiagonal,
		/**
		 * y年M月d日
		 */
		DateWithChiness
	}
	
	/**
	 * 获取format 类型
	 * TODO
	 * @param type format类型
	 * @return
	 * @return String
	 * @author jiazhi.cao
	 * @time 下午1:52:26
	 */
	private static String getFormatType(FormatType type)
	{
		String result="";
		switch (type) {
		case DateLong:
			result="yyyy-MM-dd HH:mm:ss";
			break;
		case DateShot:
			result="yyyyMMdd";
			break;
		case DateWithUnderline:
			result="yyyy-MM-dd";
			break;
		case DateWithDiagonal:
			result="yyyy/MM/dd";
			break;
		case DateWithChiness:
			result="y年M月d日";
			break;
		default:
			result="yyyy-MM-dd HH:mm:ss";
			break;
		}
		return result;
	}
	
	/**
	 * 日期时间格式化（日期）
	 * TODO
	 * @param date 日期
	 * @param type 格式枚举
	 * @return ""
	 * @return String 格式化时间
	 * @author jiazhi.cao
	 * @time 下午1:55:57
	 */
	public static String DateToString(Date date,FormatType type) {
		String result="";
		SimpleDateFormat sf=new SimpleDateFormat(getFormatType(type), Locale.getDefault());
		result=sf.format(date);
		return result;
	}
	
	/**
	 * 日期时间格式化（秒）
	 * TODO
	 * @param second Millis
	 * @param type 格式枚举
	 * @return
	 * @return String 格式化时间
	 * @author jiazhi.cao
	 * @time 下午2:08:13
	 */
	public static String MillisToString(long second,FormatType type) {
		String result="";
		Date date=new Date(second);
		DateToString(date, type);
		return result;
	}
	
	/**
	 * 改变时间格式
	 * TODO
	 * @param date 时间
	 * @param oldtype 旧时间格式类型
	 * @param newtype 新时间格式类型
	 * @return
	 * @return String
	 * @author jiazhi.cao
	 * @time 下午2:23:24
	 */
	public static String ChangeFormat(String date,FormatType oldtype,FormatType newtype)
	{
		
		String result ="";
		SimpleDateFormat sf=new SimpleDateFormat(getFormatType(oldtype), Locale.getDefault());
		SimpleDateFormat newsf=new SimpleDateFormat(getFormatType(newtype), Locale.getDefault());
		try {
			Date tempdate= sf.parse(date);
			result=newsf.format(tempdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 增加天数
	 * TODO
	 * @param date 输入日期
	 * @param diff 差距天数
	 * @param type 输入日期格式
	 * @return
	 * @return String 输出原格式日期
	 * @author jiazhi.cao
	 * @time 下午2:47:28
	 */
	public static String AddDays(String date,int diff,FormatType type)
	{
		String result="";
		try
		{
			if(diff==0)
			{
				return date;
			}
			SimpleDateFormat sf=new SimpleDateFormat(getFormatType(type));
			Date tempdate=sf.parse(date);			
			Calendar cr=Calendar.getInstance();
			cr.setTime(tempdate);
			cr.add(Calendar.DAY_OF_YEAR, diff);
			result=sf.format(cr.getTime());
		}
		catch(ParseException e)
		{
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
	/**
	 * 增加天数
	 * TODO
	 * @param date 输入日期
	 * @param diff 差距天数
	 * @param type 输入日期格式
	 * @return
	 * @return String 输出原格式日期
	 * @author jiazhi.cao
	 * @time 下午2:47:28
	 */
	public static Date AddDays(Date date,int diff)
	{
		if(diff==0)
			{
				return date;
			}			
			Calendar cr=Calendar.getInstance();
			cr.setTime(date);
			cr.add(Calendar.DAY_OF_YEAR, diff);
			return cr.getTime();

	}
	
	/**
	 * 字符串转日期
	 * TODO
	 * @param date 字符串
	 * @param type 日期格式
	 * @return
	 * @return Date 日期
	 * @author jiazhi.cao
	 * @time 下午4:12:09
	 */
	public static Date StringToDate(String date,FormatType type)
	{
		Date resultDate=null;
		SimpleDateFormat sf=new SimpleDateFormat(getFormatType(type));
		try {
			resultDate= sf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return resultDate;
	}
	
	/**
	 * 日期比较
	 * TODO
	 * @param DATE1 日期1
	 * @param DATE2 日期2
	 * @return int 1<2返回1 1>2返回-1 1=2返回0
	 * @return 
	 * @author jiazhi.cao
	 * @time 下午5:01:04
	 */
	public static int compare_date(String DATE1, String DATE2) {
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.before(dt2)) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.after(dt2)) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

	/**
	 * 比较日期是否是今天
	 * TODO
	 * @param date
	 * @return
	 * @return boolean
	 * @author jiazhi.cao
	 * @time 上午10:11:59
	 */
	public static boolean isToday(Date date) {
		String inputDate=DateToString(date, FormatType.DateShot);
		String Today=DateToString(new Date(), FormatType.DateShot);
		return inputDate.equals(Today);
	}

	/**
	 * 获取当前年份
	 * 
	 * @return
	 */
	public static int getCurrYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	/**
	 * 获取当前半年度
	 * 
	 * @return
	 */
	public static int getCurrHalfYear() {
		switch (getCurrMonth()) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
			return 1;
		default:
			return 2;
		}
	}

	/**
	 * 获取当前季度
	 * 
	 * @return
	 */
	public static int getCurrQuarter() {
		switch (getCurrMonth()) {
		case 1:
		case 2:
		case 3:
			return 1;
		case 4:
		case 5:
		case 6:
			return 2;
		case 7:
		case 8:
		case 9:
			return 3;
		default:
			return 4;
		}
	}

	/**
	 * 获取当前月份
	 * 
	 * @return
	 */
	public static int getCurrMonth() {
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}
	/**
	 时间戳转换成字符窜*/
	public static String getDateToString(long time) {
		  Date d = new Date(time);
		  SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
		return sf.format(d);
	}
	//得到最近一周日期
	public static ArrayList<String> getWeekData(){
		ArrayList<String> allDatalist=new ArrayList<String>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date currentData=new Date();
		// -7--0
		for(int i=0;i>-7;i--){
			Date date=AddDays(currentData,i);
			allDatalist.add(format.format(date));
		}
		return  allDatalist;
	}

	public static ArrayList<String> getWeekData2(){
		ArrayList<String> allDatalist=new ArrayList<String>();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Date currentData=new Date();
		// -7--0
		for(int i=0;i>-7;i--){
			Date date=AddDays(currentData,i);
			allDatalist.add(format.format(date));
		}
		return  allDatalist;
	}

	public static String getCurrentDate(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		Date date=new Date(System.currentTimeMillis());
		String sDate=sdf.format(date);
		return sDate;
	}
}
