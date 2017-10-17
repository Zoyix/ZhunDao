package com.zhaohe.zhundao.mywifidemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.mywifidemo.bean.SocketData;
import com.zhaohe.zhundao.mywifidemo.utils.SocketUtils;
import com.zhaohe.zhundao.mywifidemo.utils.StringUtils;
import com.zhaohe.zhundao.mywifidemo.utils.ToastUtis;
import com.zhaohe.zhundao.mywifidemo.utils.WifiUtils;


public class WifiConfigActivity extends AppCompatActivity {
    private LinearLayout ll_high_set;
    private TextView tv_wifi_name, tv_high;
    private EditText et_wifi_pd, et_wifi_ip, et_wifi_gateway, et_wifi_dns;
    private Button btn;
    private String wifiName;//wifiName
    private int flag, state;

    private RelativeLayout rl_wifi_name;
    private RelativeLayout rl_wifi_pd;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_config);
        initViews();
        initListener();
    }

    private void initListener() {
        tv_high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state == 0) {
                    ll_high_set.setVisibility(View.VISIBLE);
                    state = 1;
                } else if (state == 1) {
                    ll_high_set.setVisibility(View.GONE);
                    state = 0;
                }

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击确定按钮
                if (flag == 1) {
                    //有线连接
                    String ip = et_wifi_ip.getText().toString().trim();
                    String gateWay = et_wifi_gateway.getText().toString().trim();
                    String dns = et_wifi_dns.getText().toString().trim();
                    if (TextUtils.isEmpty(ip) && TextUtils.isEmpty(gateWay) && TextUtils.isEmpty(dns)) {
                        //三者均没有填写的情况,静态IP
                        SocketData socketData = new SocketData("", "", "", "", "", false, true);
                        ToastUtis.showLoading(WifiConfigActivity.this, "正在建立连接");
                        sendSocket(socketData);
                    } else {
                        //配置动态IP
                        if (TextUtils.isEmpty(ip) || !StringUtils.isValidIpAddress(ip)) {
                            ToastUtis.showToast(WifiConfigActivity.this, "请填写有效的IP地址");
                            return;
                        }
                        if (TextUtils.isEmpty(gateWay) || !StringUtils.isValidIpAddress(gateWay)) {
                            ToastUtis.showToast(WifiConfigActivity.this, "请填写有效的网关");
                            return;
                        }
                        if (TextUtils.isEmpty(dns) || !StringUtils.isValidIpAddress(dns)) {
                            ToastUtis.showToast(WifiConfigActivity.this, "请填写有效的DNS");
                            return;
                        }
                        SocketData socketData = new SocketData("", "", ip, gateWay, dns, true, true);
                        ToastUtis.showLoading(WifiConfigActivity.this, "正在建立连接");
                        sendSocket(socketData);
                    }

                } else if (flag == 2) {
                    //无线连接
                    String pass = et_wifi_pd.getText().toString().trim();
                    if (TextUtils.isEmpty(pass)) {
                        ToastUtis.showToast(WifiConfigActivity.this, "请输入正确的wifi密码!");
                        return;
                    } else {
                        if (state == 0) {
                            //无高级配置,静态IP
                            SocketData socketData = new SocketData(wifiName, pass, "", "", "", false, false);
                            ToastUtis.showLoading(WifiConfigActivity.this, "正在建立连接");
                            sendSocket(socketData);
                        } else {
                            //点击高级配置,配置动态IP
                            String ip = et_wifi_ip.getText().toString().trim();
                            String gateWay = et_wifi_gateway.getText().toString().trim();
                            String dns = et_wifi_dns.getText().toString().trim();
                            if (TextUtils.isEmpty(ip) || !StringUtils.isValidIpAddress(ip)) {
                                ToastUtis.showToast(WifiConfigActivity.this, "请填写有效的IP地址");
                                return;
                            }
                            if (TextUtils.isEmpty(gateWay) || !StringUtils.isValidIpAddress(gateWay)) {
                                ToastUtis.showToast(WifiConfigActivity.this, "请填写有效的网关");
                                return;
                            }
                            if (TextUtils.isEmpty(dns) || !StringUtils.isValidIpAddress(dns)) {
                                ToastUtis.showToast(WifiConfigActivity.this, "请填写有效的DNS");
                                return;
                            }
                            SocketData socketData = new SocketData(wifiName, pass, ip, gateWay, dns, true, false);
                            ToastUtis.showLoading(WifiConfigActivity.this, "正在建立连接");
                            sendSocket(socketData);
                        }
                    }
                }


            }
        });
    }

    private void sendSocket(SocketData socketData) {
        //建立socket连接
        if (WifiUtils.getNowSSID(WifiConfigActivity.this).contains("84E0F4200")) {
            //判断当前连接的wifi是不是与设备相关的wifi
            SocketUtils.send(socketData, new SocketUtils.SendCallBack() {
                @Override
                public void send(boolean success) {
                    ToastUtis.hideLoading();
                    if (success) {
                        ToastUtis.showToast(WifiConfigActivity.this, "连接中,请耐心等待!");
                    } else {
                        ToastUtis.showToast(WifiConfigActivity.this, "与设备连接中断,配置网络失败,请重试!");
                    }
                }
            });
        } else {
            ToastUtis.showToast(WifiConfigActivity.this, "wifi连接有误,请连接到与设备相关的wifi");
            ToastUtis.hideLoading();
        }

    }

    private void initViews() {
        ll_high_set = (LinearLayout) findViewById(R.id.ll_high_set);
        tv_wifi_name = (TextView) findViewById(R.id.tv_wifi_name);
        tv_high = (TextView) findViewById(R.id.tv_high);
        et_wifi_pd = (EditText) findViewById(R.id.et_wifi_pd);
        et_wifi_ip = (EditText) findViewById(R.id.et_wifi_ip);
        et_wifi_gateway = (EditText) findViewById(R.id.et_wifi_gateway);
        et_wifi_dns = (EditText) findViewById(R.id.et_wifi_dns);
        rl_wifi_name = (RelativeLayout) findViewById(R.id.rl_wifiname);
        rl_wifi_pd = (RelativeLayout) findViewById(R.id.rl_wifi_pd);
        btn = (Button) findViewById(R.id.btn);
        Intent intent = getIntent();
        flag = intent.getIntExtra("flag", 0);
        wifiName = intent.getStringExtra("wifiName");
        Log.e("result", flag + "-->" + wifiName);
        tv_wifi_name.setText("WIFI名称: " + wifiName);
        if (flag == 1) {
            //有线连接
            tv_high.setVisibility(View.GONE);
            rl_wifi_pd.setVisibility(View.GONE);
            rl_wifi_name.setVisibility(View.GONE);
            ll_high_set.setVisibility(View.VISIBLE);
        } else if (flag == 2) {
            //无线连接
            tv_high.setVisibility(View.VISIBLE);
            rl_wifi_pd.setVisibility(View.VISIBLE);
            rl_wifi_name.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
