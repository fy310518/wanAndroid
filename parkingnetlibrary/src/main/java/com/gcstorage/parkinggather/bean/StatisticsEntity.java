package com.gcstorage.parkinggather.bean;

import java.util.List;

/**
 * DESCRIPTION：统计信息 实体类
 * Created by fangs on 2019/7/8 14:28.
 */
public class StatisticsEntity {

    /**
     * collectInfoList : [{"collectnum":"0","name":"2019-07-02"},{"collectnum":"0","name":"2019-07-03"},{"collectnum":"2","name":"2019-07-04"},{"collectnum":"0","name":"2019-07-05"},{"collectnum":"1","name":"2019-07-06"},{"collectnum":"0","name":"2019-07-07"},{"collectnum":"0","name":"2019-07-08"}]
     * cityTotalCollectnum : 4
     * cityUsedCollectnum : 1
     * usedInfo : [{"hphm":"苏A2396V","name":"刷脸登录演示专用2"}]
     * usedCollectnum : 1
     * totalCollectnum : 3
     */

    private String cityTotalCollectnum = "0";
    private String cityUsedCollectnum = "";
    private String usedCollectnum= "";
    private String totalCollectnum= "";
    private List<CollectInfoListBean> collectInfoList;
    private List<UsedInfoBean> usedInfo;

    public String getCityTotalCollectnum() {
        return cityTotalCollectnum;
    }

    public void setCityTotalCollectnum(String cityTotalCollectnum) {
        this.cityTotalCollectnum = cityTotalCollectnum;
    }

    public String getCityUsedCollectnum() {
        return cityUsedCollectnum;
    }

    public void setCityUsedCollectnum(String cityUsedCollectnum) {
        this.cityUsedCollectnum = cityUsedCollectnum;
    }

    public String getUsedCollectnum() {
        return usedCollectnum;
    }

    public void setUsedCollectnum(String usedCollectnum) {
        this.usedCollectnum = usedCollectnum;
    }

    public String getTotalCollectnum() {
        return totalCollectnum;
    }

    public void setTotalCollectnum(String totalCollectnum) {
        this.totalCollectnum = totalCollectnum;
    }

    public List<CollectInfoListBean> getCollectInfoList() {
        return collectInfoList;
    }

    public void setCollectInfoList(List<CollectInfoListBean> collectInfoList) {
        this.collectInfoList = collectInfoList;
    }

    public List<UsedInfoBean> getUsedInfo() {
        return usedInfo;
    }

    public void setUsedInfo(List<UsedInfoBean> usedInfo) {
        this.usedInfo = usedInfo;
    }

    public static class CollectInfoListBean {
        /**
         * collectnum : 0
         * name : 2019-07-02
         */

        private String collectnum= "";
        private String name= "";

        public String getCollectnum() {
            return collectnum;
        }

        public void setCollectnum(String collectnum) {
            this.collectnum = collectnum;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class UsedInfoBean {
        /**
         * hphm : 苏A2396V
         * name : 刷脸登录演示专用2
         */

        private String hphm= "";
        private String name= "";

        public String getHphm() {
            return hphm;
        }

        public void setHphm(String hphm) {
            this.hphm = hphm;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
