/*
 * Copyright (c) 深圳绿之云
 */

package com.gcstorage.framework.net.listener;

/**
 * @Author 郑胜
 * @Date 2015/11/15
 * @Version 1.0
 */
public interface ActionCallbackListener<T> {
    /**
     * 成功时调用
     *
     * @param data 返回的数据
     */
    void onSuccess(T data) throws Exception;

    /**
     * 失败时调用
     *
     * @param errorEvent 错误码
     * @param message    错误信息
     */
    void onFailure(String errorEvent, String message);
}
