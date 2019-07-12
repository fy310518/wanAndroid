package com.gcstorage.framework.net.callback;

import android.util.Log;

import com.gcstorage.framework.net.basebean.ApiResponse;
import com.gcstorage.framework.net.listener.ActionCallbackListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;

/**
 * Created by zjs on 2018/3/16.
 */

public class GcOkhttpCallback<T> extends AbsCallback<ApiResponse<T>> {

    private Type type;
    private ActionCallbackListener<T> listener;
    public static final boolean DEBUG = true;

    public GcOkhttpCallback(Type type, ActionCallbackListener<T> listener) {
        this.type = type;
        this.listener = listener;
    }


    @Override
    public ApiResponse<T> convertResponse(okhttp3.Response response) {
        ResponseBody body = response.body();
        if (body == null) {
            return null;
        }
        ApiResponse<T> data = null;
        try {
            String json = body.string();
            if (type != null) {
                data = new Gson().fromJson(json, type);
                Log.e("yangrui", body.string());
            }
        } catch (JsonSyntaxException | IOException e) {
            e.printStackTrace();
            Log.e("yangrui", e.toString() + "");
            return null;
        }
        return data;
    }

    @Override
    public void onError(Response<ApiResponse<T>> response) {
        super.onError(response);
        listener.onFailure("-1000", "连接服务器失败");
    }

    @Override
    public void onSuccess(Response<ApiResponse<T>> response) {
        ApiResponse<T> body = response.body();
        if (null != body) {
            if (body.isSuccess()) {
                try {
                    listener.onSuccess(body.getResponse());
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure("-1001", "程序内部错误");
                }
            } else {
                listener.onFailure(body.getResultcode(), body.getResultmessage());
            }
        } else {
            listener.onFailure("-1003", "数据解析出错");
        }
    }
}
