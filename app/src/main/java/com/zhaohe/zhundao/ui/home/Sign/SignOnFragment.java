package com.zhaohe.zhundao.ui.home.sign;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.umeng.analytics.MobclickAgent;
import com.zhaohe.app.utils.IntentUtils;
import com.zhaohe.app.utils.NetworkUtils;
import com.zhaohe.app.utils.ProgressDialogUtils;
import com.zhaohe.app.utils.QueryCodeUtils;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.SignAdapter;
import com.zhaohe.zhundao.asynctask.AsyncScanCode;
import com.zhaohe.zhundao.asynctask.AsyncSign;
import com.zhaohe.zhundao.asynctask.AsyncSignupList;
import com.zhaohe.zhundao.asynctask.AsyncUpLoadSignupStatus;
import com.zhaohe.zhundao.asynctask.AsyncUpdateSignStatus;
import com.zhaohe.zhundao.bean.SignBean;
import com.zhaohe.zhundao.bean.dao.MySignListupBean;
import com.zhaohe.zhundao.dao.MySignupListDao;
import com.zhaohe.zhundao.ui.home.mine.UpgradedActivity;
import com.zhaohe.zhundao.zxing.controller.MipcaActivityCapture;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static com.zhaohe.zhundao.ui.home.action.ActionOnFragment.REFRESH_COMPLETE;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2016/12/12 13:47
 */
public class SignOnFragment extends Fragment implements View.OnClickListener, SignAdapter.SignClickListener, SwipeRefreshLayout.OnRefreshListener {

    protected View rootView;
    private SignAdapter adapter;
    private List<SignBean> list_act;
    private ListView lv_signon;
    private QueryCodeUtils queryCodeUtils;
    private Handler mHandler;
    private String mSignID;
    private SwipeRefreshLayout mSwipeLayout;
    private MySignupListDao dao;
    private TextView tv_signon_suggest;
    //列表点击位置
    private int postion;
    public static final int MESSAGE_SIGN_ALL = 94;
    public static final int MESSAGE_GET_SIGNUPLIST = 92;
    public static final int MESSAGE_SCAN_CODE = 90;
    public static final int MESSAGE_UPDATE_SIGN_STATUS = 84;
    public static final int MESSAGE_UPLOAD_SIGNUPSTATUS = 88;
    public static final int PAGE_SIZE = 100000;    //            单页显示的数据数目

    public static final int SCANNIN_GREQUEST_CODE = 89;
private boolean isGotoList;//true不跳转 false跳转签到名单

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_sigon,
                null);
        initView();
        initHandler();
        initData();
