package com.gcstorage.framework.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gcstorage.framework.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Administrator on 2018/9/21 0021.
 */

public class StringUtil {
    /**
     * 判断EditText输入是否为空
     */
    public static boolean checkEditTextIsNull(EditText editText) {
        boolean flag = false;
        if (editText.getText().toString().trim() == null
                || "".equals(editText.getText().toString().trim())
                || editText.getText().toString().length() < 1) {
            flag = true;
        }
        return flag;
    }

    /**
     * 判断是否是手机号
     */
    public static boolean isPhoneNum(String phone) {
        String regExp = "^((13[0-9])|(14[0-9])|(15[0-9])|(18[0-9])|(17[0-9]))\\d{8}$";

        Pattern p = Pattern.compile(regExp);

        Matcher m = p.matcher(phone);
        return m.find();
    }


    /**
     * 根据用户名的不同长度，来进行替换 ，达到保密效果
     *
     * @param userName 用户名
     * @return 替换后的用户名
     */
    public static String hideName(String userName) {
        if (userName.length() <= 1) {
            return "*";
        } else {

            String aa = userName.substring(userName.length() - 1, userName.length());
            String bb = "**" + aa;
            return bb;
        }

    }

    /**
     * 隐藏手机号中间6位
     *
     * @param phoneNumber
     * @return
     */
    public static String hidePhoneNumber(String phoneNumber) {
        String phonenum = "";
        phonenum = phoneNumber.replaceAll("(\\d{3})\\d{6}(\\d{2})", "$1****$2");
        return phonenum;
    }


    /**
     * 隐藏身份证
     *
     * @param number
     * @return
     */
    public static String hideNumber(String number) {
        if (number.length() <= 1) {
            return "*";
        } else {
            return number.replaceAll("(\\d{4})\\d{10}(\\w{4})", "$1*****$2");
        }
    }

