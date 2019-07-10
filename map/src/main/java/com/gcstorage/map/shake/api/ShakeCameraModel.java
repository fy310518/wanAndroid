package com.gcstorage.map.shake.api;


import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Administrator on 2016/9/2.
 */
public class ShakeCameraModel implements Serializable {

    public long _id;
    public double gps_w; // 纬度
    public double gps_h; // 经度
    public String IP;
    public String OBJECTID;
    public String FJID;
    public String NAME;
    public String CGH;
    public String FL;
    public String PCSID;
    public String ADDRESS; // 详细的地理名称
    public String PHONE; // 联系电话
    public String camera_rtsp; // 摄像头的流

    public int distance; // 距离



    public static class DistanceComparator implements Comparator<ShakeCameraModel> {
        @Override
        public int compare(ShakeCameraModel t1, ShakeCameraModel t2) {
            if (t1.distance > t2.distance) {
                return 1;
            }

            if (t1.distance < t2.distance) {
                return -1;
            }

            return 0;
        }
    }

}
