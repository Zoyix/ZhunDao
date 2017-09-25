package com.zhaohe.zhundao.ui.home.sign;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;
import com.zhaohe.app.utils.NetworkUtils;
import com.zhaohe.app.utils.ProgressDialogUtils;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.TimeUtil;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.SignupListAdapter;
import com.zhaohe.zhundao.asynctask.AsyncScanCode;
import com.zhaohe.zhundao.asynctask.AsyncSignList;
import com.zhaohe.zhundao.asynctask.AsyncSignScanPhone;
import com.zhaohe.zhundao.asynctask.AsyncSignupList;
import com.zhaohe.zhundao.bean.dao.MySignListupBean;
import com.zhaohe.zhundao.dao.MySignupListDao;
import com.zhaohe.zhundao.ui.ToolBarActivity;
import com.zhaohe.zhundao.ui.ToolBarHelper;
import com.zhaohe.zhundao.ui.home.action.SignListActivity;
import com.zhaohe.zhundao.zxing.controller.MipcaActivityCapture;

import java.util.ArrayList;
import java.util.List;

import static com.zhaohe.zhundao.ui.home.action.ActionOnFragment.MESSAGE_GET_SIGNLIST;


public class SignupListActivity extends ToolBarActivity implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener,SignupListAdapter.SignupListClickListener,Toolbar.OnMenuItemClickListener,AdapterView.OnItemClickListener {
    private String sign_id;
    private String act_id;
    private SignupListAdapter adapter, adapter2, adapter3;
    private ListView lv_signup, lv_signon, lv_signoff;
    private String signup_list,result_list;
    private TextView tv_signup_all, tv_signup_on, tv_signup_off;
    private JSONObject jsonObj;
    private JSONArray jsonArray;
    private SwipeRefreshLayout mSwipeLayout;
    private int numShould, numFact, numRest;
    private String phone;//当前选择的手机
    private List all,on,off;

    private MySignupListDao dao;
    public static final int PAGE_SIZE = 100000;
    public static final int MESSAGE_SIGN_SCAN_PHONE=100;
    public static final int SCANNIN_GREQUEST_CODE = 89;
    public static final int MESSAGE_SCAN_CODE = 90;
    private static final int REQUEST_CODE_PERMISSION = 105;

