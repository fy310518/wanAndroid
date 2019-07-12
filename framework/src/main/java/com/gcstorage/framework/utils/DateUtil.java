package com.gcstorage.framework.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Date数据处理类
 */
@SuppressLint("SimpleDateFormat")
public class DateUtil {

    private static SimpleDateFormat mDateFormat = new SimpleDateFormat(
            "MM-dd HH:mm");

    public static String formatDateTime(long time) {
        if (0 == time) {
            return "";
        }

        return mDateFormat.format(new Date(time));
    }

    /**
     * 返回当前时间字符串。 格式：yyyy-MM-dd
     *
     * @return String 指定格式的日期字符串.
     */
    public static String getCurrentDate() {
        return getFormatDateTime(new Date(), "yyyy-MM-dd");
    }

    /**
     * 返回当前指定的时间戳。格式为yyyy-MM-dd HH:mm
     *
     * @return 格式为yyyy-MM-dd HH:mm。
     */
    public static String getCurrentYMDHMDateTime() {
        return getFormatDateTime(new Date(), "yyyy-MM-dd HH:mm");
    }

    /**
     * 返回当前指定的时间戳。格式为yyyy-MM-dd HH:mm:ss
     *
     * @return 格式为yyyy-MM-dd HH:mm:ss，总共19位。
     */
    public static String getCurrentDateTime() {
        return getFormatDateTime(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 根据给定的格式与时间(Date类型的)，返回时间字符串。最为通用。<br>
     *
     * @param date   指定的日期
     * @param format 日期格式字符串
     * @return String 指定格式的日期字符串.
     */
    private static String getFormatDateTime(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 给指定的时间字符串格式化，返回时间格式为 yyyy-MM-dd的时间串
     *
     * @param time 原时间串
     * @return String 格式化后的字符串
     */
    public static String getFormatDateYMDString(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = sdf.parse(time); // 将给定的字符串中的日期提取出来
        } catch (Exception e) { // 如果提供的字符串格式有错误，则进行异常处理
            e.printStackTrace(); // 打印异常信息
        }
        return sdf.format(d);
    }

    /**
     * 离当前时间分钟数
     *
     * @param beginDate 开始时间
     * @return
     */
    public static int getBetweenMinute(String beginDate) {
        Log.d("", beginDate);
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date d1 = sim.parse(beginDate);
            Date d2 = new Date(System.currentTimeMillis());
            int aa = (int) ((d2.getTime() - d1.getTime()) / (60L * 1000));
            Log.d("", aa + " d1" + d1.getTime() + "  d2" + d2.getTime());
            return aa;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 将字符串 转化成 日期
     *
     * @param dateStr
     * @return
     */
    public static Date StringToDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 将字符串转化为时间格式
     */
    private static SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public static String format(long now) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        // System.out.println(now + " = " +
        // formatter.format(calendar.getTime()));
        // 日期转换为毫秒 两个日期想减得到天数
        return formatter.format(calendar.getTime());

    }

    public static String beforeOneWeekToNowDate() {
        Date dBefore = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dBefore);
        calendar.set(Calendar.DAY_OF_MONTH, -9);
        dBefore = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return df.format(dBefore);
    }

    /* 时间戳转换成字符窜 */
    public static String getDateToString(long time) {
        Date d = new Date(time);
        DateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sf.format(d);
    }

    /* 时间戳转换成字符窜 */
    public static String getDateToString1(long time) {
        Date d = new Date(time);
        DateFormat sf = new SimpleDateFormat("yyyy年MM月dd号 HH时mm分");
        return sf.format(d);
    }

    /**
     * 将 yyyy-MM-dd hh:mm:ss 格式的字符串转成 MM月dd日 格式的字符串
     *
     * @param dateStr yyyy-MM-dd hh:mm:ss 格式的字符串转成
     * @return MM月dd日 格式的字符串
     */
    public static String formatDateMMDD(String dateStr) {
        String monthStr = dateStr.substring(dateStr.indexOf("-") + 1, dateStr.lastIndexOf("-"));
        String dayStr = dateStr.substring(dateStr.lastIndexOf("-") + 1, dateStr.indexOf(" "));
        return monthStr + "月" + dayStr + "日";
    }

    public static String secondToMin(int time) {
        String timeStr;
        int hour;
        int minute;
        int second;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;

    }

    public static String unitFormat(int i) {
        String retStr;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    /**
     * 日期转换成字符串
     *
     * @param date
     * @return str
     */
    public static String dateToStr(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);
        return str;
    }

    /**
     * 字符串转换成日期
     *
     * @param str
     * @return date
     */
    public static Date strToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 判断两个时间是同一天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
                .get(Calendar.YEAR);
        boolean isSameMonth = isSameYear
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2
                .get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }


