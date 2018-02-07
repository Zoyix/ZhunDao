package com.zhaohe.zhundao.ui.home.sign;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.app.utils.ZXingUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.asynctask.AsyncSignDelete;
import com.zhaohe.zhundao.asynctask.AsyncSignEdit;
import com.zhaohe.zhundao.bean.SignBean;
import com.zhaohe.zhundao.ui.ToolBarActivity;

import static com.zhaohe.app.utils.ZXingUtil.createQrBitmap;


public class SignEditActivity extends ToolBarActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, Toolbar.OnMenuItemClickListener {
    private SignBean bean;
    private TextView tv_act_title;
    private EditText et_sign_add_name;
    private Spinner sp_sign_add_type, sp_sign_add_object;
    private Button btn_sign_add;
    public static final int MESSAGE_SIGN_EDIT = 99;
    public static final int MESSAGE_SIGN_DELETE = 98;

    private String CheckInType, Name, SignObject;
    private int sign_type, sign_object;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_edit);
        initToolBarNew("修改签到信息", R.layout.activity_sign_edit);
        initIntent();
        initView();
        initHandler();
    }

    private void initIntent() {
        Intent intent = this.getIntent();
        bean = (SignBean) intent.getSerializableExtra("bean");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu_sign_edit, menu);
        toolbar.setOnMenuItemClickListener(this);

        return true;
    }

    private void initView() {
        tv_act_title = (TextView) findViewById(R.id.tv_sign_add_actname4);
        tv_act_title.setText(bean.getAct_title());
        et_sign_add_name = (EditText) findViewById(R.id.et_sign_add_name2);
        et_sign_add_name.setText(bean.getAct_title());
        sp_sign_add_type = (Spinner) findViewById(R.id.sp_sign_add_type2);
        if (bean.getSign_type().equals("现场签到：")) {
            ;
            sign_type = 0;
        }
        if (bean.getSign_type().equals("离场签退：")) {
            ;
            sign_type = 1;
        }
        if (bean.getSign_type().equals("集合签到：")) {
            ;
            sign_type = 2;
        }
        sp_sign_add_type.setSelection(sign_type);
        sp_sign_add_type.setOnItemSelectedListener(this);
        sp_sign_add_object = (Spinner) findViewById(R.id.sp_sign_add_object2);
        sp_sign_add_object.setSelection(Integer.parseInt(bean.getSignObject()));

        sp_sign_add_object.setOnItemSelectedListener(this);

        btn_sign_add = (Button) findViewById(R.id.btn_sign_add2);
        btn_sign_add.setOnClickListener(this);
    }

    private void editSign(String checkInId, String name, String type, String signObject) {
        AsyncSignEdit async = new AsyncSignEdit(this, mHandler, MESSAGE_SIGN_EDIT, checkInId, name, type, signObject);
        async.execute();
    }

    private void deleteSign(String checkInId) {
        AsyncSignDelete async = new AsyncSignDelete(this, mHandler, MESSAGE_SIGN_DELETE, checkInId);
        async.execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_add2:
                Name = et_sign_add_name.getText().toString();
                if (Name.length() == 0) {
                    ToastUtil.makeText(getApplicationContext(), "签到名称不得为空！");
                    return;
                }
                editSign(bean.getSign_id(), Name, sign_type + "", SignObject);


                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (sp_sign_add_type.getSelectedItem().toString().equals("到场签到")) {
            sign_type = 0;
        }
        if (sp_sign_add_type.getSelectedItem().toString().equals("离场签退")) {
            sign_type = 1;
        }
        if (sp_sign_add_type.getSelectedItem().toString().equals("集合签到")) {
            sign_type = 2;
        }

        if (sp_sign_add_object.getSelectedItem().toString().equals("限报名人员")) {
            SignObject = "0";
        }
        if (sp_sign_add_object.getSelectedItem().toString().equals("不限报名人员")) {
            SignObject = "1";
        }

    }

    public void QrCodeDialog() {

        //LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化
        LayoutInflater factory = LayoutInflater.from(this);
        //把activity_login中的控件定义在View中
        final View v = factory.inflate(R.layout.dialog_qrcode_sign, null);
        ImageView iv_dialog_qrcode;
        iv_dialog_qrcode = (ImageView) v.findViewById(R.id.iv_dialog_qrcode_sign);
        TextView title = (TextView) v.findViewById(R.id.tv_qr_title);
        title.setText(bean.getAct_title());

        final Bitmap bitmap = createQrBitmap("https://m.zhundao.net/Inwechat/CheckInForBeacon/?checkInId=" + bean.getSign_id(), 600, 600);
        iv_dialog_qrcode.setImageBitmap(bitmap);

        ;
        new AlertDialog.Builder(this)
                //对话框的标题
//                .setTitle(bean.getAct_title())
                .setView(v)
                //设定显示的View
                //对话框中的“登陆”按钮的点击事件
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
//ZXingUtil.saveMyBitmap(ZXingUtil.createQrBitmap(bean.getUrl(),150,150),bean.getAct_title()+"二维码");
                        ZXingUtil.saveImageToGallery(getApplicationContext(), bitmap, bean.getAct_title());
                        ToastUtil.makeText(getApplicationContext(), "保存成功！");
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

    public void deleteDialog() {

        //LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化

        new AlertDialog.Builder(this)
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

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        SignObject = bean.getSignObject();
    }

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_SIGN_EDIT:
                        String result = (String) msg.obj;
                        JSONObject jsonObj = JSON.parseObject(result);
                        String message = jsonObj.getString("Msg");
                        System.out.println("sign_add_result:" + result);
                        if (jsonObj.getString("Res").equals("0"))
                        //添加或修改请求结果
                        {
                            ToastUtil.makeText(getApplicationContext(), "签到修改成功！");
                            SPUtils.put(getApplicationContext(), "updateSign", true);

                            finish();
                        } else {
                            ToastUtil.makeText(getApplicationContext(), message);
                        }
                        break;
                    case MESSAGE_SIGN_DELETE:
                        result = (String) msg.obj;
                        jsonObj = JSON.parseObject(result);
                        message = jsonObj.getString("Msg");
                        System.out.println("sign_add_result:" + result);
                        if (jsonObj.getString("Res").equals("0"))
                        //添加或修改请求结果
                        {
                            SPUtils.put(getApplicationContext(), "tab_now", 1);
                            SPUtils.put(getApplicationContext(), "updateSign", true);

                            finish();
                            ToastUtil.makeText(getApplicationContext(), "删除成功！");
                        } else {
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
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sign_delete:
                deleteDialog();
                break;
            case R.id.menu_sign_show:
                QrCodeDialog();
                break;
        }
        return false;
    }
}
