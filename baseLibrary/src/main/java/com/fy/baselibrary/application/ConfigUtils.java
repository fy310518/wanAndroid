package com.fy.baselibrary.application;

import android.content.Context;

/**
 * 应用框架基础配置工具类 （应用启动时候初始化）
 * Created by fangs on 2018/7/13.
 */
public class ConfigUtils {

    static ConfigComponent configComponent;

    public ConfigUtils(ConfigBiuder biuder) {
        configComponent = DaggerConfigComponent.builder()
                .configModule(new ConfigModule(biuder.ctx, biuder.BASE_URL))
                .build();
    }

    public static Context getAppCtx() {
        return configComponent.getContext();
    }

    public static String getBaseUrl() {
        return configComponent.getBaseUrl();
    }




    public static class ConfigBiuder {
        /** 应用 环境 */
        Context ctx;

        /** 网络请求 服务器地址 url */
        String BASE_URL = "";

        public ConfigBiuder setCtx(Context ctx) {
            this.ctx = ctx;
            return this;
        }

        public ConfigBiuder setBASE_URL(String BASE_URL) {
            this.BASE_URL = BASE_URL;
            return this;
        }



        public ConfigUtils create(){
            return new ConfigUtils(this);
        }
    }
}
