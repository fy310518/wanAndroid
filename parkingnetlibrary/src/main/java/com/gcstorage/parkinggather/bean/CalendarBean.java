package com.gcstorage.parkinggather.bean;

import java.util.List;

/**
 * DESCRIPTION：日历 （采集历史 ） 实体类
 * Created by fangs on 2019/7/9 9:36.
 */
public class CalendarBean {

    private List<String> dateList;
    private List<InfoListBean> infoList;

    public List<String> getDateList() {
        return dateList;
    }

    public void setDateList(List<String> dateList) {
        this.dateList = dateList;
    }

    public List<InfoListBean> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<InfoListBean> infoList) {
        this.infoList = infoList;
    }

    public static class InfoListBean {
        /**
         * date : 2017-06-07
         * carnum : 1
         */

        private String date = "";
        private int carnum;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getCarnum() {
            return carnum;
        }

        public void setCarnum(int carnum) {
            this.carnum = carnum;
        }

    }

}
