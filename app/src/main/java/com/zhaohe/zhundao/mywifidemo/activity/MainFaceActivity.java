package com.zhaohe.zhundao.mywifidemo.activity;

import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.mywifidemo.adapter.DeviceAdapter;
import com.zhaohe.zhundao.mywifidemo.utils.WifiUtils;
import com.zhaohe.zhundao.ui.ToolBarActivity;

import java.util.ArrayList;
import java.util.List;


public class MainFaceActivity extends ToolBarActivity {
    private LinearLayout ll_header;
    private RecyclerView rlv;
    private DeviceAdapter mDeviceAdapter;
    private List<String> mList;
    private SwipeRefreshLayout srl;
    private static final int REQUEST_CODE_PERMISSION = 100;

    private static final int REQUEST_CODE_SETTING = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBarNew("连接设备",R.layout.activity_main_face);
        initPm();
        initViews();

    }

    private void initPm() {
        AndPermission.with(this)
                .requestCode(REQUEST_CODE_PERMISSION)
                .permission(Permission.STORAGE, Permission.LOCATION)
                .callback(permissionListener)
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                // 这样避免用户勾选不再提示，导致以后无法申请权限。
                // 你也可以不设置。
                .rationale(null)
                .start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //先判断当前wifi是否开启
        initViews();
        if (WifiUtils.isWiFiActive(this)) {
            mList = new ArrayList<>();
            List<ScanResult> list = WifiUtils.getScanWifiInfo(MainFaceActivity.this);//获取所有wifi名称
            for (int i = 0; i < list.size(); i++) {
                String SSID = list.get(i).SSID;
                if (SSID.contains("84E0F4200")) {
                    //取出与设备相关的wifi
                    mList.add(SSID);
                }
            }
            if (mList.size() != 0) {
                //如果设备wifi列表不为空
                ll_header.setVisibility(View.GONE);
                mDeviceAdapter = new DeviceAdapter(MainFaceActivity.this, mList);
                rlv.setAdapter(mDeviceAdapter);
            } else {
                //如果设备wifi列表为空,显示无数据界面
                ll_header.setVisibility(View.VISIBLE);
            }

        } else {
            ll_header.setVisibility(View.VISIBLE);
            Toast.makeText(MainFaceActivity.this, "请先打开手机Wifi!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)); //直接进入手机中的wifi网络设置界面
        }

    }

    private void initViews() {
        //初始化控件
        ll_header = (LinearLayout) findViewById(R.id.ll_header);
        rlv = (RecyclerView) findViewById(R.id.rlv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlv.setLayoutManager(layoutManager);
        srl = (SwipeRefreshLayout) findViewById(R.id.srl);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mList.clear();
                List<ScanResult> list = WifiUtils.getScanWifiInfo(MainFaceActivity.this);//获取所有wifi名称
                for (int i = 0; i < list.size(); i++) {
                    String SSID = list.get(i).SSID;
                    if (SSID.contains("84E0F4200")) {
                        //取出与设备相关的wifi
                        mList.add(SSID);
                        mDeviceAdapter.notifyDataSetChanged();
                    }
                }
                srl.setRefreshing(false);
            }
        });
    }

    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {
                case REQUEST_CODE_PERMISSION: {
//                    ToastUtil.makeText(getApplicationContext(), "授权成功！");
                    break;
                }
            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            switch (requestCode) {
                case REQUEST_CODE_PERMISSION: {
                    ToastUtil.makeText(getApplicationContext(), "授权失败！请去授权管理软件，开启软件的位置和存储权限");

                    break;
                }
            }
        }
    };

}
