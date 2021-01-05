package com.love.outofmemory.Utills;

/*Java时间格式转换大全*/

import java.text.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * @author huang
 */
public class DateUtil {


    //获取当前时间Date类型yyyy-MM-dd HH:mm:ss
    public static Date getNowDateLong() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(0);
        return formatter.parse(dateString, pos);
    }

    //获取当前时间Date类型yyyy-MM-dd
    public static Date getNowDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(0);
        return formatter.parse(dateString, pos);
    }

    //获取当前详细日期时间字符串类型
    public static String getStringDateAndTime() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(currentTime);
    }


    //获取详细日期字符串类型
    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(currentTime);
    }


    //获取当前时分秒HH:mm:ss
    public static String getCurrebtTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date currentTime = new Date();
        return formatter.format(currentTime);
    }


    //将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //用来标明解析开始位，其实也可以不明传pos的
        ParsePosition pos = new ParsePosition(0);
        return formatter.parse(strDate, pos);
    }


    //将简短日期字符串转换为Date类型yyyy-MM-dd
    public static Date strToDateShort(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //用来标明解析开始位，其实也可以不明传pos的
        ParsePosition pos = new ParsePosition(0);
        return formatter.parse(strDate, pos);
    }


    //将长日期转换为字符串类型yyyy-MM-dd HH:mm:ss
    public static String dateToStrLong(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(dateDate);
    }


    //将短时间格式时间转换为字符串 yyyy-MM-dd
    public static String dateToStrShort(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(dateDate);
    }
}