package com.gcstorage.scanface.bean;

/**
 * DESCRIPTION：统一登录 获取 token
 * Created by fangs on 2019/3/22 14:54.
 */
public class LgTokenBean {

    /**
     * refresh_token : 0dc2f7ae-1eb3-4690-8f7f-e5e180338e4f
     * expires_in : 3600
     * access_token : 25da22b0-4126-436d-a6e5-4b5deabd6aa8
     * scope : login
     * openid : 7722fbe268c9615cf5e4ee411080c665
     */

    private String refresh_token = "";
    private int expires_in;
    private String access_token = "";
    private String scope = "";
    private String openid = "";

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
