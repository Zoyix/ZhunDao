package com.zhaohe.zhundao.ui.home.find;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhaohe.app.utils.CameraUtils;
import com.zhaohe.app.utils.IntentUtils;
import com.zhaohe.app.utils.NetworkUtils;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.asynctask.testScan.AsyncScanCodeMulti;
import com.zhaohe.zhundao.asynctask.testScan.AsyncSignupListMulti;
import com.zhaohe.zhundao.asynctask.testScan.AsyncUpLoadSignupStatusMulti;
import com.zhaohe.zhundao.bean.dao.MySignListupBean;
import com.zhaohe.zhundao.dao.MySignupListMultiDao;
import com.zhaohe.zhundao.ui.home.mine.setting.AboutUsActivity;
import com.zhaohe.zhundao.zxing.controller.MipcaActivityCapture;

import java.util.ArrayList;
import java.util.List;


public class MultipointActivity extends Activity implements View.OnClickListener,Toolbar.OnMenuItemClickListener{

    private EditText showScanResult;
    private TextView et_result;
    private TextView tv_sign_title;
    private TextView tv_sign_name;
    private TextView tv_sign_support;
    private TextView tv_find_multi_add;
    private TextView  tv_find_multi_status;
    private TextView tv_multi_sum,tv_multi_on,tv_multi_off;
    private Button btn_find_multi_sign;
private int sum=0,on=0,off=0;

    private MySignupListMultiDao dao;
    private Handler mHandler;
    private String mSignID;
    private TextView tv_sign_status;
    public static final int PAGE_SIZE = 1000;
    public static final int MESSAGE_SCAN_CODE = 90;
    public static final int MESSAGE_GET_SIGNUPLIST = 92;
    public static final int MESSAGE_UPLOAD_SIGNUPSTATUS = 88;
    public static final int SCANNIN_GREQUEST_CODE = 89;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multipoint);
        initToolBar();
        initHandler();
        initView();
