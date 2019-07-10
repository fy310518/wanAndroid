package com.gcstorage.map.shake.activity;

import android.app.Activity;
import android.os.Bundle;

import com.jeff.map.R;
import com.leador.api.maps.LeadorException;
import com.leador.api.maps.MapController;
import com.leador.api.maps.MapView;

public class MapActivity extends Activity {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);



       /* // Create new fragment and transaction
        DemoFragment newFragment = new DemoFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, newFragment)
                .commit();*/


        mapView = findViewById(R.id.leador_map);


        mapView.onCreate(savedInstanceState);//创建地图
        init();

    }

    private MapController lMap;

    private void init() {
        if (lMap == null) {
            try {
                lMap = mapView.getMap();


            } catch (LeadorException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 方法重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

}
