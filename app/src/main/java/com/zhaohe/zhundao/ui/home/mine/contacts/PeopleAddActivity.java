package com.zhaohe.zhundao.ui.home.mine.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhaohe.app.utils.NetworkUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.asynctask.contacts.AsyncUpdatePeople;
import com.zhaohe.zhundao.bean.dao.MyContactsBean;
import com.zhaohe.zhundao.bean.dao.MyGroupBean;
import com.zhaohe.zhundao.dao.MyGroupDao;
import com.zhaohe.zhundao.ui.ToolBarActivity;

import java.util.ArrayList;
import java.util.List;


public class PeopleAddActivity extends ToolBarActivity implements OnClickListener {
    private Handler mHandler;
    public static final int MESSAGE_AddContact = 94;
    public static final int MESSAGE_UpdateContact = 95;

    private EditText et_people_add_name, et_people_add_phone,et_people_add_company,et_people_add_duty,et_people_add_IDcard,et_people_add_SerialNo,et_people_add_remark,et_people_add_add,et_people_add_email;
    private Button btn_people_add;
    private Spinner sp_people_add_group,sp_people_add_sex;
    private MyGroupDao dao;
    private List<String> list_group = new ArrayList<String>();
    private List<String> list_id = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private MyContactsBean bean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_add);
        initList();
        iniIntent();
        initView();
        initHandler();
    }

    private void iniIntent() {
        Intent intent = this.getIntent();
        bean = (MyContactsBean) intent.getSerializableExtra("bean");
        if (bean == null) {
            initToolBarNew("新建联系人", R.layout.activity_people_add);

        } else {
            initToolBarNew("编辑联系人", R.layout.activity_people_add);

        }

    }

    private void initList() {
        dao = new MyGroupDao(this);
        List<MyGroupBean> list = new ArrayList<MyGroupBean>();
        list = dao.queryAll();
        list_group.add("无分组");
        list_id.add("0");
        for (int i = 0; i < list.size(); i++) {
            MyGroupBean bean = (MyGroupBean) list.get(i);
            list_group.add(bean.getName());
            list_id.add(bean.getID());
        }


    }


    private void initView() {
        et_people_add_name = (EditText) findViewById(R.id.et_people_add_name);
        et_people_add_phone = (EditText) findViewById(R.id.et_people_add_num);
        et_people_add_phone.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        et_people_add_company = (EditText) findViewById(R.id.et_people_add_company);
        et_people_add_duty = (EditText) findViewById(R.id.et_people_add_duty);
        et_people_add_IDcard = (EditText) findViewById(R.id.et_people_add_IDcard);
        et_people_add_SerialNo = (EditText) findViewById(R.id.et_people_add_SerialNo);
        et_people_add_remark = (EditText) findViewById(R.id.et_people_add_remark);
        et_people_add_add = (EditText) findViewById(R.id.et_people_add_add);
        et_people_add_email=(EditText) findViewById(R.id.et_people_add_email);

        btn_people_add = (Button) findViewById(R.id.btn_people_add);
        btn_people_add.setOnClickListener(this);
        sp_people_add_group = (Spinner) findViewById(R.id.sp_people_add_group);
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.dialog_item_beacon, list_group);
        sp_people_add_group.setAdapter(adapter);
        sp_people_add_sex = (Spinner) findViewById(R.id.sp_people_add_sex);


        if (bean != null) {
            et_people_add_name.setText(bean.getName());
            et_people_add_phone.setText(bean.getPhone());
            et_people_add_company.setText(bean.getCompany());
            et_people_add_duty.setText(bean.getDuty());
            et_people_add_IDcard.setText(bean.getIDcard());
            et_people_add_SerialNo.setText(bean.getSerialNo());
            et_people_add_remark.setText(bean.getRemark());
            et_people_add_add.setText(bean.getAddress());
            et_people_add_email.setText(bean.getEmail());
            if (bean.getSex().equals("1")){
              sp_people_add_sex.setSelection(1);
            }
            if (bean.getSex().equals("2")){
                sp_people_add_sex.setSelection(2);
            }

            for (int i = 0; i < list_group.size(); i++) {
                if (list_group.get(i).equals(bean.getGroupName())) {
                    sp_people_add_group.setSelection(i);
                    break;
                } else {
                    sp_people_add_group.setSelection(0);

                }
            }

        }

    }

    public void addPeople(String param) {
        if (NetworkUtils.checkNetState(this)) {
            AsyncUpdatePeople async = new AsyncUpdatePeople(this, mHandler, MESSAGE_AddContact, param);
            async.execute();
        } else {
            ToastUtil.makeText(this, R.string.net_error);
        }
    }


    public void updatePeople(String param) {
        if (NetworkUtils.checkNetState(this)) {
            AsyncUpdatePeople async = new AsyncUpdatePeople(this, mHandler, MESSAGE_UpdateContact, param);
            async.execute();
        } else {
            ToastUtil.makeText(this, R.string.net_error);
        }
    }
    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {

                    case MESSAGE_AddContact:
                        String result = (String) msg.obj;
                        JSONObject jsonObj = JSON.parseObject(result);
                        String message = jsonObj.getString("Res");
                        System.out.println("people_add_result:" + result);
                        if (message.equals("0"))
                        //添加或修改请求结果
                        {
                            ToastUtil.makeText(getApplicationContext(), "新建联系人成功！");
                            finish();
                        }
                        break;
                    case MESSAGE_UpdateContact:
                         result = (String) msg.obj;
                         jsonObj = JSON.parseObject(result);
                         message = jsonObj.getString("Res");
                        System.out.println("people_add_result:" + result);
                        if (message.equals("0"))
                        //添加或修改请求结果
                        {
                            ToastUtil.makeText(getApplicationContext(), "修改联系人成功！");
                            Intent data=new Intent();
                            String name =et_people_add_name.getText().toString();
                            bean.setName(name);
                            bean.setPhone(et_people_add_phone.getText().toString());
                            bean.setIDcard(et_people_add_IDcard.getText().toString());
                            bean.setAddress(et_people_add_add.getText().toString());
                            bean.setCompany(et_people_add_company.getText().toString());
                            bean.setDuty(et_people_add_duty.getText().toString());
                            bean.setEmail(et_people_add_email.getText().toString());
                            bean.setSex(sp_people_add_sex.getSelectedItemPosition()+"");
                            bean.setRemark(et_people_add_remark.getText().toString());
                            bean.setGroupName(sp_people_add_group.getSelectedItem()+"");
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("bean", bean);
                            data.putExtras(bundle);
                            setResult(20,data);
                            finish();
                        }
                        break;

                    default:
                        break;
                }
            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_people_add:
                String param;
                String Name = et_people_add_name.getText().toString();
                if (Name.length() == 0) {
                    ToastUtil.makeText(this, "姓名不得为空，请重新输入~");
                    return;
                }
                String Mobile = et_people_add_phone.getText().toString();
                if (Mobile.length() != 11) {
                    ToastUtil.makeText(this, "请输入11位电话号码，请检查后重新输入~");
                    return;
                }

                String ContactGroupID;
                int i = sp_people_add_group.getSelectedItemPosition();
                ContactGroupID = list_id.get(i);
                if (ContactGroupID == null) {
                    ContactGroupID = "0";
                }
                if(bean!=null){
                    param = "TrueName=" + Name + "&Mobile=" + Mobile + "&ContactGroupID=" + ContactGroupID+"&ID="+bean.getID()+"&Address="+et_people_add_add.getText().toString()
                    +"&Company="+et_people_add_company.getText().toString()+"&Duty="+et_people_add_duty.getText().toString()+"&SerialNo="+et_people_add_SerialNo.getText().toString()
                    +"&IDcard="+et_people_add_IDcard.getText().toString()+"&Remark="+et_people_add_remark.getText().toString()+"&Email="+et_people_add_email.getText().toString()+"&Sex="+sp_people_add_sex.getSelectedItemPosition();
updatePeople(param);
                }
                else{
                param = "TrueName=" + Name + "&Mobile=" + Mobile + "&ContactGroupID=" + ContactGroupID+"&Address="+et_people_add_add.getText().toString()
                        +"&Company="+et_people_add_company.getText().toString()+"&Duty="+et_people_add_duty.getText().toString()+"&SerialNo="+et_people_add_SerialNo.getText().toString()
                        +"&IDcard="+et_people_add_IDcard.getText().toString()+"&Remark="+et_people_add_remark.getText().toString()+"&Email="+et_people_add_email.getText().toString()+"&Sex="+sp_people_add_sex.getSelectedItemPosition();
                addPeople(param);
                }
                break;
        }
    }
}
