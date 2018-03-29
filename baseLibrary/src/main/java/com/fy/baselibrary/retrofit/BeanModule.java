package com.fy.baselibrary.retrofit;

import java.io.Serializable;

/**
 * 网络请求 返回数据 格式化对象
 * Created by fangs on 2017/11/6.
 */
public class BeanModule<T> implements Serializable{
    /**
     * msg : 操作成功
     * code : 0
     * rows : {"token":"3011bbfa26bea40ef490d281e7197282"}
     */

    private String msg = "";
    private int code = -1;
    private T rows;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getRows() {
        return rows;
    }

    public void setRows(T rows) {
        this.rows = rows;
    }

    /**
     * 判断请求是否成功
     * @return
     */
    public boolean isSuccess(){
        return code == 0 || Status == 0;
    }

    @Override
    public String toString() {
        return "BeanModule{" +
                "msg='" + Msg + '\'' +
                ", code=" + code +
                ", rows=" + rows +
                '}';
    }

    private int Total;
    private int Status = -1;
    private String Msg = "";
    private boolean Result;
    private T Data;

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public String getmMsg() {
        return Msg;
    }

    public void setmMsg(String msg) {
        this.Msg = msg;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public boolean isResult() {
        return Result;
    }

    public void setResult(boolean result) {
        Result = result;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }
}
