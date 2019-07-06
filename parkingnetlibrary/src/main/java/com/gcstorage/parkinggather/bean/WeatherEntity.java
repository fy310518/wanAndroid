package com.gcstorage.parkinggather.bean;

/**
 * DESCRIPTION：天气实体类
 * Created by fangs on 2019/7/6 16:33.
 */
public class WeatherEntity {

    /**
     * wind_direction : 无持续风向
     * temperaturescope : 13°-4°
     * wind_power : 3-4级 5.5~7.9m/s
     * area : 武汉
     * time : 2019-07-06 16:30:28
     * weather_code : 07
     * next : {"wind_direction":"无持续风向","temperaturescope":"8°-4°","wind_power":"微风<10m/h","area":"武汉","time":"20171130","weather_code":"07","areaid":"101200101","sunend":"17:22","temperature":null,"shidu":"5%","weather":"小雨"}
     * areaid : 101200101
     * sunend : 17:22
     * temperature : 13
     * shidu : 85%
     * weather : 小雨
     */

    private String wind_direction = "";
    private String temperaturescope= "";
    private String wind_power= "";
    private String area= "";
    private String time= "";
    private String weather_code= "";
    private NextBean next;
    private String areaid= "";
    private String sunend= "";
    private String temperature= "";
    private String shidu= "";
    private String weather= "";

    public String getWind_direction() {
        return wind_direction;
    }

    public void setWind_direction(String wind_direction) {
        this.wind_direction = wind_direction;
    }

    public String getTemperaturescope() {
        return temperaturescope;
    }

    public void setTemperaturescope(String temperaturescope) {
        this.temperaturescope = temperaturescope;
    }

    public String getWind_power() {
        return wind_power;
    }

    public void setWind_power(String wind_power) {
        this.wind_power = wind_power;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeather_code() {
        return weather_code;
    }

    public void setWeather_code(String weather_code) {
        this.weather_code = weather_code;
    }

    public NextBean getNext() {
        return next;
    }

    public void setNext(NextBean next) {
        this.next = next;
    }

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public String getSunend() {
        return sunend;
    }

    public void setSunend(String sunend) {
        this.sunend = sunend;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getShidu() {
        return shidu;
    }

    public void setShidu(String shidu) {
        this.shidu = shidu;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public static class NextBean {
        /**
         * wind_direction : 无持续风向
         * temperaturescope : 8°-4°
         * wind_power : 微风<10m/h
         * area : 武汉
         * time : 20171130
         * weather_code : 07
         * areaid : 101200101
         * sunend : 17:22
         * temperature : null
         * shidu : 5%
         * weather : 小雨
         */

        private String wind_direction= "";
        private String temperaturescope= "";
        private String wind_power= "";
        private String area= "";
        private String time= "";
        private String weather_code= "";
        private String areaid= "";
        private String sunend= "";
        private String temperature = "";
        private String shidu= "";
        private String weather= "";

        public String getWind_direction() {
            return wind_direction;
        }

        public void setWind_direction(String wind_direction) {
            this.wind_direction = wind_direction;
        }

        public String getTemperaturescope() {
            return temperaturescope;
        }

        public void setTemperaturescope(String temperaturescope) {
            this.temperaturescope = temperaturescope;
        }

        public String getWind_power() {
            return wind_power;
        }

        public void setWind_power(String wind_power) {
            this.wind_power = wind_power;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getWeather_code() {
            return weather_code;
        }

        public void setWeather_code(String weather_code) {
            this.weather_code = weather_code;
        }

        public String getAreaid() {
            return areaid;
        }

        public void setAreaid(String areaid) {
            this.areaid = areaid;
        }

        public String getSunend() {
            return sunend;
        }

        public void setSunend(String sunend) {
            this.sunend = sunend;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getShidu() {
            return shidu;
        }

        public void setShidu(String shidu) {
            this.shidu = shidu;
        }

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }
    }
}
