package com.zhaohe.zhundao.ui.home.find;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhaohe.app.utils.NetworkUtils;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.BeaconAdapter;
import com.zhaohe.zhundao.asynctask.AsyncBeaconBond;
import com.zhaohe.zhundao.asynctask.AsyncGetBeaconList;
import com.zhaohe.zhundao.asynctask.AsyncSign;
import com.zhaohe.zhundao.bean.BeaconBean;
import com.zhaohe.zhundao.zxing.controller.MipcaActivityCapture;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class BeaconListActivity extends Activity implements AdapterView.OnItemClickListener, Toolbar.OnMenuItemClickListener {

    public static final int MESSAGE_GET_BEACONLIST = 99;
    public static final int SCANNIN_GREQUEST_CODE = 89;
    public static final int MESSAGE_ADD_BEACON = 88;


    private BeaconAdapter adapter;
    private ListView lv_beacon;
    private Handler mHandler;
    private String act_id;
    private JSONObject jsonObj;
    private JSONArray jsonArray;
    private TextView tv_suggest;

    public static final int MESSAGE_SIGN_ALL = 94;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beancon_list);
        initToolBar();
        initView();
        initHandler();

        init();
//        test();
    }

    @Override
    public void onResume() {
        super.onResume();
        getBenconList();
    }

    protected void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_act_beacon_list);
        toolbar.inflateMenu(R.menu.toolbar_beacon_list);
        toolbar.setOnMenuItemClickListener(this);
//        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        toolbar.setTitle("");


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });//        返回
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }

    private void getBenconList() {
        if (NetworkUtils.checkNetState(this)) {
            if (SPUtils.contains(this, "beacon_result")) {
                jsonconver((String) SPUtils.get(getApplicationContext(), "beacon_result", ""));

            }
            AsyncGetBeaconList asyncActivity = new AsyncGetBeaconList(this, mHandler, MESSAGE_GET_BEACONLIST);
            asyncActivity.execute();
        } else {
            if (SPUtils.contains(this, "beacon_result")) {
                jsonconver((String) SPUtils.get(getApplicationContext(), "beacon_result", ""));

            } else
                return;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    // 显示扫描到的内容
                    String result = bundle.getString("result");
                    beaconBond(result, "0");

                }
                if (requestCode == RESULT_CANCELED) {
                    ToastUtil.makeText(this, "未授权相机权限，请授权后重试");
                }

                break;
        }
    }

    private void jsonconver(String result) {
        jsonObj = JSON.parseObject(result);
        jsonArray = jsonObj.getJSONArray("Data");
        List<BeaconBean> list = new ArrayList<BeaconBean>();
        for (int i = 0; i < jsonArray.size(); i++) {
            BeaconBean bean = new BeaconBean();
            bean.setTitle(jsonArray.getJSONObject(i).getString("Title"));
            bean.setBeaconName(jsonArray.getJSONObject(i).getString("BeaconName"));
            bean.setBeaconID(jsonArray.getJSONObject(i).getString("BeaconID"));
            bean.setDeviceID(jsonArray.getJSONObject(i).getString("DeviceId"));
            bean.setUrl(jsonArray.getJSONObject(i).getString("IconUrl"));
            bean.setNickName(jsonArray.getJSONObject(i).getString("NickName"));
            bean.setID(jsonArray.getJSONObject(i).getString("ID"));
            bean.setAddTime(jsonArray.getJSONObject(i).getString("AddTime"));
            list.add(bean);
        }
        showSuggest(list);
        adapter.refreshData(list);
    }

    private void init() {
        if ((NetworkUtils.checkNetState(this)) && (SPUtils.contains(this, "sign_result") == false)) {
            AsyncSign asyncSign = new AsyncSign(this, mHandler, MESSAGE_SIGN_ALL);
            asyncSign.execute();
        }
    }

    private void showSuggest(List list) {
        if (list.size() == 0) {
            lv_beacon.setVisibility(GONE);
            tv_suggest.setVisibility(View.VISIBLE);
        } else {
            lv_beacon.setVisibility(View.VISIBLE);
            tv_suggest.setVisibility(GONE);
        }
    }


    private void test() {
        List<BeaconBean> list = new ArrayList<BeaconBean>();
        for (int i = 1; i <= 20; i++) {
            BeaconBean bean = new BeaconBean();
            bean.setTitle("标题" + i);
            bean.setBeaconName("BeaconName" + i);
            bean.setDeviceID("Device" + i);
            bean.setUrl("www");
            list.add(bean);
        }
        adapter.refreshData(list);
    }

    private void initView() {
        lv_beacon = (ListView) findViewById(R.id.lv_beacon);
        adapter = new BeaconAdapter(this);
        lv_beacon.setAdapter(adapter);
        lv_beacon.setOnItemClickListener(this);
        tv_suggest = (TextView) findViewById(R.id.tv_shake_suggest);
    }

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_GET_BEACONLIST:
                        String result = (String) msg.obj;
                        if (NetworkUtils.checkNetState(getApplicationContext())) {
                            SPUtils.put(getApplicationContext(), "beacon_result", result);
                            jsonconver((String) SPUtils.get(getApplicationContext(), "beacon_result", ""));
                        }
                        System.out.println(result);


                        break;
                    case MESSAGE_SIGN_ALL:
                        String result2 = (String) msg.obj;
                        //签到列表结果
                        if (NetworkUtils.checkNetState(getApplicationContext())) {
                            SPUtils.put(getApplicationContext(), "sign_result", result2);
                        }
                        break;
                    case MESSAGE_ADD_BEACON:
                        String result3 = (String) msg.obj;
                        JSONObject jsonObj = JSON.parseObject(result3);
                        String message = jsonObj.getString("Msg");
                        if (jsonObj.getString("Res").equals("0")) {
                            getBenconList();
                        }
                        ToastUtil.makeText(getApplicationContext(), message);

                        break;

                    default:
                        break;
                }
            }
        };
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        BeaconBean bean = adapter.getItem(i);
        Intent intent = new
                Intent(this, BeaconInfActivity.class);
        intent.putExtra("IconUrl", bean.getUrl());
        intent.putExtra("BeaconName", bean.getBeaconName());
        intent.putExtra("BeaconID", bean.getBeaconID());
        intent.putExtra("DeviceId", bean.getDeviceID());
        intent.putExtra("NickName", bean.getNickName());
        intent.putExtra("ID", bean.getID());
        intent.putExtra("AddTime", bean.getAddTime());


        startActivity(intent);


    }

    private void beaconBond(String result, String type) {
//     type   0绑定摇一摇设备 1解除绑定摇一摇  result deviceID
        AsyncBeaconBond Async = new AsyncBeaconBond(this, mHandler, MESSAGE_ADD_BEACON, result, type);
        Async.execute();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_beacon_add:
                Intent intent = new Intent();
                intent.setClass(this, MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("view_show", "false");
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                startActivity(intent);
                break;


        }
        return false;
    }
}
