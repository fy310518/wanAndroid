package com.fy.baselibrary.retrofit.load.down;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 下载请求数据 实体类
 * Created by fangs on 2018/5/31.
 */
public class DownInfo implements Serializable, Cloneable {

    /**
     * 定义下载状态码 开始，正在下载，暂停，取消 ，错误，完成
     */
    public static final int STATUS_NOT_DOWNLOAD = 0;
    public static final int STATUS_DOWNLOADING = 1;
    public static final int STATUS_PAUSED = 2;
    public static final int STATUS_CANCEL = 3;
    public static final int STATUS_DOWNLOAD_ERROR = 4;
    public static final int STATUS_COMPLETE = 5;


    private String name;
    private String imageUrl;

    /** 载 url */
    private String url;
    /** 文件总长度 */
    private long countLength;
    /** 已经下载的 总长度 */
    private AtomicLong readLength = new AtomicLong();
    /**
     * 进度百分比 数
     */
    private double percent;

    private int stateInte = STATUS_NOT_DOWNLOAD;

    public DownInfo() {}

    public DownInfo(@NonNull String url) {
        this.url = url;
    }

    public DownInfo(String name, String imageUrl, String url) {
        this.name = name;
        this.imageUrl = imageUrl;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DownInfo downInfo = (DownInfo) o;

        return url.equals(downInfo.url);
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }

    @Override
    protected Object clone() {
        DownInfo downInfo = null;

        try {
            downInfo = (DownInfo) super.clone();
            downInfo.setReadLength(getReadLength());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return downInfo;
    }
}