    private static final int REQUEST_CODE_SETTING = 300;
    public static final int MESSAGE_GET_SIGNUPLIST = 92;
    public static final int REFRESH_COMPLETE = 98;
    private String status="%%";
private EditText et_signuplist_search;
    private Handler mHandler;
    private MySignListupBean mbean;
private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_list);
        initIntent();
        initToolBar(title, R.layout.activity_signup_list);
        initHandler();
        initView();
        init();
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu_sign_list_scan, menu);
        toolbar.setOnMenuItemClickListener(this);

        return true;
    }

    private void initView() {
        lv_signup = (ListView) findViewById(R.id.lv_signuplist_all);
        adapter = new SignupListAdapter(this);
        adapter.setSignupListClickListener(this);

        lv_signup.setAdapter(adapter);
        lv_signup.setOnItemClickListener(this);

        tv_signup_all = (TextView) findViewById(R.id.tv_signup_all);
        tv_signup_all.setOnClickListener(this);
        tv_signup_on = (TextView) findViewById(R.id.tv_signup_on);
        tv_signup_on.setOnClickListener(this);
        tv_signup_off = (TextView) findViewById(R.id.tv_signup_off);
        tv_signup_off.setOnClickListener(this);
        mSwipeLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_signup_list);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        dao = new MySignupListDao(this);
        all=dao.queryListByCheckinIDAndStatus(sign_id,"%%");
        on=dao.queryListByCheckinIDAndStatus(sign_id,"true");
        off=dao.queryListByCheckinIDAndStatus(sign_id,"false");
        et_signuplist_search= (EditText) findViewById(R.id.et_signuplist_search);
        et_signuplist_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //TODO:
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.refreshData(dao.queryListByPhoneNameAndCheckInID(sign_id,et_signuplist_search.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {
                //TODO:
            }
        });

    }

    public void init() {
        adapter.refreshData(dao.queryListByCheckinIDAndStatus(sign_id,"%%"));
        tv_signup_all.setText("全部（" + all.size() + "）");
        tv_signup_on.setText("已签（" + on.size() + "）");
        tv_signup_off.setText("未签（" + off.size() + "）");
        String mParam = "ActivityID=" + act_id + "&pageSize=" + PAGE_SIZE;
        getSignList(mParam);

    }

    private void initIntent() {
        Intent intent = getIntent();
        //从Intent当中根据key取得value
        sign_id = intent.getStringExtra("sign_id");
        act_id = intent.getStringExtra("act_id");

        title=intent.getStringExtra("title");

        ToastUtil.print(title);

    }

    public void resetColor() {
        tv_signup_all.setTextColor(Color.rgb(56, 56, 56));
        tv_signup_off.setTextColor(Color.rgb(56, 56, 56));
        tv_signup_on.setTextColor(Color.rgb(56, 56, 56));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_signup_all:
                resetColor();
                tv_signup_all.setTextColor(Color.rgb(87, 153, 8));
                signall();
                status="%%";
                break;

            case R.id.tv_signup_on:
                resetColor();
                tv_signup_on.setTextColor(Color.rgb(87, 153, 8));
                signon();
                status="true";
                break;
            case R.id.tv_signup_off:
                resetColor();
                signoff();
                tv_signup_off.setTextColor(Color.rgb(87, 153, 8));
                status="false";
                break;

        }
    }

    private void signon() {
        adapter.refreshData(dao.queryListByCheckinIDAndStatus(sign_id,"true"));

    }
    private void signoff() {
        adapter.refreshData(dao.queryListByCheckinIDAndStatus(sign_id,"false"));
    }
    private void signall() {
        adapter.refreshData(dao.queryListByCheckinIDAndStatus(sign_id,"%%"));
    }

    @Override
    public void onRefresh() {
        String mParam = "ID=" +sign_id + "&pageSize=" + PAGE_SIZE;
        getSignupList(mParam);
        mParam = "ActivityID=" + act_id + "&pageSize=" + PAGE_SIZE;
        getSignList(mParam);
        mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);

    }
    private void getSignupList(String sign_id) {
        Dialog dialog = ProgressDialogUtils.showProgressDialog(this, getString(R.string.progress_title), getString(R.string.progress_message));
        AsyncSignupList asyncSignupList = new AsyncSignupList(this, mHandler, dialog, MESSAGE_GET_SIGNUPLIST, sign_id);
        asyncSignupList.execute();
    }
    //有网时插入数据库
    private void insertSignupList(String result) {
        JSONObject jsonObj = JSON.parseObject(result);
        JSONArray jsonArray = jsonObj.getJSONArray("Data");
        List<MySignListupBean> list = new ArrayList<MySignListupBean>();
        if (jsonArray == null) {
            return;
        }
        for (int i = 0; i < jsonArray.size(); i++) {
            MySignListupBean bean = new MySignListupBean();
            bean.setVCode(jsonArray.getJSONObject(i).getString("VCode"));
            bean.setCheckInID(jsonArray.getJSONObject(i).getString("CheckInID"));
            bean.setStatus(jsonArray.getJSONObject(i).getString("Status"));
            bean.setName(jsonArray.getJSONObject(i).getString("TrueName"));
            bean.setPhone(jsonArray.getJSONObject(i).getString("Mobile"));
            bean.setAdminRemark(jsonArray.getJSONObject(i).getString("AdminRemark"));
            bean.setFeeName(jsonArray.getJSONObject(i).getString("FeeName"));
            bean.setFee(jsonArray.getJSONObject(i).getString("Fee"));
            bean.setUpdateStatus("false");
            bean.setCheckInTime(jsonArray.getJSONObject(i).getString("SignTime"));
            list.add(bean);
        }
        dao.save(list);


    }


    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case REFRESH_COMPLETE:
                        mSwipeLayout.setRefreshing(false);
                        break;
