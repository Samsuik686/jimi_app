package com.jimilab.uwclient.utils;

import android.os.Environment;

import com.jimilab.uwclient.BuildConfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author connie 名称：Log 功能：整合打日志的功能
 */
public class Log {
    public String fileName = "";

    private static Boolean MYLOG_SWITCH = true; // 日志文件总开关
    private static Boolean MYLOG_WRITE_TO_FILE = true;// 日志写入文件开关
    private static char MYLOG_TYPE = 'v';// 输入日志类型，w代表只输出告警信息等，v代表输出所有信息
    private static String MYLOG_PATH_SDCARD_DIR = "/sdcard0/";// 日志文件在sdcard中的路径
    private static int SDCARD_LOG_FILE_SAVE_DAYS = 5;// sd卡中日志文件的最多保存天数
    private static String MYLOGFILEName = "-" + BuildConfig.VERSION_NAME + "-Log.txt";// 本类输出的日志文件名称
    private static SimpleDateFormat myLogSdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");// 日志的输出格式
    private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd");// 日志文件格式

    public static void w(String tag, Object msg) { // 警告信息
        log(tag, msg.toString(), 'w');
    }

    public static void e(String tag, Object msg) { // 错误信息
        log(tag, msg.toString(), 'e');
    }

    public static void d(String tag, Object msg) {// 调试信息
        log(tag, msg.toString(), 'd');
    }

    public static void i(String tag, Object msg) {//
        log(tag, msg.toString(), 'i');
    }

    public static void v(String tag, Object msg) {
        log(tag, msg.toString(), 'v');
    }

    public static void w(String tag, String text) {
        log(tag, text, 'w');
    }

    public static void e(String tag, String text) {
        log(tag, text, 'e');
    }

    public static void d(String tag, String text) {
        log(tag, text, 'd');
    }

    public static void i(String tag, String text) {
        log(tag, text, 'i');
    }

    public static void v(String tag, String text) {
        log(tag, text, 'v');
    }

    /**
     * 根据tag, msg和等级，输出日志
     *
     * @param tag
     * @param msg
     * @param level
     * @return void
     * @since v 1.0
     */
    private static void log(String tag, String msg, char level) {

        if (MYLOG_SWITCH) {
            if ('e' == level && ('e' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) { // 输出错误信息
                android.util.Log.e(tag, ">=" + msg);
            } else if ('w' == level && ('w' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {
                android.util.Log.w(tag, ">=" + msg);
            } else if ('d' == level && ('d' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {
                android.util.Log.d(tag, ">=" + msg);
            } else if ('i' == level && ('d' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {
                android.util.Log.i(tag, ">=" + msg);
            } else {
                android.util.Log.v(tag, ">=" + msg);
            }
            if (MYLOG_WRITE_TO_FILE)
                writeLogtoFile(String.valueOf(level), tag, ">=" + msg);
        }
    }

    /**
     * 打开日志文件并写入日志
     *
     * @return *
     */
    private static void writeLogtoFile(String mylogtype, String tag, String text) {// 新建或打开日志文件
        Date nowtime = new Date(System.currentTimeMillis());
        String needWriteFiel = logfile.format(nowtime);

        String needWriteMessage = myLogSdf.format(nowtime) + "    " + mylogtype
                + "    " + tag + "    " + text;
        String fileName = getSDPath();//以name存在目录中
        if (fileName == null) return;
        File file = new File(fileName, needWriteFiel + MYLOGFILEName);
        try {
            FileWriter filerWriter = new FileWriter(file, true);// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(needWriteMessage);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除制定的日志文件
     */
    public static boolean delFile() {// 删除日志文件
        boolean deleteRes = false;
        String needDelFiel = logfile.format(getDateBefore());
        File file = new File(getSDPath(), needDelFiel + MYLOGFILEName);
        if (file.exists()) {
            d("Log", "needDelFiel: " + file.getAbsolutePath());
            deleteRes = file.delete();
        }
        return deleteRes;
    }

    /**
     * 得到现在时间前的几天日期，用来得到需要删除的日志文件名
     */
    private static Date getDateBefore() {
        Date nowtime = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(nowtime);
        now.set(Calendar.DATE, now.get(Calendar.DATE)
                - SDCARD_LOG_FILE_SAVE_DAYS);
        return now.getTime();
    }

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        } else {
            return null;
        }
        return sdDir.toString();
    }
}
