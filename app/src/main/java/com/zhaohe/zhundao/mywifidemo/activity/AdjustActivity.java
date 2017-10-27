package com.zhaohe.zhundao.mywifidemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.ui.ToolBarActivity;


/**
 * 有线无线选择界面
 */
public class AdjustActivity extends ToolBarActivity {
    private TextView tv_wired, tv_wireless;
    private String wifiName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBarNew("选择连接的网络种类",R.layout.activity_adjust);
        initViews();
        initListener();
    }

    private void initListener() {
        tv_wired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //有线连接
                Intent intent = new Intent(AdjustActivity.this, WifiConfigActivity.class);
                intent.putExtra("flag", 1);
                intent.putExtra("wifiName", "");
                startActivity(intent);
            }
        });

        tv_wireless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //无线连接
                Intent intent = new Intent(AdjustActivity.this, SelectWifiActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        tv_wired = (TextView) findViewById(R.id.tv_wired);
        tv_wireless = (TextView) findViewById(R.id.tv_wireless);
        Intent intent = getIntent();
        wifiName = intent.getStringExtra("wifiName");
    }
}
