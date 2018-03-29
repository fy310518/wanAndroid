package com.fy.baselibrary.utils;

/**
 * 常量
 * Created by fangs on 2017/5/8.
 */
public class ConstantUtils {

    /**
     * 默认的超时时间
     */
    public static int DEFAULT_MILLISECONDS = 60000;

    /**
     * 服务器地址
     */
    public static String BASE_URL = "http://192.168.100.158:80/";    //本地服务器 地址
//  public static String BASE_URL = "http://192.168.100.120:8080/";

    /**
     * 图片 地址 (可选)
     */
    public static String IMG_BASE_URL = BASE_URL + "image";

    /**
     * 动态服务器地址
     */
    public static String custom_Url = "";

    /**
     * 用户id
     */
    public static String userId = "userid";

    /**
     * 操作令牌
     */
    public static String token = "";

    /**
     * student实体类中id
     */
    public static int studentID = 0;

    /**
     * 当前个人信息图片上传 key
     */
    public static String selectvisitimager = "selectvisitimager";

    /**
     * 学生实体类 缓存 key
     */
    public static final String student = "stuInfo";

    /**
     * 学生头像
     */
    public static String head_portrait = "";





    /**
     * APP 当前模式 （日间/夜间）
     */
    public static final String appMode = "appModeSwitch";

    /**
     * 用户是否 第一次打开APP
     */
    public static final String isfirstOpenApp = "userIsFirstOpenApp";







}
