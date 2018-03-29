package com.fy.baselibrary.utils;

import android.content.Context;

/**
 * 解决provider冲突的util
 */
public class ProviderUtil {

    private ProviderUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static String getFileProviderName(Context context){
        return context.getPackageName()+".provider";
    }
}
