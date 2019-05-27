package com.gc.unifiedlogin.sysnotify;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;

import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.rv.adapter.RvCommonAdapter;
import com.fy.baselibrary.utils.AppUtils;
import com.fy.baselibrary.utils.GsonUtils;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.drawable.ShapeBuilder;
import com.fy.baselibrary.utils.imgload.ImgLoadUtils;
import com.fy.baselibrary.utils.notify.T;
import com.fy.baselibrary.widget.NineGridView;
import com.gc.unifiedlogin.R;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.List;

/**
 * DESCRIPTION：应用  系统通知 消息列表 适配器
 * Created by fangs on 2019/5/14 11:38.
 */
public class AppNotifyAdapter extends RvCommonAdapter<MsgData> {

    public AppNotifyAdapter(Context context, List<MsgData> datas) {
        super(context, R.layout.system_notify_adapter_item, datas);
    }

    @Override
    public void convert(ViewHolder holder, MsgData chatMsgEntity, int position) {
        ShapeBuilder.create()
                .solid(R.color.white)
                .radius(12)
                .setBackBg(holder.getView(R.id.appNotifyBg));

        final JsonObject sysNotifyJson = GsonUtils.jsonStrToJsonObj(GsonUtils.toJson(chatMsgEntity));

        ImgLoadUtils.loadRadiusImg(sysNotifyJson.get("APPID_IMG").getAsString(), R.drawable.default_pic_icon, (AppCompatImageView) holder.getView(R.id.imgHead));
        holder.setText(R.id.txtAppname, sysNotifyJson.get("APPID_NAME").getAsString());

        holder.setText(R.id.txtTitle, sysNotifyJson.get("TITLE").getAsString());
        holder.setText(R.id.txtTime, sysNotifyJson.get("SEND_TIME").getAsString());
        holder.setText(R.id.txtContent, sysNotifyJson.get("TEXT_DETAIL").getAsString());

        String submitTxt = sysNotifyJson.get("SUBMIT_TEXT").getAsString();
        holder.setText(R.id.txtInfo, TextUtils.isEmpty(submitTxt) ? "详情" : submitTxt);
        holder.setOnClickListener(R.id.txtInfo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pagepath = sysNotifyJson.get("PAGEPATH").getAsString();

                if (pagepath.startsWith("http")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("url", pagepath);
                    JumpUtils.jump((Activity) mContext, "com.gcstrage.h5App", bundle);
                } else {
                    if (AppUtils.isAvailable(pagepath)) {
                        JumpUtils.jumpUrl((Activity) mContext, getGotoAppUrl(pagepath), null);
                    } else {
                        T.showLong("应用未安装");
                    }
                }
            }
        });

        String fileList = sysNotifyJson.get("TEXT_FILE").getAsString();
        NineGridView nineGridView = holder.getView(R.id.notifyGridView);
        if (!TextUtils.isEmpty(fileList)) {
            final List<String> imgList = Arrays.asList(fileList.split(","));
            nineGridView.setVisibility(View.VISIBLE);
            nineGridView.setAdapter(new NineImageAdapter(mContext, imgList));
            nineGridView.setOnImageClickListener(new NineGridView.OnImageClickListener() {
                @Override
                public void onImageClick(int position, View view) {
//                    List<ImageItem> images = new ArrayList<>();
//                    for (String url : imgList){
//                        images.add(new ImageItem(ImgUtils.watermark(url), false));
//                    }
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(PickerConfig.KEY_IMG_FOLDER, new ImageFolder(images));
//                    bundle.putInt(PickerConfig.KEY_CURRENT_POSITION, position);
//                    JumpUtils.jump((Activity) mContext, PicturePreviewActivity.class, bundle);
                }
            });
        } else {
            nineGridView.setVisibility(View.GONE);
        }
    }


    /**
     * 获取门户定义的 启动第三方应用的 URL
     * @param appId
     * @return
     */
    public static String getGotoAppUrl(String appId){
        return "gc://" + appId + "/gc/start";
    }
}
