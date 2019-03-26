package com.gcstorage.circle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.ArrayMap;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.fy.baselibrary.aop.resultfilter.ResultCallBack;
import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.rv.adapter.MultiCommonAdapter;
import com.fy.baselibrary.rv.adapter.MultiTypeSupport;
import com.fy.baselibrary.utils.Constant;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.ResUtils;
import com.fy.baselibrary.utils.SpanUtils;
import com.fy.baselibrary.utils.TimeUtils;
import com.fy.baselibrary.utils.cache.SpfAgent;
import com.fy.baselibrary.utils.drawable.TintUtils;
import com.fy.baselibrary.utils.imgload.ImgLoadUtils;
import com.fy.baselibrary.utils.notify.T;
import com.fy.img.picker.PickerConfig;
import com.fy.img.picker.bean.ImageFolder;
import com.fy.img.picker.bean.ImageItem;
import com.fy.img.picker.preview.PicturePreviewActivity;
import com.gcstorage.circle.bean.CircleListBean;
import com.gcstorage.circle.bean.CommentListBean;
import com.gcstorage.circle.bean.LyCircleListBean;
import com.gcstorage.circle.comment.CircleCommentActivity;
import com.gcstorage.circle.details.CircleDetailsActivity;
import com.gcstorage.circle.request.ApiService;
import com.gcstorage.circle.request.NetCallBack;
import com.gcstorage.circle.widgets.Constants;
import com.gcstorage.circle.widgets.NineGridView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 战友圈列表 adapter
 * Created by fangs on 2018/4/16.
 */
public class CircleFragmentAdapter extends MultiCommonAdapter<CircleListBean> {

    private RequestOptions mRequestOptions;
    private DrawableTransitionOptions mDrawableTransitionOptions;
    private integralAnimListener integralAnimListener;

    private boolean isDetails;//是否是 帖子详情，是则没有点击事件

    public CircleFragmentAdapter(Context context, List<CircleListBean> datas) {
        super(context, datas, new MultiTypeSupport<CircleListBean>(){
            @Override
            public int getLayoutId(int itemType) {
                if (itemType == 0) {
                    return R.layout.circle_fm_list_item;
                } else {
                    return R.layout.circle_fm_list_item_comment;
                }
            }

            @Override
            public int getItemViewType(int position, CircleListBean circleListBean) {
                return circleListBean.getIsShowCamera();
            }
        });
        this.mRequestOptions = new RequestOptions().centerCrop();
        this.mDrawableTransitionOptions = DrawableTransitionOptions.withCrossFade();
    }

    @Override
    public void convert(ViewHolder holder, CircleListBean circleListBean, int position) {
        if (circleListBean.getIsShowCamera() == 0) {//帖子列表
            drawCircleListItem(holder, circleListBean.getLyCircleListBean(), position);
            if (!circleListBean.isExistenceComment()) {//代码设置 分割线
                ConstraintLayout itemLayout = holder.getView(R.id.itemLayout);
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(itemLayout.getLayoutParams());
                params.setMargins(0, 0, 0, (int) ResUtils.getDimen(R.dimen.spacing_small_much));
                itemLayout.setLayoutParams(params);
            }
        } else {//评论列表
            drawCommentListItem(holder, circleListBean.getCommentListBean(), position);

            //代码设置 分割线
            LinearLayout itemll = holder.getView(R.id.itemll);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemll.getLayoutParams());
            if (circleListBean.isExistenceComment()) {
                params.setMargins(0, 0, 0, (int) ResUtils.getDimen(R.dimen.spacing_small_much));
            } else {
                params.setMargins(0, 0, 0, 0);
            }
            itemll.setLayoutParams(params);
        }
    }

    public void drawCircleListItem(ViewHolder holder, LyCircleListBean article, int position) {
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

        if (!isDetails){
            //进入帖子详情
            holder.setOnClickListener(R.id.itemLayout, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("LyCircleListBean", article);
                    JumpUtils.jump((FragmentActivity) mContext, CircleDetailsActivity.class, bundle);
                }
            });
        }

        //头像
        ImgLoadUtils.loadImage(article.getHeadpic(), R.drawable.default_pic_icon, holder.getView(R.id.imgHead));
        holder.setOnClickListener(R.id.imgHead, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showLong("进入个人主页");
            }
        });

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
                Bundle bundle = new Bundle();
