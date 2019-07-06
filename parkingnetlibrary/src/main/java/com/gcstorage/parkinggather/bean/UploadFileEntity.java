package com.gcstorage.parkinggather.bean;

/**
 * DESCRIPTION：上传多文件 实体类
 * Created by fangs on 2019/7/6 11:03.
 */
public class UploadFileEntity {

    /**
     * url : http://47.107.134.212:13201/tax00/M00/02/05/QUIPAF0gCEOAdhAtAAFBSQ1MYTw643.jpg
     * height : 1536
     * md5 : 3431f5cb2432a9dbf832a8d46f839600
     * width : 864
     */

    private String url = "";
    private String height = "";
    private String md5 = "";
    private String width = "";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }


}
