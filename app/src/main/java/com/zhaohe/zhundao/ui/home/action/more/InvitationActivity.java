package com.zhaohe.zhundao.ui.home.action.more;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
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
import com.zhaohe.app.commons.dialog.DialogUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.app.utils.ZXingUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.bean.ActionBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.shaohui.bottomdialog.BottomDialog;

import static com.zhaohe.app.camera.Camera.max_select_count;
import static com.zhaohe.app.utils.ZXingUtil.createQrBitmap;


public class InvitationActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    @BindView(R.id.btn_invitation_save)
    Button btnInvitationSave;
    @BindView(R.id.rl_invitation_parent)
    RelativeLayout rlInvitationParent;
    private IndicatorViewPager indicatorViewPager;
    private LayoutInflater inflate;
    private ActionBean bean;
    private int[] layout = {R.layout.dialog_invitation_local0, R.layout.dialog_invitation_local1, R.layout.dialog_invitation_local2};
    private Bitmap bitmap0, bitmap1, bitmap2;
    private Bitmap[] bitmaps = {bitmap0, bitmap1, bitmap2};
    private UMShareListener mShareListener;
    private static final int REQUEST_IMAGE = 2000;


    private static final int REQUEST_CODE_PERMISSION = 100;

    private static final int REQUEST_CODE_SETTING = 300;
    NormalSelectionDialog dialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_invitation);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//去掉Activity上面的状态栏

        ButterKnife.bind(this);
        initIntent();
        init();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.toolbar_menu_invitation_user, menu);
