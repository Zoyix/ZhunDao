package com.zhaohe.zhundao.ui.home.action.msg;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wevey.selector.dialog.NormalAlertDialog;
import com.zhaohe.app.utils.IntentUtils;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.asynctask.AsyncGetUserInf;
import com.zhaohe.zhundao.asynctask.msg.AsyncGetMsgBuy;
import com.zhaohe.zhundao.bean.ToolUserBean;
import com.zhaohe.zhundao.ui.ToolBarActivity;
import com.zhaohe.zhundao.ui.home.mine.WalletActivity;
import com.zhaohe.zhundao.view.ColorFontTextView;
import com.zhaohe.zhundao.view.PayPsdInputView;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhaohe.zhundao.ui.home.action.ActionOnFragment.MESSAGE_GET_USERINF;

public class MsgBuyActivity extends ToolBarActivity implements RadioGroup.OnCheckedChangeListener, Toolbar.OnMenuItemClickListener {

    @BindView(R.id.tv_count)
    ColorFontTextView tvCount;
    @BindView(R.id.rb_msg_type1)
    RadioButton rbMsgType1;
    @BindView(R.id.rb_msg_type2)
    RadioButton rbMsgType2;
    @BindView(R.id.rb_msg_type3)
    RadioButton rbMsgType3;
    @BindView(R.id.rb_msg_type4)
    RadioButton rbMsgType4;
    @BindView(R.id.rg_msg)
    RadioGroup rgMsg;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.et_password)
    PayPsdInputView etPassword;
    @BindView(R.id.tv_password)
    TextView tvPassword;
    private int inputNum = 0;
    private double Count = 0.00;
    private NormalAlertDialog dialog;
    private String pwd;
    public static final int MESSAGE_MSG_BUY = 90;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBarNew("购买短信包", R.layout.activity_msg_buy);
        ButterKnife.bind(this);
        initView();
        initHandler();
    }

    private void initView() {

        rgMsg.setOnCheckedChangeListener(this);
        initCount();

        pwd = "123456789";
        etPassword.setComparePassword(pwd, new PayPsdInputView.onPasswordListener() {
            @Override
            public void onDifference() {
                // TODO: 2017/5/7   和上次输入的密码不一致  做相应的业务逻辑处理
                pwd = etPassword.getPasswordString();

                AsyncGetMsgBuy asyncActivity = new AsyncGetMsgBuy(getApplicationContext(), mHandler, MESSAGE_MSG_BUY, pwd, inputNum + "");
                asyncActivity.execute();

            }

            @Override
            public void onEqual(String psd) {
                // TODO: 2017/5/7 两次输入密码相同，那就去进行支付楼
//                    Toast.makeText(MsgBuyActivity.this, "密码相同" , Toast.LENGTH_SHORT).show();
                pwd = etPassword.getPasswordString();
                AsyncGetMsgBuy asyncActivity = new AsyncGetMsgBuy(getApplicationContext(), mHandler, MESSAGE_MSG_BUY, pwd, inputNum + "");
                asyncActivity.execute();

            }
        });

    }

    private void initCount() {
        if (inputNum <= 0) {
            Count = 0.00;
        }
        if (inputNum <= 100) {
            Count = inputNum * 0.1;
        } else if (inputNum <= 5000) {
            Count = inputNum * 0.08;
        } else {
            Count = inputNum * 0.07;
        }

        DecimalFormat df = new DecimalFormat("#0.00");

        tvCount.setTextStyle("共计：" + df.format(Count) + "元", df.format(Count) + "", "18");

    }

    private void selectCount() {

        EditText et1 = new EditText(this);
        et1.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        final EditText et = et1;

        new AlertDialog.Builder(this)
                .setTitle("输入自定义购买的短信包数量")
//      .setIcon(android.R.drawable.ic_dialog_info)
                .setView(et)
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();
                        if (input.equals("")) {
                            Toast.makeText(getApplicationContext(), "短信包数量不能为空！" + input, Toast.LENGTH_LONG).show();
                            selectCount();
                        } else {
                            inputNum = Integer.parseInt(input);
                            initCount();
                            if (inputNum <= 0) {
                                Toast.makeText(getApplicationContext(), "短信包数量必须大于0！当前：" + input, Toast.LENGTH_LONG).show();
                                selectCount();
                            } else {

                            }
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInf();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu_signlist_msg_more, menu);
        toolbar.setOnMenuItemClickListener(this);

        return true;
    }


    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {

                    case MESSAGE_GET_USERINF:
                        String result3 = (String) msg.obj;
                        SPUtils.put(getApplicationContext(), "UserInfo", result3);
                        savaUserInf(result3);
                        break;
                    case MESSAGE_MSG_BUY:
                        result3 = (String) msg.obj;
                        ToolUserBean bean = (ToolUserBean) JSON.parseObject(result3, ToolUserBean.class);
                        ToastUtil.makeText(getApplicationContext(), bean.getMsg());

                        etPassword.setText("");
                        tvPassword.setVisibility(View.GONE);
                        etPassword.setVisibility(View.GONE);
                        if (bean.getMsg().equals("余额不足")) {
                            moneyEmptyDialog();
                        }
                        if (bean.getMsg().equals("支付密码错误")) {
                            etPassword.setText("");
                            tvPassword.setVisibility(View.VISIBLE);
                            etPassword.setVisibility(View.VISIBLE);
                        }
                        break;

                    default:
                        break;
                }
            }
        };
    }

    private void savaUserInf(String result) {

        JSONObject jsonObj = JSON.parseObject(result);
        JSONObject jsonObject2 = JSON.parseObject(jsonObj.getString("Data"));


        if (null != jsonObject2.getString("PayPassWord")) {
            SPUtils.put(this, "PayPassWord", true);
        } else {


        }
    }


    private void passWrodDialog() {
        new android.support.v7.app.AlertDialog.Builder(this)
                //对话框的标题
                .setTitle("未设置支付密码？")
                //设定显示的View
                //对话框中的“登陆”按钮的点击事件
                .setMessage("确定后，前往我的钱包设置密码。")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        IntentUtils.startActivity(MsgBuyActivity.this, WalletActivity.class);

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

    private void moneyEmptyDialog() {
        new android.support.v7.app.AlertDialog.Builder(this)
                //对话框的标题
                .setTitle("余额不足")
                //设定显示的View
                //对话框中的“登陆”按钮的点击事件
                .setMessage("您钱包余额不足，无法完成充值，您可以通过准到PC平台使用支付宝进行自助充值")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }

                })

                // 设置dialog是否为模态，false表示模态，true表示非模态
                .setCancelable(true)
                //对话框的创建、显示
                .create().show();
    }


    private void getUserInf() {

        AsyncGetUserInf userInf = new AsyncGetUserInf(this, mHandler, MESSAGE_GET_USERINF);
        userInf.execute();

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_msg_type1:
                inputNum = 100;
                initCount();
                break;
            case R.id.rb_msg_type2:
                inputNum = 5000;
                initCount();

                break;
            case R.id.rb_msg_type3:
                inputNum = 10000;
                initCount();

                break;
            case R.id.rb_msg_type4:
                selectCount();
                break;

        }
    }


    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        if (inputNum == 0) {
            Toast.makeText(getApplicationContext(), "短信包数量不能为空！", Toast.LENGTH_LONG).show();
            return;
        }

        if (!(Boolean) SPUtils.get(this, "PayPassWord", false)) {

            passWrodDialog();


            return;
        }
        etPassword.setVisibility(View.VISIBLE);
        etPassword.setFocusable(true);
        tvPassword.setVisibility(View.VISIBLE);

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_signlist_msg_more:
                IntentUtils.startActivity(this, MsgBuyMoreActivity.class);

                break;

        }
        return false;
    }
}
