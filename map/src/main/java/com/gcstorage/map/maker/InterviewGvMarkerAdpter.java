package com.gcstorage.map.maker;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeff.map.R;

public class InterviewGvMarkerAdpter extends ArrayAdapter<GridviewMarkerPO> {

    private int resource;
    private Context mContext;

    public InterviewGvMarkerAdpter(Context context, int resource) {
        super(context, resource);
        this.resource = resource;
        this.mContext = context;

        this.add(new GridviewMarkerPO("2", "嫌疑车", R.mipmap.mark_carg, R.mipmap.mark_carw));
        this.add(new GridviewMarkerPO("3", "嫌疑人", R.mipmap.mark_mang, R.mipmap.mark_manw));
        this.add(new GridviewMarkerPO("5", "集合点", R.mipmap.mark_bannerg, R.mipmap.mark_bannerw));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        GridviewMarkerPO data = getItem(position);
        View view = convertView;
        if (view == null) {
            view = View.inflate(mContext, R.layout.gridview_chushimark, null);
        }
        ImageView btn_icon = (ImageView) view.findViewById(R.id.btn_icon);
        TextView tv_text = (TextView) view.findViewById(R.id.tv_title);

        tv_text.setText(data.markerTitle);
        if (!data.isSelect) {
            btn_icon.setImageResource(data.iconNormalRes);
        } else {
            btn_icon.setImageResource(data.iconPressedRes);
        }
        return view;
    }
}
