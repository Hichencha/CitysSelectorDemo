package com.chencha.citysselectordemo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chencha.citysselectordemo.location.BaiduLocation;
import com.chencha.citysselectordemo.utils.SPUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

/**
 * Desc:
 * Author: chencha
 * Date: 17/11/19
 */

public class FistMainActivity extends AppCompatActivity implements View.OnClickListener {

    RxPermissions mRxPermissions;
    private TextView textCity;
    private Button button;
    String cityName, mLocationCity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_layout);
        initView();
    }

    private void initView() {
        mRxPermissions = new RxPermissions(this);

        textCity = (TextView) findViewById(R.id.text_city);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            cityName = bundle.getString("city");
            mLocationCity = bundle.getString("LocationCity");
            if (!TextUtils.isEmpty(mLocationCity)) {
                textCity.setText(mLocationCity);
            } else {
                textCity.setText(cityName);
            }
        }

        mRxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            //如果有数据  就不进行定位操作
                            if (TextUtils.isEmpty(cityName)) {
                                myLocation();
                            }
                        } else {
                            showMissingPermissionDialog();
                        }
                    }
                });

    }

    /**
     * 百度地图定位的请求方法
     */
    private void myLocation() {
        BaiduLocation.getLocation(this);
        BaiduLocation.setMyLocationListener(new BaiduLocation.MyLocationListener() {
            @Override
            public void myLocatin(final double mylongitude, final double mylatitude, final String province, final String city, final String street) {
                // 得到的定位数据进行处理
                Log.e("=========", "mylongitude = " + mylongitude + "mylatitude = " + mylatitude + "province = " + province + "city = " + city + "street = " + street);
                cityName = city;
                SPUtils.put("cityName", city);
                textCity.setText(city);
            }

        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                Bundle bundle = new Bundle();
                Intent intent = new Intent(FistMainActivity.this, MainActivity.class);
                bundle.putString("city", cityName);
                bundle.putString("LocationCity", mLocationCity);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }

    }


    /**
     * //     * 权限管理
     * //
     */
    public void showMissingPermissionDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("帮助");
        builder.setMessage(R.string.basic_string_help_text);

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.basic_quit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton(R.string.basic_settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAppSettings();
            }
        });

        builder.setCancelable(false);

        builder.show();
    }

    // 启动应用的设置
    public void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }
}
