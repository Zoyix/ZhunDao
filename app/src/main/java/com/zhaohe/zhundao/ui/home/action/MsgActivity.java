package com.zhaohe.zhundao.ui.home.action;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhaohe.app.utils.IntentUtils;
import com.zhaohe.app.utils.JSONUtils;
import com.zhaohe.app.utils.ProgressDialogUtils;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.TimeUtil;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.InfAdapter;
import com.zhaohe.zhundao.adapter.SignListAdapter;
import com.zhaohe.zhundao.asynctask.msg.AsyncGetMsgId;
import com.zhaohe.zhundao.asynctask.msg.AsyncGetMsgInf;
import com.zhaohe.zhundao.asynctask.msg.AsyncPostSendMsg;
import com.zhaohe.zhundao.bean.InfBean;
import com.zhaohe.zhundao.bean.SignListBean;
import com.zhaohe.zhundao.bean.ToolUserBean;
import com.zhaohe.zhundao.bean.jsonbean.MsgBean;
import com.zhaohe.zhundao.constant.Constant;
import com.zhaohe.zhundao.ui.ToolBarActivity;
import com.zhaohe.zhundao.ui.home.mine.UpgradedActivity;
import com.zhaohe.zhundao.view.ColorFontTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MsgActivity extends ToolBarActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    @BindView(R.id.tv_count)
    TextView tvCount;
    int Count;
    @BindView(R.id.tv_select)
    TextView tvSelect;
    @BindView(R.id.et_msg)
    EditText etMsg;
    @BindView(R.id.tv_msg_count)
    ColorFontTextView tvMsgCount;
    @BindView(R.id.tv_msg_sign)
    ColorFontTextView tvMsgSign;
    @BindView(R.id.tv_mode)
    TextView tvMode;
    @BindView(R.id.tv_msg_suggest)
    TextView tvMsgSuggest;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_receive)
    TextView tvReceive;
    @BindView(R.id.tv_receive_count)
    TextView tvReceiveCount;
    @BindView(R.id.tv_msg_count_user)
    ColorFontTextView tvMsgCountUser;
    @BindView(R.id.tv_msg_count_sum)
    ColorFontTextView tvMsgCountSum;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_single)
    TextView tvSingle;
    @BindView(R.id.tv_single_count)
    TextView tvSingleCount;
    private Handler mHandler;
    public static final int MESSAGE_OPEN_MSG = 88;
    public static final int MESSAGE_MSG_INF = 89;
    public static final int MESSAGE_MSG_SEND = 90;

    private InfAdapter InfAdapter;

    private int MsgCount = 0, MsgSum = 1, MsgUser = 0, Sum = 1;
    private MsgBean bean;
    private String MsgID, MsgSign = "【准到】";
    private List<SignListBean> list = new ArrayList<SignListBean>();
    private String signup_list;
    private JSONObject jsonObj;
    private JSONArray jsonArray;
    private SignListAdapter adapter;
    private String act_id;
    private Map<String, String> map = new HashMap<>();
    private ListView listView;
    private InfBean beanInf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBarNew("短信群发", R.layout.activity_msg);
        ButterKnife.bind(this);
        initHandler();
        init();
        initView();

    }

    private void init() {
        int vip = (int) SPUtils.get(this, "vip", 0);

        if (vip < 2) {
            UpgradedDialog(this);
        } else {
            getMsgID();
        }


    }

    private void getMsgID() {
        AsyncGetMsgId asyncActivity = new AsyncGetMsgId(this, mHandler, MESSAGE_OPEN_MSG);
        asyncActivity.execute();
    }

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_OPEN_MSG:
                        String result = (String) msg.obj;
                        ToolUserBean toolUserBean = JSONUtils.parseObject(result, ToolUserBean.class);
                        if (toolUserBean.isSucess()) {
                            MsgID = toolUserBean.getUrl();
                            getMsgInf();
                        }


                        break;
                    case MESSAGE_MSG_INF:
                        result = (String) msg.obj;
                        bean = JSONUtils.parseObject(result, MsgBean.class);
                        MsgSign = bean.getData().get(0).getJH_Remark();
                        tvMsgSign.setTextStyle("群发签名：" + MsgSign, MsgSign, "18");
                        MsgUser = bean.getData().get(0).getEs_pay();
                        tvMsgCountUser.setTextStyle("短信包当前剩余额：" + MsgUser + "条", MsgUser + "", "18");
                        tvMsgCountUser.setTextStyle("#222222");

                        if (MsgUser == 0) {
                            tvStatus.setText("短信包余额不足");

                        }

                        break;

                    case MESSAGE_MSG_SEND:
                        result = (String) msg.obj;
                        toolUserBean = JSONUtils.parseObject(result, ToolUserBean.class);
                        if (toolUserBean.isSucess()) {
                            finish();
                        }
                        ToastUtil.makeText(getApplicationContext(), toolUserBean.getMsg());
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void getMsgInf() {
        AsyncGetMsgInf asyncActivity = new AsyncGetMsgInf(getApplicationContext(), mHandler, MESSAGE_MSG_INF, MsgID);
        asyncActivity.execute();
    }

    private void sendMsg(String phones) {
        Dialog dialog = ProgressDialogUtils.showProgressDialog(this, getString(R.string.progress_title), getString(R.string.progress_message));
        ToastUtil.print(MsgID);
        AsyncPostSendMsg asyncActivity = new AsyncPostSendMsg(getApplicationContext(), mHandler, dialog, MESSAGE_MSG_SEND, MsgID, etMsg.getText().toString(), phones);
        asyncActivity.execute();
    }

    private void initView() {
        MsgCount = MsgSign.length();
        tvMsgCount.setTextStyle(MsgCount + "个字/分为" + MsgSum + "条", MsgCount + "|" + MsgSum, "25|25");

        etMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //TODO:
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MsgCount = etMsg.getText().length() + MsgSign.length();

                if (etMsg.getText().length() <= 70) {

                    MsgSum = 1;
                } else {


                    MsgSum = MsgCount / 67 + 1;
                }


                ToastUtil.print(MsgCount + "");


                tvMsgCount.setTextStyle(MsgCount + "个字/分为" + MsgSum + "条", MsgCount + "|" + MsgSum, "25|25");
                Sum = Count * MsgSum;
                tvMsgCountSum.setTextStyle("合记:" + Sum + "条", Sum + "", "18");
//                tvMsgCountSum .setTextStyle("#222222");

                tvSingleCount.setText("x" + MsgSum + "条");
                if (MsgUser < Count * MsgSum) {
                    tvStatus.setText("短信包余额不足");
                } else {
                    tvStatus.setText("");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //TODO:
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initIntent();
    }

    private void initIntent() {
        Intent intent = getIntent();
        act_id = intent.getStringExtra("act_id");
        if (act_id == null || act_id == "") {
            act_id = (String) SPUtils.get(this, "act_id_now", "");

        }
        ToastUtil.print("活动ID" + act_id);


        Count = intent.getIntExtra("count", 0);
        Sum = Count * MsgSum;

        tvCount.setText("已选择" + Count + "人");
        tvReceiveCount.setText(Count + "人");
        tvMsgCountSum.setTextStyle("合记:" + Sum + "条", Sum + "", "18");
        String a = Sum + "";
        tvMsgCountSum.setPositon(3, 3 + a.length());
        tvMsgCountSum.setTextStyle("#222222");


    }

    private String getPhone() {
        signup_list = (String) SPUtils.get(this, "listup_" + act_id, "");
        ToastUtil.print("活动内容" + signup_list);

        jsonObj = JSON.parseObject(signup_list);
        jsonArray = jsonObj.getJSONArray("Data");

        SparseBooleanArray booleanArray = Constant.booleanArray_msg;
        ArrayList list = new ArrayList();
        for (int i = 0; i < booleanArray.size(); i++) {
            if (booleanArray.get(i)) {
                if (jsonArray.getJSONObject(i).getString("Mobile").length() == 11) {
                    list.add(jsonArray.getJSONObject(i).getString("Mobile"));
                }
            }
        }
        if (list.size() == 0) {
            ToastUtil.makeText(this, "没有有效的手机号码，请核对后再试");

            return "phones=" + "";

        }
        ToastUtil.print(JSONUtils.addComma(list));
        LinkedHashMap<String, String> jsonMap = new LinkedHashMap<>();
        jsonMap.put("phones", JSONUtils.addComma(list));
        ToastUtil.print(JSONUtils.mapToString(jsonMap));

        return JSONUtils.mapToString(jsonMap);
//return "phones="+JSONUtils.addComma(list);

    }

    public void UpgradedDialog(final Activity activity) {

        //LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化

        new AlertDialog.Builder(activity)
                //对话框的标题
                .setTitle("对不起,您的权限不够！需要V2及以上会员才能使用")
                //设定显示的View
                //对话框中的“登陆”按钮的点击事件
                .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        IntentUtils.startActivity(activity, UpgradedActivity.class);
                        finish();
                    }

                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }
                })
                // 设置dialog是否为模态，false表示模态，true表示非模态
                .setCancelable(true)
                //对话框的创建、显示
                .create().show();

    }

    private void saveMap(Map<String, String> map) {
        String result = JSONObject.toJSONString(map);
        ToastUtil.print("map" + result);
        SPUtils.put(this, "map_mode", result);
    }

    private Map<String, String> getMap() {
//        String result = (String) SPUtils.get(this, "map_inf", "{\"x\":\"y\"}");
        String result = (String) SPUtils.get(this, "map_mode", "{}");

        map = JSONObject.parseObject(result, Map.class);

        return map;
    }

    public void setMode() {

        //LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化
        LayoutInflater factory = LayoutInflater.from(this);
        //把activity_login中的控件定义在View中
        final View textEntryView = factory.inflate(R.layout.activity_inf, null);
        listView = (ListView) textEntryView.findViewById(R.id.lv_inf);
        InfAdapter = new InfAdapter(this);
        listView.setAdapter(InfAdapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        map = getMap();
        initList();
        //将LoginActivity中的控件显示在对话框中
        new AlertDialog.Builder(this)
                //对话框的标题
                .setTitle("选择文案")
                //设定显示的View
                .setView(textEntryView)
                //对话框中的“登陆”按钮的点击事件
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {


                    }
                })
                //对话框的“退出”单击事件
                .setNegativeButton("新增", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        addMode();
                    }
                })
                // 设置dialog是否为模态，false表示模态，true表示非模态
                .setCancelable(false)
                //对话框的创建、显示
                .create().show();
    }

    public void addMode() {

        //LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化
        LayoutInflater factory = LayoutInflater.from(this);
        //把activity_login中的控件定义在View中
        final View textEntryView = factory.inflate(R.layout.dialog_edit, null);
        final EditText editText = (EditText) textEntryView.findViewById(R.id.et);
        //将LoginActivity中的控件显示在对话框中
        new AlertDialog.Builder(this)
                //对话框的标题
                .setTitle("新增文案")
                //设定显示的View
                .setView(textEntryView)
                //对话框中的“登陆”按钮的点击事件
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        map = getMap();
                        map.put(TimeUtil.getNowTime(), editText.getText().toString());
                        saveMap(map);
                        setMode();


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

    public void deleteMode() {

        //LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化

        //将LoginActivity中的控件显示在对话框中
        new AlertDialog.Builder(this)
                //对话框的标题
                .setTitle("删除文案")
                .setMessage(beanInf.getTitle())
                //设定显示的View
                //对话框中的“登陆”按钮的点击事件
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        map = getMap();
                        map.remove(beanInf.getAddTime());
                        saveMap(map);
                        initList();


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

    private void initList() {
        List<InfBean> list = new ArrayList<InfBean>();

        for (String key : map.keySet()) {
            InfBean bean = new InfBean();
            bean.setTitle(map.get(key));
            bean.setAddTime(key);
            bean.setRead(true);
            list.add(bean);
        }
        InfAdapter.refreshData(list);


    }

    @OnClick(R.id.tv_select)
    public void onViewClicked() {
        finish();
    }

    @OnClick({R.id.tv_mode, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_mode:

                setMode();
                break;
            case R.id.btn_login:
//                if (MsgUser< Count * MsgSum){
//                    ToastUtil.makeText(this,"短信包余额不足");
//                    return;
//                }
                if (etMsg.getText().length() <= 0) {
                    ToastUtil.makeText(this, "请输入短信内容");
                    return;
                }
                sendMsg(getPhone());
                ;

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        beanInf = InfAdapter.getItem(position);
        etMsg.setText(beanInf.getTitle());
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        beanInf = InfAdapter.getItem(position);
        deleteMode();
        return true;
    }
}
