package com.fy.baselibrary.application;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * 提供依赖对象
 * Created by fangs on 2018/7/13.
 */
@Module
public class ConfigModule {

    private Context context;
    private String  url;

    public ConfigModule(Context context, String url) {
        this.context = context;
        this.url     = url;
    }

    @Singleton
    @Provides
    public Context provideContext() {
        return context;
    }

    @Singleton
    @Provides
    public String provideUrl(){
        return url;
    }

}