//        test();

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());
        upload();
        init();

    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 在使用这个view之前首先判断其是否存在parent view，这调用getParent()方法可以实现。
        // 如果存在parent view，那么就调用removeAllViewsInLayout()方法
        Log.i("test", "AFragment-->onCreateView");
        ViewGroup perentView = (ViewGroup) rootView.getParent();
        if (perentView != null) {
            perentView.removeAllViewsInLayout();
        }
        return rootView;

    }

    private void init() {
        if (SPUtils.contains(getActivity(),"sign_result")) {
            jsonconver((String) SPUtils.get(getActivity(), "sign_result", ""));
            getSignAllNoneDialog();
        }
        else if(NetworkUtils.checkNetState(getActivity()))
        {
        getSignAll();
//            getSignAllNoneDialog();
        }
        else{
            ToastUtil.makeText(getActivity(),R.string.net_error);
        }


}




    private void getSignAll() {
        Dialog dialog = ProgressDialogUtils.showProgressDialog(getActivity(), getString(R.string.progress_title), getString(R.string.progress_message));
        AsyncSign asyncSign = new AsyncSign(getActivity(), mHandler, dialog, MESSAGE_SIGN_ALL);
        asyncSign.execute();
    }
    private void getSignAllNoneDialog() {
        AsyncSign asyncSign = new AsyncSign(getActivity(), mHandler, MESSAGE_SIGN_ALL);
        asyncSign.execute();
    }

    private void test() {
        List<SignBean> list = new ArrayList<SignBean>();
        for (int i = 1; i <= 20; i++) {
            SignBean bean = new SignBean();
            bean.setSign_title("今天天气真不错之网球小王子争夺战" + i);
            bean.setSign_type("现场报名");
            bean.setSign_num("" + i);
            bean.setSignup_num("" + i);
            bean.setStoptime("200" + i);
            list.add(bean);
        }
        adapter.refreshData(list);
    }

    private void jsonconver(String result) {
        if ((result == null)||(result=="")) {
            ToastUtil.makeText(getActivity(), "请联网后再试");
        } else {
            JSONObject jsonObj = JSON.parseObject(result);
            JSONArray jsonArray = jsonObj.getJSONArray("Data");
            List<SignBean> list = new ArrayList<SignBean>();
            for (int i = 0; i < jsonArray.size(); i++) {
                SignBean bean = new SignBean();
                bean.setSign_title(jsonArray.getJSONObject(i).getString("ActivityName"));
                bean.setAct_title(jsonArray.getJSONObject(i).getString("Name"));
                bean.setStoptime(jsonArray.getJSONObject(i).getString("AddTime"));
                bean.setSign_num(jsonArray.getJSONObject(i).getString("NumShould"));
                bean.setSignup_num(jsonArray.getJSONObject(i).getString("NumFact"));
                bean.setAct_id(jsonArray.getJSONObject(i).getString("ActivityID"));
                bean.setSign_id(jsonArray.getJSONObject(i).getString("ID"));
                bean.setSign_status(jsonArray.getJSONObject(i).getString("Status"));
                bean.setSignObject(jsonArray.getJSONObject(i).getString("SignObject"));

                //签到类型  默认0 到场签到   1离场签退  2 集合签到"
                if (jsonArray.getJSONObject(i).getString("CheckInType") .equals("0")) {
                    bean.setSign_type("到场签到：");
                }
                if (jsonArray.getJSONObject(i).getString("CheckInType") .equals("1")) {
                    bean.setSign_type("离场签退：");
                }
                if (jsonArray.getJSONObject(i).getString("CheckInType") .equals("2")) {
                    bean.setSign_type("集合签到：");
                }
                bean.setAct_id(jsonArray.getJSONObject(i).getString("ActivityID"));

                if (jsonArray.getJSONObject(i).getString("Status") .equals("true") ) {
                    list.add(bean);

                } else {

                }
            }
            showSuggest(list);
            adapter.refreshData(list);
        }
    }
    private void showSuggest(List<SignBean> list) {
        if (list.size() == 0) {
            lv_signon.setVisibility(GONE);
            tv_signon_suggest.setVisibility(View.VISIBLE);
        } else {
            lv_signon.setVisibility(View.VISIBLE);
            tv_signon_suggest.setVisibility(GONE);
        }
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
            bean.setSignTime(jsonArray.getJSONObject(i).getString("SignTime"));
            bean.setUpdateStatus("false");
            list.add(bean);
            System.out.println(bean.toString());
            dao.save(bean);
        }

    }

    private void initData() {
//        queryCodeUtils = new QueryCodeUtils();
    }


    private void initView() {
        lv_signon = (ListView) rootView.findViewById(R.id.lv_signon);
        adapter = new SignAdapter(getActivity());
        adapter.setSignClickListener(this);
//        adapter.setSignOnItemClickListener(this);
//        lv_signon.setOnItemClickListener(this);
        lv_signon.setAdapter(adapter);
        mSwipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_ly3);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        dao = new MySignupListDao(getActivity());
        tv_signon_suggest = (TextView) rootView.findViewById(R.id.tv_signon_suggest);

    }

    //    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        queryCodeUtils.onActivityResult(requestCode, resultCode, data);
