package com.fy.baselibrary.application;

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

    public static int getLoadImg(){
        return configComponent.getConfigBiuder().loadImg;
    }

    public static int getActHead(){
        return configComponent.getConfigBiuder().activity_head;
    }


    public static class ConfigBiuder {

        /** 网络请求 服务器地址 url */
        String BASE_URL = "";

        /** https 公钥证书字符串 */
        String cer = "";

        /** 闪屏页图片 id */
        int loadImg;

        /** 自定义标题栏 */
        int activity_head;

        public ConfigBiuder setBASE_URL(String BASE_URL) {
            this.BASE_URL = BASE_URL;
            return this;
        }

        public ConfigBiuder setCer(String cer) {
            this.cer = cer;
            return this;
        }

        public ConfigBiuder setLoadImg(int loadImg) {
            this.loadImg = loadImg;
            return this;
        }

        public ConfigBiuder setActivity_head(int activity_head) {
            this.activity_head = activity_head;
            return this;
        }

        public ConfigUtils create(Context context){
            return new ConfigUtils(context, this);
        }
    }
}
