package com.gcstorage.circle.bean;

import java.io.Serializable;

/**
 * @DESCRIPTION： 猎鹰登录 返回数据
 * Created by fangs on 2019/3/11 9:42.
 */
public class LyLoginBean implements Serializable {


    /**
     * pslevel :
     * useralarm :
     * username :
     * name : 林凯
     * status : 2
     * RE_permission : 1
     * describe : 420106000000
     * power : {"bases":"3"}
     * token :
     * code : 1
     * alarm : N7jRvQFvbVYQNMJfAfZq
     * phonenum : 18871175373
     * headpic : http://113.57.174.98:13201/tax03/M00/04/04/bUMPAFvZSrGAKYCTAADYGZ83ppg545.png
     * tenantid :
     */

    private String pslevel;
    private String useralarm;
    private String username;
    private String name;
    private String status;
    private String RE_permission;
    private String describe;
    private PowerBean power;
    private String token;
    private String code;
    private String alarm;
    private String phonenum;
    private String headpic;
    private String tenantid;

    public String getPslevel() {
        return pslevel;
    }

    public void setPslevel(String pslevel) {
        this.pslevel = pslevel;
    }

    public String getUseralarm() {
        return useralarm;
    }

    public void setUseralarm(String useralarm) {
        this.useralarm = useralarm;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRE_permission() {
        return RE_permission;
    }

    public void setRE_permission(String RE_permission) {
        this.RE_permission = RE_permission;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public PowerBean getPower() {
        return power;
    }

    public void setPower(PowerBean power) {
        this.power = power;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getTenantid() {
        return tenantid;
    }

    public void setTenantid(String tenantid) {
        this.tenantid = tenantid;
    }

    public static class PowerBean {
        /**
         * bases : 3
         */

        private String bases;

        public String getBases() {
            return bases;
        }

        public void setBases(String bases) {
            this.bases = bases;
        }
    }
}
