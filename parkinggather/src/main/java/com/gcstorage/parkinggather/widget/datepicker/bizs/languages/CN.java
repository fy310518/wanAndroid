package com.gcstorage.parkinggather.widget.datepicker.bizs.languages;

/**
 * 中文的默认实现类
 * 如果你想实现更多的语言请参考Language{@link DPLManager}
 *
 * The implementation class of chinese.
 * You can refer to Language{@link DPLManager} if you want to define more language.
 *
 * @author AigeStudio 2015-03-28
 */
public class CN extends DPLManager {
    @Override
    public String[] titleMonth() {
        return new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    }

    @Override
    public String titleEnsure() {
        return "确定";
    }

    @Override
    public String titleBC() {
        return "公元前";
    }

    @Override
    public String[] titleWeek() {
        return new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    }
}
