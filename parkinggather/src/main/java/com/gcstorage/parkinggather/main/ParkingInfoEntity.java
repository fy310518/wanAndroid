package com.gcstorage.parkinggather.main;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * DESCRIPTION：驻车列表 实体类
 * Created by fangs on 2019/7/2 10:14.
 */
public class ParkingInfoEntity implements Serializable {

    /**
     * data : [{"date":"2019-02-20","time":"15:15:39","extend":false,"id":"9","userId":"420115199410109818","name":"郑家双","pic":"asdfasdfasdf","carImg":"http://47.107.134.212:10004/2d80790417fe428d9a664d7c966efaa6.jpg","carNum":"鄂AD6850","carColor":"蓝","longitude":"114.236481","latitude":"30.622878","address":"江旺路10号","type":"2"}]
     * totalPage : 1
     * pageNum : 1
     * dataNum : 5
     */


    public ParkingInfoEntity(List<DataBean> data) {
        this.data = data;
    }


    private int totalPage;
    private int pageNum;
    private int dataNum;
    private List<DataBean> data;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getDataNum() {
        return dataNum;
    }

    public void setDataNum(int dataNum) {
        this.dataNum = dataNum;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * date : 2019-02-20
         * time : 15:15:39
         * extend : false
         * id : 9
         * userId : 420115199410109818
         * name : 郑家双
         * pic : asdfasdfasdf
         * carImg : http://47.107.134.212:10004/2d80790417fe428d9a664d7c966efaa6.jpg
         * carNum : 鄂AD6850
         * carColor : 蓝
         * longitude : 114.236481
         * latitude : 30.622878
         * address : 江旺路10号
         * type : 2
         */

        private String date = "";
        private String time = "";
        private boolean extend;
        private String id= "";
        private String userId= "";
        private String name= "";
        private String pic= "";
        private String carImg= "";
        private String carNum= "";
        private String carColor= "";
        private String longitude= "";
        private String latitude= "";
        private String address= "";
        private String type= "";

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public boolean isExtend() {
            return extend;
        }

        public void setExtend(boolean extend) {
            this.extend = extend;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getCarImg() {
            return carImg;
        }

        public void setCarImg(String carImg) {
            this.carImg = carImg;
        }

        public String getCarNum() {
            return carNum;
        }

        public void setCarNum(String carNum) {
            this.carNum = carNum;
        }

        public String getCarColor() {
            return carColor;
        }

        public void setCarColor(String carColor) {
            this.carColor = carColor;
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DataBean dataBean = (DataBean) o;
            return extend == dataBean.extend &&
                    Objects.equals(date, dataBean.date) &&
                    Objects.equals(time, dataBean.time) &&
                    Objects.equals(id, dataBean.id) &&
                    Objects.equals(userId, dataBean.userId) &&
                    Objects.equals(name, dataBean.name) &&
                    Objects.equals(pic, dataBean.pic) &&
                    Objects.equals(carImg, dataBean.carImg) &&
                    Objects.equals(carNum, dataBean.carNum) &&
                    Objects.equals(carColor, dataBean.carColor) &&
                    Objects.equals(longitude, dataBean.longitude) &&
                    Objects.equals(latitude, dataBean.latitude) &&
                    Objects.equals(address, dataBean.address) &&
                    Objects.equals(type, dataBean.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(date, time, extend, id, userId, name, pic, carImg, carNum, carColor, longitude, latitude, address, type);
        }
    }
}
