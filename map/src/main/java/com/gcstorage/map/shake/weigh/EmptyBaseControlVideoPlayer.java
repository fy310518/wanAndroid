package com.gcstorage.map.shake.weigh;

import android.content.Context;
import android.util.AttributeSet;

import com.jeff.map.R;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

/**
 * Created by Administrator on 2019/4/13 0013.
 */


public class EmptyBaseControlVideoPlayer extends StandardGSYVideoPlayer {
    public EmptyBaseControlVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public EmptyBaseControlVideoPlayer(Context context) {
        super(context);
    }

    public EmptyBaseControlVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.empty_base_control_view;
    }

    @Override
    protected void touchDoubleUp() {
        //super.touchDoubleUp();
        //不需要双击暂停
    }
}