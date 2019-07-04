package com.gcstorage.parkinggather.request;

import android.text.TextUtils;

import com.fy.baselibrary.retrofit.BaseBean;

import java.io.Serializable;

/**
 * 网络请求 返回数据 格式化对象
 * Created by fangs on 2017/11/6.
 */
public class BeanModule<T> implements BaseBean<T>, Serializable{

    /**
     * resultcode : 20000
     * resultmessage : 处理成功
     * response : {"data":[{"date":"2019-02-20","time":"15:15:39","extend":false,"id":"9","userId":"420115199410109818","name":"郑家双","pic":"asdfasdfasdf","carImg":"http://47.107.134.212:10004/2d80790417fe428d9a664d7c966efaa6.jpg","carNum":"鄂AD6850","carColor":"蓝","longitude":"114.236481","latitude":"30.622878","address":"江旺路10号","type":"2"},{"date":"2019-02-19","time":"16:50:59","extend":false,"id":"8","userId":"45120","name":"刘","pic":"http://120.24.1.46:13201/tax00/M00/00/04/QUIPAFxqW4GAM0-KAAEMgcj9bAw108.jpg","carImg":"13212","carNum":"鄂A45124","carColor":"蓝色","longitude":"151.154","latitude":"68.151","address":"火凤凰","type":"2"},{"date":"2019-02-18","time":"17:13:11","extend":false,"id":"7","userId":"420115199410109818","name":"郑家双","pic":"asdfasdfasdf","carImg":"http://47.107.134.212:10004/9731e8aece034503bd2009be50c44726.jpg","carNum":"苏A2396V","carColor":"蓝","longitude":"114.236809","latitude":"30.623034","address":"发展一路6号","type":"2"},{"date":"2019-02-18","time":"15:34:04","extend":false,"id":"6","userId":"45120","name":"刘","pic":"http://120.24.1.46:13201/tax00/M00/00/04/QUIPAFxqW4GAM0-KAAEMgcj9bAw108.jpg","carImg":"http://120.24.1.46:13201/tax00/M00/00/04/QUIPAFxqW4GAM0-KAAEMgcj9bAw108.jpg","carNum":"鄂A45124","carColor":"蓝色","longitude":"151.154","latitude":"68.151","address":"火凤凰","type":"2"},{"date":"2019-01-05","time":"16:28:32","extend":false,"id":"1","userId":"q1","name":"123","pic":"http://120.24.1.46:13201/tax00/M00/00/04/QUIPAFxqW4GAM0-KAAEMgcj9bAw108.jpg","carImg":"http://120.24.1.46:13201/tax00/M00/00/04/QUIPAFxqW4GAM0-KAAEMgcj9bAw108.jpg","carNum":"鄂A990N2","carColor":"黑色","longitude":"10.32515","latitude":"30.15151","address":"武汉市解放大道351号","type":"2"}],"totalPage":1,"pageNum":1,"dataNum":5}
     */

    private String resultcode;
    private String resultmessage;
    private T response;

    /**
     * errorCode 如果为负数则认为错误，此时errorMsg会包含错误信息
     * @return
     */
    @Override
    public boolean isSuccess(){
        return !TextUtils.isEmpty(resultcode) && resultcode.equals("20000");
    }

    @Override
    public int getCode() {
        return !TextUtils.isEmpty(resultcode) ? Integer.parseInt(resultcode) : -1;
    }

    @Override
    public String getMsg() {
        return resultmessage;
    }

    @Override
    public T getData() {
        return response;
    }
}
