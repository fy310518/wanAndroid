package com.fy.baselibrary.h5;

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
    private ArrayMap<String,String> header;
    private ArrayMap<String,String> params;
    private String jsMethod = "";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public ArrayMap<String, String> getHeader() {
        return header;
    }

    public void setHeader(ArrayMap<String, String> header) {
        if (null == header || header.isEmpty()){
            this.header = new ArrayMap<>();
        } else {
            this.header = header;
        }
    }

    public ArrayMap<String, String> getParams() {
        return params;
    }

    public void setParams(ArrayMap<String, String> params) {
        if (null == params || params.isEmpty()){
            this.params = new ArrayMap<>();
        } else {
            this.params = params;
        }
    }

    public String getJsMethod() {
        return jsMethod;
    }

    public void setJsMethod(String jsMethod) {
        this.jsMethod = jsMethod;
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
