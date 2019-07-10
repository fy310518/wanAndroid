package com.gcstorage.map.internetplus;

import android.os.Parcel;
import android.os.Parcelable;

public class InternetPlusBean implements Parcelable{
    //根据经纬度获取摄像头
    /**
     * id : 72057600538540943
     * deviceName : testupdate
     * cid : 538540943
     * deviceType : 100604
     * manufacturerDeviceType : 103401
     * manufacturerDeviceId : mixed
     * longitude : 114.237336
     * latitude : 30.621647
     * deviceStatus : 1
     * placeId : 1204403050070000001
     */

    private String id;          //摄像头id
    private String deviceName;  //摄像名字
    private String cid;
    private String deviceType;  //设备类型
    private String manufacturerDeviceType;
    private String manufacturerDeviceId;
    private String longitude;
    private String latitude;
    private String deviceStatus;
    private String placeId;
    //获取摄像头的流
    /**
     * beginTime : 1561107130
     * endTime : 1561108930
     * url : https://jxsr-oss1.antelopecloud.cn/records/m3u8_info2/1561107130_1561108930.m3u8?access_token=538444858_3356491776_1590646346_5c32d9bb45d1816f2f5ef3793df125dd&head=1
     */

    private String beginTime;
    private String endTime;
    private String url;

    //查询摄像头列表
    /**
     * userId : 123
     * userName : 董文波
     * address : 火凤凰云基地
     * pic : http://120.24.1.46:13201/tax00/M00/01/02/QUIPAFybHxyAY2ZiAAAPGmwh0js628.png
     * createTime : 2019-06-24 11:27:17
     */

    private String userId;
    private String userName;
    private String address;
    private String pic;
    private String createTime;


    protected InternetPlusBean(Parcel in) {
        id = in.readString();
        deviceName = in.readString();
        cid = in.readString();
        deviceType = in.readString();
        manufacturerDeviceType = in.readString();
        manufacturerDeviceId = in.readString();
        longitude = in.readString();
        latitude = in.readString();
        deviceStatus = in.readString();
        placeId = in.readString();
        beginTime = in.readString();
        endTime = in.readString();
        url = in.readString();
        userId = in.readString();
        userName = in.readString();
        address = in.readString();
        pic = in.readString();
        createTime = in.readString();
    }

    public static final Creator<InternetPlusBean> CREATOR = new Creator<InternetPlusBean>() {
        @Override
        public InternetPlusBean createFromParcel(Parcel in) {
            return new InternetPlusBean(in);
        }

        @Override
        public InternetPlusBean[] newArray(int size) {
            return new InternetPlusBean[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getManufacturerDeviceType() {
        return manufacturerDeviceType;
    }

    public void setManufacturerDeviceType(String manufacturerDeviceType) {
        this.manufacturerDeviceType = manufacturerDeviceType;
    }

    public String getManufacturerDeviceId() {
        return manufacturerDeviceId;
    }

    public void setManufacturerDeviceId(String manufacturerDeviceId) {
        this.manufacturerDeviceId = manufacturerDeviceId;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(deviceName);
        parcel.writeString(cid);
        parcel.writeString(deviceType);
        parcel.writeString(manufacturerDeviceType);
        parcel.writeString(manufacturerDeviceId);
        parcel.writeString(longitude);
        parcel.writeString(latitude);
        parcel.writeString(deviceStatus);
        parcel.writeString(placeId);
        parcel.writeString(beginTime);
        parcel.writeString(endTime);
        parcel.writeString(url);
        parcel.writeString(userId);
        parcel.writeString(userName);
        parcel.writeString(address);
        parcel.writeString(pic);
        parcel.writeString(createTime);
    }

    @Override
    public String toString() {
        return "InternetPlusBean{" +
                "id='" + id + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", cid='" + cid + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", manufacturerDeviceType='" + manufacturerDeviceType + '\'' +
                ", manufacturerDeviceId='" + manufacturerDeviceId + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", deviceStatus='" + deviceStatus + '\'' +
                ", placeId='" + placeId + '\'' +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", url='" + url + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", address='" + address + '\'' +
                ", pic='" + pic + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
