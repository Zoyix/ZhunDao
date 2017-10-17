package com.zhaohe.zhundao.ui.home.sign;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.umeng.analytics.MobclickAgent;
import com.zhaohe.app.utils.IntentUtils;
import com.zhaohe.app.utils.NetworkUtils;
import com.zhaohe.app.utils.ProgressDialogUtils;
import com.zhaohe.app.utils.QueryCodeUtils;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.app.utils.ZXingUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.SignAdapter;
import com.zhaohe.zhundao.asynctask.AsyncScanCode;
import com.zhaohe.zhundao.asynctask.AsyncSign;
import com.zhaohe.zhundao.asynctask.AsyncSignDelete;
import com.zhaohe.zhundao.asynctask.AsyncSignupList;
import com.zhaohe.zhundao.asynctask.AsyncSignuplistEmail;
import com.zhaohe.zhundao.asynctask.AsyncUpLoadSignupStatus;
import com.zhaohe.zhundao.asynctask.AsyncUpdateSignStatus;
import com.zhaohe.zhundao.bean.SignBean;
import com.zhaohe.zhundao.bean.ToolUserBean;
import com.zhaohe.zhundao.bean.dao.MySignListupBean;
import com.zhaohe.zhundao.bean.updateBean;
import com.zhaohe.zhundao.dao.MySignupListDao;
import com.zhaohe.zhundao.ui.home.mine.UpgradedActivity;
import com.zhaohe.zhundao.zxing.controller.MipcaActivityCapture;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static com.zhaohe.app.utils.ZXingUtil.createQrBitmap;
import static com.zhaohe.zhundao.ui.home.action.ActionOnFragment.REFRESH_COMPLETE;
import static com.zhaohe.zhundao.ui.home.sign.SignOnFragment.MESSAGE_SEND_SIGNUPLIST_EMAIL;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2016/12/12 14:29
 */
public class SignOffFragment extends Fragment implements View.OnClickListener, SignAdapter.SignClickListener, SwipeRefreshLayout.OnRefreshListener{
    //            单页显示的数据数目
    public static final int PAGE_SIZE = 100000;
    public static final int MESSAGE_SIGN_ALL = 94;
    public static final int MESSAGE_GET_SIGNUPLIST = 92;
    public static final int MESSAGE_SCAN_CODE = 90;
    public static final int SCANNIN_GREQUEST_CODE = 89;
    public static final int MESSAGE_SIGN_DELETE = 97;
    public static final int MESSAGE_UPLOAD_SIGNUPSTATUS = 88;


    private SignAdapter adapter;
    private List<SignBean> list_act;
    private ListView lv_signoff;
    private QueryCodeUtils queryCodeUtils;
    private Handler mHandler;
    //    点击列表获取签到ID
    private String mSignID;
    //    点击列表获取签到位置
    private int postion;
    private SwipeRefreshLayout mSwipeLayout;
    private MySignupListDao dao;
    protected View rootView;
    private TextView tv_signoff_suggest;
    String title;
    String act_id;
    PtrClassicFrameLayout ptrClassicFrameLayout;
    boolean load = true;
    int page = 0;
    List<SignBean> list = new ArrayList<SignBean>();

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_sigoff,
                null);
        initView();
        initHandler();
        initData();
        init();
