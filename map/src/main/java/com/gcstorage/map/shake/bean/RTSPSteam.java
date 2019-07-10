package com.gcstorage.map.shake.bean;

/**
 * 视频流bean类
 */
public class RTSPSteam {

    private String path;//视频流地址
    private String channelId;//摄像头id

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
