package com.zhaohe.zhundao.ui.home.action.signlist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.wevey.selector.dialog.DialogInterface;
import com.wevey.selector.dialog.NormalSelectionDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;
import com.zhaohe.app.camera.PreviewImgActivity;
import com.zhaohe.app.camera.multimgselector.MultiImageSelectorActivity;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.app.utils.ZXingUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.bean.SignListBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.shaohui.bottomdialog.BottomDialog;

import static com.zhaohe.app.utils.ZXingUtil.createQrBitmap;

public class InvitationPersonActivity extends AppCompatActivity implements View.OnLongClickListener {
    SignListBean bean;
    @BindView(R.id.tv_dialog_title)
    TextView tvDialogTitle;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_vcode_invitation)
    ImageView ivVcodeInvitation;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.rl_invitation_person)
    RelativeLayout rlInvitationPerson;
    NormalSelectionDialog dialog1;
    private static final int REQUEST_IMAGE = 2000;
    private Bitmap bitmap;
    private UMShareListener mShareListener;

    private static final int REQUEST_CODE_PERMISSION = 100;

    private static final int REQUEST_CODE_SETTING = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_invitation_person);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//去掉Activity上面的状态栏

        ButterKnife.bind(this);
        initIntent();
        initView();
    }

    private void initView() {
        tvName.setText(bean.getSign_list_name());
        tvTime.setText((String) SPUtils.get(this, "act_time", ""));
        tvDialogTitle.setText((String) (SPUtils.get(this, "act_title", "")));
        tvAdd.setText("活动地点：" + SPUtils.get(this, "act_add", ""));
        setPhoto((int) SPUtils.get(this, "invitation_type", 1), ivVcodeInvitation);

        rlInvitationPerson.setOnLongClickListener(this);
        LayoutInflater factory = LayoutInflater.from(this);
        View v = factory.inflate(R.layout.dialog_invitation_person0, null);
        ImageView iv_vcode_invitation = (ImageView) v.findViewById(R.id.iv_vcode_invitation);
        ;
        setPhoto((int) SPUtils.get(this, "invitation_type", 1), iv_vcode_invitation);
        TextView title = (TextView) v.findViewById(R.id.tv_dialog_title);
        TextView name = (TextView) v.findViewById(R.id.tv_name);
        name.setText(bean.getSign_list_name());

        title.setText((String) (SPUtils.get(this, "act_title", "")));
        TextView time = (TextView) v.findViewById(R.id.tv_time);
        TextView add = (TextView) v.findViewById(R.id.tv_add);


        time.setText("" + SPUtils.get(this, "act_time", ""));
        add.setText("活动地点：" + SPUtils.get(this, "act_add", ""));

        ;
        bitmap = ZXingUtil.createViewBitmap(v);

        ToastUtil.print(bitmap + "");
        mShareListener = new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {
                //分享开始的回调
            }

            @Override
            public void onResult(SHARE_MEDIA platform) {
                Log.d("plat", "platform" + platform);


            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                if (t != null) {
                    Log.d("throw", "throw:" + t.getMessage());
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
            }
        };
        dialog1 = new NormalSelectionDialog.Builder(this)
                .setlTitleVisible(true)   //设置是否显示标题
                .setTitleHeight(65)   //设置标题高度
                .setTitleText("选项")  //设置标题提示文本
                .setTitleTextSize(14) //设置标题字体大小 sp
                .setTitleTextColor(R.color.colorPrimary) //设置标题文本颜色
                .setItemHeight(40)  //设置item的高度
                .setItemWidth(0.9f)  //屏幕宽度*0.9
                .setItemTextColor(R.color.colorPrimaryDark)  //设置item字体颜色
                .setItemTextSize(14)  //设置item字体大小
                .setCancleButtonText("取消")  //设置最底部“取消”按钮文本
                .setOnItemListener(new DialogInterface.OnItemClickListener<NormalSelectionDialog>() {
                    @Override
                    public void onItemClick(NormalSelectionDialog dialog, View button, int position) {
                        switch (position) {
                            case 0:
                                AndPermission.with(getApplicationContext())
                                        .requestCode(REQUEST_CODE_PERMISSION)
                                        .permission(Permission.STORAGE)
                                        .callback(permissionListener)
                                        // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                                        // 这样避免用户勾选不再提示，导致以后无法申请权限。
                                        // 你也可以不设置。
                                        .rationale(null)
                                        .start();
                                dialog1.dismiss();
                                break;
                            case 1:
                                showDialog();
                                dialog1.dismiss();

                                break;
                        }
                    }  //监听item点击事件

                })
                .setCanceledOnTouchOutside(true)  //设置是否可点击其他地方取消dialog
                .build();

        ArrayList<String> s = new ArrayList<>();
        s.add("保存");
        s.add("分享");
        dialog1.setDatas(s);


    }

    private void setPhoto(int invitation_type, ImageView view) {
        switch (invitation_type) {
            case 1:
                final Bitmap bitmap = createQrBitmap("https://m.zhundao.net/event/" + bean.getAct_id(), 500, 500);
                view.setImageBitmap(bitmap);

                break;
            case 2:
                final Bitmap bitmap2 = createQrBitmap(bean.getVCode(), 1000, 1000);
                view.setImageBitmap(bitmap2);

                break;
        }
    }

    private void initIntent() {
        Intent intent = this.getIntent();
        bean = (SignListBean) intent.getSerializableExtra("bean");
    }

    private void showDialog() {
        BottomDialog.create(getSupportFragmentManager())
                .setViewListener(new BottomDialog.ViewListener() {
                    @Override
//                    自定义事件
                    public void bindView(View v) {
                        View.OnClickListener onclick = (new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch (v.getId()) {
                                    case R.id.iv_share_wechat_solid:
                                        UmengShare(SHARE_MEDIA.WEIXIN);
                                        break;
                                    case R.id.iv_share_weixin_friends_solid:
                                        UmengShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                                        break;
                                    case R.id.iv_share_weibo_solid:
                                        UmengShare(SHARE_MEDIA.SINA);
                                        break;
                                    case R.id.iv_share_qq_solid:
                                        UmengShare(SHARE_MEDIA.QQ);
                                        break;
                                    case R.id.iv_share_qqzone_solid:
                                        UmengShare(SHARE_MEDIA.QZONE);

                                        break;
                                }
                            }
                        });
                        ImageView iv_share_wechat_solid = (ImageView) v.findViewById(R.id.iv_share_wechat_solid);
                        ImageView iv_share_weixin_friends_solid = (ImageView) v.findViewById(R.id.iv_share_weixin_friends_solid);
                        ImageView iv_share_weibo_solid = (ImageView) v.findViewById(R.id.iv_share_weibo_solid);
                        ImageView iv_share_qq_solid = (ImageView) v.findViewById(R.id.iv_share_qq_solid);
                        ImageView iv_share_qqzone_solid = (ImageView) v.findViewById(R.id.iv_share_qqzone_solid);

                        iv_share_weixin_friends_solid.setOnClickListener(onclick);
                        iv_share_wechat_solid.setOnClickListener(onclick);
                        iv_share_weibo_solid.setOnClickListener(onclick);
                        iv_share_qq_solid.setOnClickListener(onclick);
                        iv_share_qqzone_solid.setOnClickListener(onclick);
                    }
                })
                .setLayoutRes(R.layout.dialog_layout)
                .setDimAmount(0.2f)
                .setTag("BottomDialog")
                .show();
    }

    private void UmengShare(SHARE_MEDIA type) {
//        UMWeb web = new UMWeb("https://"+ShareUrl+bean.getAct_id());
//        UMImage image = new UMImage(this, bean.getUrl());
//        web.setTitle( bean.getAct_title());//标题
//        web.setDescription(bean.getAct_starttime()+"\n活动地点： "+bean.getAddress());//描述
//        web.setThumb(image);
        UMImage image = new UMImage(this, bitmap);
        UMImage thumb = new UMImage(this, bitmap);
        image.setThumb(thumb);

        new ShareAction(this).setPlatform(type)
                .withText(bean.getSign_list_name() + "邀请函")
                .withMedia(image)
                .setCallback(mShareListener)
                .share();
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.rl_invitation_person:
                dialog1.show();
                break;
        }
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                // 获取返回的图片列表
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                if (path.size() != 0) {
                    ToastUtil.print("图片地址" + path.get(0));
                    Intent intent = new Intent();
                    intent.putExtra("imgpath", path.get(0));
                    intent.setClass(this, PreviewImgActivity.class);
                    this.startActivity(intent);

                }
            }
        }
        if (requestCode == REQUEST_CODE_SETTING) {
            Toast.makeText(this, R.string.message_setting_back, Toast.LENGTH_LONG).show();
        }

    }

    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {
                case REQUEST_CODE_PERMISSION: {

                    ZXingUtil.saveImageToGallery(getApplicationContext(), bitmap, bean.getSign_list_name() + "邀请函");
                    ToastUtil.makeText(getApplicationContext(), "保存成功！");
                    finish();
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
