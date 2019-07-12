/*
 * Copyright (c) 深圳绿之云
 */

package com.gcstorage.framework.net.basebean;

import java.io.Serializable;

/**
 * @Author 郑胜
 * @Date 2015/11/15
 * @Version 1.0
 */
public class ApiResponse<T> implements Serializable {
    private String resultcode;    // 返回码，0为成功
    private String resultmessage; // 返回信息
    private T response;           // 成功后返回的数据

    public ApiResponse(String event, String msg) {
        this.resultcode = event;
        this.resultmessage = msg;
    }

    public boolean isSuccess() {
        return "200".equals(resultcode) || "0".equals(resultcode) || "20000".equals(resultcode);
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
        return "ApiResponse{" +
                "resultcode='" + resultcode + '\'' +
                ", resultmessage='" + resultmessage + '\'' +
                ", response=" + response +
                '}';
    }
}
