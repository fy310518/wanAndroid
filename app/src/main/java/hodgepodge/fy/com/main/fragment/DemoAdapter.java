package hodgepodge.fy.com.main.fragment;

import android.content.Context;

import com.fy.baselibrary.rv.adapter.RvCommonAdapter;
import com.fy.baselibrary.base.ViewHolder;

import java.util.List;

import hodgepodge.fy.com.R;

/**
 * Created by Administrator on 2018/3/9.
 */
public class DemoAdapter extends RvCommonAdapter<String>{


    public DemoAdapter(Context context, List<String> datas) {
        super(context, R.layout.demo_rv_item, datas);
    }

    @Override
    public void convert(ViewHolder holder, String t, int position) {
        holder.setText(R.id.txtDemo, t);


    }
}