//        upload();
    }



    private void initView() {
        String result = (String) SPUtils.get(this,"result_multi","");
        System.out.println("result:"+result);
        JSONObject jsonObject= JSON.parseObject(result);
        JSONObject jsonObject2 =JSON.parseObject(jsonObject.getString("Data"));
        JSONObject jsonObject3=JSON.parseObject(jsonObject2.getString("CheckInDto"));

        btn_find_multi_sign= (Button) findViewById(R.id.btn_find_multi_sign);
        btn_find_multi_sign.setOnClickListener(this);
        showScanResult = (EditText) findViewById(R.id.editText1);
        tv_sign_status= (TextView) findViewById(R.id.tv_sign_status);
        et_result = (TextView) findViewById(R.id.et_result);
        tv_sign_title = (TextView) findViewById(R.id.tv_sign_title_multi);
        tv_sign_support = (TextView) findViewById(R.id.tv_sign_support);
        tv_sign_support.setOnClickListener(this);
        tv_sign_name= (TextView) findViewById(R.id.tv_sign_name);
        tv_find_multi_add=(TextView) findViewById(R.id.tv_find_multi_add);
        tv_find_multi_add.setText(jsonObject2.getString("SignArea"));
        tv_find_multi_status=(TextView) findViewById(R.id.tv_find_multi_status);
        tv_multi_sum=(TextView) findViewById(R.id.tv_multi_sum);
        tv_multi_on=(TextView) findViewById(R.id.tv_multi_on);
        tv_multi_off=(TextView) findViewById(R.id.tv_multi_off);
        sum=jsonObject3.getInteger("NumShould");
        on=jsonObject3.getInteger("NumFact");
        off=sum-on;
        setNum();
if ("true".equals(jsonObject3.getString("Status")))
{ tv_find_multi_status.setText("进行中");}
        else{
    { tv_find_multi_status.setText("已关闭");}

}


        mSignID = (String) SPUtils.get(this, "sign_id", "");
        String mParam = "ID=" + mSignID + "&pageSize=" + PAGE_SIZE;
        dao = new MySignupListMultiDao(this);
        getSignupList(mParam);
        String sign_title = (String) SPUtils.get(this, "sign_title", "");
        if (sign_title != null) {
            tv_sign_title.setText(sign_title);
        }
        String UserName = (String) SPUtils.get(this, "UserName", "");
        if (UserName != null) {
            tv_sign_name.setText(UserName);
        }
    }

    private void setNum() {
        if(sum<=on){
            on=sum;
        }
        tv_multi_sum.setText("总人数:"+sum);
        tv_multi_on.setText("已签到:"+on);
        tv_multi_off.setText("未签到:"+(sum-on));
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
                    showScanResult.setText(result);
                    String CheckInID = data.getStringExtra("CheckInID");
                    System.out.println(result);
                    if (NetworkUtils.checkNetState(this)) {
//                    有网时与服务器上的VCode进行比对
                        scanCode(result);
                    } else {
                        List<MySignListupBean> list = dao.queryListByVCodeAndCheckInID(result, mSignID);
                        List<MySignListupBean> list2 = dao.queryListStatus(result, mSignID, "true");
                        if (list.size() == 0) {
                            tv_sign_status.setText("扫码失败，该凭证码有误");
                            et_result.setText("");
                        } else if (list2.size() == 1) {
                            MySignListupBean bean=(MySignListupBean)list.get(0);
                            String Name= bean.getName();
                            String Phone=bean.getPhone();
                            String AdminRemark=bean.getAdminRemark();
                            String FeeName=bean.getFeeName();
                            String Fee=bean.getFee();
                            String FeeStr = FeeName + "：" + Fee;
                            if (FeeName == null) {
                                FeeStr = "";
                            }
                            String newPhone= Phone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");

                            tv_sign_status.setText("该用户已经签到！");
                            et_result.setText("姓名：" + Name + "\n" + "电话：" + newPhone + "\n" +
                                    "备注：" + AdminRemark + "\n" + FeeStr);
                        } else {
                            MySignListupBean bean = new MySignListupBean();
                            bean.setVCode(result);
                            bean.setStatus("true");
                            bean.setUpdateStatus("true");
                            bean.setCheckInID(mSignID);
                            dao.update(bean);
                            tv_sign_status.setText("扫码成功");
                            MySignListupBean bean2=(MySignListupBean)list.get(0);
                            String Name= bean2.getName();
                            String Phone=bean2.getPhone();
                            String AdminRemark=bean2.getAdminRemark();
                            String FeeName=bean2.getFeeName();
                            String Fee=bean2.getFee();
                            String FeeStr = FeeName + "：" + Fee;
                            if (FeeName == null) {
                                FeeStr = "";
                            }
                            String newPhone= Phone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");

                            et_result.setText("姓名：" + Name + "\n" + "电话：" + newPhone + "\n" +
                                    "备注：" + AdminRemark + "\n" + FeeStr);
                            on=on+1;
                            setNum();
                        }
                    }
                }
                if (requestCode == RESULT_CANCELED) {
                    ToastUtil.makeText(this, "未授权相机权限，请授权后重试");
                }

                break;
        }
    }
    protected void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_find_multi_point);
        toolbar.inflateMenu(R.menu.toolbar_multi_point);
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
    private void getSignupList(String sign_id) {
        AsyncSignupListMulti asyncSignupList = new AsyncSignupListMulti(this, mHandler, MESSAGE_GET_SIGNUPLIST, sign_id);
        asyncSignupList.execute();
    }

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_SCAN_CODE:
                        String result = (String) msg.obj;
                        JSONObject jsonObj = JSON.parseObject(result);
                        String message = jsonObj.getString("Msg");
                        if(jsonObj.getString("Res").equals("0"))
                        {
                            JSONObject jsonObject2 = JSON.parseObject(jsonObj.getString("Data"));
                            String Name = jsonObject2.getString("Name");
                            String Phone = jsonObject2.getString("Phone");
                            String newPhone= Phone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
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
                            et_result.setText("姓名：" + Name + "\n" + "电话：" + newPhone + "\n" +
                                    "备注：" + AdminRemark + "\n" + FeeStr);
                            tv_sign_status.setText(message);
                            if (jsonObj.getString("Url").equals(100)) {
                                on = on + 1;
                                setNum();
                            }
                        }
                        else{
                            tv_sign_status.setText(message);
                            et_result.setText("");
                            on=on+1;
                            setNum();
                        }
                        break;
                    case MESSAGE_GET_SIGNUPLIST:
                        String result2 = (String) msg.obj;
                        insertSignupList(result2);
                        break;
                    case MESSAGE_UPLOAD_SIGNUPSTATUS:
                        String result4 = (String) msg.obj;
                        JSONObject jsonObj2 = JSON.parseObject(result4);
                        if (jsonObj2.getString("Res") == "0") {
                            changeStatus();
                            ToastUtil.makeText(getApplicationContext(), "数据上传成功");
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }

    public void scanCode(String result) {

        AsyncScanCodeMulti asyncScanCode = new AsyncScanCodeMulti(this, mHandler, MESSAGE_SCAN_CODE, result);
        asyncScanCode.execute();
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
            list.add(bean);
            System.out.println(bean.toString());
        }
        dao.save(list);
    }
    //    上传成功后 修改已上传数据更新状态
    private void changeStatus() {
        List<MySignListupBean> list = dao.queryUpdateStatus();
        for (int i = 0; i < list.size(); i++) {
            MySignListupBean bean2 = (MySignListupBean) list.get(i);
            MySignListupBean bean = new MySignListupBean();
            bean.setVCode(bean2.getVCode());
            bean.setStatus("true");
            bean.setUpdateStatus("false");
            bean.setCheckInID(bean2.getCheckInID());
            dao.update(bean);
        }

    }
    private void upload() {
        if (NetworkUtils.checkNetState(this)) {
            List<MySignListupBean> list = dao.queryUpdateStatus();
            String jsonString = JSON.toJSONString(list);
            if (list.size() == 0) {
                ToastUtil.makeText(getApplicationContext(), "已是最新数据");
                return;
            }
//        System.out.println(jsonString);
//ToastUtil.makeText(getActivity(),jsonString);
//        还要上传数据，现在获得了数据

            AsyncUpLoadSignupStatusMulti async = new AsyncUpLoadSignupStatusMulti(this, mHandler, MESSAGE_UPLOAD_SIGNUPSTATUS, jsonString);
            async.execute();
        } else {
            ToastUtil.makeText(getApplicationContext(), "暂无网络");

        }
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sign_support:
                IntentUtils.startActivity(this,AboutUsActivity.class);
                break;
            case R.id.btn_find_multi_sign:
                if (CameraUtils.cameraIsCanUse()) {
                     {
                        Intent intent = new Intent();
                        intent.setClass(this, MipcaActivityCapture.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("CheckInID", mSignID);
                        intent.putExtra("view_show","false" );
                        startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                    }

                } else {
                    ToastUtil.makeText(this, "未获得相机权限，请授权后再试！");

                }
            default:
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_upload_multi:
                upload();
        }
        return false;
    }
}
