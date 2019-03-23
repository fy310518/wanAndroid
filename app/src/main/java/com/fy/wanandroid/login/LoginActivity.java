package com.fy.wanandroid.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.ParcelableSpan;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.arcsoft.face.AgeInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.Face3DAngle;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.FaceSimilar;
import com.arcsoft.face.GenderInfo;
import com.arcsoft.face.LivenessInfo;
import com.arcsoft.face.VersionInfo;
import com.fy.baselibrary.aop.annotation.ClickFilter;
import com.fy.baselibrary.aop.annotation.NeedPermission;
import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.application.ioc.ConfigUtils;
import com.fy.baselibrary.base.mvp.BaseMVPActivity;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.retrofit.observer.IProgressDialog;
import com.fy.baselibrary.utils.Constant;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.ResUtils;
import com.fy.baselibrary.utils.cache.ACache;
import com.fy.baselibrary.utils.cache.SpfAgent;
import com.fy.baselibrary.utils.notify.T;
import com.fy.wanandroid.R;
import com.fy.wanandroid.entity.LoginBean;
import com.fy.wanandroid.main.MainActivity;
import com.fy.wanandroid.utils.SelectUtils;
import com.gcstorage.scanface.ArcSoftDetectActivity;
import com.gcstorage.scanface.Constants;
import com.gcstorage.scanface.bean.LgCodeBean;
import com.gcstorage.scanface.bean.LgTokenBean;
import com.gcstorage.scanface.bean.LgUserInfoBean;
import com.gcstorage.scanface.request.ApiService;
import com.gcstorage.scanface.request.NetCallBack;
import com.gcstorage.scanface.request.NetDialog;
import com.gcstorage.scanface.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 登录
 * Created by fangs on 2017/12/12.
 */
public class LoginActivity extends BaseMVPActivity<LogingPresenter> implements IBaseActivity, View.OnClickListener, LoginContract.LoginView {
    private static final String TAG = "LoginActivity";
    @BindView(R.id.editName)
    TextInputEditText editName;


    @BindView(R.id.iLayoutPass)
    TextInputLayout iLayoutPass;
    @BindView(R.id.editPass)
    TextInputEditText editPass;

    @BindView(R.id.btnLogin)
    Button btnLogin;

    @Override
    protected LogingPresenter createPresenter() {
        return new LogingPresenter();
    }

    @Override
    public boolean isShowHeadView() {
        return true;
    }

    @Override
    public int setView() {
        return R.layout.activity_login;
    }

