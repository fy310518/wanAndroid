package com.gcstorage.circle;

import android.content.Context;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.rv.adapter.RvCommonAdapter;
import com.fy.baselibrary.utils.Constant;
import com.fy.baselibrary.utils.ResUtils;
import com.fy.baselibrary.utils.TimeUtils;
import com.fy.baselibrary.utils.cache.SpfAgent;
import com.fy.baselibrary.utils.drawable.TintUtils;
import com.fy.baselibrary.utils.imgload.ImgLoadUtils;
import com.fy.baselibrary.utils.notify.T;
import com.gcstorage.circle.bean.LyCircleListBean;
import com.gcstorage.circle.request.ApiService;
import com.gcstorage.circle.request.NetCallBack;
import com.gcstorage.circle.widgets.Constants;
import com.gcstorage.circle.widgets.NineGridView;

import java.util.Arrays;
import java.util.List;

/**
 * 首页列表实体类
 * Created by fangs on 2018/4/16.
 */
public class CircleFragmentAdapter extends RvCommonAdapter<LyCircleListBean> {

    private RequestOptions mRequestOptions;
    private DrawableTransitionOptions mDrawableTransitionOptions;
    private integralAnimListener integralAnimListener;


    public CircleFragmentAdapter(Context context, List<LyCircleListBean> datas) {
        super(context, R.layout.circle_fm_list_item, datas);
        this.mRequestOptions = new RequestOptions().centerCrop();
        this.mDrawableTransitionOptions = DrawableTransitionOptions.withCrossFade();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
//        payloads 对象不会为null，但是它可能是空（empty），这时候需要完整绑定(所以我们在方法里只要判断isEmpty就好，不用重复判空)。
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
//            LyCircleListBean article = mDatas.get(position - getHeadersCount());
//            setCollectImg(holder, article.isCollect());
        }
    }

    @Override
    public void convert(ViewHolder holder, LyCircleListBean article, int position) {
        //头像
        ImgLoadUtils.loadImage(article.getHeadpic(), R.drawable.default_pic_icon, holder.getView(R.id.imgHead));
        //姓名
        holder.setText(R.id.txt_post_name, article.getName());
        //等级
        holder.setText(R.id.tv_level, ResUtils.getReplaceStr(R.string.circleLvN, article.getOnline_level()));
        //部门
        holder.setText(R.id.txt_dynamic_unit, article.getDepartment());
        //时间
        holder.setText(R.id.txt_dynamic_time, TimeUtils.getTimeDifference(article.getPushtime() * 1000));
        //地址
        holder.setText(R.id.txt_dynamic_location, TextUtils.isEmpty(article.getAddress()) ? "未公开位置信息" : article.getAddress());
        //浏览次数
        holder.setText(R.id.txt_read_count, ResUtils.getReplaceStr(R.string.browseNum, article.getRead_count()));
        //圈子内容
        holder.setText(R.id.txt_content, article.getContent());
//        holder.setOnClickListener(R.id.txt_state, v -> setTextState(holder, ));//全文 伸缩 未实现

        //点赞数 和 是否点赞图标
        TextView txt_good_count = holder.getView(R.id.txt_good_count);
        txt_good_count.setText(article.getPraise_count());
        int ispraiseImgId = article.getIspraise().equals("1") ? R.drawable.icon_dynamic_honor_check_new : R.drawable.icon_dynamic_honor_new;
        TintUtils.setTxtIconLocal(txt_good_count, TintUtils.getDrawable(ispraiseImgId, 0), 1);
        txt_good_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String praiseAction = "";
                String isPraise = "";
                switch (article.getIspraise()) {
                    case "0":       //没有点赞
                        praiseAction = Constants.POST_PRAISE_ADD;
                        isPraise = "1";
                        break;
                    case "1":       //已点赞
                        praiseAction = Constants.POST_PRAISE_DEL;
                        isPraise = "0";
                        break;
                }

                setPraise(article, praiseAction, isPraise, position);
            }
        });
        //评论数
        holder.setText(R.id.txt_comment_count, article.getComment_count());
        holder.setOnClickListener(R.id.txt_comment_count, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //积分
        TextView tv_integral_count = holder.getView(R.id.tv_integral_count);
        int integralColor = article.getIspraise().equals("1") ? R.color.color_ffb100 : R.color.text_color_gray_80;
        tv_integral_count.setTextColor(ResUtils.getColor(integralColor));
        tv_integral_count.setText(article.getIntegral_count());
        int integral_count = article.getIspraise().equals("1") ? R.drawable.icon_circle_integral : R.drawable.icon_circle_integral_none;
        TintUtils.setTxtIconLocal(tv_integral_count, TintUtils.getDrawable(integral_count, 0), 1);
        tv_integral_count.setOnClickListener(v -> integralGiveReward(article, position));


        List<LyCircleListBean.PraiseListBean> praiseList = article.getPraise_list();
        if (null != praiseList && praiseList.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (LyCircleListBean.PraiseListBean praiseListBean : praiseList) {
                sb.append(praiseListBean.getPraise_name()).append("，");
            }

            //点赞人名 列表
            holder.setText(R.id.praise_content, sb.substring(0, sb.toString().length() - 1));
        }


        //圈子 图片列表
        if (!TextUtils.isEmpty(article.getPicture())) {
            List<String> imgList = Arrays.asList(article.getPicture().split(","));
            NineGridView nineGridView = holder.getView(R.id.nine_grid_view);
            nineGridView.setVisibility(View.VISIBLE);
            nineGridView.setAdapter(new NineImageAdapter(mContext, mRequestOptions,
                    mDrawableTransitionOptions, imgList));
        }


    }

    public void setIntegralAnimListener(CircleFragmentAdapter.integralAnimListener integralAnimListener) {
        this.integralAnimListener = integralAnimListener;
    }

    //点赞 与 取消点赞
    private void setPraise(LyCircleListBean article, String praiseAction, final String isPraise, int position) {
        ArrayMap<String, Object> params = new ArrayMap<>();
        params.put("action", praiseAction);
        params.put("alarm", SpfAgent.getString(Constant.baseSpf, Constant.userName));
        params.put("token", SpfAgent.getString(Constant.baseSpf, Constant.token));
        params.put("postid", article.getPostid());
        params.put("postuser", article.getAlarm());
        RequestUtils.create(ApiService.class)
                .setpraiseNew(params)
                .compose(RxHelper.handleResult())
                .compose(RxHelper.bindToLifecycle(mContext))
                .subscribe(new NetCallBack<List<LyCircleListBean.PraiseListBean>>() {
                    @Override
                    protected void onSuccess(List<LyCircleListBean.PraiseListBean> dataList) {

                        if (null == dataList || dataList.size() == 0) return;

                        LyCircleListBean.PraiseListBean circlePraise = dataList.get(0);
                        List<LyCircleListBean.PraiseListBean> praiseList = article.getPraise_list();

                        int count = Integer.parseInt(article.getPraise_count());
                        if (isPraise.equals("0")) {//取消点赞
                            praiseList.remove(circlePraise);
                            article.setPraise_count((count - 1) + "");
                        } else {
                            praiseList.add(0, circlePraise);
                            article.setPraise_count((count + 1) + "");
                        }

                        article.setIspraise(isPraise);
                        notifyItemChanged(position);
                    }

                    @Override
                    protected void updataLayout(int flag) {
                    }
                });
    }

    //积分 打赏
    private void integralGiveReward(LyCircleListBean article, int position) {
        ArrayMap<String, Object> params = new ArrayMap<>();
        params.put("alarm", SpfAgent.getString(Constant.baseSpf, Constant.userName));
        params.put("token", SpfAgent.getString(Constant.baseSpf, Constant.token));
        params.put("toalarm", article.getAlarm());
        params.put("postid", article.getPostid());
        params.put("source", "");
        params.put("action", "reward");

        RequestUtils.create(ApiService.class)
                .integralGiveReward(params)
                .compose(RxHelper.handleResult())
                .compose(RxHelper.bindToLifecycle(mContext))
                .subscribe(new NetCallBack<String>() {
                    @Override
                    protected void onSuccess(String integral) {
                        T.showLong("打赏成功");
                        article.setIspraise("1");
                        article.setIntegral_count(integral);
                        if (null != integralAnimListener) integralAnimListener.runIntegralAnim();
                        notifyItemChanged(position);
                    }

                    @Override
                    protected void updataLayout(int flag) {
                    }
                });
    }


    /**
     * 定义积分动画回调接口
     */
    public interface integralAnimListener {
        void runIntegralAnim();
    }


}
