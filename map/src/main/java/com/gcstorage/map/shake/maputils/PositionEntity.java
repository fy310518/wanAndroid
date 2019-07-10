package com.gcstorage.map.shake.maputils;


/**
 * Function: 封装的关于位置的实体,
 * 将高德或者立得定位的信息,转换成我们定义的实体类
 */
public class PositionEntity {

    public double latitue;

    public double longitude;
    public String province;    //获取省的名称。
    public String district;
    public String street;
    public String road;
    public String poiName;
    public String aoiName;
    public String number;
    public String isjiankong;  //如果从监控页面进入 :0

    public String city;
    public String address; //高德帮忙合成的地址
    public String customAddress; //自己根省市区街道拼接成的


    public double getLatitue() {
        return latitue;
    }

    public void setLatitue(double latitue) {
        this.latitue = latitue;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public PositionEntity() {
    }

    public PositionEntity(double latitude, double longtitude, String address, String city) {
        this.latitue = latitude;
        this.longitude = longtitude;
        this.address = address;
    }

    @Override
    public String toString() {
        return "PositionEntity{" +
                "latitue=" + latitue +
                ", longitude=" + longitude +
                ", province='" + province + '\'' +
                ", district='" + district + '\'' +
                ", street='" + street + '\'' +
                ", road='" + road + '\'' +
                ", poiName='" + poiName + '\'' +
                ", aoiName='" + aoiName + '\'' +
                ", number='" + number + '\'' +
                ", isjiankong='" + isjiankong + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", customAddress='" + customAddress + '\'' +
                '}';
    }
}