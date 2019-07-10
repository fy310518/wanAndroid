package com.gcstorage.parkinggather;

/**
 * DESCRIPTION：驻车采集 常量类
 * Created by fangs on 2019/7/6 15:31.
 */
public class Constant {
    public static double sLongitude = 0.0; // 经度
    public static double sLatitude = 0.0; // 纬度
    public static String address = "未获取到位置信息";
    public static String code = "-1"; // - 1 表示没有获取到经纬度




    /** spf 缓存文件 名 */
    public static final String baseSpf = "spfCache";

    /** 用户是否登录 key */
    public static final String isLogin = "is_Login";


    /** 登录用户 id */
    public static final String userId = "user_Id";
    /** 登录用户 身份证号码 */
    public static final String userIdCard = "user_Id_Card";

    /** 登录用户 账号 */
    public static final String userAccount = "user_Account_Number";
    /** 登录用户 用户名 */
    public static final String userName = "user_Name";

    /** 登录用户 头像 url */
    public static final String userImg = "user_Head_Img";

    /** 登录用户 警号 */
    public static final String userAlarm = "user_Alarm";
    /** 用户 部门 */
    public static final String userDepart = "user_depart";
    /** 用户 部门Id */
    public static final String userDepartId = "user_depart_Id";


    /** 登录 返回 token */
    public static final String token = "user_token";


    /** 其它应用跳转到 本应用的 应用id */
    public static final String otherAppID = "otherAppID";





    /**
     * 聊天通知渠道 key
     */
    public static final String chatNumKEY = "chatNumKEY";

    /**
     * app 声音 是否打开
     */
    public static final String voiceKEY = "appVoiceKEY";
    /**
     * app 震动 是否打开
     */
    public static final String shockKEY = "appShockKEY";

    /**
     * app 位置共享开关 是否打开
     */
    public static final String addressKEY = "appaddressKEY";
}