//        toolbar.setOnMenuItemClickListener(this);
//
//        return true;
//    }

    private void init() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.guide_viewPager);
        Indicator indicator = (Indicator) findViewById(R.id.guide_indicator);
        btnInvitationSave.setAlpha((float) 0.2);
        // 将viewPager和indicator使用
        indicatorViewPager = new IndicatorViewPager(indicator, viewPager, true);

        inflate = LayoutInflater.from(getApplicationContext());
        // 设置indicatorViewPager的适配器
        indicatorViewPager.setAdapter(adapter);
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
                                showDialog(bean);
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

    private void initIntent() {
        Intent intent = this.getIntent();
        bean = (ActionBean) intent.getSerializableExtra("bean");
        LayoutInflater factory = LayoutInflater.from(this);
        for (int i = 0; i < layout.length; i++) {
            final View v = factory.inflate(layout[i], null);
            ImageView iv_vcode_invitation;
            iv_vcode_invitation = (ImageView) v.findViewById(R.id.iv_vcode_invitation);
            final Bitmap bitmap = createQrBitmap("https://m.zhundao.net/event/" + bean.getAct_id(), 500, 500);
            iv_vcode_invitation.setImageBitmap(bitmap);
            TextView title = (TextView) v.findViewById(R.id.tv_dialog_title);
            title.setText(bean.getAct_title());
            TextView time = (TextView) v.findViewById(R.id.tv_time);
            time.setTextSize(8);
            String newtime = bean.getAct_starttime();
            TextView add = (TextView) v.findViewById(R.id.tv_add);
            add.setTextSize(8);
            String newadd = bean.getAddress();
            if (i == 0 || i == 2) {
                if (bean.getAct_starttime().contains("活动开始")) {
                    newtime = bean.getAct_starttime().replace("活动开始", "时间");
                }
                if (bean.getAct_starttime().contains("活动结束")) {
                    newtime = bean.getAct_starttime().replace("活动结束", "时间");
                }


                newadd = "地点：" + newadd;
            }

            if (i == 1) {
                if (bean.getAct_starttime().contains("活动开始")) {
                    newtime = bean.getAct_starttime().replace("活动开始：", "");
                }
                if (bean.getAct_starttime().contains("活动结束")) {
                    newtime = bean.getAct_starttime().replace("活动结束：", "");
                }
            }
            time.setText(newtime);
            add.setText(newadd);


            bitmaps[i] = ZXingUtil.convertViewToBitmap(v);
            ToastUtil.print(bitmaps[i] + "");
        }
    }


    private IndicatorViewPager.IndicatorPagerAdapter adapter = new IndicatorViewPager.IndicatorViewPagerAdapter() {
        private int[] images = {R.mipmap.invitation2, R.mipmap.invitation3, R.mipmap.invitation2,
                R.mipmap.invitation3};

        /**
         * 获取tab
         */
        @Override
        public View getViewForTab(int position, View convertView,
                                  ViewGroup container) {
            if (convertView == null) {
                convertView = inflate.inflate(R.layout.tab_guide, container,
                        false);
            }
            return convertView;
        }

        /**
         * 获取每一个界面
         */
        @Override
        public View getViewForPage(int position, View convertView,
                                   ViewGroup container) {
            if (convertView == null) {
                convertView = new View(getApplicationContext());
                convertView.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
//            convertView.setBackgroundResource(images[position]);
            Drawable drawable = new BitmapDrawable(bitmaps[position]);
            convertView.setBackground(drawable);
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    dialog1.show();
                    return false;
                }
            });


            return convertView;
        }

        /**
         * 获取界面数量
         */
        @Override
        public int getCount() {
            return bitmaps.length;
        }
    };

    @OnClick(R.id.btn_invitation_save)
    public void onViewClicked() {
        AndPermission.with(this)
                .requestCode(REQUEST_CODE_PERMISSION)
                .permission(Permission.STORAGE)
                .callback(permissionListener)
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                // 这样避免用户勾选不再提示，导致以后无法申请权限。
                // 你也可以不设置。
                .rationale(null)
                .start();

    }

    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {
                case REQUEST_CODE_PERMISSION: {
                    ZXingUtil.saveImageToGallery(getApplicationContext(), bitmaps[indicatorViewPager.getCurrentItem()], bean.getAct_title());
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


    private void showDialog(final ActionBean bean) {
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
                                        UmengShare(bean, SHARE_MEDIA.WEIXIN);
                                        break;
                                    case R.id.iv_share_weixin_friends_solid:
                                        UmengShare(bean, SHARE_MEDIA.WEIXIN_CIRCLE);
                                        break;
                                    case R.id.iv_share_weibo_solid:
                                        UmengShare(bean, SHARE_MEDIA.SINA);
                                        break;
                                    case R.id.iv_share_qq_solid:
                                        UmengShare(bean, SHARE_MEDIA.QQ);
                                        break;
                                    case R.id.iv_share_qqzone_solid:
                                        UmengShare(bean, SHARE_MEDIA.QZONE);

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

    private void UmengShare(ActionBean bean, SHARE_MEDIA type) {
//        UMWeb web = new UMWeb("https://"+ShareUrl+bean.getAct_id());
//        UMImage image = new UMImage(this, bean.getUrl());
//        web.setTitle( bean.getAct_title());//标题
//        web.setDescription(bean.getAct_starttime()+"\n活动地点： "+bean.getAddress());//描述
//        web.setThumb(image);
        UMImage image = new UMImage(this, bitmaps[indicatorViewPager.getCurrentItem()]);
        UMImage thumb = new UMImage(this, bitmaps[indicatorViewPager.getCurrentItem()]);
        image.setThumb(thumb);

        new ShareAction(this).setPlatform(type)
                .withText(bean.getAct_title() + "邀请函")
                .withMedia(image)
                .setCallback(mShareListener)
                .share();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_invitation_share:
                showDialog(bean);
//                selectImage(1);
                break;

            case R.id.menu_invitation_save:
                AndPermission.with(this)
                        .requestCode(REQUEST_CODE_PERMISSION)
                        .permission(Permission.STORAGE)
                        .callback(permissionListener)
                        // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                        // 这样避免用户勾选不再提示，导致以后无法申请权限。
                        // 你也可以不设置。
                        .rationale(null)
                        .start();
                break;
        }
        return false;
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


}

