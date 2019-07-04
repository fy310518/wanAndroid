package com.gcstorage.parkinggather.util;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

import com.fy.baselibrary.utils.ResUtils;
import com.fy.baselibrary.utils.drawable.TintUtils;
import com.gcstorage.parkinggather.R;

/**
 * DESCRIPTION：驻车采集 工具类
 * Created by fangs on 2019/7/1 19:00.
 */
public class PGAppUtils {

    /**
     * 获取 指定 ID的 drawable，生成 选择器
     * @param draId          drawable Id
     * @param drawableType   drawable 类型
     * @param pressedColor   选中颜色
     * @param defaultColor   默认颜色
     * @return
     */
    public static Drawable getSelector(@DrawableRes int draId, int drawableType,
                                       @ColorRes int pressedColor, @ColorRes int defaultColor){
        int[] colors = new int[]{
                ResUtils.getColor(pressedColor),
                ResUtils.getColor(pressedColor),
                ResUtils.getColor(pressedColor),
                ResUtils.getColor(defaultColor)};

        int[][] states = new int[4][];
        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{android.R.attr.state_selected};
        states[2] = new int[]{android.R.attr.state_checked};
        states[3] = new int[]{};

        Drawable drawable = TintUtils.getDrawable(draId, drawableType);
//        StateListDrawable stateListDrawable = TintUtils.getStateListDrawable(drawable, states);

        return TintUtils.tintSelector(drawable, colors, states);
    }

    /**
     * 天气 code
     * @param weather_code
     * @return 返回 天气图标 id
     */
    public static int getWeatherIcon(String weather_code) {
        int id = 0;
        switch (weather_code) {
            case "00"://晴
                id = R.mipmap.icon_weather_sunny;
                break;
            case "01"://多云
                id = R.mipmap.icon_weather_cloudy;
                break;
            case "02"://阴
                id = R.mipmap.icon_weather_cloud;
                break;
            case "03"://阵雨
                id = R.mipmap.icon_weather_thundershower;
                break;
            case "04"://雷阵雨
                id = R.mipmap.icon_weather_thundershower;
                break;
            case "05"://雷阵雨伴有冰雹
                id = R.mipmap.icon_weather_raiuandhail;
                break;
            case "06"://雨夹雪
                id = R.mipmap.icon_weather_rainandsnow;
                break;
            case "07"://小雨
                id = R.mipmap.icon_weather_smallrain;
                break;
            case "08"://中雨
                id = R.mipmap.icon_weather_middlerain;
                break;
            case "09"://大雨
                id = R.mipmap.icon_weather_heavyrain;
                break;
            case "10"://暴雨
                id = R.mipmap.icon_weather_downpour;
                break;
            case "11"://大暴雨
                id = R.mipmap.icon_weather_downpour;
                break;
            case "12"://特大暴雨
                id = R.mipmap.icon_weather_extraodinaryrainstorm;
                break;
            case "13"://阵雪
                id = R.mipmap.icon_weather_raiuandhail;
                break;
            case "14"://小雪
                id = R.mipmap.icon_weather_spit;
                break;
            case "15"://中雪
                id = R.mipmap.icon_weather_middlesnow;
                break;
            case "16"://大雪
                id = R.mipmap.icon_weather_heavysnow;
                break;
            case "17"://暴雪
                id = R.mipmap.icon_weather_snowstorm;
                break;
            case "18"://雾
                id = R.mipmap.icon_weather_fog;
                break;
            case "19"://冻雨
                id = R.mipmap.icon_weather_rainandsnow;
                break;
            case "20"://沙尘暴
                id = R.mipmap.icon_weather_sandstorm;
                break;
            case "21"://小到中雨
                id = R.mipmap.icon_weather_middlerain;
                break;
            case "22"://中到大雨
                id = R.mipmap.icon_weather_heavyrain;
                break;
            case "23"://大到暴雨
                id = R.mipmap.icon_weather_downpour;
                break;
            case "24"://暴雨到大暴雨
                id = R.mipmap.icon_weather_downpour;
                break;
            case "25"://大暴雨到特大暴雨
                id = R.mipmap.icon_weather_extraodinaryrainstorm;
                break;
            case "26"://小到中雪
                id = R.mipmap.icon_weather_middlesnow;
                break;
            case "27"://中到大雪
                id = R.mipmap.icon_weather_heavysnow;
                break;
            case "28"://大到暴雪
                id = R.mipmap.icon_weather_snowstorm;
                break;
            case "29"://浮尘
                id = R.mipmap.icon_weather_flyash;
                break;
            case "30"://扬沙
                id = R.mipmap.icon_weather_flyash;
                break;
            case "31"://强沙尘暴
                id = R.mipmap.icon_weather_sandstorm;
                break;
            case "53"://霾
                id = R.mipmap.icon_weather_haze;
                break;
            case "99"://无
                id = R.mipmap.icon_weather_cloud;
                break;
            case "301"://雨
                id = R.mipmap.icon_weather_smallrain;
                break;
            case "302"://雪
                id = R.mipmap.icon_weather_spit;
                break;
        }
        return id;
    }
}
