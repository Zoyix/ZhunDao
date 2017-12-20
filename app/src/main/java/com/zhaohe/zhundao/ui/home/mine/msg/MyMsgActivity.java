package com.zhaohe.zhundao.ui.home.mine.msg;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhaohe.app.utils.IntentUtils;
import com.zhaohe.app.utils.JSONUtils;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.asynctask.msg.AsyncGetMsgId;
import com.zhaohe.zhundao.asynctask.msg.AsyncGetMsgInf;
import com.zhaohe.zhundao.bean.ToolUserBean;
import com.zhaohe.zhundao.bean.jsonbean.MsgBean;
import com.zhaohe.zhundao.ui.ToolBarActivity;
import com.zhaohe.zhundao.ui.home.action.msg.MsgBuyActivity;
import com.zhaohe.zhundao.ui.home.mine.UpgradedActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhaohe.zhundao.ui.home.action.msg.MsgSendActivity.MESSAGE_MSG_INF;
import static com.zhaohe.zhundao.ui.home.action.msg.MsgSendActivity.MESSAGE_OPEN_MSG;

public class MyMsgActivity extends ToolBarActivity implements Toolbar.OnMenuItemClickListener {
    @BindView(R.id.tv_msg_num)
    TextView tvMsgNum;
    @BindView(R.id.btn_charge)
    Button btnCharge;
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    private MsgBean bean;
    private int MsgUser = 0;
    private String MsgID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBarNew("我的短信", R.layout.activity_my_msg);
        ButterKnife.bind(this);
        init();
        initHandler();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu_signlist_msg_more, menu);
        toolbar.setOnMenuItemClickListener(this);

        return true;
    }

    private void getMsgInf() {
        AsyncGetMsgInf asyncActivity = new AsyncGetMsgInf(getApplicationContext(), mHandler, MESSAGE_MSG_INF, MsgID);
        asyncActivity.execute();
    }

    private void getMsgID() {
        AsyncGetMsgId asyncActivity = new AsyncGetMsgId(this, mHandler, MESSAGE_OPEN_MSG);
        asyncActivity.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMsgID();
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
                        MsgUser = bean.getData().get(0).getEs_pay();
                        tvMsgNum.setText(MsgUser + "");

                        break;


                    default:
                        break;
                }
            }
        };
    }

    private void init() {
        int vip = (int) SPUtils.get(this, "vip", 0);

        if (vip < 2) {
            UpgradedDialog(this);
        }

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

    private void questionDialog() {
        new android.support.v7.app.AlertDialog.Builder(this)
                //对话框的标题
                .setTitle("常见问题")
                //设定显示的View
                //对话框中的“登陆”按钮的点击事件
                .setMessage("关注准到微信公众号（微信号izhundao），发送关键词“短信”，了解准到短信平台相关功能和收费标准！")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }

                })

                // 设置dialog是否为模态，false表示模态，true表示非模态
                .setCancelable(true)
                //对话框的创建、显示
                .create().show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_signlist_msg_more:
                IntentUtils.startActivity(this, MsgMoreActivity.class);
                break;

        }
        return false;
    }

    @OnClick({R.id.btn_charge, R.id.btn_back, R.id.tv_question})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_charge:
                IntentUtils.startActivity(this, MsgBuyActivity.class);
                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_question:
                questionDialog();
                break;
        }
    }
}
