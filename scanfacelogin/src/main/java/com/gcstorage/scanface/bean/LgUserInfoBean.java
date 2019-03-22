package com.gcstorage.scanface.bean;

/**
 * DESCRIPTION：统一登录 获取 个人信息实体类
 * Created by fangs on 2019/3/22 15:06.
 */
public class LgUserInfoBean {

    /**
     * idcard : 420115199410109818
     * units :
     * avatar : http://120.24.1.46:13201/tax00/M00/00/00/QUIPAFv9C2WACcVhAAAP-KtTeas406.jpg
     * user_name : 郑家双
     * face_similarity : 0.6
     * openid : 7722fbe268c9615cf5e4ee411080c665
     * police_num : 015176
     * phone : 15972953583
     * org_id : 1
     * user_type : 1
     */

    private String idcard;
    private String units;
    private String avatar;
    private String user_name;
    private String face_similarity;
    private String openid;
    private String police_num;
    private String phone;
    private String org_id;
    private String user_type;

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getFace_similarity() {
        return face_similarity;
    }

    public void setFace_similarity(String face_similarity) {
        this.face_similarity = face_similarity;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getPolice_num() {
        return police_num;
    }

    public void setPolice_num(String police_num) {
        this.police_num = police_num;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrg_id() {
        return org_id;
    }

    public void setOrg_id(String org_id) {
        this.org_id = org_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
