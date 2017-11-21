package com.zhaohe.zhundao.ui.home.mine;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.squareup.picasso.Picasso;
import com.zhaohe.app.camera.Camera;
import com.zhaohe.app.camera.multimgselector.MultiImageSelectorActivity;
import com.zhaohe.app.commons.dialog.DialogUtils;
import com.zhaohe.app.utils.CircleTransform;
import com.zhaohe.app.utils.ProgressDialogUtils;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.asynctask.testScan.AsyncUpdateUserInfo;
import com.zhaohe.zhundao.bean.ToolUserBean;
import com.zhaohe.zhundao.ui.ToolBarActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhaohe.app.camera.Camera.max_select_count;
import static com.zhaohe.zhundao.R.id.img_head;

public class UpdateUserInfoActivity extends ToolBarActivity {

    @BindView(R.id.et_nickname)
    EditText etNickname;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.img_head)
    ImageView imgHead;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.sp_sex)
    Spinner spSex;
    @BindView(R.id.et_unit)
    EditText etUnit;
    @BindView(R.id.et_industry)
    EditText etIndustry;
    @BindView(R.id.et_duty)
    EditText etDuty;
    private Camera camera;
    private String PhotoTitle;
    private String HeadImgurl;
    private Handler mHandler;
    public static final int MESSAGE_UPLOAD_COMPLETE = 1000;
    public static final int MESSAGE_DELETE_COMPLETE = 999;
    public static final int MESSAGE_SUBMIT = 97;
    private static final int REQUEST_IMAGE = 2000;

    private String mParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBarNew("修改个人信息", R.layout.activity_update_user_info);
        ButterKnife.bind(this);
        initHandler();
        initView();
    }

    private void initView() {
        PhotoTitle = "头像";
//        camera = new Camera(this, cameraGridview, false, 1, mHandler, PhotoTitle);
//        camera.showInternet((String) SPUtils.get(this, "HeadImgurl", ""));
        etNickname.setText((String) SPUtils.get(this, "NickName", ""));
        etName.setText((String) SPUtils.get(this, "TrueName", ""));
        if (etName.getText().length() > 0) {
            etName.setInputType(View.TEXT_ALIGNMENT_INHERIT);
        }
        etDuty.setText((String) SPUtils.get(this, "Duty", ""));
        etIndustry.setText((String) SPUtils.get(this, "Industry", ""));
        etUnit.setText((String) SPUtils.get(this, "Company", ""));
        etEmail.setText((String) SPUtils.get(this, "Email", ""));
        tvPhone.setText((String) SPUtils.get(this, "Mobile", ""));
        HeadImgurl = (String) SPUtils.get(this, "HeadImgurl", "");
        int sex = Integer.parseInt((String) SPUtils.get(this, "Sex", "0"));
        spSex.setSelection(sex);

        if (HeadImgurl == "") {
            Picasso.with(this).load(R.drawable.unkown_head).transform(new CircleTransform()).into(imgHead);

        } else {
            Picasso.with(this).load(HeadImgurl).error(R.drawable.unkown_head).transform(new CircleTransform()).into(imgHead);
        }
    }


    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {

                    case MESSAGE_SUBMIT:
                        String result2 = (String) msg.obj;
                        ToolUserBean bean = (ToolUserBean) JSON.parseObject(result2, ToolUserBean.class);
                        Toast.makeText(getApplicationContext(), bean.getMsg(), Toast.LENGTH_LONG).show();

                        if (bean.isSucess()) {
                            finish();

                        } else {


                        }
                        break;
                    case MESSAGE_UPLOAD_COMPLETE:

                        String cameraUrl = "";

                        ArrayList<String> a = camera.getUploadUrl();
                        for (int i = 0; i < a.size(); i++) {
                            HeadImgurl = a.get(i);
                            Picasso.with(getApplicationContext()).load(HeadImgurl).error(R.drawable.unkown_head).transform(new CircleTransform()).into(imgHead);

                        }


                        ToastUtil.makeText(getApplicationContext(), "图片上传成功！");
                        break;
                    case MESSAGE_DELETE_COMPLETE:

                        cameraUrl = "";
                        PhotoTitle = (String) msg.obj;
                        a = camera.getUploadUrl();
                        for (int i = 0; i < a.size(); i++) {

                        }


                        ToastUtil.print("PhotoTitle" + PhotoTitle + cameraUrl);

                        ToastUtil.print("图片删除成功");
                    default:
                        break;
                }
            }
        };
    }

    protected void updateUserInf() {
        Dialog dialog = ProgressDialogUtils.showProgressDialog(this, getString(R.string.progress_title), getString(R.string.progress_message));
        String name = etNickname.getText().toString();
        mParam = "NickName=" + name + "&HeadImgurl=" + HeadImgurl;
        setParam();
        ToastUtil.print(mParam);
        AsyncUpdateUserInfo async = new AsyncUpdateUserInfo(this, mHandler, dialog, MESSAGE_SUBMIT, mParam);
        async.execute();
    }


    @OnClick({R.id.btn_login, img_head})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                updateUserInf();
                break;
            case img_head:
                try {
                    selectImage(1);
                } catch (Exception e) {
                    ToastUtil.makeText(this, R.string.tv_save_permission);
                    e.printStackTrace();
                }
                break;
        }


    }

    private String setParam() {
        if (etName.getText().length() > 0) {
            mParam = mParam + "&TrueName=" + etName.getText().toString();
        }
        if (etEmail.getText().length() > 0) {
            mParam = mParam + "&Email=" + etEmail.getText().toString();
        }
        if (etUnit.getText().length() > 0) {
            mParam = mParam + "&Company=" + etUnit.getText().toString();
        }
        if (etDuty.getText().length() > 0) {
            mParam = mParam + "&Duty=" + etDuty.getText().toString();
        }
        if (etIndustry.getText().length() > 0) {
            mParam = mParam + "&Industry=" + etIndustry.getText().toString();
        }
        if (spSex.getSelectedItemPosition() != 0) {
            mParam = mParam + "&Sex=" + spSex.getSelectedItemPosition();
        }


        return mParam;
    }

    public void selectImage(int selectCount) {

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {// 判断手机是否装载SDCard
            // 可选照片数 = 最大照片数 - 已选照片数
            int extra_select_count = max_select_count - selectCount;

            Intent intent = new Intent(this, MultiImageSelectorActivity.class);
            // 是否显示调用相机拍照
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
            // 最大图片选择数量
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, selectCount);
            // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);

            this.startActivityForResult(intent, REQUEST_IMAGE);


        } else {
            // Sdcard 不存在
            DialogUtils.showDialogViewFinish(this, R.string.app_sdcardnotexist);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                // 获取返回的图片列表
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                if (path.size() != 0) {
                    ToastUtil.print("图片地址" + path.get(0));

                    camera = new Camera(this, mHandler, path.get(0));

                }
            }
        }


    }


}
