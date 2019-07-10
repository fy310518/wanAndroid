package com.gcstorage.map.utils;

import com.gcstorage.map.MarkerData;
import com.jeff.map.R;

/**
 * 地图上面不同的标记点,资源页面
 */
public class MapRes {
    public static int defaultRes = 0; //人员
    public static int personnel = 1; //人员
    public static int camera = 2; //摄像头
    public static int suspect = 3; //嫌疑人
    public static int uav = 4; //无人机
    public static int duikang = 5; //对抗
    public static int weinu = 6; //围补图标
    public static int line = 7; //地图标记点
    public static int default_spot = 8; //地图标记点

    public static int[] tabRes =
            {R.mipmap.map_marker_moren, R.mipmap.map_marker_renyuan, R.mipmap.map_marker_shexiangtou,
                    R.mipmap.map_marker_xianyiren, R.mipmap.map_marker_wurenji, R.mipmap.map_marker_dvkang,
                    R.mipmap.map_marker_weibu, R.mipmap.map_instr_spot,R.mipmap.carport_place_ic};

    //标记嫌疑人嫌疑车方向
    public static int[] markSusDirectionArr = {R.mipmap.mark_blue_down, R.mipmap.mark_blue_left, R.mipmap.mark_blue_up,
            R.mipmap.mark_blue_right, R.mipmap.mark_blue_leftdown, R.mipmap.mark_blue_leftup, R.mipmap.mark_blue_rightup,
            R.mipmap.mark_blue_rightdown};

    /**
     * 标记方向未选中图表（嫌疑人嫌疑车）
     *
     * @param type
     * @param direction
     * @return
     */
    public static int markSusDirectionSelect(int type, int direction) {
        int RESOUCE = 0;
        switch (type) {
            case 2: // 嫌疑车
                switch (direction) {
                    case 0:
                        RESOUCE = R.mipmap.mark_car_down;
                        break;
                    case 1:
                        RESOUCE = R.mipmap.mark_car_left;
                        break;
                    case 2:
                        RESOUCE = R.mipmap.mark_car_up;
                        break;
                    case 3:
                        RESOUCE = R.mipmap.mark_car_right;
                        break;
                    case 4:
                        RESOUCE = R.mipmap.mark_car_leftdown;
                        break;
                    case 5:
                        RESOUCE = R.mipmap.mark_car_leftup;
                        break;
                    case 6:
                        RESOUCE = R.mipmap.mark_car_rightup;
                        break;
                    case 7:
                        RESOUCE = R.mipmap.mark_car_rightdown;
                        break;
                }

                break;

            case 3:   //嫌疑人
                switch (direction) {
                    case 0:
                        RESOUCE = R.mipmap.mark_people_down;
                        break;
                    case 1:
                        RESOUCE = R.mipmap.mark_people_left;
                        break;
                    case 2:
                        RESOUCE = R.mipmap.mark_people_up;
                        break;
                    case 3:
                        RESOUCE = R.mipmap.mark_people_right;

                        break;
                    case 4:
                        RESOUCE = R.mipmap.mark_people_leftdown;
                        break;
                    case 5:
                        RESOUCE = R.mipmap.mark_people_leftup;
                        break;
                    case 6:
                        RESOUCE = R.mipmap.mark_people_rightup;
                        break;
                    case 7:
                        RESOUCE = R.mipmap.mark_people_rightdown;
                        break;
                }

                break;

            case 5:  //集合点
                RESOUCE = R.mipmap.draft_mark_jhd_white;
                break;
        }
        return RESOUCE;
    }

    //打标资源类型
    public static int markSignSelect(int type) {
        return sign_list_normal[type];
    }


    public static int[] sign_list_normal = {R.mipmap.draft_mark_goodw, R.mipmap.mark_footw, R.mipmap.draft_mark_carw,
            R.mipmap.draft_mark_manw, R.mipmap.draft_mark_suspectw, R.mipmap.draft_mark_jhd};

    public static int[] sign_list_check = {R.mipmap.draft_mark_goodw_white, R.mipmap.mark_footw, R.mipmap.mark_carw,
            R.mipmap.draft_mark_manw_white, R.mipmap.draft_mark_suspectw_white, R.mipmap.draft_mark_jhd_white};

}
