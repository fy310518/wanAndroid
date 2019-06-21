package com.gc.unifiedlogin.sysnotify;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.h5.BaseAndroidJSInterface;
import com.fy.baselibrary.utils.GsonUtils;
import com.fy.baselibrary.utils.notify.L;
import com.gc.unifiedlogin.R;
import com.google.gson.JsonObject;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;

/**
 * DESCRIPTION：显示门户发送 的广播消息 的 h5页面
 * Created by fangs on 2019/5/28 9:33.
 */
public class H5AppActivity extends AppCompatActivity implements IBaseActivity {

    @BindView(R.id.webView)
    WebView webView;

    @BindView(R.id.webProgress)
    ProgressBar webProgress;

    @Override
    public boolean isShowHeadView() {
        return false;
    }

    @Override
    public int setView() {
        return R.layout.webview_activity;
    }

    @StatusBar(statusColor = R.color.statusBar, navColor = R.color.statusBar)
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        initWebView();

        initReceiver();
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != webView) {
            webView.setWebViewClient(null);
            webView.setWebChromeClient(null);
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);

            //释放资源
            webView.destroy();
            webView = null;
        }

        unregisterReceiver(sysNotifyReceiver);
    }


    private void initWebView(){
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);  //设置与Js交互的权限
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setAllowContentAccess(true); // 是否可访问Content Provider的资源，默认值 true
        settings.setAllowFileAccess(true);    // 是否可访问本地文件，默认值 true
        // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
        settings.setAllowFileAccessFromFileURLs(false);
        // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
        settings.setAllowUniversalAccessFromFileURLs(false);
        // 设置允许JS弹窗
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 支持缩放
//        settings.setSupportZoom(true);

//        settings.setJavaScriptCanOpenWindowsAutomatically(true);  // 设置允许JS弹窗
        //第一个参数把自身传给js 第二个参数是this的一个名字
        //这个方法用于让H5调用android方法
        CustomJavascriptInterface anInterface = new CustomJavascriptInterface(this);
//        anInterface.setDefaultParams("alarm", GlobalUserInfo.getAlarm(this));

        webView.addJavascriptInterface(anInterface, "android");
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (null == webProgress)return;

                webProgress.setProgress(newProgress);
                webProgress.setVisibility(newProgress == 100 ? View.GONE : View.VISIBLE);
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                return true;
            }
        });


        webView.loadUrl("file:///android_asset/home/index.html");
    }


    private class CustomJavascriptInterface extends BaseAndroidJSInterface {
        public CustomJavascriptInterface(Context context) {
            super(context, webView, "http://120.79.194.253:8768");
        }

        @JavascriptInterface
        public void back() {
            finish();
        }

        @JavascriptInterface
        public String getMsgList(){
            List<MsgData> newsList = LitePal
                    .order("SEND_TIME asc")
                    .limit(10)
                    .find(MsgData.class);

            return GsonUtils.listToJson(newsList);
        }
    }



    /**
     * 注册消息相关的广播接收者
     */
    private void initReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.MESSAGE_ARRIVED");
//        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.setPriority(100);
        registerReceiver(sysNotifyReceiver, filter);
    }


    BroadcastReceiver sysNotifyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String message = bundle.getString("message", "");
            JsonObject sysNotifyJson = GsonUtils.jsonStrToJsonObj(message);
            String msgDataStr = sysNotifyJson.get("MSG").getAsJsonObject().get("DATA").getAsJsonObject().toString();

            MsgData msgData = GsonUtils.fromJson(msgDataStr, MsgData.class);
            msgData.save();

            List<MsgData> newsList = LitePal
                    .order("SEND_TIME desc")
                    .limit(1)
                    .find(MsgData.class);

            L.e("大王", "--------------------------------------------------");

            webView.post(new Runnable() {
                @Override
                public void run() {
                    // 注意调用的JS方法名要对应上
                    // 调用javascript的callJS()方法
                    webView.loadUrl("javascript:getData('" + GsonUtils.listToJson(newsList) + "')");
                }
            });
        }
    };
}