//    }
    private void gotoSignupList(String result) {


        Intent intent = new
                Intent(getActivity(), SignupListActivity.class);
        JSONObject jsonObj = JSON.parseObject(result);
        if (result == null) {
            return;
        }
        if (jsonObj.getString("Data") == null) {
            return;
        }
        if (jsonObj.getByte("Count") == 0) {
            ToastUtil.makeText(getActivity(), "暂无人签到");
            return;
        } else {
            JSONArray jsonArray = jsonObj.getJSONArray("Data");
            String sign_id = jsonArray.getJSONObject(0).getString("CheckInID");
            SPUtils.put(getActivity(), "signup_" + sign_id, result);
            System.out.println(SPUtils.get(getActivity(), "signup_" + sign_id, ""));
            //在Intent对象当中添加一个键值对
            //设置签到列表签到数字
            String result2 = (String) SPUtils.get(getActivity(), "sign_result", "");
            JSONObject jsonObj2 = JSON.parseObject(result2);
            JSONArray jsonArray2 = jsonObj2.getJSONArray("Data");
            intent.putExtra("NumFact", jsonArray2.getJSONObject(postion).getString("NumFact"));
            intent.putExtra("NumShould", jsonArray2.getJSONObject(postion).getString("NumShould"));
            intent.putExtra("sign_id", sign_id);
            intent.putExtra("result", result);
//            如果是相机第一次拿名单，则不跳转
if (isGotoList){return;}
            else
{startActivity(intent);}
        }
    }

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_SIGN_ALL:
                        String result = (String) msg.obj;
                        //签到列表结果
                        if (NetworkUtils.checkNetState(getActivity())) {
                            SPUtils.put(getActivity(), "sign_result", result);
                            jsonconver((String) SPUtils.get(getActivity(), "sign_result", ""));
                        }
                        break;
                    case REFRESH_COMPLETE:
                        mSwipeLayout.setRefreshing(false);
                        break;
