package org.jeecg.modules.qwert.jst.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JstConstant  {
    public static boolean runflag = false;
    public static boolean runall = false;
    public static boolean watchdog=false;
    public static String user = "admin";
    public static String password = "admin";
    public static String host = "localhost";
    public static int port = 61613;
//    public static String destination = "/queue/socket";
//    public static String destination2 = "/queue/snmp";
    public static String destination = "/topic/socket";
    public static String destination2 = "/topic/snmp";
    public static int debugflag = 1;
    public static int sleeptime =50;
    public static String devcat = "devcat";

    public static boolean isNow(Date date) {
	   	 // 默认的年月日的格式. yyyy-MM-dd
	   	final String PATTEN_DEFAULT_YMD = "yyyy-MM-dd";
	   	// 当前时间
	   	Date now = new Date();
	   	SimpleDateFormat sf = new SimpleDateFormat(PATTEN_DEFAULT_YMD);
	   	//获取今天的日期
	   	String nowDay = sf.format(now);
	   	//对比的时间
	   	String day = sf.format(date);
	   	return day.equals(nowDay);
   }
}