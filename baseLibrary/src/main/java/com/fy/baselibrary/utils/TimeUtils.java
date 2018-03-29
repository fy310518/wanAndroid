package com.fy.baselibrary.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间转换工具类
 *
 * Created by fangs on 2017/3/22.
 */
public class TimeUtils {

    private TimeUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 将一个时间戳转化成时间字符串，自定义格式
     *
     * @param time
     * @param format
     *            如 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String Long2DataString(long time, String format) {
        if (time == 0) {
            return "";
        }

        Date date = new Date(time);

        return Data2String(date, format);
    }

    /**
     * 将一个时间字符串转换为时间戳，自定义格式
     * @param timeStr
     * @param format
     * @return
     */
    public static long timeString2long(String timeStr, String format){

        if(null == timeStr || "".equals(timeStr))return -1;

        Date date;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));
            date = simpleDateFormat .parse(timeStr);

            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return -1L;
    }

    /**
     * 将一个日期对象转换成 时间字符串
     * @param date
     * @param format
     * @return
     */
    public static String Data2String(Date date, String format){

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        return sdf.format(date);
    }

    /**
     * 计算 年龄
     * @param birthday
     * @param format
     * @return
     */
    public static int calculationAge(String birthday, String format){
        long   birthdayLong = timeString2long(birthday, format);
        if (birthdayLong == -1){
            return 0;
        }

        String currentYear  = Long2DataString(System.currentTimeMillis(), "yyyy");
        String birthdayYear = Long2DataString(birthdayLong, "yyyy");

        return Integer.parseInt(currentYear) - Integer.parseInt(birthdayYear);
    }
}
