package com.fy.baselibrary.utils;

/**
 * 常量
 * Created by fangs on 2017/5/8.
 */
public class Constant {

    /**
     * 默认的超时时间
     */
    public static int DEFAULT_MILLISECONDS = 60000;

    /**
     * 服务器地址
     */
    public static String BASE_URL = "http://www.wanandroid.com/";

    /**
     * 动态服务器地址
     */
    public static String custom_Url = "";


    /**
     * 文件下载 默认下载线程数
     */
    public static final int THREAD_COUNT = 3;

    /**
     * 一个下载任务 已经下载的进度百分比 数值
     */
    public static final String DownPercent = "Task_DownLoad_Percent";

    /**
     * 一个下载任务 已经下载的总长度
     */
    public static final String DownTask = "Task_DownLoad_length";

    /**
     * 一个线程 下载数据的长度
     */
    public static final String DownTherad = "Therad_DownLoad_length";



    /**
     * 用户id
     */
    public static String userId = "userid";

    /**
     * 操作令牌
     */
    public static String token = "";

    /**
     * 用户是否登录 key
     */
    public static String isLogin = "is_Login";

    /**
     * 缓存用户名 key
     */
    public static String userName = "User_Name";




    /**
     * APP 当前模式 （日间/夜间）
     */
    public static final String appMode = "appModeSwitch";

    /**
     * 用户是否 第一次打开APP
     */
    public static final String isfirstOpenApp = "userIsFirstOpenApp";


    /**
     * 吸附 ViewType
     */
    public static final int StickyType = 58;

    /**
     * 进入 搜索 界面 传递搜索关键字的 key
     */
    public static final String queryKey = "QueryKey";

}