//        test();


    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());
        upload();
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
        if (SPUtils.contains(getActivity(), "sign_result_off")) {
            jsonconver((String) SPUtils.get(getActivity(), "sign_result_off", ""));
            getSignAllNoneDialog();
        }
        else if(NetworkUtils.checkNetState(getActivity()))
        {
//            getSignAll();
            upload();
            getSignAllNoneDialog();

        }
        else{
            ToastUtil.makeText(getActivity(),R.string.net_error);
        }


    }

    private void getSignAll() {
        Dialog dialog = ProgressDialogUtils.showProgressDialog(getActivity(), getString(R.string.progress_title), getString(R.string.progress_message));
        AsyncSign asyncSign = new AsyncSign(getActivity(), mHandler, dialog, MESSAGE_SIGN_ALL, 2);
        asyncSign.execute();
    }
    private void getSignAllNoneDialog() {
        AsyncSign asyncSign = new AsyncSign(getActivity(), mHandler, MESSAGE_SIGN_ALL, 2);
        asyncSign.execute();
    }
    private void jsonconver(String result) {

        if (!load) {
            return;
        }
        int i2;
        int size = 10;
        if (page == 0) {
            list.clear();
            ptrClassicFrameLayout.setLoadMoreEnable(true);
            ptrClassicFrameLayout.setPullToRefresh(true);
            ToastUtil.print("重置后能更新吗" + ptrClassicFrameLayout.isLoadMoreEnable());

//    initFrameLayout();
//ptrClassicFrameLayout.
        }
        i2 = page * size;
        ToastUtil.print("当前页数" + i2);
        JSONObject jsonObj = JSON.parseObject(result);
        JSONArray jsonArray = jsonObj.getJSONArray("Data");
        int m = 0;
        if ((page + 1) * size < jsonArray.size()) {
            m = (page + 1) * size;

        } else {
            page--;
            m = jsonArray.size();
//            ToastUtil.makeText(getActivity(),"已无更多数据");
            ptrClassicFrameLayout.setLoadMoreEnable(false);
            load = false;

        }
        ToastUtil.print("总数量" + jsonArray.size());
        ToastUtil.print("\n当前页码" + m);



        if ((result == null)||(result=="")) {
            ToastUtil.makeText(getActivity(), "请联网后再试");
        }
        else{

            for (int i = i2; i < m; i++) {
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
                int NumShould = Integer.parseInt(bean.getSign_num());
                int NubFact = Integer.parseInt(bean.getSignup_num());
                if (NumShould == dao.queryListSize(bean.getSign_id())) {
                    bean.setList_status("true");
                }
            if (jsonArray.getJSONObject(i).getString("Status") .equals("false")) {
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
            lv_signoff.setVisibility(GONE);
            tv_signoff_suggest.setVisibility(View.VISIBLE);
        } else {
            lv_signoff.setVisibility(View.VISIBLE);
            tv_signoff_suggest.setVisibility(GONE);
        }

    }

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

    private void initData() {
//        queryCodeUtils = new QueryCodeUtils();
    }


    private void initView() {
        initFrameLayout();

        lv_signoff = (ListView) rootView.findViewById(R.id.lv_signoff);
        adapter = new SignAdapter(getActivity());
        adapter.setSignClickListener(this);
//        adapter.setSignOnItemClickListener(this);
        lv_signoff.setAdapter(adapter);
        mSwipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_ly4);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        dao = new MySignupListDao(getActivity());
        tv_signoff_suggest = (TextView) rootView.findViewById(R.id.tv_signoff_suggest);


    }

    private void initFrameLayout() {
        ptrClassicFrameLayout = (PtrClassicFrameLayout) rootView.findViewById(R.id.test_list_view_frame);

        ptrClassicFrameLayout.setLoadMoreEnable(true);

        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        load = true;
                        page = 0;
//                        jsonconver((String) SPUtils.get(getActivity(), "act_result_on", ""));
                        init();
                        ptrClassicFrameLayout.refreshComplete();


//                        adapter.refreshData(list);

                        if (!ptrClassicFrameLayout.isLoadMoreEnable()) {
                            ptrClassicFrameLayout.setLoadMoreEnable(true);
                        }
                    }
                }, 1000);
            }
        });


        ptrClassicFrameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        ++page;
                        jsonconver((String) SPUtils.get(getActivity(), "sign_result_off", ""));
                        ptrClassicFrameLayout.loadMoreComplete(true);
