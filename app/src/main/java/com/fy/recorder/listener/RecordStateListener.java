package com.fy.recorder.listener;

import com.fy.recorder.RecordHelper;

/**
 * Created by fangs on 2020/2/17 13:54..
 */
public interface RecordStateListener {

    /**
     * 当前的录音状态发生变化
     *
     * @param state 当前状态
     */
    void onStateChange(RecordHelper.RecordState state);

    /**
     * 录音错误
     *
     * @param error 错误
     */
    void onError(String error);

}
