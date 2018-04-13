package wanandroid.fy.com.web;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.base.dialog.CommonDialog;
import com.fy.baselibrary.base.dialog.DialogConvertListener;
import com.fy.baselibrary.base.dialog.NiceDialog;
import com.fy.baselibrary.statusbar.MdStatusBar;
import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.ResourceUtils;
import com.fy.baselibrary.utils.T;

import butterknife.BindView;
import wanandroid.fy.com.R;
import wanandroid.fy.com.entity.Bookmark;

/**
 * 通用 加载 web 网页 activity
 * Created by fangs on 2018/4/13.
 */
public class WebViewActivity extends AppCompatActivity implements IBaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @Override
    public boolean isShowHeadView() {
        return true;
    }

    @Override
    public int setView() {
        return R.layout.activity_web;
    }

    @Override
    public void setStatusBar(Activity activity) {
        MdStatusBar.setColorBar(activity, R.color.statusBar, R.color.statusBar);
    }

    @SuppressLint("NewApi")
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        Bookmark bookmark = (Bookmark) getIntent().getExtras().getSerializable("Bookmark");

        toolbar.setTitle(bookmark.getName());

        initWeb(bookmark.getLink());
    }

    @Override
    public void onClick(View v) {}

    @Override
    public void reTry() {

    }

    private void initWeb(String url) {
//        webView.loadUrl("file:///android_asset/test.html");//加载asset文件夹下html
        webView.loadUrl(url);//加载url

        //使用webview显示html代码
//        webView.loadDataWithBaseURL(null,"<html><head><title> 欢迎您 </title></head>" +
//                "<body><h2>使用webview显示 html代码</h2></body></html>", "text/html" , "utf-8", null);

        webView.addJavascriptInterface(this, "android");//添加js监听 这样html就能调用客户端
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(webViewClient);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js

        /*
         * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
         * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
         * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         */
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.

        //是否支持屏幕缩放
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        //不显示webview缩放按钮
//        webSettings.setDisplayZoomControls(false);
    }

    //WebViewClient主要帮助WebView处理各种通知、请求事件
    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            L.i("ansen", "拦截url:" + url);
            if (url.equals("http://www.google.com/")) {
                T.showLong("国内不能访问google,拦截该url");
                return true;//表示我已经处理过了
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    };

    //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
    private WebChromeClient webChromeClient = new WebChromeClient() {
        //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
            NiceDialog.init()
                    .setLayoutId(R.layout.dialog_permission)
                    .setDialogConvertListener(new DialogConvertListener() {
                        @Override
                        protected void convertView(ViewHolder holder, CommonDialog dialog) {
                            holder.setText(R.id.tvPermissionTitle, ResourceUtils.getStr(R.string.dialog_title));
                            holder.setText(R.id.tvPermissionDescribe, "js 调用 本地 弹窗");

                            holder.setText(R.id.tvpermissionConfirm, ResourceUtils.getStr(R.string.ok));
                            holder.setOnClickListener(R.id.tvpermissionConfirm, v -> dialog.dismiss(false));

                            holder.setText(R.id.tvPermissionCancel, getString(R.string.cancel));
                            holder.setOnClickListener(R.id.tvPermissionCancel, v -> dialog.dismiss(false));
                        }
                    }).show(getSupportFragmentManager());


            //注意:
            //必须要这一句代码:result.confirm()表示:
            //处理结果为确定状态同时唤醒WebCore线程
            //否则不能继续点击按钮
            result.confirm();
            return true;
        }

        //获取网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            L.i("ansen", "网页标题:" + title);
        }

        //加载进度回调
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        L.i("ansen", "是否有上一个页面:" + webView.canGoBack());
        if (webView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {//点击返回按钮的时候判断有没有上一页
            webView.goBack(); // goBack()表示返回webView的上一页面
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * JS调用android的方法
     *
     * @param str
     * @return
     */
    @JavascriptInterface //仍然必不可少
    public void getClient(String str) {
        L.i("ansen", "html调用客户端:" + str);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != webView) {
            //释放资源
            webView.destroy();
            webView = null;
        }
    }
}
