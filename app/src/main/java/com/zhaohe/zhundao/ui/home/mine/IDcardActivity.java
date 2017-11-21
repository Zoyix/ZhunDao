package com.zhaohe.zhundao.ui.home.mine;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.DigitsKeyListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhaohe.app.camera.Camera;
import com.zhaohe.app.utils.JSONUtils;
import com.zhaohe.app.utils.ProgressDialogUtils;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.asynctask.AsyncPostIdCard;
import com.zhaohe.zhundao.bean.jsonbean.UserInfBean;
import com.zhaohe.zhundao.ui.ToolBarActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IDcardActivity extends ToolBarActivity {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_idcard)
    EditText etIdcard;
    @BindView(R.id.camera_idcard_front)
    GridLayout cameraIdcardFront;
    @BindView(R.id.camera_idcard_back)
    GridLayout cameraIdcardBack;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    private Camera camera;
    private String PhotoTitle;
    private String IdCardFront;
    private String IdCardBack;
    private Handler mHandler;
    private UserInfBean bean;
    private int status = -1;

    LinkedHashMap<String, String> jsonMap = new LinkedHashMap<>();
    LinkedHashMap<String, String> map = new LinkedHashMap<>();

    private HashMap<String, Camera> cameraHashMap = new HashMap<>();
    public static final int MESSAGE_UPLOAD_COMPLETE = 1000;
    public static final int MESSAGE_DELETE_COMPLETE = 999;
    public static final int MESSAGE_SENT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBarNew("实名认证", R.layout.activity_idcard);
        ButterKnife.bind(this);
        initHandler();
        init();
        initView();
    }

    private void init() {
        bean = (UserInfBean) JSON.parseObject((String) SPUtils.get(this, "UserInfo", ""), UserInfBean.class);
        if (bean.getData().getAuthentication() != null) {
            status = bean.getData().getAuthentication().getStatus();
        }
    }

    private void initView() {
        String title = "IdCardFront";
        final Camera camera1 = new Camera(this, cameraIdcardFront, false, 1, mHandler, title);
        initCamera(title, camera1, cameraIdcardFront);
        title = "IdCardBack";
        final Camera camera2 = new Camera(this, cameraIdcardBack, false, 1, mHandler, title);
        initCamera(title, camera2, cameraIdcardBack);

        String digists = "0123456789xX";
        etIdcard.setKeyListener(DigitsKeyListener.getInstance(digists));
        if (status == 0 || status == 1 || status == 2) {

            etPhone.setText(bean.getData().getAuthentication().getMobile());
            etIdcard.setText(bean.getData().getAuthentication().getIDCard());
            etName.setText(bean.getData().getAuthentication().getName());


        }
        switch (status) {

            case 0:
                tvStatus.setText(R.string.tv_idcard_0);
                break;
            case 1:
                camera1.showInternet(bean.getData().getAuthentication().getIdCardFront());
                camera2.showInternet(bean.getData().getAuthentication().getIdCardBack());
                tvStatus.setText(R.string.tv_idcard_1);
                break;
            case 2:
                tvStatus.setText(R.string.tv_idcard_2);
                break;

        }


    }

    private void initCamera(final String title, final Camera camera1, GridLayout view) {
        PhotoTitle = title;
        cameraHashMap.put(title, camera1);
        view.requestDisallowInterceptTouchEvent(true);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                ToastUtil.print("ontouch");
                camera = camera1;
                PhotoTitle = title;

                return false;
            }
        });
    }

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_SENT:
                        String result = (String) msg.obj;
                        JSONObject jsonObj = JSON.parseObject(result);
                        String message = jsonObj.getString("Res");
                        if (message.equals("0"))
                        //添加或修改请求结果
                        {
                            ToastUtil.makeText(getApplicationContext(), jsonObj.getString("Msg"));
                            finish();
                        } else {
                            ToastUtil.makeText(getApplicationContext(), jsonObj.getString("Msg"));

                        }
                        break;
                    case MESSAGE_UPLOAD_COMPLETE:

                        String cameraUrl = "";

                        ArrayList<String> a = camera.getUploadUrl();
                        for (int i = 0; i < a.size(); i++) {
                            cameraUrl = cameraUrl + a.get(i);
                        }
                        ToastUtil.print("PhotoTitle" + PhotoTitle + cameraUrl);
                        jsonMap.put(PhotoTitle, cameraUrl);


                        ToastUtil.makeText(getApplicationContext(), "图片上传成功！");
                        break;
                    case MESSAGE_DELETE_COMPLETE:

                        cameraUrl = "";
                        PhotoTitle = (String) msg.obj;
                        camera = cameraHashMap.get(PhotoTitle);


                        ToastUtil.print("PhotoTitle" + PhotoTitle + cameraUrl);
                        jsonMap.put(PhotoTitle, cameraUrl);
                        ToastUtil.print("图片删除成功");
                        break;
                    default:
                        break;
                }
            }
        };
    }

    public void sent(String param) {
        Dialog dialog = ProgressDialogUtils.showProgressDialog(this, getString(R.string.progress_title), getString(R.string.progress_message));

        AsyncPostIdCard editAction = new AsyncPostIdCard(this, mHandler, dialog, MESSAGE_SENT, param);
        editAction.execute();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        camera.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        if (status == 1) {
            ToastUtil.makeText(getApplication(), "已经通过实名认证！");
            return;
        }


        if (etName.getText().length() < 1 || etPhone.getText().length() < 1 || etIdcard.getText().length() < 1) {
            ToastUtil.makeText(getApplication(), "请完善信息");

            return;
        }


        IdCardFront = jsonMap.get("IdCardFront");
        ToastUtil.print("IdCardFront" + IdCardFront);
        if (IdCardFront == null || IdCardFront.length() < 5) {
            ToastUtil.makeText(getApplication(), "请上传身份证正面");
            return;

        }

        IdCardBack = jsonMap.get("IdCardBack");
        ToastUtil.print("IdCardBack" + IdCardBack);
        if (IdCardBack == null || IdCardBack.length() < 5) {
            ToastUtil.makeText(getApplication(), "请上传身份证背面");
            return;
        }

        if (status == 0 || status == 2) {
            map.put("ID", bean.getData().getAuthentication().getID() + "");
        }
        map.put("Name", etName.getText().toString());
        map.put("IDCard", etIdcard.getText().toString());
        map.put("Mobile", etPhone.getText().toString());
        map.put("IdCardFront", IdCardFront);
        map.put("IdCardBack", IdCardBack);

//String param= String.valueOf(map);
        ;
        String param = JSONUtils.mapToString(map);
//        String param="Name="+etName.getText()+"&Mobile="+etPhone.getText()+"&IDCard="+etIdcard.getText()+"&IdCardFront="+IdCardFront+"&IdCardBack="+IdCardBack;
        sent(param);
        ToastUtil.print(param);
    }


}
