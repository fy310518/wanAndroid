package com.fy.wanandroid.request;

import com.fy.baselibrary.retrofit.BaseBean;

import java.io.Serializable;

/**
 * 网络请求 返回数据 格式化对象
 * Created by fangs on 2017/11/6.
 */
public class BeanModule<T> implements BaseBean<T>, Serializable{

    /**
     * data : {}
     * errorCode : 0
     * errorMsg :
     */

    private T data;
    private int errorCode;
    private String errorMsg;

    private int code;
    private String msg;


    public void setData(T data) {
        this.data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }


    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "BeanModule{" +
                "data=" + data +
                ", errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }

    /**
     * errorCode如果为负数则认为错误，此时errorMsg会包含错误信息
     * @return
     */
    @Override
    public boolean isSuccess(){
        return errorCode >= 0 || code == 200;
    }

    @Override
    public int getCode() {
        return errorCode;
    }

    @Override
    public String getMsg() {
        return errorMsg;
    }

    @Override
    public T getData() {
        return data;
    }
}
