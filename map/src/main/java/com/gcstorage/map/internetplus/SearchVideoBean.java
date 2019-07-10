package com.gcstorage.map.internetplus;

import java.io.Serializable;
import java.util.List;

public class SearchVideoBean implements Serializable {

    private List<DeviceTypeBean> deviceType;
    private List<ManufacturerDeviceTypeBean> manufacturerDeviceType;
    private List<PathIdBean> pathId;

    public List<DeviceTypeBean> getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(List<DeviceTypeBean> deviceType) {
        this.deviceType = deviceType;
    }

    public List<ManufacturerDeviceTypeBean> getManufacturerDeviceType() {
        return manufacturerDeviceType;
    }

    public void setManufacturerDeviceType(List<ManufacturerDeviceTypeBean> manufacturerDeviceType) {
        this.manufacturerDeviceType = manufacturerDeviceType;
    }

    public List<PathIdBean> getPathId() {
        return pathId;
    }

    public void setPathId(List<PathIdBean> pathId) {
        this.pathId = pathId;
    }

    public static class DeviceTypeBean implements Serializable {
        /**
         * id : 1006112
         * code : 100602
         * name : 球机
         * typeCode : 100600
         */

        private String id;
        private String code;
        private String name;
        private String typeCode;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTypeCode() {
            return typeCode;
        }

        public void setTypeCode(String typeCode) {
            this.typeCode = typeCode;
        }

        @Override
        public String toString() {
            return "DeviceTypeBean{" +
                    "id='" + id + '\'' +
                    ", code='" + code + '\'' +
                    ", name='" + name + '\'' +
                    ", typeCode='" + typeCode + '\'' +
                    '}';
        }
    }

    public static class ManufacturerDeviceTypeBean implements Serializable{
        /**
         * id : 1005237
         * code : 103401
         * name : 相机
         * typeCode : 103400
         */

        private String id;
        private String code;
        private String name;
        private String typeCode;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTypeCode() {
            return typeCode;
        }

        public void setTypeCode(String typeCode) {
            this.typeCode = typeCode;
        }

        @Override
        public String toString() {
            return "ManufacturerDeviceTypeBean{" +
                    "id='" + id + '\'' +
                    ", code='" + code + '\'' +
                    ", name='" + name + '\'' +
                    ", typeCode='" + typeCode + '\'' +
                    '}';
        }
    }

    public static class PathIdBean implements Serializable {
        /**
         * id : 1006315
         * code : 119101
         * name : 小区
         * typeCode : 119100
         */

        private String id;
        private String code;
        private String name;
        private String typeCode;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTypeCode() {
            return typeCode;
        }

        public void setTypeCode(String typeCode) {
            this.typeCode = typeCode;
        }

        @Override
        public String toString() {
            return "PathIdBean{" +
                    "id='" + id + '\'' +
                    ", code='" + code + '\'' +
                    ", name='" + name + '\'' +
                    ", typeCode='" + typeCode + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SearchVideoBean{" +
                "deviceType=" + deviceType +
                ", manufacturerDeviceType=" + manufacturerDeviceType +
                ", pathId=" + pathId +
                '}';
    }
}
