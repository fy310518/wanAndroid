package com.gcstorage.parkinggather.bean;

/**
 * DESCRIPTION：登录 实体类
 * Created by fangs on 2019/7/6 11:45.
 */
public class LoginEntity {

    /**
     * pslevel : 4
     * status : 2
     * describe : 30
     * token : 734b00d61c2a0cd40c0402ed6305efda
     * open_id :
     * isSixmodule : 1
     * useralarm : 999999
     * IDCard : 420704199210204259
     * onlinelevel : 4
     * isSecurity_group : 0
     * isSecurity_module : 0
     * name : 刷脸登录演示专用2
     * username : 999999
     * RE_permission : 0
     * phonenum : 12345679
     * power : {"bases":"3"}
     * headpic : http://47.107.134.212:13201/tax00/M00/00/09/QUIPAFzGumaAUyjoAAKl-baW5ac219.jpg
     * code : 0
     * isAudit : 0
     * describeName : 技术支持
     * alarm : SLDLYSZY002
     * tenantid :
     */

    private int pslevel;
    private String status = "";
    private String describe= "";
    private String token= "";
    private String open_id= "";
    private String isSixmodule= "";
    private String useralarm= "";
    private String IDCard= "";
    private String onlinelevel= "";
    private String isSecurity_group= "";
    private String isSecurity_module= "";
    private String name= "";
    private String username= "";
    private String RE_permission= "";
    private String phonenum= "";
    private PowerBean power;
    private String headpic= "";
    private String code= "";
    private String isAudit= "";
    private String describeName= "";
    private String alarm= "";
    private String tenantid= "";

    public int getPslevel() {
        return pslevel;
    }

    public void setPslevel(int pslevel) {
        this.pslevel = pslevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOpen_id() {
        return open_id;
    }

    public void setOpen_id(String open_id) {
        this.open_id = open_id;
    }

    public String getIsSixmodule() {
        return isSixmodule;
    }

    public void setIsSixmodule(String isSixmodule) {
        this.isSixmodule = isSixmodule;
    }

    public String getUseralarm() {
        return useralarm;
    }

    public void setUseralarm(String useralarm) {
        this.useralarm = useralarm;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public String getOnlinelevel() {
        return onlinelevel;
    }

    public void setOnlinelevel(String onlinelevel) {
        this.onlinelevel = onlinelevel;
    }

    public String getIsSecurity_group() {
        return isSecurity_group;
    }

    public void setIsSecurity_group(String isSecurity_group) {
        this.isSecurity_group = isSecurity_group;
    }

    public String getIsSecurity_module() {
        return isSecurity_module;
    }

    public void setIsSecurity_module(String isSecurity_module) {
        this.isSecurity_module = isSecurity_module;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRE_permission() {
        return RE_permission;
    }

    public void setRE_permission(String RE_permission) {
        this.RE_permission = RE_permission;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public PowerBean getPower() {
        return power;
    }

    public void setPower(PowerBean power) {
        this.power = power;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIsAudit() {
        return isAudit;
    }

    public void setIsAudit(String isAudit) {
        this.isAudit = isAudit;
    }

    public String getDescribeName() {
        return describeName;
    }

    public void setDescribeName(String describeName) {
        this.describeName = describeName;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
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

        private String bases = "";

        public String getBases() {
            return bases;
        }

        public void setBases(String bases) {
            this.bases = bases;
        }
    }
}
