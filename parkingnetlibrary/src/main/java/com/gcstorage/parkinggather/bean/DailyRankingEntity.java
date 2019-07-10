package com.gcstorage.parkinggather.bean;

/**
 * DESCRIPTION：个人排行 实体类
 * Created by fangs on 2019/7/9 9:36.
 */
public class DailyRankingEntity {

    /**
     * RE_headpic : http://47.107.134.212:13201/tax00/M00/00/09/QUIPAFzGumaAUyjoAAKl-baW5ac219.jpg
     * max : 刷脸登录演示专用2
     * alarm : 420704199210204259
     * department : 湖北省武汉市公安局反恐怖工作支队二大队
     * carnum : 5
     */

    private String RE_headpic = "";
    private String max= "";
    private String alarm= "";
    private String department= "";
    private String orgId= "";
    private int carnum;

    public String getRE_headpic() {
        return RE_headpic;
    }

    public void setRE_headpic(String RE_headpic) {
        this.RE_headpic = RE_headpic;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getCarnum() {
        return carnum;
    }

    public void setCarnum(int carnum) {
        this.carnum = carnum;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
