package com.zhaohe.zhundao.ui.home.mine;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhaohe.app.utils.IntentUtils;
import com.zhaohe.app.utils.JSONUtils;
import com.zhaohe.app.utils.NetworkUtils;
import com.zhaohe.app.utils.ProgressDialogUtils;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.AccountAdapter;
import com.zhaohe.zhundao.asynctask.login.AsyncLogin;
import com.zhaohe.zhundao.bean.AccessKeyBean;
import com.zhaohe.zhundao.bean.AccountBean;
import com.zhaohe.zhundao.dao.MyAccountDao;
import com.zhaohe.zhundao.ui.ToolBarActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zhaohe.zhundao.ui.login.LoginActivity.MESSAGE_PHONE_ENTY;

public class AccountActivity extends ToolBarActivity implements AdapterView.OnItemClickListener, Toolbar.OnMenuItemClickListener, AdapterView.OnItemLongClickListener {
    AccountAdapter adapter;
    @BindView(R.id.lv_account)
    ListView lvAccount;
    MyAccountDao dao;
    List<AccountBean> list;
    AccountBean accountBean;
    int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBarNew("多账号管理", R.layout.activity_account);
        ButterKnife.bind(this);
        initView();
        initHandler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initList();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu_account, menu);
        toolbar.setOnMenuItemClickListener(this);

        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_account_add:
                IntentUtils.startActivity(this, AccountAddActivity.class);


        }


        return false;
    }

    private void initList() {
        list = new ArrayList<>();
        list = dao.queryAll();
        AccountBean bean;
        for (int i = 0; i < list.size(); i++) {
            bean = list.get(i);
            ToastUtil.print(bean.getName() + bean.getStatus() + "");
            if (bean.getStatus().equals("true")) {
                lvAccount.setItemChecked(i, true);
            }
        }
//        Collections.reverse(list);
        adapter.refreshData(list);
    }

    public void initView() {


        dao = new MyAccountDao(this);

        adapter = new AccountAdapter(this);
        lvAccount.setAdapter(adapter);
        lvAccount.setOnItemClickListener(this);
        lvAccount.setOnItemLongClickListener(this);

        lvAccount.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        accountBean = adapter.getItem(position);
        mPosition = position;
        init();

    }

    private void changeAccount(AccountBean bean, int position) {

        if (bean.getStatus().equals("true")) {
            return;
        } else {
            SPUtils.clear(this);

            AccountBean bean2;
            for (int i = 0; i < list.size(); i++) {
                bean2 = list.get(i);
                ToastUtil.print(bean2.getName() + bean2.getStatus() + "");
                if (bean2.getStatus().equals("true")) {
                    bean2.setStatus("false");
                    dao.save(bean2);
                }
            }
            SPUtils.put(getApplicationContext(), "islogin", true);
            SPUtils.put(this, "updateAction", true);
            SPUtils.put(this, "updateSign", true);

            bean.setStatus("true");
            dao.save(bean);
            initList();
            lvAccount.setItemChecked(position, true);
            finish();
            ToastUtil.print("苗药" + (String) SPUtils.get(this, "accessKey", ""));

        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        AccountBean bean = adapter.getItem(position);
        deleteDialog(bean);
        return true;
    }


    private void deleteDialog(final AccountBean bean) {
        new android.support.v7.app.AlertDialog.Builder(this)
                //对话框的标题
                .setTitle("确定删除该账号吗")
                //设定显示的View
                //对话框中的“登陆”按钮的点击事件
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if (bean.getStatus().equals("true")) {
                            ToastUtil.makeText(getApplicationContext(), "当前选中账号不得删除！");
                        } else {
                            dao.deleteGroupByID(bean.getPhone());
                            initList();

                        }

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

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_PHONE_ENTY:
                        String result = (String) msg.obj;
//                        DialogUtils.showDialog(LoginActivity.this, R.string.app_serviceReturnrRigth);
//                        Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG).show();
                        System.out.println("code" + result);
                        AccessKeyBean accessKeyBean = JSONUtils.parseObject(result, AccessKeyBean.class);
                        System.out.println("parseBeanObject()方法：accessKeyBean==" + "accessKeyBean" + accessKeyBean.getAccessKey() + "getMsg" + accessKeyBean.getMsg() + "getRes" + accessKeyBean.getRes());
                        Log.i("result", "" + accessKeyBean.toString());
                        if (accessKeyBean.getRes() == 0) {
//                            Constant.ACCESSKEY=accessKeyBean.getAccessKey();
                            changeAccount(accountBean, mPosition);
                            SPUtils.put(getApplicationContext(), "accessKey", accessKeyBean.getAccessKey());
                            ToastUtil.makeText(getApplicationContext(), "切换成功！");
                            finish();
//                            IntentUtils.startActivity(AccountAddActivity.this, HomeActivity.class);
                        } else {
                            ToastUtil.makeText(getApplicationContext(), "账号密码已修改，请重新登录添加！");

                        }

                        break;

                    default:
                        break;
                }
            }
        };
    }

    private void init() {

        if (NetworkUtils.checkNetState(this)) {

            Dialog dialog = ProgressDialogUtils.showProgressDialog(this, getString(R.string.progress_title), getString(R.string.progress_message));
            AsyncLogin asyncLogin = new AsyncLogin(this, mHandler, dialog, MESSAGE_PHONE_ENTY, accountBean.getPhone(), accountBean.getAccessKey());
            asyncLogin.execute();


        } else {
            ToastUtil.makeText(this, R.string.app_serviceError);
        }
    }
}
