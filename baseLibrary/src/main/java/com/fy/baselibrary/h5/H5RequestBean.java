package com.fy.baselibrary.h5;

import android.text.TextUtils;
import android.util.ArrayMap;

/**
 * DESCRIPTION：h5 android 交互 test
 * Created by fangs on 2019/3/27 17:03.
 */
public class H5RequestBean {

    /**
     * url :
     * requestMethod : GET
     * header : {"Content-Type":"application/x-www-form-urlencoded","access_token":""}
     * params : {"param1":"value1","param2":"value2","param3":"value3","param4":"value4"}
     * jsMethod :
     */

    private String url = "";
    private String requestMethod = "";
    private ArrayMap<String,Object> header;
    private ArrayMap<String,Object> params;
    private String jsMethod = "";

    public String getUrl() {
        return url == null ? "" : url;
    }

    public void setUrl(String url) {
        this.url = url == null ? "" : url;
    }

    public String getRequestMethod() {
        return requestMethod == null ? "" : requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod == null ? "" : requestMethod;
    }

    public ArrayMap<String, Object> getHeader() {
        return null == header ? new ArrayMap<>() : header;
    }

    public void setHeader(ArrayMap<String, Object> header) {
        this.header = header;
    }

    public ArrayMap<String, Object> getParams() {
        return null == params ? new ArrayMap<>() : params;
    }

    public void setParams(ArrayMap<String, Object> params) {
        this.params = params;
    }

    public String getJsMethod() {
        return jsMethod == null ? "" : jsMethod;
    }

    public void setJsMethod(String jsMethod) {
        this.jsMethod = jsMethod == null ? "" : jsMethod;
    }

    @Override
    public String toString() {
        return "H5RequestBean{" +
                "url='" + url + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                ", header=" + header +
                ", params=" + params +
                ", jsMethod='" + jsMethod + '\'' +
                '}';
    }
}
