package com.gcstorage.map.maker;

import java.io.Serializable;

public class MarkerBean implements Serializable {
    public String taskId;  //任务ID
    public String name;    //用户名字
    public String alarm;   //警号
    public String markeraddress;  //地址
    public int susType;  //标记类型

    static class Address {
        public double longitude;
        public double latitude;
        public String addre;
    }
}
