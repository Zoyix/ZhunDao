package com.zhaohe.zhundao.mywifidemo.activity;

import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.mywifidemo.adapter.WifiAdapter;
import com.zhaohe.zhundao.mywifidemo.utils.WifiUtils;
import com.zhaohe.zhundao.ui.ToolBarActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class SelectWifiActivity extends ToolBarActivity {
    private LinearLayout ll_header;
    private RecyclerView rlv;
    private WifiAdapter mWifiAdapter;
    private List<String> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBarNew("wifi选择",R.layout.activity_select_wifi);
        initViews();
    }

    private void initViews() {
        //初始化控件
        ll_header = (LinearLayout) findViewById(R.id.ll_header);
        rlv = (RecyclerView) findViewById(R.id.rlv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlv.setLayoutManager(layoutManager);
        mList = new ArrayList<>();
        List<ScanResult> list = WifiUtils.getScanWifiInfo(SelectWifiActivity.this);
        for (int i = 0; i < list.size(); i++) {
            String SSID = list.get(i).SSID;
            if (!TextUtils.isEmpty(SSID) && !SSID.contains("84E0F4200")) {
                //去除与设备相关的wifi
                mList.add(SSID);
            }
        }

        if (mList.size() != 0) {
            //有数据时显示的界面
            mWifiAdapter = new WifiAdapter(SelectWifiActivity.this, removeDuplicate(mList));
            rlv.setAdapter(mWifiAdapter);
            ll_header.setVisibility(View.GONE);
        } else {
            //没有数据时显示的界面
            ll_header.setVisibility(View.VISIBLE);
        }
    }

    public static List removeDuplicate(List list) {
        //去除名称相同的wifi
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }
}
