package com.gcstorage.map;

/**
 * 地图中获取不同类型数据或标记点的bean
 * 将所有的marker使用这个MarkerData进行封装
 */
public class MarkerData {
    public MarkerData() {
    }

    //类型
    public Type type;
    //数据
    public Object data;

    public MarkerData(Type type, Object data) {
        this.type = type;
        this.data = data;
    }

    public enum Type {
        //标记(四表合一)
        MARKER_MEMBER,           //人员标记
        MARKER_CAMERA,           //摄像头标记
        MARKER_CRIME_PLACE,      //案发地
        MARKER_UAV,              //无人机
        MARKER_DEFAULT_SPOT,     //默认
    }
}
