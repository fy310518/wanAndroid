package com.fy.recorder.listener;

/**
 * Created by fangs on 2020/2/17 13:54.
 */
public interface RecordFftDataListener {

    /**
     * @param data 录音可视化数据，即傅里叶转换后的数据：fftData
     */
    void onFftData(byte[] data);

}