//                bundle.putParcelable("LyCircleListBean", article);
                bundle.putString("postId", article.getPostid());
                JumpUtils.jump((FragmentActivity)mContext, CircleCommentActivity.class, bundle, R.id.txt_comment_count, new ResultCallBack(){
                    @Override
                    public void onActResult(int requestCode, int resultCode, Intent data) {
                        if (resultCode == Activity.RESULT_OK && requestCode == R.id.txt_comment_count && null != data) {
                            Bundle bundle1 = data.getExtras();
                            assert bundle1 != null;
                            ArrayList<CommentListBean> commentBeans = (ArrayList<CommentListBean>) bundle1.getSerializable("CommentListBeans");
                            assert commentBeans != null;

                            CommentListBean commentListBeanData = null;
                            //获取新旧 评论列表 差异
                            for (CommentListBean newCommentListBean : commentBeans) {
                                for (CommentListBean oldCommentListBean : article.getComment_list()) {
                                    if (!newCommentListBean.equals(oldCommentListBean)) {
                                        commentListBeanData = newCommentListBean;
                                    }
                                }
                            }

                            //如果为空 说明 旧数据集没有评论
                            if (null == commentListBeanData)commentListBeanData = commentBeans.get(0);

                            //更新评论数
                            mDatas.get(position).getLyCircleListBean().setComment_count(commentBeans.size() + "");
                            notifyItemChanged(position);

                            //把差异数据 插入到 列表数据源,并更新相关的两个 评论条目
                            int spli = position + article.getComment_list().size();
                            mDatas.get(spli).setExistenceComment(false);
                            addData(spli + 1, new CircleListBean(1, commentListBeanData, true));
                            notifyItemRangeChanged(spli, 2);
                        }
                    }
                });
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
            nineGridView.setOnImageClickListener(new NineGridView.OnImageClickListener() {
                @Override
                public void onImageClick(int position, View view) {
                    List<ImageItem> images = new ArrayList<>();
                    for (String url : imgList){
                        images.add(new ImageItem(url, false));
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PickerConfig.KEY_IMG_FOLDER, new ImageFolder(images));
                    bundle.putInt(PickerConfig.KEY_CURRENT_POSITION, position);
                    JumpUtils.jump((Activity) mContext, PicturePreviewActivity.class, bundle);
                }
            });
        }
    }

    private void drawCommentListItem(ViewHolder holder, CommentListBean commentBean, int position){
        SpannableStringBuilder sbsb = SpanUtils.getBuilder("")
                .setFgColor(R.color.statusBar)
                .append(commentBean.getReply_name(), new SpanUtils.ClickText() {
                    @Override
                    public void onClick(@NonNull View view) {
                        T.showLong("1111");
                    }
                })
                .setFgColor(R.color.txtSuperColor)
                .append(ResUtils.getStr(R.string.reply), null)
                .setFgColor(R.color.statusBar)
                .append(commentBean.getPublish_name(), new SpanUtils.ClickText() {
                    @Override
                    public void onClick(@NonNull View view) {
                        T.showLong("bbbb");
                    }
                })
                .setFgColor(R.color.txtSuperColor)
                .append(" ：", null)
                .setFgColor(R.color.txtSuperColor)
                .append(commentBean.getContent(), new SpanUtils.ClickText() {
                    @Override
                    public void onClick(@NonNull View view) {
                        T.showLong("内容");
                    }
                })
                .create();

        TextView txtcontent = holder.getView(R.id.txt_content);
        txtcontent.setText(sbsb);//评论内容
        txtcontent.setMovementMethod(LinkMovementMethod.getInstance());
        txtcontent.setFocusable(false);

        NineGridView nineGridView = holder.getView(R.id.nine_grid_view);
        if (!TextUtils.isEmpty(commentBean.getPicture())) {
            List<String> imgList = Arrays.asList(commentBean.getPicture().split(","));
            nineGridView.setVisibility(View.VISIBLE);
            nineGridView.setAdapter(new NineImageAdapter(mContext, mRequestOptions,
                    mDrawableTransitionOptions, imgList));
        } else {
            nineGridView.setVisibility(View.GONE);
        }
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


    public void setDetails(boolean details) {
        isDetails = details;
    }

    public void setIntegralAnimListener(CircleFragmentAdapter.integralAnimListener integralAnimListener) {
        this.integralAnimListener = integralAnimListener;
    }

    /**
     * 定义积分动画回调接口
     */
    public interface integralAnimListener {
        void runIntegralAnim();
    }


}
