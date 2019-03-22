package com.gcstorage.scanface.request;

import com.fy.baselibrary.retrofit.BaseBean;

/**
 * 网络请求 返回数据 格式化对象
 * Created by fangs on 2017/11/6.
 */
public class BeanModule<T> implements BaseBean<T>{


    /**
     * resultcode : 0
     * response : ""
     * resultmessage : 成功
     */

    private String code = "";
    private String message = "";
    private T response;

    /**
     * errorCode如果为负数则认为错误，此时errorMsg会包含错误信息
     * @return
     */
    @Override
    public boolean isSuccess(){
        return code.equals("0");
    }

    @Override
    public int getCode() {
        return Integer.parseInt(code);
    }

    @Override
    public String getMsg() {
        return message;
    }

    @Override
    public T getData() {
        return response;
    }


    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}
