package com.zhaohe.zhundao.ui.home.find;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhaohe.app.utils.NetworkUtils;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.BeaconAdapter;
import com.zhaohe.zhundao.asynctask.AsyncGetBeaconList;
import com.zhaohe.zhundao.asynctask.AsyncSign;
import com.zhaohe.zhundao.bean.BeaconBean;
import com.zhaohe.zhundao.ui.ToolBarActivity;
import com.zhaohe.zhundao.ui.ToolBarHelper;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class BeaconListActivity extends ToolBarActivity implements AdapterView.OnItemClickListener {

    public static final int MESSAGE_GET_BEACONLIST = 99;

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
        initToolBar("摇一摇的周边信息", R.layout.activity_beancon_list);
        initView();
        initHandler();
        getBenconList();
        init();
//        test();
    }

    private void initToolBar(String text, int layoutResID) {
        ToolBarHelper mToolBarHelper;
        mToolBarHelper = new ToolBarHelper(this, layoutResID);
        mToolBarHelper.setTvTitle(text);
        super.setTitle("");
        setContentView(mToolBarHelper.getContentView());
        toolbar = mToolBarHelper.getToolBar();
  /*把 toolbar 设置到Activity 中*/
        setSupportActionBar(toolbar);
    }
private void getBenconList(){
    if (NetworkUtils.checkNetState(this)) {
        AsyncGetBeaconList asyncActivity = new AsyncGetBeaconList(this, mHandler, MESSAGE_GET_BEACONLIST);
        asyncActivity.execute();
    } else {
        if (SPUtils.contains(this, "beacon_result")) {
            jsonconver((String) SPUtils.get(getApplicationContext(), "beacon_result", ""));

        } else
            return;
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
            bean.setDeviceID(jsonArray.getJSONObject(i).getString("DeviceID"));
            bean.setUrl(jsonArray.getJSONObject(i).getString("IconUrl"));
            bean.setNickName(jsonArray.getJSONObject(i).getString("NickName"));
            bean.setID(jsonArray.getJSONObject(i).getString("ID"));
            list.add(bean);
        }
        showSuggest(list);
        adapter.refreshData(list);
    }
    private void init() {
        if ((NetworkUtils.checkNetState(this))&&(SPUtils.contains(this, "sign_result")==false)){
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
          bean.setTitle("标题"+i);
            bean.setBeaconName("BeaconName"+i);
            bean.setDeviceID("Device"+i);
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
        tv_suggest= (TextView) findViewById(R.id.tv_shake_suggest);
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


                        break;
                    case MESSAGE_SIGN_ALL:
                        String result2 = (String) msg.obj;
                        //签到列表结果
                        if (NetworkUtils.checkNetState(getApplicationContext())) {
                            SPUtils.put(getApplicationContext(), "sign_result", result2);
                        }

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

        startActivity(intent);


    }
}
