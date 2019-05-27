package com.gc.unifiedlogin.sysnotify;

import org.litepal.crud.LitePalSupport;

/**
 * DESCRIPTION： 系统消息
 * Created by fangs on 2019/5/27 16:53.
 */
public class MsgData extends LitePalSupport {

    /**
     * APPID_NAME : 测试应用
     * APPID_IMG : http://47.107.134.212:13201/tax00/M00/03/06/QUIPAFzc3CWAQlTXAABIHip_7Vs963.png
     * PAGEPATH : com.gcstorage.equipmentmanagement
     * SEND_TIME : 2019-05-27 15:51:05
     * TITLE : json 解析
     * SUBMIT_TEXT : 
     * TEXT_FILE : http://47.107.134.212:13201/tax00/M00/03/01/QUIPAFzbh9CAVQT1AAByHR9c9qQ585.jpg,http://47.107.134.212:13201/tax00/M00/03/01/QUIPAFzbh9CAVQT1AAByHR9c9qQ585.jpg,http://47.107.134.212:13201/tax00/M00/03/01/QUIPAFzbh9CAVQT1AAByHR9c9qQ585.jpg
     * APPID : 3d7174691cb5400e97b21b01a52425de
     * TEXT : 测试
     * TEXT_DETAIL : json 大法好
     */

    private String APPID_NAME;
    private String APPID_IMG;
    private String PAGEPATH;
    private String SEND_TIME;
    private String TITLE;
    private String SUBMIT_TEXT;
    private String TEXT_FILE;
    private String APPID;
    private String TEXT;
    private String TEXT_DETAIL;

    public String getAPPID_NAME() {
        return APPID_NAME;
    }

    public void setAPPID_NAME(String APPID_NAME) {
        this.APPID_NAME = APPID_NAME;
    }

    public String getAPPID_IMG() {
        return APPID_IMG;
    }

    public void setAPPID_IMG(String APPID_IMG) {
        this.APPID_IMG = APPID_IMG;
    }

    public String getPAGEPATH() {
        return PAGEPATH;
    }

    public void setPAGEPATH(String PAGEPATH) {
        this.PAGEPATH = PAGEPATH;
    }

    public String getSEND_TIME() {
        return SEND_TIME;
    }

    public void setSEND_TIME(String SEND_TIME) {
        this.SEND_TIME = SEND_TIME;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getSUBMIT_TEXT() {
        return SUBMIT_TEXT;
    }

    public void setSUBMIT_TEXT(String SUBMIT_TEXT) {
        this.SUBMIT_TEXT = SUBMIT_TEXT;
    }

    public String getTEXT_FILE() {
        return TEXT_FILE;
    }

    public void setTEXT_FILE(String TEXT_FILE) {
        this.TEXT_FILE = TEXT_FILE;
    }

    public String getAPPID() {
        return APPID;
    }

    public void setAPPID(String APPID) {
        this.APPID = APPID;
    }

    public String getTEXT() {
        return TEXT;
    }

    public void setTEXT(String TEXT) {
        this.TEXT = TEXT;
    }

    public String getTEXT_DETAIL() {
        return TEXT_DETAIL;
    }

    public void setTEXT_DETAIL(String TEXT_DETAIL) {
        this.TEXT_DETAIL = TEXT_DETAIL;
    }
}
