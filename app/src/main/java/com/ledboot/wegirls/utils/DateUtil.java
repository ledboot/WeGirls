package com.ledboot.wegirls.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/**
 * 日期工具类
 * @author liuweile
 *
 */
public class DateUtil {
	public static final String ALL="yyyy年MM月dd日  HH:mm:ss";
	public static final String MONTH_DAY="MM月dd日";
	public static final String HOUR_MINUTE_SECOND="HH:mm:ss";
	public static final String HOUR_MINUTE="HH:mm";
	public static final String WEEK_PREFIX="周";
	public static final String POINT_TIME="yyyy.MM.dd HH:mm";
	
	private static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>();
	
	private static final Object object = new Object();
	
	
	//得到当前时间
	public static String getCurrentDate(String format){
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		String time=sdf.format(new Date());
		return time;
	}
	
	/**
	 * 获取当前时间
	 * @return
	 */
	public static String getCurrentTime(){
		return toString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS);
	}
	
	/**
	 * 获取当前时间
	 * @return
	 */
	/*public static long getCurrentLongTime(){
		return new Date().getTime();
	}*/
	
	/**
	 * 获取SimpleDateFormat
	 * @param pattern 日期格式
	 * @return SimpleDateFormat对象
	 * @throws RuntimeException 异常：非法日期格式
	 */
 	private static SimpleDateFormat getDateFormat(String pattern) throws Exception{
		SimpleDateFormat dateFormat = threadLocal.get();
		if(dateFormat == null){
			synchronized (object) {
				if(dateFormat == null){
					dateFormat = new SimpleDateFormat(pattern);
					dateFormat.setLenient(true);
					threadLocal.set(dateFormat);
				}
			}
		}
		dateFormat.applyPattern(pattern);
		return dateFormat;
	}
	
	/**
	 * 根据时间戳转换成制定格式的时间(不针对php)
	 * @param time 时间戳
	 * @param format 时间格式
	 * @return
	 */
	public static String getDate(String time,String format){
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		String times=sdf.format(new Date(time));
		return times;
	}
	
	/**
	 * 将长整型数字转换为日期格式的字符串
	 * @param time
	 * @param format
	 * @return
	 */
	public static String toString(long time,DateStyle style){
		Date date = new Date(time);
		String dateStr = toString(date, style.getValue());
		return dateStr;
	}
	
	/**
	 * 将日期类型转为日期格式的字符串
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String toString(Date date, String pattern) {
		String dateString = null;
		if (date != null) {
			try {
				dateString = getDateFormat(pattern).format(date);
			} catch (Exception e) {
			}
		}
		return dateString;
	}
	
	/**
	 * 将日期类型转为日期格式的字符串
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String toString(Date date,DateStyle style){
		return toString(date, style.getValue());
	}
	
	/**
	 * 判断用户的设备时区是否为东八区（中国） 
	 * 
	 * @return
	 */
	public static boolean isInEasternEightZones() {
		boolean defaultVaule = true;
		if (TimeZone.getDefault() == TimeZone.getTimeZone("GMT+08"))
			defaultVaule = true;
		else
			defaultVaule = false;
		return defaultVaule;
	}

	/**
	 * 根据不同时区，转换时间 
	 * 
	 * @param time
	 * @return
	 */
	public static Date transformTime(Date date, TimeZone oldZone,
			TimeZone newZone) {
		Date finalDate = null;
		if (date != null) {
			int timeOffset = oldZone.getOffset(date.getTime())
					- newZone.getOffset(date.getTime());
			finalDate = new Date(date.getTime() - timeOffset);
		}
		return finalDate;

	}


	/**
	 * 讲long型时间转为日期对象
	 * @param date
	 * @return
	 */
	public static Date toDate(long date){
		Date formatDate = new Date(date);
		return formatDate;
	}
	
	/**
	 * 比较时间 , 比较dateTwo是否大于dateOne 5分钟
	 * @param dateOne
	 * @param dateTwo
	 * @return
	 */
	public static boolean gtTwoMin(long dateOne,long dateTwo){
		Calendar beginCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();
		beginCalendar.setTimeInMillis(dateOne);
		beginCalendar.add(Calendar.MINUTE, 2);
		endCalendar.setTimeInMillis(dateTwo);
		return beginCalendar.before(endCalendar);
	}
	



	/**
	 * 将时间戳转换成具体的时间
	 * @param time
	 * @return
	 */
	public static String getShowTime(long time){
		String result="";
		long currentTime=System.currentTimeMillis();
		
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date(currentTime));		
		int currentYear=calendar.get(Calendar.YEAR);
		int currentMonth=calendar.get(Calendar.MONTH);
		int currentDay=calendar.get(Calendar.DATE);		
		
		calendar.setTime(new Date(time));
		int year=calendar.get(Calendar.YEAR);
		int month=calendar.get(Calendar.MONTH);
		int day=calendar.get(Calendar.DATE);		
		
		if(currentYear!=year){
			result =toString(time, DateStyle.YYYY_MM_DD);
		}
		else if(currentMonth!=month){
			result =toString(time, DateStyle.MM_DD);
		}
		else if(currentDay!=day){
			result =toString(time, DateStyle.MM_DD);
		}
		else{
			result =toString(time, DateStyle.HH_MM);
		}
		return result;
	}
	
	/**
	 * 将字符串时间转换成时间戳
	 * @param time 字符串格式 (例如:time=2015-12-12 12:12:12,那么format=yyyy-MM-dd HH:mm:ss)
	 * @param format 转换的时间格式
	 * @return 时间戳
	 */
	public static long getTimeStampByString(String time,String format){
		long currentTime=0L;
		SimpleDateFormat sdf=new SimpleDateFormat(format,Locale.getDefault());
		try {
			Date date=sdf.parse(time);			
			currentTime=date.getTime();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return currentTime;
	}
	
}
