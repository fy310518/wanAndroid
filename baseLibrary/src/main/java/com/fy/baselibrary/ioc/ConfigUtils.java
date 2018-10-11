package com.fy.baselibrary.ioc;

import android.content.Context;


/**
 * 应用框架基础配置工具类 （应用启动时候初始化）
 * Created by fangs on 2018/7/13.
 */
public class ConfigUtils {

    static ConfigComponent configComponent;

    public ConfigUtils(Context context, ConfigBiuder biuder) {
        configComponent = DaggerConfigComponent.builder()
                .configModule(new ConfigModule(context, biuder))
                .build();
    }

    public static Context getAppCtx() {
        return configComponent.getContext();
    }

    public static String getBaseUrl() {
        return configComponent.getConfigBiuder().BASE_URL;
    }

    public static String getCer() {
        return configComponent.getConfigBiuder().cer;
    }

    public static int getTitleColor(){
        return configComponent.getConfigBiuder().titleColor;
    }

    public static int getBgColor(){
        return configComponent.getConfigBiuder().bgColor;
    }

    public static boolean isTitleCenter(){
        return configComponent.getConfigBiuder().isTitleCenter;
    }

    public static int getBackImg(){
        return configComponent.getConfigBiuder().backImg;
    }


    public static class ConfigBiuder {

        /** 网络请求 服务器地址 url */
        String BASE_URL = "";

        /** https 公钥证书字符串 */
        String cer = "";

        /** 标题栏背景色 */
        int bgColor;
        /** 标题是否居中 */
        boolean isTitleCenter;
        /** 标题字体颜色 */
        int titleColor;

        /** 标题栏返回按钮 图片 */
        int backImg;

        public ConfigBiuder setBASE_URL(String BASE_URL) {
            this.BASE_URL = BASE_URL;
            return this;
        }

        public ConfigBiuder setCer(String cer) {
            this.cer = cer;
            return this;
        }

        public ConfigBiuder setBgColor(int bgColor) {
            this.bgColor = bgColor;
            return this;
        }

        public ConfigBiuder setTitleColor(int titleColor) {
            this.titleColor = titleColor;
            return this;
        }

        public ConfigBiuder setTitleCenter(boolean titleCenter) {
            isTitleCenter = titleCenter;
            return this;
        }

        public ConfigBiuder setBackImg(int backImg) {
            this.backImg = backImg;
            return this;
        }


        public ConfigUtils create(Context context){
            return new ConfigUtils(context, this);
        }
    }
}
