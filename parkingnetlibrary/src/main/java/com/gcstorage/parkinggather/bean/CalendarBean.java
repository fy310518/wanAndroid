package com.gcstorage.parkinggather.bean;

import java.util.List;

/**
 *
 * Created by Administrator on 2017/6/7.
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

        private String date;
        private String carnum;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getCarnum() {
            return carnum;
        }

        public void setCarnum(String carnum) {
            this.carnum = carnum;
        }

        @Override
        public String toString() {
            return "InfoListBean{" +
                    "date='" + date + '\'' +
                    ", carnum='" + carnum + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CalendarBean{" +
                "dateList=" + dateList +
                ", infoList=" + infoList +
                '}';
    }
}