//                    获取用户名单，将数据插入数据库
                    case MESSAGE_GET_SIGNUPLIST:
                        String result2 = (String) msg.obj;
                        updateList(result2);

                        break;
                    case MESSAGE_SIGN_SCAN_PHONE:
                    String result3 = (String) msg.obj;
                    JSONObject jsonObj = JSON.parseObject(result3);
                    String message = jsonObj.getString("Msg");
                    if (jsonObj.getString("Res").equals("0")) {
                        mbean.setStatus("true");
                        mbean.setCheckInTime(TimeUtil.getNowTime());
                        dao.updateByPhone(mbean);
                        updateList();
                        adapter.refreshData(dao.queryListByCheckinIDAndStatus(sign_id,status));

//                            ToastUtil.makeText(getActivity(), message + "\n" + "姓名：" + Name + "\n" + "电话：" + newPhone + "\n" +
//                                    "备注：" + AdminRemark + "\n" + FeeStr);

                      ToastUtil.makeText(getApplicationContext(),message);

                    }


                    else
                    {
                        ToastUtil.makeText(getApplicationContext(),message);
//                            ToastUtil.makeText(getActivity(), message);
                    }
                    break;

                    case MESSAGE_SCAN_CODE:
                         result3 = (String) msg.obj;
                        jsonObj = JSON.parseObject(result3);
                        message = jsonObj.getString("Msg");
                        if (jsonObj.getString("Res").equals("0")) {
                            JSONObject jsonObject2 = JSON.parseObject(jsonObj.getString("Data"));
                            String Name = jsonObject2.getString("Name");
                            String Phone = jsonObject2.getString("Phone");
                            String newPhone = Phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                            String AdminRemark = jsonObject2.getString("AdminRemark");
                            if (AdminRemark == null) {
                                AdminRemark = "无";
                            }
                            String FeeName = jsonObject2.getString("FeeName");
                            String Fee = jsonObject2.getString("Fee");
                            String FeeStr = FeeName + "：" + Fee;
                            if (FeeName == null) {
                                FeeStr = "";
                            }
//                            ToastUtil.makeText(getActivity(), message + "\n" + "姓名：" + Name + "\n" + "电话：" + newPhone + "\n" +
//                                    "备注：" + AdminRemark + "\n" + FeeStr);
                            mbean.setStatus("true");
                            mbean.setCheckInTime(TimeUtil.getNowTime());
                            dao.update(mbean);
                            updateList();
                            resultDialog(message,"姓名：" + Name + "\n" + "电话：" + newPhone + "\n" +
                                    "备注：" + AdminRemark + "\n" + FeeStr);

                        } else {
                            resultDialog("扫码失败！",message);
//                            ToastUtil.makeText(getActivity(), message);
                        }
                        break;

                    case MESSAGE_GET_SIGNLIST:
                         result2 = (String) msg.obj;
                        jsonObj = JSON.parseObject(result2);
                        message = jsonObj.getString("Msg");

                        if (jsonObj.getString("Res").equals("0") ){

                            SPUtils.put(getApplicationContext(), "listup_" + act_id, result2);
//                        GoToList();
                    }
                    else{
                                                        ToastUtil.makeText(getApplicationContext(), message);

                        }

                        break;
                    default:
                        break;
                }
            }
        };


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
                    String CheckInID = data.getStringExtra("CheckInID");

                    System.out.println(result);
                    if (NetworkUtils.checkNetState(this)) {
//                    有网时与服务器上的VCode进行比对
                        mbean=new MySignListupBean();
                        mbean.setVCode(result);
                        mbean.setCheckInID(sign_id);
                        mbean.setUpdateStatus("false");

                        scanCode(result);
                    } else {
                        Toast.makeText(this, CheckInID, Toast.LENGTH_LONG);
                        List<MySignListupBean> list = dao.queryListByVCodeAndCheckInID(result, CheckInID);
                        List<MySignListupBean> list2 = dao.queryListStatus(result, CheckInID, "true");
                        if (list.size() == 0) {
                            resultDialog("扫码失败","凭证码无效！");
                        } else if (list2.size() == 1) {
                            MySignListupBean bean = (MySignListupBean) list.get(0);
                            String Name = bean.getName();
                            String Phone = bean.getPhone();
                            String AdminRemark = bean.getAdminRemark();
                            String FeeName = bean.getFeeName();
                            String Fee = bean.getFee();
                            String FeeStr = FeeName + "：" + Fee;
                            if (FeeName == null) {
                                FeeStr = "";
                            }
                            String newPhone = Phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
//                            ToastUtil.makeText(getActivity(), "该用户已经签到！" + "\n" + "姓名：" + Name + "\n" + "电话：" + newPhone + "\n" +
//                                    "备注：" + AdminRemark + "\n" + FeeStr);
                            resultDialog("该用户已经签到","姓名：" + Name + "\n" + "电话：" + newPhone + "\n" +
                                    "备注：" + AdminRemark + "\n" + FeeStr);
                        } else {
                            MySignListupBean bean = new MySignListupBean();
                            bean.setVCode(result);
                            bean.setStatus("true");
                            bean.setUpdateStatus("true");
                            bean.setCheckInID(CheckInID);
                            bean.setCheckInTime(TimeUtil.getNowTime());
                            dao.update(bean);
                            MySignListupBean bean2 = (MySignListupBean) list.get(0);
                            String Name = bean2.getName();
                            String Phone = bean2.getPhone();
                            String AdminRemark = bean2.getAdminRemark();
                            String FeeName = bean2.getFeeName();
                            String Fee = bean2.getFee();
                            String FeeStr = FeeName + "：" + Fee;
                            if (FeeName == null) {
                                FeeStr = "";
                            }
                            String newPhone = Phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
//                            ToastUtil.makeText(getActivity(), "扫码成功" + "\n" + "姓名：" + Name + "\n" + "电话：" + newPhone + "\n" +
//                                    "备注：" + AdminRemark + "\n" + FeeStr);
                            updateList();
                            resultDialog("扫码成功","姓名：" + Name + "\n" + "电话：" + newPhone + "\n" +
                                    "备注：" + AdminRemark + "\n" + FeeStr);

                        }


                    }
                }
                if (requestCode == RESULT_CANCELED) {
                    ToastUtil.makeText(this, "未授权相机权限，请授权后重试");
                }
                if (requestCode == REQUEST_CODE_SETTING) {
                    Toast.makeText(this, R.string.message_setting_back, Toast.LENGTH_LONG).show();
                }

                ;
        }
    }
