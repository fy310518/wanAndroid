package com.gcstorage.framework.net;

/**
 * 文件上传后返回结果中response中的数据
 * Created by zjs on 2018/3/16.
 */

public class UploadBean {
    private String url;
    private String width;
    private String height;
    private String md5;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
