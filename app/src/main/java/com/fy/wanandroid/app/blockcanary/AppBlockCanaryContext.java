package com.fy.wanandroid.app.blockcanary;

import com.fy.baselibrary.BuildConfig;
import com.github.moduth.blockcanary.BlockCanaryContext;

/**
 * 检测 APP UI卡顿
 * describe：监控自己的上下文
 * Created by fangs on 2018/12/4 10:23.
 */
public class AppBlockCanaryContext extends BlockCanaryContext {
    //设置卡顿判断的阙值
    public int getConfigBlockThreshold() {
        return 500;
    }

    //是否需要显示卡顿的信息
    public boolean isNeedDisplay() {
        return BuildConfig.DEBUG;
    }

    //设置log保存在sd卡的目录位置
    public String getLogPath() {
        return "/blockcanary/performance";
    }
}
