package com.gcstorage.parkinggather.bean;

/**
 * DESCRIPTION：用户信息 实体类
 * Created by fangs on 2019/7/8 16:54.
 */
public class PersonEntity {

    /**
     * phone : 12345678911
     * head_pic : http://47.107.134.212:13201/tax00/M00/02/01/QUIPAF0W4-6AR5YnAAJ4ECPACyo159.jpg
     * name : 方帅
     * id : 12444
     * policenum : 558566
     * depart : 湖北省武汉市第二看守所
     * orgid : 420100191000
     * account : 420621199202074539
     */

    private String phone = "";
    private String head_pic= "";
    private String name= "";
    private String id= "";
    private String policenum= "";
    private String depart= "";
    private String orgid= "";
    private String account= "";

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHead_pic() {
        return head_pic;
    }

    public void setHead_pic(String head_pic) {
        this.head_pic = head_pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPolicenum() {
        return policenum;
    }

    public void setPolicenum(String policenum) {
        this.policenum = policenum;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