    /**
     * 忽略秒数
     *
     * @param date 带秒数的日期字符串  yyyy-MM-dd HH:mm:ss
     * @return 返回一个忽略秒数的日期字符串 yyyy-MM-dd HH:mm:00
     */
    public static String ignoreSec(String date) {
        return date.substring(0, date.length() - 2) + "00";
    }

    /**
     * 消息列表需要的特殊的时间显示方式：09:12、昨天、09-08
     * 判断是否是今天，如果是今天，就显示时分；
     * 如果不是今天，判断是否是昨天，如果是昨天，就显示昨天；如果不是昨天就显示月日
     * 如：09:12、昨天、09-08
     *
     * @param dateStr 给定的时间字符串，格式："2016-08-05 12:25:12"
     * @return 09:12、昨天、09-08
     */
    public static String convertMsgListTime(String dateStr) {
        if (dateStr == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date value = null;
        try {
            value = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        Date nowDate = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(nowDate);

        sdf = new SimpleDateFormat("yyyy-MM-dd");
        //给定日期value日期字符串
        String timeStr = sdf.format(value);
        String nowDateStr = sdf.format(nowDate);

        String result = "";

        //判断给定日期value是否是今天
        if (nowDateStr.equals(timeStr)) {
            sdf = new SimpleDateFormat("HH:mm");
            //时分
            result = sdf.format(value);
        } else {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            //昨天日期
            calendar.add(Calendar.DATE, -1);
            Date beforeDate = calendar.getTime();
            String beforeDateStr = sdf.format(beforeDate);
            //重置今天日期为日历日期
            calendar.setTime(nowDate);
            if (beforeDateStr.equals(timeStr)) {
                result = "昨天";
            } else {
                sdf = new SimpleDateFormat("MM月dd日");
                //月日
                result = sdf.format(value);
            }
        }
        return result;
    }

    /**
     * 比较2条聊天消息的时间，是否相隔3分钟
     *
     * @param nowTime    当前聊天消息时间；格式："2016-08-05 12:25:12"
     * @param beforeTime 上条聊天消息时间；格式："2016-08-05 12:28:12"
     * @return 如果相隔3分钟，就返回true；否则返回false
     */
    public static boolean compareChatTime(String nowTime, String beforeTime) {
        boolean result = false;
        if (nowTime != null && beforeTime != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date nowDate = sdf.parse(nowTime);
                Date beforeDate = sdf.parse(beforeTime);

                if (nowDate.getTime() - beforeDate.getTime() > 3 * 60 * 1000) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                result = false;
            }
        }
        return result;
    }

    /**
     * 毫秒数
     *
     * @param time
     * @return 距离现在的时间
     */
    public static String timeToUntilNow(long time) {
        String strTime;
        Date date = new Date(time);
        long timeMillis = System.currentTimeMillis();
        long l = timeMillis - time;
        if (l <= 60000) {
            strTime = "刚刚";
        } else if (l > 60000 && l < 3600000) {
            strTime = (l / 60000) + "分钟之前";
        } else if (l >= 3600000 && l < 86400000) {
            strTime = (l / 3600000) + "小时之前";
        } else {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            strTime = sd.format(date);
        }
        return strTime;
    }

    /**
     * 将标准时间字符串转换成 年-月-日
     *
     * @param str
     * @return
     */
    public static String strToYYMMdd(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatDateYYMMdd(date);
    }


    /**
     * 将标准时间字符串转换成 小时:分钟
     *
     * @param str
     * @return
     */
    public static String strToHHmm(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatDateHHmm(date);
    }

    /**
     * 日期转换成 小时:分钟
     *
     * @param
     * @return
     */
    public static String formatDateHHmm(Date date) {
        DateFormat sf = new SimpleDateFormat("HH:mm");
        return sf.format(date);
    }


    /**
     * 日期转换成 年月日
     *
     * @param date
     * @return
     */
    public static String formatDateYYMMdd(Date date) {
        DateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(date);
    }

    /**
     * 将字符串 转化成 日期
     *
     * @param dateStr
     * @return
     */
    public static Date stringToDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 获取当前年月日和星期几
     * @return
     */
    public static String getDateWeek() {

        String mYear;
        String mMonth;
        String mDay;
        String mWay;
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return mYear + "年" + mMonth + "月" + mDay + "日" + " 星期" + mWay;
    }


}
