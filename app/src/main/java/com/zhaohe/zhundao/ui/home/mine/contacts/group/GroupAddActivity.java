package com.zhaohe.zhundao.ui.home.mine.contacts.group;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhaohe.app.utils.NetworkUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.asynctask.contacts.AsyncUpdateGroup;
import com.zhaohe.zhundao.bean.dao.MyGroupBean;
import com.zhaohe.zhundao.ui.ToolBarActivity;

public class GroupAddActivity extends ToolBarActivity implements View.OnClickListener {
    private EditText et_group_add_name, et_group_add_num;
    public static final int MESSAGE_AddContactGroup = 94;
    public static final int MESSAGE_UpdateContactGroup = 95;

    private MyGroupBean bean;

    private Button btn_people_add;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_add);
        initView();
        initIntent();

        initHandler();

    }

    private void initIntent() {

            Intent intent = this.getIntent();
            bean = (MyGroupBean) intent.getSerializableExtra("bean");
        if (bean!=null){
            initToolBarNew("编辑群组", R.layout.activity_group_add);
            et_group_add_num.setText(bean.getSequence());
            et_group_add_name.setText(bean.getName());

        }
        else{
            initToolBarNew("新建群组", R.layout.activity_group_add);

        }
    }

    private void initView() {
        et_group_add_name = (EditText) findViewById(R.id.et_people_add_name);
        et_group_add_num = (EditText) findViewById(R.id.et_people_add_num);
        et_group_add_num.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        btn_people_add = (Button) findViewById(R.id.btn_people_add);
        btn_people_add.setOnClickListener(this);

    }

    public void updateGroup(String param) {
        if (NetworkUtils.checkNetState(this)) {
            AsyncUpdateGroup async = new AsyncUpdateGroup(this, mHandler, MESSAGE_AddContactGroup, param);
            async.execute();
        } else {
            ToastUtil.makeText(this, R.string.net_error);
        }
    }
    public void editGroup(String param) {
        if (NetworkUtils.checkNetState(this)) {
            AsyncUpdateGroup async = new AsyncUpdateGroup(this, mHandler, MESSAGE_UpdateContactGroup, param);
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

                    case MESSAGE_AddContactGroup:
                        String result = (String) msg.obj;
                        JSONObject jsonObj = JSON.parseObject(result);
                        String message = jsonObj.getString("Res");
                        System.out.println("group_add_result:" + result);
                        if (message.equals("0"))
                        //添加或修改请求结果
                        {
                            ToastUtil.makeText(getApplicationContext(), "新建群组成功！");
                            finish();
                        }
                        break;
                    case MESSAGE_UpdateContactGroup:
                         result = (String) msg.obj;
                         jsonObj = JSON.parseObject(result);
                         message = jsonObj.getString("Res");
                        System.out.println("group_add_result:" + result);
                        if (message.equals("0"))
                        //添加或修改请求结果
                        {
                            ToastUtil.makeText(getApplicationContext(), "编辑群组成功！");
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
                String name = et_group_add_name.getText().toString();
                if (name.length() == 0) {
                    ToastUtil.makeText(this, "分组名称不得为空，请重新输入~");
                    return;
                }
                String num = et_group_add_num.getText().toString();
                if (num.length() == 0) {
                    ToastUtil.makeText(this, "分组名称不得为空，请重新输入~");
                    return;
                }
                if(bean!=null){
                    param = "GroupName=" + name + "&Sequence=" + num+"&ID="+bean.getID();
                    editGroup(param);

                }
                else{
                    param = "GroupName=" + name + "&Sequence=" + num;
                    updateGroup(param);
                }

                break;
        }
    }
}
