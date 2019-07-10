package com.gcstorage.parkinggather.main;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.gcstorage.parkinggather.R;
import com.gongwen.marqueen.MarqueeFactory;

/**
 * Created by Administrator on 2017/6/6.
 */
public class TextMargeeAdapter extends MarqueeFactory<TextView, SpannableString> {

    private LayoutInflater inflater;

    public TextMargeeAdapter(Context mContext) {
        super(mContext);
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public TextView generateMarqueeItemView(SpannableString data) {
        TextView marqueeView = (TextView) inflater.inflate(R.layout.parking_gather_marquee_item, null);
        marqueeView.setText(data, TextView.BufferType.SPANNABLE);
        return marqueeView;
    }
}
