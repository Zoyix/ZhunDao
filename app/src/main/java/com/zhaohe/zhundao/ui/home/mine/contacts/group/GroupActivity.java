package com.zhaohe.zhundao.ui.home.mine.contacts.group;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
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
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.MyGroupAdapter;
import com.zhaohe.zhundao.asynctask.contacts.AsyncDeleteGroup;
import com.zhaohe.zhundao.asynctask.contacts.AsyncGetGroup;
import com.zhaohe.zhundao.bean.dao.MyGroupBean;
import com.zhaohe.zhundao.dao.MyGroupDao;
import com.zhaohe.zhundao.ui.ToolBarActivity;
import com.zhaohe.zhundao.ui.home.mine.contacts.ContactsActivity;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends ToolBarActivity implements View.OnClickListener, AdapterView.OnItemClickListener, Toolbar.OnMenuItemClickListener, AdapterView.OnItemLongClickListener {
    private Handler mHandler;
    private MyGroupDao dao;
    private TextView tv_group_all, tv_group_none;
    private ListView lv_group;
    private MyGroupAdapter adapter;
    private MyGroupBean bean;
    public static final int MESSAGE_GET_GROUP = 94;
    public static final int MESSAGE_DELETE_GROUP = 95;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        initToolBarNew("分组", R.layout.activity_group);
        initHandler();
        initView();
        showData();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu_group, menu);
        toolbar.setOnMenuItemClickListener(this);

        return true;
    }

    private void showData() {
        adapter.refreshData(dao.queryAll());

    }

    private void initView() {
        dao = new MyGroupDao(this);
        tv_group_all = (TextView) findViewById(R.id.tv_group_all);
        tv_group_all.setOnClickListener(this);
        tv_group_none = (TextView) findViewById(R.id.tv_group_none);
        tv_group_none.setOnClickListener(this);
        lv_group = (ListView) findViewById(R.id.lv_group);
        adapter = new MyGroupAdapter(this);
        lv_group.setAdapter(adapter);
        lv_group.setOnItemClickListener(this);
        lv_group.setOnItemLongClickListener(this);
        registerForContextMenu(lv_group);


    }

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {

                    case MESSAGE_GET_GROUP:
                        String result = (String) msg.obj;
                        insertData(result);
                        showData();

                        break;
                    case MESSAGE_DELETE_GROUP:
                        result = (String) msg.obj;
                        JSONObject jsonObj = JSON.parseObject(result);
                        String message = jsonObj.getString("Res");
                        System.out.println("group_delete_result:" + result);
                        if (message.equals("0"))
                        //添加或修改请求结果
                        {
                            ToastUtil.makeText(getApplicationContext(), "删除分组成功！");
                            dao.deleteGroupByID(bean.getID());
                            showData();
                        }
                        break;


                    default:
                        break;
                }
            }
        };
    }

    public void getData() {
        if (NetworkUtils.checkNetState(this)) {
            AsyncGetGroup async = new AsyncGetGroup(this, mHandler, MESSAGE_GET_GROUP);
            async.execute();
        }
    }

    public void deletGroup() {
        if (NetworkUtils.checkNetState(this)) {
            AsyncDeleteGroup async = new AsyncDeleteGroup(this, mHandler, MESSAGE_DELETE_GROUP, bean.getID());
            async.execute();
        }
    }

    private void insertData(String result) {
        JSONObject jsonObj = JSON.parseObject(result);
        JSONArray jsonArray = jsonObj.getJSONArray("Data");
        List<MyGroupBean> list = new ArrayList<MyGroupBean>();
        if (jsonArray == null) {
            return;
        }
        for (int i = 0; i < jsonArray.size(); i++) {
            MyGroupBean bean = new MyGroupBean();
            bean.setName(jsonArray.getJSONObject(i).getString("GroupName"));
            bean.setSequence(jsonArray.getJSONObject(i).getString("Sequence"));
            bean.setID(jsonArray.getJSONObject(i).getString("ID"));
            bean.setAdminUserID(jsonArray.getJSONObject(i).getString("AdminUserID"));
            bean.setName(jsonArray.getJSONObject(i).getString("GroupName"));
            bean.setUpdateStatus("false");
            list.add(bean);
        }
        dao.save(list);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_group_all:
                IntentUtils.startActivity(this, ContactsActivity.class);
                break;
            case R.id.tv_group_none:
                Intent intent = new Intent();
                intent.setClass(this, ContactsActivity.class);
                intent.putExtra("GroupID", "0");
                this.startActivity(intent);
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MyGroupBean bean = adapter.getItem(i);
        System.out.println(bean.getID());
        Intent intent = new Intent();
        intent.setClass(this, ContactsActivity.class);
        intent.putExtra("GroupID", bean.getID());
        this.startActivity(intent);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_group_add:
                IntentUtils.startActivity(this, GroupAddActivity.class);

                break;

        }
        return false;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        bean = adapter.getItem(i);

        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, 1, 0, "编辑分组");
        menu.add(0, 2, 0, "删除分组");


    }

    public void deleteDialog() {

        //LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化

        new AlertDialog.Builder(this)
                //对话框的标题
                .setTitle("确认要删除分组吗？")
                //设定显示的View
                //对话框中的“登陆”按钮的点击事件
//                .setMessage("确定后，该组的群员将没有分组。")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deletGroup();
                    }

                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                // 设置dialog是否为模态，false表示模态，true表示非模态
                .setCancelable(true)
                //对话框的创建、显示
                .create().show();

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Intent intent = new Intent();
                intent.setClass(this, GroupAddActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", bean);
                intent.putExtras(bundle);
                this.startActivity(intent);
                break;
            case 2:
                deleteDialog();
                break;
        }
        return true;
    }
}