//    public void resultDialog(String status,String message){
//        new AlertDialog.Builder(this)
//                .setIcon(R.mipmap.ic_launcher)
//                .setTitle(status)
//                .setMessage(message)
//                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                })
//
//                .setCancelable(true)
//                .show();
//    }
public void resultDialog(String status,String message){
    new AlertDialog.Builder(this)
            .setIcon(R.mipmap.ic_launcher)
            .setTitle(status)
            .setMessage(message)
            .setPositiveButton("继续扫码", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setClass(getApplication(), MipcaActivityCapture.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("CheckInID", sign_id);
                    startActivityForResult(intent, SCANNIN_GREQUEST_CODE);


                }
            })

            .setNeutralButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    init();
                }
            })
            .setCancelable(true)
            .show();
}

    private void updateList(String result2) {
        JSONObject jsonObj = JSON.parseObject(result2);
        if (result2 == null) {
            return;
        }
        if (jsonObj.getString("Data") == null) {
            return;
        }
        if (jsonObj.getByte("Count") == 0) {
            ToastUtil.makeText(getApplicationContext(), "暂无人签到");
            return;
        }
        insertSignupList(result2);
        SPUtils.put(getApplicationContext(), "signup_" + sign_id, result2);
        updateList();
    }

    private void updateList() {
        tv_signup_all.setText("全部（" + dao.queryListByCheckinIDAndStatus(sign_id,"%%").size()
        + "）");
        tv_signup_on.setText("已签（" + dao.queryListByCheckinIDAndStatus(sign_id,"true").size() + "）");
        tv_signup_off.setText("未签（" + dao.queryListByCheckinIDAndStatus(sign_id,"false").size() + "）");
        adapter.refreshData(dao.queryListByCheckinIDAndStatus(sign_id,status));
    }

    private  void SignScanPhone(String phone){
        AsyncSignScanPhone async = new AsyncSignScanPhone(this, mHandler, MESSAGE_SIGN_SCAN_PHONE, phone,sign_id);
        async.execute();
    }
    public void cancelDialog(final MySignListupBean bean) {

        mbean = bean;


            String Name = bean.getName();
            String Phone = bean.getPhone();
            String newPhone = Phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            String AdminRemark = bean.getAdminRemark();
            if (AdminRemark == null) {
                AdminRemark = "无";
            }
            String FeeName = bean.getFeeName();
            String Fee = bean.getFee();
            String FeeStr = FeeName + "：" + Fee;
            if (FeeName == null) {
                FeeStr = "";
            }
            new AlertDialog.Builder(this)
                    //对话框的标题
                    .setTitle("是否代签")
                    //设定显示的View
                    //对话框中的“登陆”按钮的点击事件
                    .setMessage("姓名：" + Name + "\n" + "电话：" + newPhone + "\n" +
                            "备注：" + AdminRemark + "\n" + FeeStr)
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            signup(bean);

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
    public void scanCode(String result) {

        AsyncScanCode asyncScanCode = new AsyncScanCode(this, mHandler, MESSAGE_SCAN_CODE, result, sign_id);
        asyncScanCode.execute();
    }

    @Override
    public void signupClick(MySignListupBean bean) {
        cancelDialog(bean);
    }

    private void signup(MySignListupBean bean) {
        mbean=bean;
        if (NetworkUtils.checkNetState(this)) {
            SignScanPhone(bean.getPhone());
        }
        else{
            bean.setStatus("true");
            bean.setUpdateStatus("true");
            bean.setCheckInTime(TimeUtil.getNowTime());
            dao.updateByPhone(bean);
            updateList();
            ToastUtil.makeText(getApplicationContext(),"代签成功");

        }
    }

    private void getSignList(String mParam) {
        AsyncSignList asyncSignList = new AsyncSignList(this, mHandler, MESSAGE_GET_SIGNLIST, mParam);
        asyncSignList.execute();
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_sign_list_scan:
//                String a="0";
//                String b ="0";
//                System.out.println("a==b?"+(a==b)+"a equals b?"+(a.equals(b))+"a hashcode"+a.hashCode()+"b hash code"+b.hashCode()
//                );

//                if (cameraIsCanUse()) {
//                    if (SPUtils.contains(this, "signup_" + sign_id) == true) {
//                        Intent intent = new Intent();
//                        intent.setClass(this, MipcaActivityCapture.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        intent.putExtra("CheckInID",sign_id);
//                        intent.putExtra("view_show","true" );
//                        startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
//                    } else if (NetworkUtils.checkNetState(this)) {
//                        String mParam = "ID=" + sign_id;
//                        ToastUtil.makeText(this, "暂未获取报名名单，自动跳转获取");
//                        getSignupList(mParam);
//                    } else {
//                        ToastUtil.makeText(this, "未获得名单，无网络，请在有网后重试");
//
//                    }
//                } else {
//                    ToastUtil.makeText(this, "未获得相机权限，请授权后再试！");
//
//                }
                AndPermission.with(this)
                        .requestCode(REQUEST_CODE_PERMISSION)
                        .permission(Permission.CAMERA)
                        .callback(permissionListener)
                        // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                        // 这样避免用户勾选不再提示，导致以后无法申请权限。
                        // 你也可以不设置。
                        .rationale(null)
                        .start();
                break;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MySignListupBean bean= adapter.getItem(i);
        phone=bean.getPhone();


        if (SPUtils.contains(this, "listup_" + act_id)) {
//            ToastUtil.makeText(this,"请先获取该签到的活动名单再试~");
            GoToList();

        } else {
            ToastUtil.makeText(this, R.string.net_error);
        }

    }

    private void GoToList() {
        Intent intent = new
                Intent(this, SignListActivity.class);
        intent.putExtra("act_id", act_id);
        intent.putExtra("phone",phone);
        startActivity(intent);
    }

    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {
                case REQUEST_CODE_PERMISSION: {
                    if (SPUtils.contains(getApplication(), "signup_" + sign_id) == true) {
                        Intent intent = new Intent();
                        intent.setClass(getApplication(), MipcaActivityCapture.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("CheckInID", sign_id);
                        intent.putExtra("view_show", "true");
                        startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                    } else if (NetworkUtils.checkNetState(getApplication())) {
                        String mParam = "ID=" + sign_id + "&pageSize=" + PAGE_SIZE;
                        ToastUtil.makeText(getApplication(), "暂未获取报名名单，自动跳转获取");
                        getSignupList(mParam);
                    } else {
                        ToastUtil.makeText(getApplication(), "未获得名单，无网络，请在有网后重试");

                    }
                    break;
                }
            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            switch (requestCode) {
                case REQUEST_CODE_PERMISSION: {
                    ToastUtil.makeText(getApplicationContext(), "授权失败！");

                    break;
                }
            }
        }
    };

}