package com.gcstorage.map.maker;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gcstorage.framework.net.listener.ActionCallbackListener;
import com.gcstorage.framework.utils.Logger;
import com.google.gson.Gson;
import com.jeff.map.R;
import com.leador.api.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;

public class AddMarkerActivity extends Activity {

    private View iv_back;
    private TextView tv_title;
    private TextView tv_right;
    private EditText et_title;
    private TextView txt_name;
    private TextView locaction_address;
    private EditText et_content;
    private LatLng position;
    private MapApi mapApi;
    private MarkerBean markerBean;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_marker);
        position = getIntent().getParcelableExtra("markerPostion");
        markerBean = (MarkerBean) getIntent().getSerializableExtra("markerBean");
        initView();
    }

    private void initView() {
        mapApi = MapApi.getInstan();
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("新建标记");
        tv_right = findViewById(R.id.tv_right);
        et_title = findViewById(R.id.et_title);
        txt_name = findViewById(R.id.txt_name);
        txt_name.setText(markerBean.name);
        locaction_address = findViewById(R.id.tv_locaction_address);
        locaction_address.setText(markerBean.markeraddress);
        et_content = findViewById(R.id.et_content);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updataMaker();
            }
        });
    }

    private void updataMaker() {
        String signName = et_title.getText().toString();
        if (signName == null) {
            return;
        }
        String content = et_content.getText().toString();
        if (content == null) {
            return;
        }
        double longitude = position.longitude;
        double latitude = position.latitude;
        //地址转换
        MarkerBean.Address addressBean = new MarkerBean.Address();
        addressBean.latitude = latitude;
        addressBean.longitude = longitude;
        addressBean.addre = markerBean.markeraddress;
        String toJson = new Gson().toJson(addressBean);
        mapApi.createMaker(markerBean.taskId, Integer.valueOf(markerBean.susType), signName,
                toJson, content, markerBean.alarm, markerBean.name, this, new ActionCallbackListener<String>() {
                    @Override
                    public void onSuccess(String data) throws Exception {
                        Logger.e("dong", "标记上传成功==" + data.toString());
                        EventBus.getDefault().post(new MapEvents.MarkerUpdate());
                        Toast.makeText(AddMarkerActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(String errorEvent, String message) {
                        Logger.e("dong", "标记上传onFailure==");

                    }
                });
    }
}