//                    获取用户名单，将数据插入数据库
                    case MESSAGE_GET_SIGNUPLIST:
                        String result2 = (String) msg.obj;
                        JSONObject jsonObj2 = JSON.parseObject(result2);
                        if ("201".equals(jsonObj2.getString("Url"))){
                            UpgradedDialog(getActivity());
                            return;
                        }
                        insertSignupList(result2);
                        gotoSignupList(result2);
                        break;
                    case MESSAGE_SCAN_CODE:
                        String result3 = (String) msg.obj;
                        JSONObject jsonObj = JSON.parseObject(result3);
                        String message = jsonObj.getString("Msg");
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
                            resultDialog(message,"姓名：" + Name + "\n" + "电话：" + newPhone + "\n" +
                                    "备注：" + AdminRemark + "\n" + FeeStr);
                        } else {
                            resultDialog("扫码失败！",message);
//                            ToastUtil.makeText(getActivity(), message);
                        }
                        break;
                    case MESSAGE_UPLOAD_SIGNUPSTATUS:
                        String result4 = (String) msg.obj;
                        JSONObject jsonObj4 = JSON.parseObject(result4);
                        if (jsonObj4.getString("Res") == "0") {
                            changeStatus();
                            ToastUtil.makeText(getActivity(), "数据上传成功");
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


            default:
                break;
        }
    }

    public void scanCode(String result) {

        AsyncScanCode asyncScanCode = new AsyncScanCode(getActivity(), mHandler, MESSAGE_SCAN_CODE, result, mSignID);
        asyncScanCode.execute();
    }


    @Override
    public void onSignSwitch(SignBean bean) {
        updateSignStatus(bean.getSign_id());
        new Handler().postDelayed(new Runnable(){
            public void run() {
                init();            }
        }, 2000);

    }

    @Override
    public void onEditTitle(SignBean bean) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), SignEditActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", bean);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

    @Override
    public void onGetList(SignBean bean) {

        isGotoList=false;
        mSignID = bean.getSign_id();
        if (NetworkUtils.checkNetState(getActivity())) {
            List<MySignListupBean> list = dao.queryUpdateStatus();
if (list.size()!=0){
    upload();
    ToastUtil.makeText(getActivity(),"正在同步数据，等提示数据上传成功后再试~");
    return;
}
            //            单页显示的数据数目
            String mParam = "ID=" + bean.getSign_id() + "&pageSize=" + PAGE_SIZE;
            getSignupList(mParam);

        } else if (SPUtils.contains(getActivity(), "signup_" + bean.getSign_id()) == true) {
            Intent intent = new
                    Intent(getActivity(), SignupListActivity.class);
            //在Intent对象当中添加一个键值对
            String result = (String) SPUtils.get(getActivity(), "signup_" + bean.getSign_id(), "");
            intent.putExtra("result", result);
            intent.putExtra("sign_id", bean.getSign_id());
            startActivity(intent);

        } else {
            ToastUtil.makeText(getActivity(), "请联网后再试");
            return;
        }

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
                    if (NetworkUtils.checkNetState(getActivity())) {
//                    有网时与服务器上的VCode进行比对
                        scanCode(result);
                    } else {
                        Toast.makeText(getActivity(), CheckInID, Toast.LENGTH_LONG);
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
                            resultDialog("扫码成功","姓名：" + Name + "\n" + "电话：" + newPhone + "\n" +
                                    "备注：" + AdminRemark + "\n" + FeeStr);

                        }


                    }
                }
                if (requestCode == RESULT_CANCELED) {
                    ToastUtil.makeText(getActivity(), "未授权相机权限，请授权后重试");
                }

                break;
        }
    }

    @Override
    public void onRefresh() {
        init();
        System.out.print("成功刷新");
        mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);

    }

    private void updateSignStatus(String sign_id) {
        AsyncUpdateSignStatus async = new AsyncUpdateSignStatus(getActivity(), mHandler, MESSAGE_UPDATE_SIGN_STATUS, sign_id);
        async.execute();

    }


    private void getSignupList(String sign_id) {
        Dialog dialog = ProgressDialogUtils.showProgressDialog(getActivity(), getString(R.string.progress_title), getString(R.string.progress_message));
        AsyncSignupList asyncSignupList = new AsyncSignupList(getActivity(), mHandler, dialog, MESSAGE_GET_SIGNUPLIST, sign_id);
        asyncSignupList.execute();
    }

    //相机是否授权
    public static boolean cameraIsCanUse() {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }

        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }

    @Override
    public void onSignscanClick(SignBean bean) {
        isGotoList=true;
        mSignID = bean.getSign_id();
        if (cameraIsCanUse()) {
            if (SPUtils.contains(getActivity(), "signup_" + bean.getSign_id()) == true) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("CheckInID", bean.getSign_id());
                intent.putExtra("view_show","true" );


                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
            } else if (NetworkUtils.checkNetState(getActivity())) {
                String mParam = "ID=" + bean.getSign_id();
                ToastUtil.makeText(getActivity(), "暂未获取报名名单，自动跳转获取");
                getSignupList(mParam);
            } else {
                ToastUtil.makeText(getActivity(), "未获得名单，无网络，请在有网后重试");

            }
        } else {
            ToastUtil.makeText(getActivity(), "未获得相机权限，请授权后再试！");

        }

    }
    public void resultDialog(String status,String message){
        new AlertDialog.Builder(getActivity())
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(status)
                .setMessage(message)
                .setPositiveButton("继续扫码", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), MipcaActivityCapture.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("CheckInID", mSignID);
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
    private void upload() {
        if (NetworkUtils.checkNetState(getActivity())) {
            List<MySignListupBean> list = dao.queryUpdateStatus();
            String jsonString = JSON.toJSONString(list);
            if (list.size() == 0) {
                return;
            }

            AsyncUpLoadSignupStatus async = new AsyncUpLoadSignupStatus(getActivity(), mHandler, MESSAGE_UPLOAD_SIGNUPSTATUS, jsonString);
            async.execute();
        }
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
    public void UpgradedDialog(final Activity activity) {

        //LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化

        new AlertDialog.Builder(getActivity())
                //对话框的标题
                .setTitle("对不起,您的权限不够！")
                //设定显示的View
                //对话框中的“登陆”按钮的点击事件
                .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        IntentUtils.startActivity(activity, UpgradedActivity.class);
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
}