    @StatusBar(statusColor = R.color.statusBar, navColor = R.color.statusBar)
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {

        btnLogin.setBackground(SelectUtils.getBtnSelector(R.drawable.shape_btn, 0));
        editPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = editPass.getText().toString().trim();

                if (!TextUtils.isEmpty(text) && text.length() > 12) {
                    iLayoutPass.setError("密码不符合规则!!!");
                } else {
                    if (null != iLayoutPass.getError()) {
                        iLayoutPass.setError(null);
                    }
                }
            }
        });

        activeEngine();

    }

    @ClickFilter
    @NeedPermission({Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE})
    @OnClick({R.id.btnLogin, R.id.tvRegister})
    @Override
    public void onClick(View view) {
        String mUserName = editName.getText().toString().trim();//"fangshuai"
        switch (view.getId()) {
            case R.id.btnLogin:
                String mPassWord = editPass.getText().toString().trim();//"fangs123"

                mPresenter.login(mUserName, mPassWord);
                break;
            case R.id.tvRegister:
//                JumpUtils.jump(mContext, RegisterActivity.class, null);
//                JumpUtils.jump(this, StatusDemoActivity.class, null);
//                JumpUtils.jump(this, TestStatusFragmentActivity.class, null);
//                JumpUtils.jump(this, RevealEffectActivity.class, null);
//                JumpUtils.jump(this, TestActivity.class, null);
//                JumpUtils.jump(this, TestListActivity.class, null);
                Bundle bundle = new Bundle();
                bundle.putString("idCard", mUserName);
                JumpUtils.jump(this, ArcSoftDetectActivity.class, bundle);
                break;
        }
    }

    @Override
    public void loginSuccess(LoginBean login) {
        ACache mCache = ACache.get(ConfigUtils.getAppCtx());
        mCache.put(Constant.userName, login);

        new SpfAgent(Constant.baseSpf)
                .saveBoolean(Constant.isLogin, true)
                .saveString(Constant.userName, login.getUsername())
                .commit(false);

        Bundle bundle = new Bundle();
        bundle.putString("大王", "大王叫我来巡山");
        JumpUtils.jump(LoginActivity.this, MainActivity.class, bundle);
    }

    @Override
    public void onBackPressed() {
        JumpUtils.backDesktop(this);
    }


    /** 人脸引擎类，其中定义了人脸操作相关的函数，包含 SDK 的授权激活、引擎初始 化以及人脸处理相关方法*/
    private FaceEngine faceEngine;
    private int afCode = -1;
    /**
     * 初始化引擎
     */
    private void initEngine() {
        faceEngine = new FaceEngine();
        afCode = faceEngine.init(this, FaceEngine.ASF_DETECT_MODE_IMAGE, FaceEngine.ASF_OP_0_HIGHER_EXT,
                16, 10, FaceEngine.ASF_FACE_RECOGNITION | FaceEngine.ASF_FACE_DETECT | FaceEngine.ASF_AGE | FaceEngine.ASF_GENDER | FaceEngine.ASF_FACE3DANGLE | FaceEngine.ASF_LIVENESS);
        VersionInfo versionInfo = new VersionInfo();
        faceEngine.getVersion(versionInfo);
        if (afCode != ErrorInfo.MOK) {
            Toast.makeText(this, getString(com.gcstorage.scanface.R.string.init_failed, afCode), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("CheckResult")
    private void runprocessImage() {
        IProgressDialog progressDialog = new NetDialog().init(this)
                .setDialogMsg(com.gcstorage.scanface.R.string.user_login);

        Observable.create(new ObservableOnSubscribe<FaceFeature>() {
            @Override
            public void subscribe(ObservableEmitter<FaceFeature> emitter) throws Exception {
                FaceFeature faceFeature = processImage(BitmapFactory.decodeFile("/storage/emulated/0/DCIM/Camera/王光明.jpg"));
                emitter.onNext(faceFeature);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetCallBack<FaceFeature>(progressDialog) {
                    @Override
                    protected void onSuccess(FaceFeature faceFeature) {
                        Constants.faceFeature = faceFeature;
                        T.showLong("图片分析完毕");
                    }
                });
    }

    private void runNetRequest() {
        IProgressDialog progressDialog = new NetDialog().init(this)
                .setDialogMsg(com.gcstorage.scanface.R.string.user_login);

        ArrayMap<String, Object> codeParam = new ArrayMap<>();
        codeParam.put("appid", Constants.appId);
        codeParam.put("uidcard", editName.getText().toString().trim());
        codeParam.put("scope", "snsapi_userinfo");
        codeParam.put("state", System.currentTimeMillis());

        RequestUtils.create(ApiService.class)
                .getcode(codeParam)
                .compose(RxHelper.handleResult())
                .compose(RxHelper.bindToLifecycle(this))
                .flatMap(new Function<LgCodeBean, ObservableSource<LgTokenBean>>() {
                    @Override
                    public ObservableSource<LgTokenBean> apply(LgCodeBean lgCodeBean) throws Exception {
                        ArrayMap<String, Object> tokenParam = new ArrayMap<>();
                        tokenParam.put("appid", Constants.appId);
                        tokenParam.put("secret", Constants.appSecret);
                        tokenParam.put("code", lgCodeBean.getCode());

                        return RequestUtils.create(ApiService.class)
                                .getToken(tokenParam)
                                .compose(RxHelper.handleResult())
                                .compose(RxHelper.bindToLifecycle(LoginActivity.this));
                    }
                }).flatMap(new Function<LgTokenBean, ObservableSource<LgUserInfoBean>>() {
            @Override
            public ObservableSource<LgUserInfoBean> apply(LgTokenBean lgTokenBean) throws Exception {
                ArrayMap<String, Object> infoParam = new ArrayMap<>();
                infoParam.put("appid", Constants.appId);
                infoParam.put("access_token",lgTokenBean.getAccess_token());
                infoParam.put("openid",lgTokenBean.getOpenid());

                return RequestUtils.create(ApiService.class)
                        .getInfo(infoParam)
                        .compose(RxHelper.handleResult())
                        .compose(RxHelper.bindToLifecycle(LoginActivity.this));
            }
        }).flatMap(new Function<LgUserInfoBean, ObservableSource<ResponseBody>>() {
            @Override
            public ObservableSource<ResponseBody> apply(LgUserInfoBean lgUserInfoBean) throws Exception {
                return RequestUtils.create(ApiService.class)
                        .getBitmap(lgUserInfoBean.getAvatar())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(RxHelper.bindToLifecycle(LoginActivity.this));
            }
        }).map(new Function<ResponseBody, FaceFeature>() {
            @Override
            public FaceFeature apply(ResponseBody responseBody) throws Exception {
                return processImage(BitmapFactory.decodeStream(responseBody.byteStream()));
            }
        }) .subscribe(new NetCallBack<FaceFeature>(progressDialog) {
            @Override
            protected void onSuccess(FaceFeature faceFeature) {
                Constants.faceFeature = faceFeature;
                T.showLong("人脸分析完毕");
            }
        });
    }

    /**
     * 根据传递的人脸 特征和 bitmap 是否是同一个人（相似度大于 0.6f认为是同一个人）
     * @return
     */
    public FaceFeature processImage(Bitmap mBitmap) {
        /**
         * 1.准备操作（校验，显示，获取BGR）
         */
        if (mBitmap == null)  return null;

        Bitmap bitmap = mBitmap.copy(Bitmap.Config.ARGB_8888, true);

        final SpannableStringBuilder notificationSpannableStringBuilder = new SpannableStringBuilder();
        if (afCode != ErrorInfo.MOK || bitmap == null || faceEngine == null) {
            T.showLong("图片不存在");
            return null;
        }

        bitmap = ImageUtil.alignBitmapForBgr24(bitmap);
        if (bitmap == null) {
            T.showLong("图片不存在");
            return null;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        //bitmap转bgr
        byte[] bgr24 = ImageUtil.bitmapToBgr(bitmap);

        addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD), "start face detection,imageWidth is " + width + ", imageHeight is " + height + "\n");

        if (bgr24 == null) {
            addNotificationInfo(notificationSpannableStringBuilder, new ForegroundColorSpan(Color.RED), "can not get bgr24 data of bitmap!\n");
            return null;
        }


        List<FaceInfo> faceInfoList = new ArrayList<>();

        /**
         * 2.成功获取到了BGR24 数据，开始人脸检测
         */
        int detectCode = faceEngine.detectFaces(bgr24, width, height, FaceEngine.CP_PAF_BGR24, faceInfoList);
        if (detectCode == ErrorInfo.MOK) {
//            Log.i(TAG, "processImage: fd costTime = " + (System.currentTimeMillis() - fdStartTime));
        }

        //绘制bitmap
        Bitmap bitmapForDraw = bitmap.copy(Bitmap.Config.RGB_565, true);
        Canvas canvas = new Canvas(bitmapForDraw);
        Paint paint = new Paint();
        addNotificationInfo(notificationSpannableStringBuilder, null, "detect result:\nerrorCode is :", String.valueOf(detectCode), "   face Number is ", String.valueOf(faceInfoList.size()), "\n");
        /**
         * 3.若检测结果人脸数量大于0，则在bitmap上绘制人脸框并且重新显示到ImageView，若人脸数量为0，则无法进行下一步操作，操作结束
         */
        if (faceInfoList.size() > 0) {
            addNotificationInfo(notificationSpannableStringBuilder, null, "face list:\n");
            paint.setAntiAlias(true);
            paint.setStrokeWidth(5);
            paint.setColor(Color.YELLOW);
            for (int i = 0; i < faceInfoList.size(); i++) {
                //绘制人脸框
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawRect(faceInfoList.get(i).getRect(), paint);
                //绘制人脸序号
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                int textSize = faceInfoList.get(i).getRect().width() / 2;
                paint.setTextSize(textSize);

                canvas.drawText(String.valueOf(i), faceInfoList.get(i).getRect().left, faceInfoList.get(i).getRect().top, paint);
                addNotificationInfo(notificationSpannableStringBuilder, null, "face[", String.valueOf(i), "]:", faceInfoList.get(i).toString(), "\n");
            }
        } else {
            addNotificationInfo(notificationSpannableStringBuilder, null, "can not do further action, exit!");
            return null;
        }
        addNotificationInfo(notificationSpannableStringBuilder, null, "\n");


        /**
         * 4.上一步已获取到人脸位置和角度信息，传入给process函数，进行年龄、性别、三维角度检测
         */
        int faceProcessCode = faceEngine.process(bgr24, width, height, FaceEngine.CP_PAF_BGR24, faceInfoList, FaceEngine.ASF_AGE | FaceEngine.ASF_GENDER | FaceEngine.ASF_FACE3DANGLE | FaceEngine.ASF_LIVENESS);

        if (faceProcessCode != ErrorInfo.MOK) {
            addNotificationInfo(notificationSpannableStringBuilder, new ForegroundColorSpan(Color.RED), "process failed! code is ", String.valueOf(faceProcessCode), "\n");
        } else {
//            Log.i(TAG, "processImage: process costTime = " + (System.currentTimeMillis() - processStartTime));
        }
        //年龄信息结果
        List<AgeInfo> ageInfoList = new ArrayList<>();
        //性别信息结果
        List<GenderInfo> genderInfoList = new ArrayList<>();
        //人脸三维角度结果
        List<Face3DAngle> face3DAngleList = new ArrayList<>();
        //活体检测结果
        List<LivenessInfo> livenessInfoList = new ArrayList<>();
        //获取年龄、性别、三维角度、活体结果
        int ageCode = faceEngine.getAge(ageInfoList);
        int genderCode = faceEngine.getGender(genderInfoList);
        int face3DAngleCode = faceEngine.getFace3DAngle(face3DAngleList);
        int livenessCode = faceEngine.getLiveness(livenessInfoList);

        if ((ageCode | genderCode | face3DAngleCode | livenessCode) != ErrorInfo.MOK) {
            addNotificationInfo(notificationSpannableStringBuilder, null, "at least one of age,gender,face3DAngle detect failed!,codes are:",
                    String.valueOf(ageCode), " , ", String.valueOf(genderCode), " , ", String.valueOf(face3DAngleCode));
            return null;
        }
        /**
         * 5.年龄、性别、三维角度已获取成功，添加信息到提示文字中
         */
        //年龄数据
        if (ageInfoList.size() > 0) {
            addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD), "age of each face:\n");
        }
        for (int i = 0; i < ageInfoList.size(); i++) {
            addNotificationInfo(notificationSpannableStringBuilder, null, "face[", String.valueOf(i), "]:", String.valueOf(ageInfoList.get(i).getAge()), "\n");
        }
        addNotificationInfo(notificationSpannableStringBuilder, null, "\n");

        //性别数据
        if (genderInfoList.size() > 0) {
            addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD), "gender of each face:\n");
        }
        for (int i = 0; i < genderInfoList.size(); i++) {
            addNotificationInfo(notificationSpannableStringBuilder, null, "face[", String.valueOf(i), "]:"
                    , genderInfoList.get(i).getGender() == GenderInfo.MALE ?
                            "MALE" : (genderInfoList.get(i).getGender() == GenderInfo.FEMALE ? "FEMALE" : "UNKNOWN"), "\n");
        }
        addNotificationInfo(notificationSpannableStringBuilder, null, "\n");


        //人脸三维角度数据
        if (face3DAngleList.size() > 0) {
            addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD), "face3DAngle of each face:\n");
            for (int i = 0; i < face3DAngleList.size(); i++) {
                addNotificationInfo(notificationSpannableStringBuilder, null, "face[", String.valueOf(i), "]:", face3DAngleList.get(i).toString(), "\n");
            }
        }
        addNotificationInfo(notificationSpannableStringBuilder, null, "\n");

        //活体检测数据
        if (livenessInfoList.size() > 0) {
            addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD), "liveness of each face:\n");
            for (int i = 0; i < livenessInfoList.size(); i++) {
                String liveness = null;
                switch (livenessInfoList.get(i).getLiveness()) {
                    case LivenessInfo.ALIVE:
                        liveness = "ALIVE";
                        break;
                    case LivenessInfo.NOT_ALIVE:
                        liveness = "NOT_ALIVE";
                        break;
                    case LivenessInfo.UNKNOWN:
                        liveness = "UNKNOWN";
                        break;
                    case LivenessInfo.FACE_NUM_MORE_THAN_ONE:
                        liveness = "FACE_NUM_MORE_THAN_ONE";
                        break;
                    default:
                        liveness = "UNKNOWN";
                        break;
                }
                addNotificationInfo(notificationSpannableStringBuilder, null, "face[", String.valueOf(i), "]:", liveness, "\n");
            }
        }
        addNotificationInfo(notificationSpannableStringBuilder, null, "\n");

        /**
         * 6.最后将图片内的所有人脸进行一一比对并添加到提示文字中
         */
        if (faceInfoList.size() > 0) {
            FaceFeature[] faceFeatures = new FaceFeature[faceInfoList.size()];
            int[] extractFaceFeatureCodes = new int[faceInfoList.size()];

            addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD), "faceFeatureExtract:\n");
            for (int i = 0; i < faceInfoList.size(); i++) {
                faceFeatures[i] = new FaceFeature();
                //从图片解析出人脸特征数据
                extractFaceFeatureCodes[i] = faceEngine.extractFaceFeature(bgr24, width, height, FaceEngine.CP_PAF_BGR24, faceInfoList.get(i), faceFeatures[i]);

                if (extractFaceFeatureCodes[i] != ErrorInfo.MOK) {
                    addNotificationInfo(notificationSpannableStringBuilder, null, "faceFeature of face[", String.valueOf(i), "]",
                            " extract failed, code is ", String.valueOf(extractFaceFeatureCodes[i]), "\n");
                } else {
//                    Log.i(TAG, "processImage: fr costTime = " + (System.currentTimeMillis() - frStartTime));
                    addNotificationInfo(notificationSpannableStringBuilder, null, "faceFeature of face[", String.valueOf(i), "]",
                            " extract success\n");
                }
            }
            addNotificationInfo(notificationSpannableStringBuilder, null, "\n");

            return faceFeatures[0];

            //人脸特征的数量大于2，将所有特征进行比较
