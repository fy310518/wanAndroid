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
     * 动态服务器地址
     */
    public static String custom_Url = "";


    /**
     * 所有下载任务 缓存key
     */
    public static final String AllDownTask = "All_Task_DownLoad";

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
     * 操作令牌 key
     */
    public static String token = "user_token";

    /**
     * 程序是否必须登录
     */
    public static boolean isMustAppLogin = false;

    /**
     * 程序是否需要横竖屏切换
     */
    public static boolean isOrientation = false;

    /**
     * 缓存用户名 key
     */
    public static final String SP_KEY_OPENID = "open_id";
    public static final String SP_KEY_ID = "user_id";
    public static final String SP_KEY_ALARM = "user_alarm";//超警 警号
    public static final String SP_KEY_AppALARM = "Appuser_alarm";//门户 警号
    public static final String userName = "User_Name";
    public static final String SP_KEY_TOKEN = "user_token";
    public static final String SP_KEY_HEAD_PIC = "user_head_pic";
    public static final String SP_KEY_DEPT_NAME = "user_dept_name";
    public static final String SP_KEY_DEPT_ID = "user_dept_id";
    public static final String SP_KEY_SEX = "user_sex";
    public static final String SP_KEY_AGE = "user_age";
    public static final String SP_KEY_ID_CARD = "user_id_card";
    public static final String SP_KEY_LOGIN = "user_login_state";
    public static final String SP_KEY_PHONE= "user_phone";


    /**
     * APP 当前模式 （日间/夜间）
     */
    public static final String appMode = "appModeSwitch";

    /**
     * baseLibrary SharedPreferences 文件名
     */
    public static final String baseSpf = "baseSpf";


    /**
     * 吸附 ViewType
     */
    public static final int StickyType = 58;

    /**
     * 进入 搜索 界面 传递搜索关键字的 key
     */
    public static final String queryKey = "QueryKey";


//  多状态视图 常量 flag
    /** 内容 flag */
    public static final int LAYOUT_CONTENT_ID = 0;

    /** 异常 flag */
    public static final int LAYOUT_ERROR_ID = 1;

    /** 网络异常 flag */
    public static final int LAYOUT_NETWORK_ERROR_ID = 2;

    /** 空数据 flag */
    public static final int LAYOUT_EMPTYDATA_ID = 3;

    /** 请求失败 标记 */
    public static final int REQUEST_FAIL = 1006;
//  多状态视图 常量 flag


// Activity life Events
    public static final String CREATE = "CREATE";
    public static final String START = "START";
    public static final String RESUME = "RESUME";
    public static final String PAUSE = "PAUSE";
    public static final String STOP = "STOP";
    public static final String DESTROY = "DESTROY";

// Fragment life  Events
    public static final String ATTACH = "ATTACH";
    public static final String CREATE_VIEW = "CREATE_VIEW";
    public static final String DESTROY_VIEW = "DESTROY_VIEW";
    public static final String DETACH = "DETACH";

}
