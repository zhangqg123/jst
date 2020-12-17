package org.jeecg.modules.qwert.jst.utils;

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
}