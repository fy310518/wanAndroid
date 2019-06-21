package com.fy.wanandroid.test;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.aop.resultfilter.ResultCallBack;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.utils.AppUtils;
import com.fy.baselibrary.utils.FileUtils;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.notify.L;
import com.fy.baselibrary.utils.PhotoUtils;
import com.fy.baselibrary.utils.notify.T;
import com.fy.img.picker.ImagePicker;
import com.fy.img.picker.PickerConfig;
import com.fy.img.picker.bean.ImageFolder;
import com.fy.img.picker.multiselect.ImgPickerActivity;
import com.fy.wanandroid.R;

import java.io.File;

import butterknife.BindView;

/**
 * DESCRIPTION：h5 android 交互 test
 * Created by fangs on 2019/3/27 17:03.
 */
public class H5Interactive extends AppCompatActivity implements IBaseActivity {

    @BindView(R.id.webview)
    WebView webview;

    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int PHOTO_REQUEST = 100;
    private final static int VIDEO_REQUEST = 120;

    private final static String url = "http://172.18.3.198:8080/#/index";
    private boolean videoFlag = false;

    @Override
    public boolean isShowHeadView() {
        return true;
    }

    @Override
    public int setView() {
        return R.layout.test_h5_interactive;
    }

    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();// 返回前一个页面
        } else {
            super.onBackPressed();
        }
    }

    @StatusBar(statusColor = R.color.statusBar, navColor = R.color.statusBar)
    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {

        //在JSHook类里实现javascript想调用的方法，并将其实例化传入webview, "hello"这个字串告诉javascript调用哪个实例的方法
        webview.addJavascriptInterface(new JSHook(), "android");


        WebSettings settings = webview.getSettings();
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
        //开启JavaScript支持
        settings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 支持缩放
        settings.setSupportZoom(true);

        //辅助WebView设置处理关于页面跳转，页面请求等操作
        webview.setWebViewClient(new MyWebViewClient());
        //辅助WebView处理图片上传操作
        webview.setWebChromeClient(new MyChromeWebClient());
        //加载地址
//        webview.loadUrl("file:///android_asset/dist/index.html");
        webview.loadUrl(url);
    }

    //自定义 WebViewClient 辅助WebView设置处理关于页面跳转，页面请求等操作【处理tel协议和视频通讯请求url的拦截转发】
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            L.e("WebViewClient", url);
            if (!TextUtils.isEmpty(url)) {
                videoFlag = url.contains("video");
            }
            if (url.trim().startsWith("tel")) {//特殊情况tel，调用系统的拨号软件拨号【<a href="tel:1111111111">1111111111</a>】
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            } else {
                String port = url.substring(url.lastIndexOf(":") + 1, url.lastIndexOf("/"));//尝试要拦截的视频通讯url格式(808端口)：【http://xxxx:808/?roomName】
                if (port.equals("808")) {//特殊情况【若打开的链接是视频通讯地址格式则调用系统浏览器打开】
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } else {//其它非特殊情况全部放行
                    view.loadUrl(url);
                }
            }
            return true;
        }
    }

    private Uri imageUri;

    //自定义 WebChromeClient 辅助WebView处理图片上传操作【<input type=file> 文件上传标签】
    public class MyChromeWebClient extends WebChromeClient {
        // For Android 3.0-
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            L.e("For Android 3.0-", "openFileChoose(ValueCallback<Uri> uploadMsg)");
            mUploadMessage = uploadMsg;
            if (videoFlag) {
                recordVideo();
            } else {
                takePhoto();
            }
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            L.e("For Android 3.0+", "openFileChoose( ValueCallback uploadMsg, String acceptType )");
            mUploadMessage = uploadMsg;
            if (videoFlag) {
                recordVideo();
            } else {
                takePhoto();
            }
        }

        //For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            L.e("For Android 4.1", "openFileChoose(ValueCallback<Uri> uploadMsg, String acceptType, String capture)");
            mUploadMessage = uploadMsg;
            if (videoFlag) {
                recordVideo();
            } else {
                takePhoto();
            }
        }

        // For Android 5.0+
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            L.e("For Android 5.0+", "onShowFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)");
            mUploadCallbackAboveL = filePathCallback;

            //通过html的input标签的accept 类型判断执行拍照、相册选择还是录像
            String[] acceptTypes = fileChooserParams.getAcceptTypes();
            if (videoFlag) {
                recordVideo();
            } else {
                takePhoto();
            }
            return true;
        }
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        File newFile = FileUtils.createFile("/DCIM/camera/", "IMG_", ".png", 2);

        Intent takePictureIntent = PhotoUtils.takePicture(this, newFile);
        imageUri = takePictureIntent.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
        startActivityForResult(takePictureIntent, PHOTO_REQUEST);
    }

    /**
     * 录像
     */
    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        //限制时长
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
        //开启摄像机
        startActivityForResult(intent, VIDEO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        } else if (requestCode == VIDEO_REQUEST) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;

            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                if (resultCode == RESULT_OK) {
                    mUploadCallbackAboveL.onReceiveValue(new Uri[]{result});
                    mUploadCallbackAboveL = null;
                } else {
                    mUploadCallbackAboveL.onReceiveValue(new Uri[]{});
                    mUploadCallbackAboveL = null;
                }

            } else if (mUploadMessage != null) {
                if (resultCode == RESULT_OK) {
                    mUploadMessage.onReceiveValue(result);
                    mUploadMessage = null;
                } else {
                    mUploadMessage.onReceiveValue(Uri.EMPTY);
                    mUploadMessage = null;
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != PHOTO_REQUEST || mUploadCallbackAboveL == null) {
            return;
        }
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                results = new Uri[]{imageUri};
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }

                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        mUploadCallbackAboveL.onReceiveValue(results);
        mUploadCallbackAboveL = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webview != null) {
            webview.setWebViewClient(null);
            webview.setWebChromeClient(null);
            webview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webview.clearHistory();
//            ((ViewGroup) webview.getParent()).removeView(webview);
            webview.destroy();
            webview = null;
        }
    }




    public class JSHook {

        String path = "";

        @JavascriptInterface
        public void openCamera() {//秀一秀
            T.showLong("秀一秀");

            Bundle bundle = new Bundle();
            bundle.putBoolean(PickerConfig.KEY_ISTAKE_picture, true);
            bundle.putInt(PickerConfig.KEY_MAX_COUNT, 1);
//            bundle.putSerializable(PickerConfig.KEY_ALREADY_SELECT, new ImageFolder(mDataList));
            JumpUtils.jump(H5Interactive.this, ImgPickerActivity.class, bundle, ImagePicker.Picture_Selection, new ResultCallBack() {
                @Override
                public void onActResult(int requestCode, int resultCode, Intent data) {
                    if (resultCode == RESULT_OK && requestCode == ImagePicker.Picture_Selection && null != data) {
                        ImageFolder imageFolder = (ImageFolder) data.getExtras().getSerializable(ImagePicker.imgFolderkey);
                        path = imageFolder.images.get(0).path;

                        aaa();
                    }
                }
            });
        }

        private void aaa() {
            // 通过Handler发送消息
            webview.post(new Runnable() {
                @Override
                public void run() {
                    // 注意调用的JS方法名要对应上
                    // 调用javascript的callJS()方法
                    webview.loadUrl("javascript:getCamera('" + path + "')");
                }
            });

//           webview.evaluateJavascript("javascript:getcamera('" + path + "')", new ValueCallback<String>() {
//               @Override
//               public void onReceiveValue(String value) {
//                   //此处为 js 返回的结果
//                   T.showLong("js 返回的结果");
//               }
//           });
        }
    }

    /**
     * 跳转到第三方应用
     *
     * @param appId 应用id
     */
    public void gotoOtherApp(@NonNull String appId) {
//        if (isAvilible("com.gcstorage.circleapp", getContext())){

        if (AppUtils.isAvailable(this, appId)) {
            Bundle bundle = new Bundle();
//            bundle.putString("head_pic", GlobalUserInfo.getHeadpic(StoreApplication.instance)); //头像
//            bundle.putString("name", GlobalUserInfo.getName(StoreApplication.instance));        //姓名
//            bundle.putString("depart", GlobalUserInfo.getDeptName(StoreApplication.instance));  //部门
//            bundle.putString("policenum", GlobalUserInfo.getAlarm(StoreApplication.instance));  //警号
//            bundle.putString("idCard", GlobalUserInfo.getIdcard(StoreApplication.instance));    //idCard

            Intent intent = new Intent();
            intent.setData(Uri.parse("csd://pull.csd.demo/cyn?type=110"));
            intent.putExtras(bundle); //这里Intent当然也可传递参数,但是一般情况下都会放到上面的URL中进行传递
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else {
            Toast.makeText(this, "请安装战友圈", Toast.LENGTH_LONG).show();
        }
    }
}
