package com.fy.baselibrary.application;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;

/**
 * activity 实现接口 统一规范
 * 项目自己新建的 activity 建议实现 此 接口
 * Created by fangs on 2018/3/13.
 */
public interface IBaseActivity {

    /**
     * 是否显示 头部 标题栏
     * @return
     */
    boolean isShowHeadView();

    /**
     * 设置Activity 界面布局文件
     * @return
     */
    int setView();

    /**
     * 设置当前 activity 状态栏导航栏
     */
    void setStatusBar(Activity activity);

    /**
     * 初始化Activity 数据
     */
    void initData(Activity activity, Bundle savedInstanceState);

}