//                        Toast.makeText(getActivity(), "load more complete", Toast.LENGTH_SHORT)
//                                .show();

                        if (page == 1) {
                            //set load more disable
//                            ptrClassicFrameLayout.setLoadMoreEnable(false);
                        }
                    }
                }, 1000);
            }
        });
    }
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
            String result2 = (String) SPUtils.get(getActivity(), "sign_result_off", "");
            JSONObject jsonObj2 = JSON.parseObject(result2);
            JSONArray jsonArray2 = jsonObj2.getJSONArray("Data");
            intent.putExtra("sign_id", sign_id);
            intent.putExtra("result", result);
            intent.putExtra("title",title);
            intent.putExtra("act_id", act_id);

//            intent.putExtra("result", result);
            startActivity(intent);
        }
    }

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_SIGN_ALL:
                        String result = (String) msg.obj;

                        System.out.println("Sign result:  " + result);
                        //活动列表结果
                        if (NetworkUtils.checkNetState(getActivity())) {
                            page = 0;
                            load = true;
                            SPUtils.put(getActivity(), "sign_result_off", result);
                            jsonconver((String) SPUtils.get(getActivity(), "sign_result_off", ""));
                        }
                        break;
                    case REFRESH_COMPLETE:
                        mSwipeLayout.setRefreshing(false);
                        break;
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
                    case   MESSAGE_SIGN_DELETE :
                        result = (String) msg.obj;
                        jsonObj = JSON.parseObject(result);
                        message = jsonObj.getString("Msg");
                        System.out.println("sign_add_result:"+result);
                        if (jsonObj.getString("Res").equals("0"))
                        //添加或修改请求结果
                        {
                            init();
                            ToastUtil.makeText(getActivity(), "删除成功！");

                        }
                        else{
                            ToastUtil.makeText(getActivity(),message);
                        }
                        break;
                    case MESSAGE_SEND_SIGNUPLIST_EMAIL:
                        result2 = (String) msg.obj;
                        ToolUserBean bean = (ToolUserBean) JSON.parseObject(result2, ToolUserBean.class);
                        Toast.makeText(getActivity(), bean.getMsg(), Toast.LENGTH_LONG).show();
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
            bean.setCheckInTime(bean2.getCheckInTime());
            dao.update(bean);
        }

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
        signItem(bean);
    }
    public void signItem(final SignBean bean) {
        mSignID=bean.getSign_id();

        final AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater factory = LayoutInflater.from(getActivity());
        //把activity_login中的控件定义在View中
        final View textEntryView = factory.inflate(R.layout.dialog_sign_item, null);
        builder                //对话框的标题
                .setTitle("签到操作")
                //设定显示的View
                .setView(textEntryView)
                //对话框中的“登陆”按钮的点击事件
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        //获取用户输入的“用户名”，“密码”






                    }
                })
                //对话框的“退出”单击事件
//                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                    }
//                })
                // 设置dialog是否为模态，false表示模态，true表示非模态
                .setCancelable(true)
                //对话框的创建、显示
                .create();
        final AlertDialog  dialog  = builder.show();

        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId())
                {
                    case R.id.tv_sign_edit:
                        dialog.dismiss();
                        signEdit(bean);
                        break;
                    case R.id.tv_sign_delete:
                        dialog.dismiss();

                        deleteDialog(bean);
                        break;
                    case R.id.tv_sign_qcode:
                        dialog.dismiss();
                        QrCodeDialog(bean);
                        break;
                    case R.id.tv_sign_email:
                        dialog.dismiss();
                        sendEmail();
                        break;

                }

            }
        };
        //LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化

        //注意：textEntryView.findViewById很重要，因为上面factory.inflate(R.layout.activity_login, null)将页面布局赋值给了textEntryView了
        final TextView tv_sign_edit = (TextView) textEntryView.findViewById(R.id.tv_sign_edit);
        tv_sign_edit.setOnClickListener(onClickListener);
        final TextView tv_sign_delete = (TextView) textEntryView.findViewById(R.id.tv_sign_delete);
        tv_sign_delete.setOnClickListener(onClickListener);

        final TextView tv_sign_qcode = (TextView) textEntryView.findViewById(R.id.tv_sign_qcode);
        tv_sign_qcode.setOnClickListener(onClickListener);
        final TextView tv_sign_email = (TextView) textEntryView.findViewById(R.id.tv_sign_email);
        tv_sign_email.setOnClickListener(onClickListener);
        //将LoginActivity中的控件显示在对话框中
        builder                //对话框的标题
                .setTitle("签到操作")
                //设定显示的View
                .setView(textEntryView)
                //对话框中的“登陆”按钮的点击事件
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        //获取用户输入的“用户名”，“密码”






                    }
                })
                //对话框的“退出”单击事件