//            if (faceFeatures.length >= 2) {
//                addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD), "similar of faces:\n");
//
//                for (int i = 0; i < faceFeatures.length; i++) {
//                    for (int j = i + 1; j < faceFeatures.length; j++) {
//                        addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD_ITALIC), "compare face[", String.valueOf(i), "] and  face["
//                                , String.valueOf(j), "]:\n");
//                        //若其中一个特征提取失败，则不进行比对
//                        boolean canCompare = true;
//                        if (extractFaceFeatureCodes[i] != 0) {
//                            addNotificationInfo(notificationSpannableStringBuilder, null, "faceFeature of face[", String.valueOf(i), "] extract failed, can not compare!\n");
//                            canCompare = false;
//                        }
//                        if (extractFaceFeatureCodes[j] != 0) {
//                            addNotificationInfo(notificationSpannableStringBuilder, null, "faceFeature of face[", String.valueOf(j), "] extract failed, can not compare!\n");
//                            canCompare = false;
//                        }
//                        if (!canCompare) {
//                            continue;
//                        }
//
//                        FaceSimilar matching = new FaceSimilar();
//                        //比对两个人脸特征获取相似度信息
//                        faceEngine.compareFaceFeature(faceFeatures[i], faceFeatures[j], matching);
//                        //新增相似度比对结果信息
//                        addNotificationInfo(notificationSpannableStringBuilder, null, "similar of face[", String.valueOf(i), "] and  face[",
//                                String.valueOf(j), "] is:", String.valueOf(matching.getScore()), "\n");
//                    }
//                }
//            }

        }

        return null;
    }

    /**
     * 追加提示信息
     * @param stringBuilder 提示的字符串的存放对象
     * @param styleSpan     添加的字符串的格式
     * @param strings       字符串数组
     */
    private void addNotificationInfo(SpannableStringBuilder stringBuilder, ParcelableSpan styleSpan, String... strings) {
        if (stringBuilder == null || strings == null || strings.length == 0) {
            return;
        }
        int startLength = stringBuilder.length();
        for (String string : strings) {
            stringBuilder.append(string);
        }
        int endLength = stringBuilder.length();
        if (styleSpan != null) {
            stringBuilder.setSpan(styleSpan, startLength, endLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    /**
     * 激活引擎
     */
    @SuppressLint("CheckResult")
    public void activeEngine() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                FaceEngine faceEngine = new FaceEngine();
                int activeCode = faceEngine.active(LoginActivity.this, Constants.APP_ID, Constants.SDK_KEY);
                emitter.onNext(activeCode);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer activeCode) throws Exception {
                        if (activeCode == ErrorInfo.MOK) {
                            T.showLong(com.gcstorage.scanface.R.string.active_success);
                        } else if (activeCode == ErrorInfo.MERR_ASF_ALREADY_ACTIVATED) {
                            T.showLong(com.gcstorage.scanface.R.string.already_activated);
                        } else {
                            T.showLong(ResUtils.getReplaceStr(com.gcstorage.scanface.R.string.active_failed, activeCode));
                            return;
                        }

                        initEngine();
//                        runprocessImage();
                        runNetRequest();
                    }
                });
    }
}
