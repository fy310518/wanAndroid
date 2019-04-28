package com.fy.wanandroid.entity;

/**
 * DESCRIPTION：应用更新 实体类
 * Created by fangs on 2019/4/23 17:47.
 */
public class AppUpdateEntity {


    /**
     * resultcode : 20000
     * resultmessage : 处理成功
     * response : {"id":"0","packageName":"com.gcstorage.newapp","appName":"移动门户","appIcon":"http://120.24.1.46:13201/tax00/M00/01/02/QUIPAFybHxyAY2ZiAAAPGmwh0js628.png","appDescrible":"移动门户","appType":"30","createTime":"2019-04-25T01:29:18.000+0000","appStatus":"1","packageUrl":"http://47.107.134.212/ydmh.apk","versionNum":"1.0","versionDescrible":"初始版本","type":false}
     */

    private String resultcode;
    private String resultmessage;
    private ResponseBean response;

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getResultmessage() {
        return resultmessage;
    }

    public void setResultmessage(String resultmessage) {
        this.resultmessage = resultmessage;
    }

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public static class ResponseBean {
        /**
         * id : 0
         * packageName : com.gcstorage.newapp
         * appName : 移动门户
         * appIcon : http://120.24.1.46:13201/tax00/M00/01/02/QUIPAFybHxyAY2ZiAAAPGmwh0js628.png
         * appDescrible : 移动门户
         * appType : 30
         * createTime : 2019-04-25T01:29:18.000+0000
         * appStatus : 1
         * packageUrl : http://47.107.134.212/ydmh.apk
         * versionNum : 1.0
         * versionDescrible : 初始版本
         * type : false
         */

        private String id;
        private String packageName;
        private String appName;
        private String appIcon;
        private String appDescrible;
        private String appType;
        private String createTime;
        private String appStatus;
        private String packageUrl;
        private String versionNum;
        private String versionDescrible;
        private boolean type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getAppIcon() {
            return appIcon;
        }

        public void setAppIcon(String appIcon) {
            this.appIcon = appIcon;
        }

        public String getAppDescrible() {
            return appDescrible;
        }

        public void setAppDescrible(String appDescrible) {
            this.appDescrible = appDescrible;
        }

        public String getAppType() {
            return appType;
        }

        public void setAppType(String appType) {
            this.appType = appType;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getAppStatus() {
            return appStatus;
        }

        public void setAppStatus(String appStatus) {
            this.appStatus = appStatus;
        }

        public String getPackageUrl() {
            return packageUrl;
        }

        public void setPackageUrl(String packageUrl) {
            this.packageUrl = packageUrl;
        }

        public String getVersionNum() {
            return versionNum;
        }

        public void setVersionNum(String versionNum) {
            this.versionNum = versionNum;
        }

        public String getVersionDescrible() {
            return versionDescrible;
        }

        public void setVersionDescrible(String versionDescrible) {
            this.versionDescrible = versionDescrible;
        }

        public boolean isType() {
            return type;
        }

        public void setType(boolean type) {
            this.type = type;
        }
    }
}
