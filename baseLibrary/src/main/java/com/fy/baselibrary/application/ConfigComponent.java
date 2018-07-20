package com.fy.baselibrary.application;

import android.content.Context;
import javax.inject.Singleton;
import dagger.Component;

/**
 * 作为桥梁，沟通调用者和依赖对象库
 * Created by fangs on 2018/7/13.
 */
@Singleton
@Component(modules = ConfigModule.class)
public interface ConfigComponent {

    Context getContext();

    ConfigUtils.ConfigBiuder  getConfigBiuder();
}
