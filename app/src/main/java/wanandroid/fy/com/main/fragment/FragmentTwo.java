package wanandroid.fy.com.main.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fy.baselibrary.base.BaseFragment;
import com.fy.baselibrary.base.PopupDismissListner;
import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.base.popupwindow.NicePopup;
import com.fy.baselibrary.rv.adapter.RvCommonAdapter;
import com.fy.baselibrary.rv.divider.ListItemDecoration;
import com.fy.baselibrary.utils.T;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import wanandroid.fy.com.R;

/**
 * Created by Administrator on 2017/12/12.
 * 数据
 */
public class FragmentTwo extends BaseFragment {

    @BindView(R.id.rvDemo)
    RecyclerView rvDemo;

    @Override
    protected int setContentLayout() {
        return R.layout.fragment_main_two;
    }

    @Override
    protected void baseInit() {
        initRv();
    }

    @OnClick({R.id.tvPopup})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.tvPopup:
                showPopupWindow(view);
                break;
            case R.id.pop_computer:
                T.showLong("computer");
                mPopWindow.dismiss();
                break;
            case R.id.pop_financial:
                T.showLong("financial");
                mPopWindow.dismiss();
                break;
            case R.id.pop_manage:
                T.showLong("manage");
                mPopWindow.dismiss();
                break;
        }
    }

    private PopupWindow mPopWindow;
    private void showPopupWindow(View view) {

        mPopWindow = NicePopup.Builder.init()
                .setLayoutId(R.layout.demo_popup)
                .setConvertListener(new NicePopup.PopupConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder) {
                        TextView tv1 = holder.getView(R.id.pop_computer);
                        TextView tv2 = holder.getView(R.id.pop_financial);
                        TextView tv3 = holder.getView(R.id.pop_manage);

                        tv1.setText("大王叫我来巡山");
                        tv1.setOnClickListener(FragmentTwo.this);
                        tv2.setOnClickListener(FragmentTwo.this);
                        tv3.setOnClickListener(FragmentTwo.this);
                    }
                }).create()
                .setDismissListner(new PopupDismissListner() {
                    @Override
                    public void onDismiss() {
                        T.showLong("popupWindow 已关闭");
                    }
                }).setAnim(R.style.AnimUp)
                .setOutside(true)
                .onCreateView(getContext());

        mPopWindow.showAtLocation(getActivity().findViewById(android.R.id.content),
                Gravity.BOTTOM, 0, 0);

    }

    private void initRv(){
        rvDemo.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvDemo.addItemDecoration(new ListItemDecoration(getActivity(), 10));

        rvDemo.setAdapter(new RvCommonAdapter<User>(getActivity(), R.layout.main_demo_rv_item, getData()) {
            @Override
            public void convert(ViewHolder holder, User user, int position) {
                holder.setText(R.id.text, user.getName());
            }
        });
    }

    private List<User> getData() {
        List<User> Users = new ArrayList<>();

        User User = new User("香港明星");
        Users.add(User);

        User ldh = new User("刘德华", 10);
        Users.add(ldh);
        User zxy = new User("张学友", 10);
        Users.add(zxy);
        User zrf = new User("周润发", 10);
        Users.add(zrf);
        User lcw = new User("梁朝伟", 10);
        Users.add(lcw);
        User wyj = new User("吴毅将", 10);
        Users.add(wyj);
        User lm = new User("黎明", 10);
        Users.add(lm);
        User cgx = new User("陈冠希", 10);
        Users.add(cgx);
        User gfc = new User("郭富城", 10);
        Users.add(gfc);
        User xtf = new User("谢霆锋", 10);
        Users.add(xtf);

//        User UserTw = new User("台湾明星：指的是中国台湾的一些有名气的电影，电视演员和歌手，他们具有很高的人气，成名时间早，成名时间久");
//        Users.add(UserTw);

        User rxq = new User("任贤齐", 10);
        Users.add(rxq);
        User mtw = new User("孟庭苇", 10);
        Users.add(mtw);
        User ldy = new User("罗大佑", 10);
        Users.add(ldy);
        User lzs = new User("李宗盛", 10);
        Users.add(lzs);
        User xc = new User("小虫", 10);
        Users.add(xc);
        User zhj = new User("周华健", 10);
        Users.add(zhj);
        User zhl = new User("周杰伦", 10);
        Users.add(zhl);

//        User UserNl = new User("内陆明星");
//        Users.add(UserNl);

        User lh = new User("鹿晗", 10);
        Users.add(lh);
        User wzw = new User("王志文", 10);
        Users.add(wzw);
        User yq = new User("羽泉", 10);
        Users.add(yq);
        User lxl = new User("李小璐", 10);
        Users.add(lxl);
        User hh = new User("韩红", 10);
        Users.add(hh);
        User ny = new User("那英", 10);
        Users.add(ny);
        User lhh = new User("刘欢", 10);
        Users.add(lhh);
        User yk = new User("杨坤", 10);
        Users.add(yk);
        User zj = new User("周杰", 10);
        Users.add(zj);

//        User UserOm = new User("美国明星");
//        Users.add(UserOm);
        User mm = new User("梅梅", 10);
        Users.add(mm);
        User ade = new User("Gaga", 10);
        Users.add(ade);
        User hff = new User("黑寡妇", 10);
        Users.add(hff);
        User xlz = new User("小李子", 10);
        Users.add(xlz);

        User UserNba = new User("NBA明星");
        Users.add(UserNba);
        User xhd = new User("小皇帝", 10);
        Users.add(xhd);
        User kb = new User("科比", 10);
        Users.add(kb);
        User ym = new User("姚明", 10);
        Users.add(ym);
        User md = new User("麦迪", 10);
        Users.add(md);
        User dlt = new User("杜兰特", 10);
        Users.add(dlt);
        User kl = new User("库里", 10);
        Users.add(kl);
        User ouw = new User("欧文", 10);
        Users.add(ouw);
        User qd = new User("乔丹", 10);
        Users.add(qd);
        User alzw = new User("奥拉朱旺", 10);
        Users.add(alzw);
        User pp = new User("皮蓬", 10);
        Users.add(pp);
        User ldm = new User("罗德曼", 10);
        Users.add(ldm);
        User ke = new User("科尔", 10);
        Users.add(ke);
        User pesi = new User("皮尔斯", 10);
        Users.add(pesi);
        User jnt = new User("加内特", 10);
        Users.add(jnt);
        User lal = new User("雷阿伦", 10);
        Users.add(lal);
        User zmg = new User("字母哥", 10);
        Users.add(zmg);
        User adn = new User("安东尼", 10);
        Users.add(adn);

//        User UserDy = new User("导演");
//        Users.add(UserDy);
        User jzk = new User("贾樟柯", 10);
        Users.add(jzk);
        User ly = new User("李杨", 10);
        Users.add(ly);
        User fxg = new User("冯小刚", 10);
        Users.add(fxg);
        User lyy = new User("娄烨", 10);
        Users.add(lyy);
        User zym = new User("张艺谋", 10);
        Users.add(zym);

        return Users;
    }
}
