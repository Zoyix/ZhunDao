package com.zhaohe.zhundao.ui.home.action;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.SignListAdapter;
import com.zhaohe.zhundao.bean.SignListBean;
import com.zhaohe.zhundao.constant.Constant;
import com.zhaohe.zhundao.ui.ToolBarActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignListSelectActivity extends ToolBarActivity implements AdapterView.OnItemClickListener, Toolbar.OnMenuItemClickListener {
    @BindView(R.id.lv_signlist)
    ListView lvSignlist;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.btn_login)
    Button btnLogin;
    private List<SignListBean> list = new ArrayList<SignListBean>();
    private String signup_list;
    private JSONObject jsonObj;
    private JSONArray jsonArray;
    private SignListAdapter adapter;
    private String act_id;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBarNew("选择人员", R.layout.activity_sign_list_select);
        ButterKnife.bind(this);
        initView();
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu_signlist_select, menu);
        toolbar.setOnMenuItemClickListener(this);

        return true;
    }


    private void initView() {
        adapter = new SignListAdapter(this, 2);
        lvSignlist.setAdapter(adapter);
        lvSignlist.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        lvSignlist.setOnItemClickListener(this);
    }

    public void init() {
        Intent intent = getIntent();

        if (intent != null) {
            act_id = intent.getStringExtra("act_id");
            if (act_id == null || act_id == "") {
                act_id = (String) SPUtils.get(this, "act_id_now", "");

            }
            ToastUtil.print("活动ID" + act_id);
//            UserInfo = intent.getStringExtra("UserInfo");
//            ActivityFees = intent.getStringExtra("ActivityFees");

        }

        signup_list = (String) SPUtils.get(this, "listup_" + act_id, "");
        ToastUtil.print("活动内容" + signup_list);

        jsonObj = JSON.parseObject(signup_list);
        jsonArray = jsonObj.getJSONArray("Data");
        for (int i = 0; i < jsonArray.size(); i++) {
            SignListBean bean = new SignListBean();
            bean.setSign_list_id(jsonArray.getJSONObject(i).getString("ID"));
            bean.setSign_list_name(jsonArray.getJSONObject(i).getString("UserName"));
            bean.setSign_list_time(jsonArray.getJSONObject(i).getString("AddTime"));
            bean.setSign_list_phone(jsonArray.getJSONObject(i).getString("Mobile"));
            bean.setNickname(jsonArray.getJSONObject(i).getString("NickName"));
            bean.setAdminRemark(jsonArray.getJSONObject(i).getString("AdminRemark"));
            bean.setVCode(jsonArray.getJSONObject(i).getString("VCode"));

            bean.setmIndex(i);
            bean.setAct_id(act_id);
//          Status  -1取消报名，0报名成功，1待缴费
            if (jsonArray.getJSONObject(i).getString("Status").equals("0")) {
                bean.setSign_list_status("报名成功");
            }
            if (jsonArray.getJSONObject(i).getString("Status").equals("1")) {
                bean.setSign_list_status("待缴费");
            }
            if (jsonArray.getJSONObject(i).getString("Status").equals("-1")) {
                bean.setSign_list_status("取消报名");
            }
            if (jsonArray.getJSONObject(i).getString("Status").equals("2")) {
                bean.setSign_list_status("待审核");
            }
            if (jsonArray.getJSONObject(i).getString("Status").equals("3")) {
                bean.setSign_list_status("审核失败");
            }


            list.add(bean);
        }

        adapter.refreshData(list);
        for (int i = 0; i < list.size(); i++) {
            lvSignlist.setItemChecked(i, true);
        }
        refreshCount();

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        refreshCount();

    }

    private void refreshCount() {
        tvCount.setText("已选择" + lvSignlist.getCheckedItemCount() + "人");
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_signlist_select_all:
                for (int i = 0; i < list.size(); i++) {
                    lvSignlist.setItemChecked(i, true);
                }

                break;
            case R.id.menu_signlist_select_none:
                for (int i = 0; i < list.size(); i++) {
                    lvSignlist.setItemChecked(i, false);
                }

                break;
            case R.id.menu_signlist_select_anti:
                for (int i = 0; i < list.size(); i++) {

                    lvSignlist.setItemChecked(i, !lvSignlist.isItemChecked(i));
                }
                break;
        }
        refreshCount();

        return false;
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        if (lvSignlist.getCheckedItemCount() <= 0) {
            ToastUtil.makeText(getApplicationContext(), "至少选择一个人");


            return;
        }
        Constant.booleanArray_msg = lvSignlist.getCheckedItemPositions();

        Intent intent = new Intent(this, MsgActivity.class);
        //可以把要传递的参数放到一个bundle里传递过去，bumdle可以看做一个特殊的map。
        Bundle bundle = new Bundle();
        bundle.putString("act_id", act_id);
        bundle.putInt("count", lvSignlist.getCheckedItemCount());

        //bundle_path.putSerializable("DATA", new String[]{Path,Path1,Path2});
//        bundle.putSerializable("DATA", (Serializable) booleanArray) ;
        intent.putExtras(bundle);
        startActivity(intent);

    }
}


