package com.gcstorage.circle.bean;

import java.io.Serializable;

/**
 * DESCRIPTION：上传文件 返回对象实体类
 * Created by fangs on 2019/3/18 18:15.
 */
public class UploadBean implements Serializable {

    /**
     * url : http://112.74.129.54:13201/tax00/M00/02/02/QUIPAFyPb4WAD2gEAAK--HbfA90182.jpg
     * height : 2560
     * md5 : 492f34c2ce2121cf69b0b39009436291
     * width : 1440
     */

    private String url = "";
    private String md5  = "";
    private String height  = "0";
    private String width  = "0";

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
