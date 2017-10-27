package com.zhaohe.zhundao.ui.home.mine.contacts;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;
import com.zhaohe.app.utils.NetworkUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.asynctask.contacts.AsyncDeletePeople;
import com.zhaohe.zhundao.bean.dao.MyContactsBean;
import com.zhaohe.zhundao.dao.MyContactsDao;
import com.zhaohe.zhundao.ui.ToolBarActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class PeopleActivity extends ToolBarActivity implements Toolbar.OnMenuItemClickListener, SearchView.OnClickListener {

    private MyContactsBean bean;
    private TextView tv_contacts_name, tv_contacts_phone, tv_contacts_group, tv_contacts_sex, tv_contacts_company, tv_contacts_duty,
            tv_contacts_IDcard, tv_contacts_SerialNo, tv_contacts_remark, tv_contacts_add, tv_contacts_email;
    private Handler mHandler;
    private MyContactsDao dao;
    private ImageView iv_contacts_phone, iv_contacts_msg;

    public static final int MESSAGE_DELETE_PEOPLE = 95;
    String phone;
    private static final int REQUEST_CODE_PHONE = 100;
    private static final int REQUEST_CODE_MSG = 101;

    private static final int REQUEST_CODE_SETTING = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);
        ButterKnife.bind(this);
        initHandler();
        initIntent();
        initToolBarNew("联系人详情", R.layout.activity_people);
        initView();
        showView();

    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu_people, menu);
        toolbar.setOnMenuItemClickListener(this);

        return true;
    }

    private void initView() {
        iv_contacts_phone = (ImageView) findViewById(R.id.iv_contacts_phone);
//        iv_contacts_phone.setImageBitmap(ZXingUtil.getLoacalBitmap("/mnt/sdcard/Zhundao/不错"));
        iv_contacts_phone.setOnClickListener(this);
        iv_contacts_msg = (ImageView) findViewById(R.id.iv_contacts_msg);
        iv_contacts_msg.setOnClickListener(this);
        tv_contacts_name = (TextView) findViewById(R.id.tv_contacts_name);
        tv_contacts_name.setText(bean.getName());
        tv_contacts_phone = (TextView) findViewById(R.id.tv_contacts_phone);
        tv_contacts_phone.setText(bean.getPhone());
        tv_contacts_sex = (TextView) findViewById(R.id.tv_contacts_sex);

        tv_contacts_company = (TextView) findViewById(R.id.tv_contacts_company);


        tv_contacts_duty = (TextView) findViewById(R.id.tv_contacts_duty);

        tv_contacts_SerialNo = (TextView) findViewById(R.id.tv_contacts_SerialNo);

        tv_contacts_IDcard = (TextView) findViewById(R.id.tv_contacts_IDcard);

        tv_contacts_remark = (TextView) findViewById(R.id.tv_contacts_remark);

        tv_contacts_email = (TextView) findViewById(R.id.tv_contacts_email);

        tv_contacts_add = (TextView) findViewById(R.id.tv_contacts_add);

        dao = new MyContactsDao(this);
        tv_contacts_group = (TextView) findViewById(R.id.tv_contacts_group);


    }

    private void showView() {
        if (bean.getSex().equals("1")) {
            tv_contacts_sex.setText("男");

        }
        if (bean.getSex().equals("2")) {
            tv_contacts_sex.setText("女");
        }
        if (bean.getCompany() != null) {
            tv_contacts_company.setText(bean.getCompany());

        }
        if (bean.getDuty() != null) {
            tv_contacts_duty.setText(bean.getDuty());
        }
        if (bean.getSerialNo() != null) {
            tv_contacts_SerialNo.setText(bean.getSerialNo());
        }
        if (bean.getCompany() != null) {
            tv_contacts_IDcard.setText(bean.getIDcard());
        }
        if (bean.getRemark() != null) {
            tv_contacts_remark.setText(bean.getRemark());

        }
        if (bean.getEmail() != null) {
            tv_contacts_email.setText(bean.getEmail());

        }
        if (bean.getAddress() != null) {
            tv_contacts_add.setText(bean.getAddress());

        }
        if (bean.getGroupName() == null) {
            tv_contacts_group.setText("无分组");
        } else {
            tv_contacts_group.setText(bean.getGroupName());
        }

    }

    private void initIntent() {
        Intent intent = this.getIntent();
        bean = (MyContactsBean) intent.getSerializableExtra("bean");
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_people_edit:
                Intent intent = new Intent();
                intent.setClass(this, PeopleAddActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", bean);
                intent.putExtras(bundle);
                startActivityForResult(intent, 100);
                break;
            case R.id.menu_people_delete:
                deleteDialog();
//                IntentUtils.startActivity(this,textActivity.class);
                //测试BIND ID
                break;


        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //可以根据多个请求代码来作相应的操作
        if (20 == resultCode) {
            bean = (MyContactsBean) data.getSerializableExtra("bean");
            showView();


        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    public void deleteDialog() {

        //LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化

        new AlertDialog.Builder(this)
                //对话框的标题
                .setTitle("确认要删除联系人吗？")
                //设定显示的View
                //对话框中的“登陆”按钮的点击事件
//                .setMessage("确定后，该组的群员将没有分组。")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deletePeople();
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

    private void deletePeople() {
        if (NetworkUtils.checkNetState(this)) {
            AsyncDeletePeople async = new AsyncDeletePeople(this, mHandler, MESSAGE_DELETE_PEOPLE, bean.getID());
            async.execute();
        }
    }

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {

                    case MESSAGE_DELETE_PEOPLE:
                        String result = (String) msg.obj;
                        JSONObject jsonObj = JSON.parseObject(result);
                        String message = jsonObj.getString("Res");
                        System.out.println("group_delete_result:" + result);
                        if (message.equals("0"))
                        //添加或修改请求结果
                        {
                            ToastUtil.makeText(getApplicationContext(), "删除联系人成功！");
                            dao.deleteGroupByID(bean.getID());
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
        phone = tv_contacts_phone.getText().toString();
        switch (view.getId()) {
            case R.id.iv_contacts_phone:

                if (phone.equals("") && phone.equals(null)) {
                    ToastUtil.makeText(this, "号码不得为空");
                } else {
                    AndPermission.with(this)
                            .requestCode(REQUEST_CODE_PHONE)
                            .permission(Permission.PHONE)
                            .callback(permissionListener)
                            // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                            // 这样避免用户勾选不再提示，导致以后无法申请权限。
                            // 你也可以不设置。
                            .rationale(null)
                            .start();


                }
                break;
            case R.id.iv_contacts_msg:
                if (phone.equals("") && phone.equals(null)) {
                    ToastUtil.makeText(this, "号码不得为空");
                } else {

                    AndPermission.with(this)
                            .requestCode(REQUEST_CODE_MSG)
                            .permission(Permission.SMS)
                            .callback(permissionListener)
                            // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                            // 这样避免用户勾选不再提示，导致以后无法申请权限。
                            // 你也可以不设置。
                            .rationale(null)
                            .start();

                }
                break;


        }
    }

    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {
                case REQUEST_CODE_PHONE: {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));

                    startActivity(intent);
                    break;


                }
                case REQUEST_CODE_MSG:
                    Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("smsto:" + phone));

                    startActivity(intent);
                    break;

            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            switch (requestCode) {
                case REQUEST_CODE_PHONE: {

                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));

                    startActivity(intent);

                    break;
                }
                case REQUEST_CODE_MSG:
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phone));

                    startActivity(intent);
                    break;
            }
        }
    };


    @OnClick(R.id.tv_contacts_head)
    public void onViewClicked() {
        ToastUtil.makeText(this, "头像");
    }
}
