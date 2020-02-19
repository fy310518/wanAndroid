package com.fy.recorder.listener;

/**
 * Created by fangs on 2020/2/17 13:54.
 */
public interface RecordSoundSizeListener {

    /**
     * 实时返回音量大小
     *
     * @param soundSize 当前音量大小
     */
    void onSoundSize(int soundSize);

}
