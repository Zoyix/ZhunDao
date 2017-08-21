package com.zhaohe.zhundao.ui.home.mine.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhaohe.app.utils.IntentUtils;
import com.zhaohe.app.utils.NetworkUtils;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.asynctask.contacts.AsyncGetContact;
import com.zhaohe.zhundao.asynctask.contacts.AsyncGetGroup;
import com.zhaohe.zhundao.bean.dao.MyContactsBean;
import com.zhaohe.zhundao.bean.dao.MyGroupBean;
import com.zhaohe.zhundao.dao.MyContactsDao;
import com.zhaohe.zhundao.dao.MyGroupDao;
import com.zhaohe.zhundao.ui.ToolBarActivity;
import com.zhaohe.zhundao.ui.home.mine.contacts.group.GroupActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ContactsActivity extends ToolBarActivity implements AdapterView.OnItemClickListener,Toolbar.OnMenuItemClickListener {
    private Handler mHandler;

    private ListView listView;
    private SideBar sideBar;
    private String GroupID;
    private TextView tv_group_suggest;
    private EditText et_contacts_search;
//    private ArrayList<MyContactsBean> list;
    List<MyContactsBean> list = new ArrayList<MyContactsBean>();

    private MyContactsDao dao;
    private MyGroupDao dao_group;

    SortAdapter adapter;
    public static final int MESSAGE_GET_CONSTACTS = 94;
    public static final int MESSAGE_GET_GROUP = 95;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        initToolBarNew("我的通讯录",R.layout.activity_contacts);
        initHandler();
        initIntent();
        initView();
        initData();
        initGroup();
    }
    public void initGroup(){
         if ((boolean) SPUtils.get(this, "initGroup", false) == false) {

                 if (NetworkUtils.checkNetState(this)) {
                     AsyncGetGroup async = new AsyncGetGroup(this, mHandler, MESSAGE_GET_GROUP);
                     async.execute();
                 }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            GroupID = intent.getStringExtra("GroupID");
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu_contacts, menu);
        toolbar.setOnMenuItemClickListener(this);

        return true;
    }
    private void initView() {
        tv_group_suggest= (TextView) findViewById(R.id.tv_group_suggest);
        dao=new MyContactsDao(this);
        dao_group= new MyGroupDao(this);
        listView = (ListView) findViewById(R.id.lv_contacts);
        sideBar = (SideBar) findViewById(R.id.contacts_sidebar);
        sideBar.setOnStrSelectCallBack(new SideBar.ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int index, String selectStr) {
                for (int i = 0; i < list.size(); i++) {
                    if (selectStr.equalsIgnoreCase(list.get(i).getFirstLetter())) {
                        listView.setSelection(i); // 选择到首字母出现的位置
                        return;
                    }
                }
            }
        });
        et_contacts_search= (EditText) findViewById(R.id.et_contacts_search);
        et_contacts_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //TODO:
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(GroupID==null){
                    list=dao.queryListdPhoneOrName(et_contacts_search.getText().toString());
                }else{
                list=  dao.queryListGroupIDAndPhoneOrName(GroupID,et_contacts_search.getText().toString());}
                refreshData();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //TODO:
            }
        });    }
    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {

                    case MESSAGE_GET_CONSTACTS:
                        String result = (String) msg.obj;
                        insertData(result);
                        ShowData();
                        break;

                    case MESSAGE_GET_GROUP:
                       result = (String) msg.obj;
                        insertGroupData(result);
                        SPUtils.put(getApplicationContext(), "initGroup", true);


                    default:
                        break;
                }
            }
        };     }

    private void ShowData() {
        if (GroupID==null){
            list=dao.queryAll();}
        else{
            list=dao.queryGroupID(GroupID);
        }
        if (list.size()==0){
            tv_group_suggest.setVisibility(View.VISIBLE);
        }
        else{
            tv_group_suggest.setVisibility(View.GONE);

        }
        Collections.sort(list); // 对list进行排序，需要让User实现Comparable接口重写compareTo方法
        adapter.refresh(list);

        et_contacts_search.setHint("搜索"+list.size()+"位联系人");
    }

    private void initData() {
        if (GroupID==null){
            list=dao.queryAll();}
        else{
            list=dao.queryGroupID(GroupID);
        }
        if (list.size()==0){
            tv_group_suggest.setVisibility(View.VISIBLE);
        }
        else{
            tv_group_suggest.setVisibility(View.GONE);

        }


        Collections.sort(list); // 对list进行排序，需要让User实现Comparable接口重写compareTo方法
        adapter = new SortAdapter(getApplicationContext(), list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        et_contacts_search.setHint("搜索"+list.size()+"位联系人");
    }
    private void refreshData() {
        Collections.sort(list); // 对list进行排序，需要让User实现Comparable接口重写compareTo方法

        adapter.refresh(list);
//        adapter = new SortAdapter(getApplicationContext(), list);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(this);
    }


//    private void initData() {
//        list = new ArrayList<>();
//        list.add(new MyContactsBean("亳州")); // 亳[bó]属于不常见的二级汉字
//        list.add(new MyContactsBean("大娃"));
//        list.add(new MyContactsBean("二娃"));
//        list.add(new MyContactsBean("三娃"));
//        list.add(new MyContactsBean("四娃"));
//        list.add(new MyContactsBean("五娃"));
//        list.add(new MyContactsBean("六娃"));
//        list.add(new MyContactsBean("七娃"));
//        list.add(new MyContactsBean("喜羊羊"));
//        list.add(new MyContactsBean("美羊羊"));
//        list.add(new MyContactsBean("懒羊羊"));
//        list.add(new MyContactsBean("沸羊羊"));
//        list.add(new MyContactsBean("暖羊羊"));
//        list.add(new MyContactsBean("慢羊羊"));
//        list.add(new MyContactsBean("灰太狼"));
//        list.add(new MyContactsBean("红太狼"));
//        list.add(new MyContactsBean("孙悟空"));
//        list.add(new MyContactsBean("黑猫警长"));
//        list.add(new MyContactsBean("舒克"));
//        list.add(new MyContactsBean("贝塔"));
//        list.add(new MyContactsBean("海尔"));
//        list.add(new MyContactsBean("阿凡提"));
//        list.add(new MyContactsBean("邋遢大王"));
//        list.add(new MyContactsBean("哪吒"));
//        list.add(new MyContactsBean("没头脑"));
//        list.add(new MyContactsBean("不高兴"));
//        list.add(new MyContactsBean("蓝皮鼠"));
//        list.add(new MyContactsBean("大脸猫"));
//        list.add(new MyContactsBean("大头儿子"));
//        list.add(new MyContactsBean("小头爸爸"));
//        list.add(new MyContactsBean("蓝猫"));
//        list.add(new MyContactsBean("淘气"));
//        list.add(new MyContactsBean("叶峰"));
//        list.add(new MyContactsBean("楚天歌"));
//        list.add(new MyContactsBean("江流儿"));
//        list.add(new MyContactsBean("Tom"));
//        list.add(new MyContactsBean("Jerry"));
//        list.add(new MyContactsBean("12345"));
//        list.add(new MyContactsBean("54321"));
//        list.add(new MyContactsBean("_(:з」∠)_"));
//        list.add(new MyContactsBean("……%￥#￥%#"));
//        Collections.sort(list); // 对list进行排序，需要让User实现Comparable接口重写compareTo方法
//        SortAdapter adapter = new SortAdapter(this, list);
//        listView.setAdapter(adapter);
//
//    }
    private void insertData(String result) {
        JSONObject jsonObj = JSON.parseObject(result);
        JSONArray jsonArray = jsonObj.getJSONArray("Data");
        List<MyContactsBean> list = new ArrayList<MyContactsBean>();
        if (jsonArray == null) {
            return;
        }
        for (int i = 0; i < jsonArray.size(); i++) {
            MyContactsBean bean = new MyContactsBean();
            if(jsonArray.getJSONObject(i).getString("TrueName")==null||jsonArray.getJSONObject(i).getString("TrueName").equals("")){
                bean.setName("未知");
            }
            else
            {
            bean.setName(jsonArray.getJSONObject(i).getString("TrueName"));}
            bean.setAddress(jsonArray.getJSONObject(i).getString("Address"));
            bean.setEmail(jsonArray.getJSONObject(i).getString("Email"));
            bean.setGroupName(jsonArray.getJSONObject(i).getString("GroupName"));
            //分组ID若为null则替换成0，方便后面未分组的查询
            if (jsonArray.getJSONObject(i).getString("ContactGroupID")==null){
                bean.setGroupID("0");
            }
            else {
                bean.setGroupID(jsonArray.getJSONObject(i).getString("ContactGroupID"));
            }
            bean.setSex(jsonArray.getJSONObject(i).getString("Sex"));
            bean.setID(jsonArray.getJSONObject(i).getString("ID"));
            bean.setPhone(jsonArray.getJSONObject(i).getString("Mobile"));
            bean.setHeadImgurl(jsonArray.getJSONObject(i).getString("HeadImgurl"));
            bean.setSerialNo(jsonArray.getJSONObject(i).getString("SerialNo"));
            bean.setRemark(jsonArray.getJSONObject(i).getString("Remark"));
            bean.setCompany(jsonArray.getJSONObject(i).getString("Company"));
            bean.setIDcard(jsonArray.getJSONObject(i).getString("IDcard"));
            bean.setDuty(jsonArray.getJSONObject(i).getString("Duty"));
            bean.setUpdateStatus("false");
            list.add(bean);
        }
        dao.save(list);


    }

    private void insertGroupData(String result) {
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
        dao_group.save(list);


    }

    public void getData() {
        if (NetworkUtils.checkNetState(this)) {
            AsyncGetContact async = new AsyncGetContact(this, mHandler, MESSAGE_GET_CONSTACTS);
            async.execute();
        }    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MyContactsBean bean= (MyContactsBean) adapter.getItem(i);
        Intent intent = new Intent();
        intent.setClass(this, PeopleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", bean);
        intent.putExtras(bundle);
        this.startActivity(intent);

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_contacts_add:
                    IntentUtils.startActivity(this, PeopleAddActivity.class);

                break;
            case R.id.menu_contacts_group:
                IntentUtils.startActivity(this, GroupActivity.class);
                break;
        }
        return false;
    }
}