    //生成很多个*号
    public static String createAsterisk(int length) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            stringBuffer.append("*");
        }
        return stringBuffer.toString();
    }

    /**
     * 去除字符串中所有空格
     */
    public static String removeSpace(String resource) {
        StringBuffer buffer = new StringBuffer();
        int position = 0;
        char currentChar;

        while (position < resource.length()) {
            currentChar = resource.charAt(position++);
            if (currentChar != ' ')
                buffer.append(currentChar);
        }
        return buffer.toString();
    }


    /**
     * 把字符串转化成泛型为String的 Json 集合
     *
     * @param str 字符串
     * @return
     */
    public static JsonParser parser = new JsonParser();

    public static JsonArray string2Json(String picList) {
        int startIndex = picList.indexOf("\""); //返回的是一string对象,截取
        int lastIndex = picList.lastIndexOf("\"");
        String substring = picList.substring(startIndex + 1, lastIndex);
        JsonElement jsonEntily = parser.parse(substring); //转换为json实体
        JsonArray asJsonArray = jsonEntily.getAsJsonArray();
        return asJsonArray;
    }

    /**
     * null转化为空字符串
     */
    public static String deNull(String str) {
        if (str == null) {
            return "";
        }
        return str;
    }

    /**
     * Object 转化成 String
     */
    public static String Object2String(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    /**
     * 是否为空白,包括null和""
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0 || "null".equals(str);
    }

    public static boolean isBlank(Object str) {
        return str == null || str.toString().trim().length() == 0
                || "null".equals(str);
    }

    /**
     * 将byte[]数组转化成String
     */
    public static String byte2String(byte[] arg) {
        String dataString = "";
        try {
            dataString = new String(arg, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return dataString;
    }

    /**
     * 字符串的半角转为全角的代码如下
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 替换、过滤特殊字符
     */
    public static String StringFilter(String str) throws PatternSyntaxException {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 将泛型为字符串类型的集合转化成数组格式的字符串
     *
     * @param list 泛型为字符串类型的集合
     * @return 数组格式的字符串
     */
    public static String list2StringArray(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (String alarm : list) {
            sb.append("\"").append(alarm).append("\",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }

    /**
     * 判断是否是URL地址
     *
     * @param scanStr 需要判断地址的字符串
     * @return
     */
    public static boolean isUrl(String scanStr) {
        Pattern pattern = Pattern
                .compile("http://(([a-zA-z0-9]|-){1,}\\.){1,}[a-zA-z0-9]{1,}-*");
        Matcher matcher = pattern.matcher(scanStr);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    /**
     * 时间字符转换成long类型
     *
     * @param time
     * @return
     */
    public static long getStrByLong(String time) {
        long longTime = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date parse = sdf.parse(time);
            longTime = parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return longTime;
    }

    /**
     * 时间字符转换成long类型
     *
     * @param time
     * @return
     */
    public static String getStr(long time) {
        String strTime = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date parse = new Date(time);
        strTime = sdf.format(parse);
        return strTime;
    }

    /**
     * 将泛型为字符串类型的集合转化成以逗号分割，拼接起来的字符串
     *
     * @param list 泛型为字符串类型的集合
     * @return 拼接起来的字符串
     */
    public static String list2String(List<String> list) {
        if (list == null || list.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String alarm : list) {
            sb.append(alarm).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 把字符串转化成泛型为String的List集合
     *
     * @param str 字符串
     * @return
     */
    public static List<String> string2List(String str) {
        List<String> list = new ArrayList<>();
        if (str != null) { // 如果图片地址不为空
            str = str.trim();
            if (str.length() != 0) {
                String[] strings = str.split(","); // 把字符串分割成字符串数组
                for (String s : strings) {
                    list.add(s);
                }
            }
        }
        return list;
    }

    public static String int2String(long time) {
        String strTime;
        Date date = new Date(time);
        long timeMillis = System.currentTimeMillis();
        long l = timeMillis - time;
        if (l <= 60000) {
            strTime = "刚刚";
        } else if (l > 60000 && l < 3600000) {
            strTime = (l / 60000) + "分钟前";
        } else if (l >= 3600000 && l < 86400000) {
            strTime = (l / 3600000) + "小时前";
        } else {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            strTime = sd.format(date);
        }
        return strTime;
    }

    /**
     * 消息列表的时间显示样式
     *
     * @param time
     * @return
     */
    public static String chatListTimeFormat(String time) {
        String string = time;
        boolean today = false;
        try {
            today = isToday(time);
            if (today) {
                string = getTodayStr(time);
            } else {
                string = getYesterdayStr(time);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return string;
    }

    /**
     * 显示昨天的时间模式
     *
     * @param time
     * @return
     */
    public static String getYesterdayStr(String time) {
        String timeStr = time;
        boolean yesterday = false;
        try {
            yesterday = isYesterday(time);
            if (yesterday) {
                timeStr = "昨天";
            } else {
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat sd1 = new SimpleDateFormat("MM-dd");
                Date date = sd.parse(time);
                timeStr = sd1.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStr;
    }

    /**
     * 显示今天的时间模式
     *
     * @param time
     * @return
     */
    public static String getTodayStr(String time) {
        String timeStr = time;
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //原来的时间模式
        SimpleDateFormat sd1 = new SimpleDateFormat("hh:mm");//新的显示模式
        SimpleDateFormat sd3 = new SimpleDateFormat("aa");//判断是上午还是下午的模式
        try {
            Date date = sd.parse(time);
            String format = sd3.format(date);
            if ("下午".equals(format)) {
                timeStr = "下午 " + sd1.format(date);//TextView txt_time控件的时间显示方式
            } else if ("上午".equals(format)) {
                timeStr = "上午 " + sd1.format(date);//TextView txt_time控件的时间显示方式
            } else if ("PM".equals(format)) {
                timeStr = "下午 " + sd1.format(date);//TextView txt_time控件的时间显示方式
            } else if ("AM".equals(format)) {
                timeStr = "上午 " + sd1.format(date);//TextView txt_time控件的时间显示方式
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStr;
    }

    /**
     * 判断是否为今天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean isToday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为昨天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean isYesterday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 格式化人员列表
     * 小红、小明、小张等5人
     *
     * @param nameList
     * @return
     */
    public static String formatNameList(List<String> nameList) {
        if (nameList == null || nameList.size() == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nameList.size(); i++) {
            if (i == 2) {
                sb.deleteCharAt(sb.length() - 1);
                sb.append("等" + nameList.size() + "人");
                return sb.toString();
            }

            String alarm = nameList.get(i);
            if (alarm.length() > 4) {
                alarm = alarm.substring(0, 3) + "...";
            }
            sb.append(alarm).append("、");
        }

        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }


    /**
     * 去掉url中的路径，留下请求参数部分
     *
     * @param strURL url地址
     * @return url请求参数部分
     */
    public static String getUrlParam(String strURL) {
        if (strURL == null) {
            return null;
        }
        String strAllParam = null;
        String[] arrSplit = null;

//        strURL=strURL.trim().toLowerCase();
        arrSplit = strURL.split("[?]");
        if (strURL.length() > 1) {
            if (arrSplit.length > 1) {
                if (arrSplit[1] != null) {
                    strAllParam = arrSplit[1];
                }
            }
        }
        return strAllParam;
    }

    /**
     * 解析出url参数中的键值对
     * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     *
     * @param strParam url地址
     * @return url请求参数部分
     */
    public static Map<String, String> getUrlParamMap(String strParam) {
        Map<String, String> mapRequest = new HashMap<String, String>();
        String[] arrSplit = null;
        String strUrlParam = getUrlParam(strParam);
        if (strUrlParam == null) {
            return mapRequest;
        }
        //每个键值为一组
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");
            //解析出键值
            if (arrSplitEqual.length > 1) {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
            } else {
                if (arrSplitEqual[0] != "") {
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    /**
     * 根据消息的内容的长度返回消息的倒计时时间
     * 规则：18字以内10秒消失，18字以后，没增加2个字，多加1秒。
     *
     * @param length 消息体是内容长度
     * @return
     */
    public static int getTimeByContentLength(int length) {
        int time = 11;
        if (length <= 18) {
            time = 11;
        } else {
            int i = length - 18;
            int i1 = i / 2;
            time = 11 + i1;
        }
        return time;
    }

    /**
     * 根据字符串返回l  g值的时间
     *
     * @param strTime
     * @return
     */
    public static long getStringTime(String strTime) {
        final SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long gr_creattime;
        try {
            gr_creattime = sd.parse(strTime).getTime() / 1000;
            return gr_creattime;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }

    }


    //分割逗号字符串成数组
    public static String[] convertStrToArray(String str) {
        String[] strArray = null;
        strArray = str.split(",");
        return strArray;
    }

    /**
     * 将误差转变为d日h时m分s秒格式
     *
     * @param lTime
     * @return
     */
    public static String setLtime(long lTime) {
        long mTime = Math.abs(lTime);
        String str;
        long d = mTime / (1000 * 60 * 60 * 24);
        long h = mTime % (1000 * 60 * 60 * 24) / (1000 * 60 * 60);
        long m = mTime % (1000 * 60 * 60 * 24) % (1000 * 60 * 60) / (1000 * 60);
        long s = mTime % (1000 * 60 * 60 * 24) % (1000 * 60 * 60) % (1000 * 60) / 1000;
        if (d != 0) {
            if (lTime > 0) {
                str = String.format("  快%d日%d时%02d分%02d秒", d, h, m, s);
            } else {
                str = String.format("  慢%d日%d时%02d分%02d秒", d, h, m, s);
            }
        } else if (h != 0) {
            if (lTime > 0) {
                str = String.format("  快%d时%02d分%02d秒", h, m, s);
            } else {
                str = String.format("  慢%d时%02d分%02d秒", h, m, s);
            }
        } else if (h == 0 && m != 0) {
            if (lTime > 0) {
                str = String.format("  快%02d分%02d秒", m, s);
            } else {
                str = String.format("  慢%02d分%02d秒", m, s);
            }
        } else if (h == 0 && m == 0 && s != 0) {
            if (lTime > 0) {
                str = String.format("  快%02d秒", s);
            } else {
                str = String.format("  慢%02d秒", s);
            }
        } else {
            str = "  (0\")";
        }
        return str;
    }

    /**
     * 替换null
     * @param content 内容
     * @return
     */
    public static String replaceEmpty(String content){
        return TextUtils.isEmpty(content) ? "" : content;
    }

}
