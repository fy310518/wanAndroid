package com.fy.baselibrary.application;

import android.content.Context;

public class ContextUtils {

    static ContextComponent contextComponent;

    public ContextUtils(Context ctx) {
        contextComponent = DaggerContextComponent.builder()
                .contextModule(new ContextModule(ctx))
                .build();
    }

    public static Context getAppCtx() {
        return contextComponent.getContext();
    }
}
