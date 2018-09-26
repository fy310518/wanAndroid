package com.fy.baselibrary.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.CHINA);
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

        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        return sdf.format(date);
    }


    /**
     * 获得给定时间戳表示的日期 零时零分零秒零毫秒的时间戳
     * @return
     */
    public static long initDateByDay(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
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

    /**
     * 获取指定时间戳 所在的一周的时间集合
     * @param time
     * @param isMoudel isMoudel == 0 以指定的时间戳为第一天；
     *                 isMoudel == 1 以指定的时间戳为最后一天；
     *                 isMoudel == 2 以指定的时间戳为一个礼拜其中一天；
     * @return
     */
    public static List<Date> dateToWeek(long time, int isMoudel) {
        //指定毫秒数所在的日期 零点零分零秒的毫秒数
        long zero = initDateByDay(time);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(zero);

        int d = calendar.get(Calendar.DAY_OF_WEEK);
        long startTime = isMoudel == 0 ? calendar.getTimeInMillis() :
                isMoudel == 1 ? calendar.getTimeInMillis() - 7 * 24 * 3600000 : calendar.getTimeInMillis() - d * 24 * 3600000;

        Date fdate;
        List<Date> list = new ArrayList<>();
        for (int a = 0; a < 7; a++) {
            fdate = new Date();
            fdate.setTime(startTime + a * 24 * 3600000);

            list.add(fdate);
        }
        return list;
    }

    /**
     * 判断两个时间戳是否同一天
     * @param time1
     * @param time2
     * @return
     */
    public static boolean isSameDay(long time1, long time2){
        String date1 = TimeUtils.Long2DataString(time1, "yyyy-MM-dd");
        String date2 = TimeUtils.Long2DataString(time2, "yyyy-MM-dd");

        return date1.equals(date2);
    }
}
