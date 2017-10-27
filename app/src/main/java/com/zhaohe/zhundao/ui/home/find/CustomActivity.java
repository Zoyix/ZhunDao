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
import com.zhaohe.app.utils.IntentUtils;
import com.zhaohe.app.utils.NetworkUtils;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.CustomAdapter;
import com.zhaohe.zhundao.asynctask.AsyncCustomAll;
import com.zhaohe.zhundao.bean.CustomBean;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class CustomActivity extends Activity implements Toolbar.OnMenuItemClickListener, AdapterView.OnItemClickListener {
    private CustomAdapter adapter;
    private List<CustomBean> list_custom;
    private ListView lv_custom;
    private Handler mHandler;
    public static final int MESSAGE_GET_CUSTOM_ALL = 81;
    private TextView tv_custom_suggest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        initToolBar();
        initHandler();
        initView();
    }
    @Override
    public void onResume() {
        super.onResume();
        getCustomAll();
    }

    private void initView() {
        lv_custom = (ListView) findViewById(R.id.lv_custom);
        adapter = new CustomAdapter(this);
        lv_custom.setAdapter(adapter);
        lv_custom.setOnItemClickListener(this);
        tv_custom_suggest= (TextView) findViewById(R.id.tv_custom_suggest);

    }

    protected void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_act_custom);
        toolbar.inflateMenu(R.menu.toolbar_find_custom);
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

    private void test() {
        List<CustomBean> list = new ArrayList<CustomBean>();
        for (int i = 1; i <= 20; i++) {
            CustomBean bean = new CustomBean();
            bean.setType("Type" + i);
            bean.setTitle("测试" + i);
            bean.setRequired("是");
            list.add(bean);
        }
        adapter.refreshData(list);
    }

    private void getCustomAll() {
       {
      if (NetworkUtils.checkNetState(this)) {
                AsyncCustomAll async = new AsyncCustomAll(this, mHandler, MESSAGE_GET_CUSTOM_ALL);
                async.execute();
            }
               else
                return;
        }
    }

    private void jsonconver(String result) {
        JSONObject jsonObj = JSON.parseObject(result);
        JSONArray jsonArray = jsonObj.getJSONArray("Data");
        List<CustomBean> list = new ArrayList<CustomBean>();
        for (int i = 0; i < jsonArray.size(); i++) {
            CustomBean bean = new CustomBean();
            bean.setTitle(jsonArray.getJSONObject(i).getString("Title"));
            bean.setID(jsonArray.getJSONObject(i).getString("ID"));
            String InputType = jsonArray.getJSONObject(i).getString("InputType");
            String Option = jsonArray.getJSONObject(i).getString("Option");
            bean.setOption(Option);
            bean.setType(InputType);
            String status = null;

            switch (jsonArray.getJSONObject(i).getString("Required")) {
                case "true":
                    status = "是";
                    break;
                case "false":
                    status = "否";
                    break;
                default:
                    status = "否";
                    break;

            }

            bean.setRequired(status);
            list.add(bean);
        }
        showSuggest(list);
        adapter.refreshData(list);
    }
    private void showSuggest(List<CustomBean> list) {
        if (list.size() == 0) {
            lv_custom.setVisibility(GONE);
            tv_custom_suggest.setVisibility(View.VISIBLE);
        } else {
            lv_custom.setVisibility(View.VISIBLE);
            tv_custom_suggest.setVisibility(GONE);
        }
    }

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_GET_CUSTOM_ALL:
                        String result = (String) msg.obj;
                        //自定义选项列表结果
                        if (NetworkUtils.checkNetState(getApplicationContext())) {
                            SPUtils.put(getApplicationContext(), "custom_result", result);

                            try {
                                jsonconver((String) SPUtils.get(getApplicationContext(), "custom_result", ""));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                        break;


                    default:
                        break;
                }
            }
        };
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_find_add:
                IntentUtils.startActivity(this, CustomItemActivity.class);
                break;


        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CustomBean bean = adapter.getItem(i);
        String ID = bean.getID();
        String Title = bean.getTitle();
        String InputType = bean.getType();
        String Option = bean.getOption();
        String Required = bean.getRequired();
        Intent intent = new
                Intent(this, CustomItemEditActivity.class);
        intent.putExtra("ID", ID);
        intent.putExtra("Title", Title);
        intent.putExtra("InputType", InputType);
        intent.putExtra("Required", Required);
        intent.putExtra("Option", Option);

        startActivity(intent);


    }
}
