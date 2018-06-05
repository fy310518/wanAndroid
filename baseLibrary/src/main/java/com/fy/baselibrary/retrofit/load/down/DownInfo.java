package com.fy.baselibrary.retrofit.load.down;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 下载请求数据 实体类
 * Created by fangs on 2018/5/31.
 */
public class DownInfo implements Serializable {

    /**
     * 定义下载状态码 开始，正在下载，暂停，取消 ，错误，完成
     */
    public static final int START = 0;
    public static final int DOWN = 1;
    public static final int PAUSE = 2;
    public static final int CANCEL = 3;
    public static final int ERROR = 4;
    public static final int FINISH = 5;


    /**
     * 下载 url
     */
    @NonNull
    private String url;
    /**
     * 文件总长度
     */
    private long countLength;
    /**
     * 已经下载的 总长度
     */
    private AtomicLong readLength = new AtomicLong();
    /**
     * 进度百分比 数
     */
    private double percent;

    private int stateInte = START;

    public DownInfo() {}

    public DownInfo(@NonNull String url) {
        this.url = url;
    }

    public long getCountLength() {
        return countLength;
    }

    public void setCountLength(long countLength) {
        this.countLength = countLength;
    }

    public AtomicLong getReadLength() {
        return readLength;
    }

    public void setReadLength(AtomicLong readLength) {
        this.readLength = readLength;
    }

    public int getStateInte() {
        return stateInte;
    }

    public void setStateInte(int stateInte) {
        this.stateInte = stateInte;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    @Override
    public String toString() {
        return "DownInfo{" +
                "url='" + url + '\'' +
                ", countLength=" + countLength +
                ", readLength=" + readLength +
                ", percent=" + percent +
                ", stateInte=" + stateInte +
                '}';
    }
}