//                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                    }
//                })
                // 设置dialog是否为模态，false表示模态，true表示非模态
                .setCancelable(true)
                //对话框的创建、显示
                .create();

    }
    public void deleteDialog(final SignBean bean) {

        //LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化

        new AlertDialog.Builder(getActivity())
                //对话框的标题
                .setTitle("确认要删除签到？")
                //设定显示的View
                //对话框中的“登陆”按钮的点击事件
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteSign(bean.getSign_id());

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
    public void QrCodeDialog(final SignBean bean) {

        //LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化
        LayoutInflater factory = LayoutInflater.from(getContext());
        //把activity_login中的控件定义在View中
        final View v = factory.inflate(R.layout.dialog_qrcode_sign, null);
        ImageView iv_dialog_qrcode;
        iv_dialog_qrcode = (ImageView) v.findViewById(R.id.iv_dialog_qrcode_sign);
        TextView title= (TextView) v.findViewById(R.id.tv_qr_title);
        title.setText(bean.getAct_title());

        final Bitmap bitmap = createQrBitmap("https://m.zhundao.net/ck/" + bean.getSign_id()+"/"+bean.getAct_id()+"/3", 600, 600);
        iv_dialog_qrcode.setImageBitmap(bitmap);

        ;
        new AlertDialog.Builder(getActivity())
                //对话框的标题
//                .setTitle(bean.getAct_title())
                .setView(v)
                //设定显示的View
                //对话框中的“登陆”按钮的点击事件
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
//ZXingUtil.saveMyBitmap(ZXingUtil.createQrBitmap(bean.getUrl(),150,150),bean.getAct_title()+"二维码");
                        ZXingUtil.saveImageToGallery(getContext(), bitmap, bean.getAct_title());
                        ToastUtil.makeText(getContext(), "保存成功！");
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
    private void deleteSign(String checkInId){
        AsyncSignDelete async =new AsyncSignDelete(getActivity(),mHandler,MESSAGE_SIGN_DELETE,checkInId);
        async.execute();
    }
    private void signEdit(SignBean bean) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), SignEditActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", bean);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }
    private void upload() {
        if (NetworkUtils.checkNetState(getActivity())) {
            List<updateBean> list = dao.queryUpdateStatusNew();
            String jsonString = JSON.toJSONString(list);
            if (list.size() == 0) {
                ToastUtil.print("已是最新数据");
                return;
            }

            AsyncUpLoadSignupStatus async = new AsyncUpLoadSignupStatus(getActivity(), mHandler, MESSAGE_UPLOAD_SIGNUPSTATUS, jsonString);
            async.execute();
        }
    }
    @Override
    public void onGetList(SignBean bean){
        act_id= bean.getAct_id();

        title=bean.getAct_title();
        mSignID = bean.getSign_id();
        if (SPUtils.contains(getActivity(), "signup_" + bean.getSign_id()) == true) {
            Intent intent = new
                    Intent(getActivity(), SignupListActivity.class);
            //在Intent对象当中添加一个键值对
            String result = (String) SPUtils.get(getActivity(), "signup_" + bean.getSign_id(), "");
            intent.putExtra("result", result);
            intent.putExtra("sign_id", bean.getSign_id());
            intent.putExtra("title",title);
            intent.putExtra("act_id", act_id);

            startActivity(intent);

        } else if (NetworkUtils.checkNetState(getActivity())) {
            //            单页显示的数据数目
            List<MySignListupBean> list = dao.queryUpdateStatus();
            if (list.size() != 0) {
                upload();
                ToastUtil.makeText(getActivity(), "正在同步数据，等提示数据上传成功后再试~");
                return;
            }

            String mParam = "ID=" + bean.getSign_id() + "&pageSize=" + PAGE_SIZE;
            getSignupList(mParam);

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
//                            ToastUtil.makeText(getActivity(), "扫码失败，该凭证码有误");
                            resultDialog("扫码失败","该凭证码无效！");
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
                            resultDialog("该用户已经签到！","姓名：" + Name + "\n" + "电话：" + newPhone + "\n" +
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
                            resultDialog("扫码成功！","姓名：" + Name + "\n" + "电话：" + newPhone + "\n" +
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
        page = 0;
        if (NetworkUtils.checkNetState(getActivity())) {
            init();
            upload();
            System.out.print("成功刷新");
            mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
        } else {
            ToastUtil.makeText(getActivity(), "请联网后再试");
            mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
        }

    }

    private void updateSignStatus(String sign_id) {
        AsyncUpdateSignStatus async = new AsyncUpdateSignStatus(getActivity(), mHandler, MESSAGE_GET_SIGNUPLIST, sign_id);
        async.execute();

    }


    private void getSignupList(String sign_id) {
        Dialog dialog = ProgressDialogUtils.showProgressDialog(getActivity(), getString(R.string.progress_title), getString(R.string.progress_message));
        AsyncSignupList asyncSignupList = new AsyncSignupList(getActivity(), mHandler, dialog, MESSAGE_GET_SIGNUPLIST, sign_id);
        asyncSignupList.execute();
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
    private void sendSignListByEmail(String email,String mSignID ){
        Dialog dialog = ProgressDialogUtils.showProgressDialog(getActivity(), getString(R.string.progress_title), getString(R.string.progress_message));
        AsyncSignuplistEmail async = new AsyncSignuplistEmail(getActivity(), mHandler,dialog, MESSAGE_SEND_SIGNUPLIST_EMAIL, email,mSignID);
        async.execute();

    }
    public void sendEmail() {

        //LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化
        LayoutInflater factory = LayoutInflater.from(getActivity());
        //把activity_login中的控件定义在View中
        final View textEntryView = factory.inflate(R.layout.dialog_email, null);

        //将LoginActivity中的控件显示在对话框中
        new AlertDialog.Builder(getActivity())
                //对话框的标题
                .setTitle("发送签到名单到邮箱")
                //设定显示的View
                .setView(textEntryView)
                //对话框中的“登陆”按钮的点击事件
                .setPositiveButton("发送", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        //获取用户输入的“用户名”，“密码”
                        //注意：textEntryView.findViewById很重要，因为上面factory.inflate(R.layout.activity_login, null)将页面布局赋值给了textEntryView了
                        final EditText etPassword = (EditText) textEntryView.findViewById(R.id.et_dialog_password);

                        //将页面输入框中获得的“用户名”，“密码”转为字符串
                        String email = etPassword.getText().toString();
                        if (email==null||email.equals("")){
                            ToastUtil.makeText(getActivity(),"邮箱不得为空！");
                            return;
                        }
                        else{
                            sendSignListByEmail(email,mSignID);
                        }
                        //现在为止已经获得了字符型的用户名和密码了，接下来就是根据自己的需求来编写代码了
                        //这里做一个简单的测试，假定输入的用户名和密码都是1则进入其他操作页面（OperationActivity）

                    }
                })
                //对话框的“退出”单击事件
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                // 设置dialog是否为模态，false表示模态，true表示非模态
                .setCancelable(false)
                //对话框的创建、显示
                .create().show();
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
