package com.gcstorage.circle.request;

import com.fy.baselibrary.retrofit.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 网络请求 返回数据 格式化对象
 * Created by fangs on 2017/11/6.
 */
public class BeanModule<T> implements BaseBean<T>, Serializable{


    /**
     * resultcode : 0
     * response : ""
     * resultmessage : 成功
     */

    private String resultcode = "0";
    private String resultmessage = "";
    private T response;

    /**
     * errorCode如果为负数则认为错误，此时errorMsg会包含错误信息
     * @return
     */
    @Override
    public boolean isSuccess(){
        int code = Integer.parseInt(resultcode);
        return code == 0;
    }

    @Override
    public int getCode() {
        return Integer.parseInt(resultcode);
    }

    @Override
    public String getMsg() {
        return resultmessage;
    }

    @Override
    public T getData() {
        return response;
    }



    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getResultmessage() {
        return resultmessage;
    }

    public void setResultmessage(String resultmessage) {
        this.resultmessage = resultmessage;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "BeanModule{" +
                "resultcode='" + resultcode + '\'' +
                ", resultmessage='" + resultmessage + '\'' +
                ", response=" + response +
                '}';
    }
}
