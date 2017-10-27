package com.zhaohe.zhundao.ui.home.action.more;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;
import com.zhaohe.app.utils.IntentUtils;
import com.zhaohe.app.utils.NetworkUtils;
import com.zhaohe.app.utils.ProgressDialogUtils;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.app.utils.ZXingUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.ActionMoreAdapter;
import com.zhaohe.zhundao.asynctask.AsyncSignList;
import com.zhaohe.zhundao.asynctask.action.AsyncActionDelete;
import com.zhaohe.zhundao.asynctask.action.AsyncActionInvitation;
import com.zhaohe.zhundao.asynctask.action.AsyncActionUnDue;
import com.zhaohe.zhundao.bean.ActionBean;
import com.zhaohe.zhundao.bean.ActionMoreBean;
import com.zhaohe.zhundao.constant.Constant;
import com.zhaohe.zhundao.ui.ToolBarActivity;
import com.zhaohe.zhundao.ui.ToolBarHelper;
import com.zhaohe.zhundao.ui.home.HomeActivity;
import com.zhaohe.zhundao.ui.home.action.ActionDetailsActivity;
import com.zhaohe.zhundao.ui.home.action.EditActWebActivity;
import com.zhaohe.zhundao.ui.home.action.SignListActivity;

import java.util.ArrayList;
import java.util.List;

import me.shaohui.bottomdialog.BottomDialog;

import static com.zhaohe.app.utils.ZXingUtil.createQrBitmap;
import static com.zhaohe.zhundao.constant.Constant.Url.ShareUrl;

public class ActionMoreActivity extends ToolBarActivity implements AdapterView.OnItemClickListener ,View.OnClickListener{
    public static final int POSITION_EDIT = 0;                                       // 编辑活动
    public static final int POSITION_LIST = 1;                                  // 报名名单
    public static final int POSITION_CONSULT = 2;                                // 活动咨询
    public static final int POSITION_URL = 3;                                     // 活动链接
    public static final int POSITION_STOP = 4;                                    // 报名截止
    public static final int POSITION_DELETE = 5;                             // 删除活动
    public static final int POSITION_SHARE = 6;                                  // 分享活动
    public static final int POSITION_INVITATION = 7;                         // 邀请函
    public static final int POSITION_QRCODE = 8;                                   // 下载二维码
    public static final int POSITION_SIGNUP = 10;                                   // 编辑活动
    public static final int POSITION_SIGN_ADD = 9;                                   // 签到管理

    public static final int MESSAGE_GET_SIGNLIST = 93;
    public static final int MESSAGE_DELETE_ACTION = 100;
    public static final int MESSAGE_UNDUE_ACTION = 99;
    public static final int MESSAGE_INVITATION_ACTION = 98;


    public static final int PAGE_SIZE = 100000;
    private Handler mHandler;
    private GridView gridView;
    private ImageView iv_act_more_icon;
    private TextView tv_act_more_title;
    private TextView tv_act_more_endtime;
    private TextView tv_act_more_starttime;
    private TextView tv_act_more_sign_num;
    private TextView tv_act_more_see_num;
    private TextView tv_act_more_income_num;
    private IWXAPI api;
    private RelativeLayout rl_act_more_details;
    private UMShareListener mShareListener;
    private static final int REQUEST_CODE_PERMISSION = 105;
int j=0;
    private static final int REQUEST_CODE_SETTING = 300;
    Bitmap bitmap;

    private ActionBean bean;
    private ActionMoreAdapter adapter;
    private int[] gridStrings = {R.string.act_more_edit, R.string.act_more_list,
            R.string.act_more_consult, R.string.act_more_url, R.string.act_more_stop, R.string.act_more_delete,
            R.string.act_more_share, R.string.act_more_invitation, R.string.act_more_qrcode,R.string.act_more_sign_add,R.string.act_more_signup};
    private int[] gridImages = {R.mipmap.act_more_edit, R.mipmap.act_more_list,
            R.mipmap.act_more_consult, R.mipmap.act_more_url, R.mipmap.act_more_stop, R.mipmap.act_more_delete,
            R.mipmap.act_more_share, R.mipmap.act_more_invitation, R.mipmap.act_more_qrcode,R.mipmap.act_more_sign_add,R.mipmap.act_more_sign};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_more);
        initToolBar("活动更多功能", R.layout.activity_action_more);
        initHandler();
        initIntent();
        initView();
        initWx();
    }

    private void initIntent() {
        Intent intent = this.getIntent();
        bean = (ActionBean) intent.getSerializableExtra("bean");
    }

    public void initToolBar(String text, int layoutResID) {
        ToolBarHelper mToolBarHelper;
        mToolBarHelper = new ToolBarHelper(this, layoutResID);
        mToolBarHelper.setTvTitle(text);
        super.setTitle("");
        setContentView(mToolBarHelper.getContentView());
        toolbar = mToolBarHelper.getToolBar();
  /*把 toolbar 设置到Activity 中*/
        setSupportActionBar(toolbar);
    }

    private void initView() {
        rl_act_more_details= (RelativeLayout) findViewById(R.id.rl_act_more_details);
        rl_act_more_details.setOnClickListener(this);
        iv_act_more_icon = (ImageView) findViewById(R.id.iv_act_more_icon);
        if (bean.getUrl() != null) {
            Picasso.with(this).load(bean.getUrl()).error(R.mipmap.ic_launcher).into(iv_act_more_icon);
        }
        tv_act_more_title = (TextView) findViewById(R.id.tv_act_more_title);
        tv_act_more_title.setText(bean.getAct_title());
        tv_act_more_starttime = (TextView) findViewById(R.id.tv_act_more_starttime);
//        对调了就开始和截止时间
        tv_act_more_starttime.setText(bean.getAct_endtime());
        tv_act_more_endtime = (TextView) findViewById(R.id.tv_act_more_endtime);
        tv_act_more_endtime.setText(bean.getAct_starttime());

        tv_act_more_sign_num = (TextView) findViewById(R.id.tv_act_more_sign_num);
        tv_act_more_sign_num.setText(bean.getAct_sign_num());
        tv_act_more_income_num = (TextView) findViewById(R.id.tv_act_more_income_num);
        tv_act_more_income_num.setText(bean.getAct_sign_income());
        tv_act_more_see_num = (TextView) findViewById(R.id.tv_act_more_see_num);
        tv_act_more_see_num.setText(bean.getClick_num());


        gridView = (GridView) findViewById(R.id.gridview);
        adapter = new ActionMoreAdapter(this);
        gridView.setAdapter(adapter);
        List<ActionMoreBean> list = new ArrayList<ActionMoreBean>();
        for (int i = 0, len = gridImages.length; i < len; i++) {
            String s = gridStrings[i] > 0 ? getString(gridStrings[i]) : "";
            ActionMoreBean bean = new ActionMoreBean(gridImages[i], s);
            list.add(bean);
        }
        adapter.appendToList(list);
        gridView.setOnItemClickListener(this);
        mShareListener = new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {
                //分享开始的回调
            }
            @Override
            public void onResult(SHARE_MEDIA platform) {
                Log.d("plat","platform"+platform);

//                Toast.makeText(getApplicationContext(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
//                Toast.makeText(getApplicationContext(),platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                if(t!=null){
                    Log.d("throw","throw:"+t.getMessage());
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
//                Toast.makeText(getApplicationContext(),platform + " 分享取消了", Toast.LENGTH_SHORT).show();
            }
        };


    }

    private void initWx() {
        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID, true);
        api.registerApp(Constant.APP_ID);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case POSITION_EDIT:
                Intent intent = new Intent(this, EditActWebActivity.class);

//        Intent intent = new Intent(this, EditActActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("act_id", bean.getAct_id());
                intent.putExtras(bundle);
                this.startActivity(intent);
                break;
            case POSITION_LIST:
                SPUtils.put(this,"act_title",bean.getAct_title());
                SPUtils.put(this,"act_time",bean.getAct_starttime());
                SPUtils.put(this,"act_add",bean.getAddress());
                SPUtils.put(this,"act_url",bean.getUrl());
                if (SPUtils.contains(this, "listup_" + bean.getAct_id()) == true) {
                    intent = new
                            Intent(this, SignListActivity.class);
                    //在Intent对象当中添加一个键值对
                    intent.putExtra("act_id", bean.getAct_id());
                    intent.putExtra("UserInfo",bean.getBaseItem());
                    intent.putExtra("ActivityFees",bean.getActivityFees());
                    startActivity(intent);

                } else if (NetworkUtils.checkNetState(this)) {
                    if (bean.getAct_id() == null) {
                        bean.setAct_id((String) SPUtils.get(this, "Act_id_now", ""));
                    }
                    String mParam = "ActivityID=" + bean.getAct_id() + "&pageSize=" + PAGE_SIZE + "&position=1";
                    getSignList(mParam);
                } else {
                    ToastUtil.makeText(this, "请联网后再试");
                    return;
                }
                break;
            case POSITION_CONSULT:
                intent = new
                        Intent(this, ActionConsultActivity.class);
                //在Intent对象当中添加一个键值对
                intent.putExtra("act_id", bean.getAct_id());
                startActivity(intent);                break;
            case POSITION_URL:
                ClipboardManager cmb = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);

                cmb.setText("https://m.zhundao.net/event/" + bean.getAct_id());
                ToastUtil.makeText(this, "复制成功！");
                break;
            case POSITION_STOP:
                undueDialog();
                break;
            case POSITION_DELETE:
                deleteDialog();

                break;
            case POSITION_SHARE:
                showDialog(bean);

                break;
            case POSITION_INVITATION:
//                actionInvitation();
//                invitationDialogLocal();
                intent = new Intent();
                intent.setClass(this, InvitationActivity.class);
//                intent.setClass(this, InvitationUserActivity.class);

                bundle = new Bundle();
                bundle.putSerializable("bean", bean);
                intent.putExtras(bundle);
                this.startActivity(intent);
                break;
            case POSITION_QRCODE:
                QrCodeDialog();
                break;

            case POSITION_SIGN_ADD:
                intent = new Intent(this, ActionSignAddActivity.class);
                bundle = new Bundle();
                bundle.putString("act_id", bean.getAct_id());
                bundle.putString("act_title", bean.getAct_title());
                intent.putExtras(bundle);
                this.startActivity(intent);
                break;
            case POSITION_SIGNUP:
               intent = new Intent(this, ActionSignActivity.class);
                  bundle = new Bundle();
                bundle.putString("act_id", bean.getAct_id());
                bundle.putString("act_title", bean.getAct_title());
                intent.putExtras(bundle);
                if (SPUtils.contains(this,"sign_result")==false) {
                    if (NetworkUtils.checkNetState(this)==false) {
                        ToastUtil.makeText(this, R.string.net_error);
                        return;
                    }
                }
                this.startActivity(intent);
                break;


        }
    }

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {

                    case MESSAGE_GET_SIGNLIST:
                        String result2 = (String) msg.obj;
                        System.out.println("SignupList result:  " + result2);
                        if (NetworkUtils.checkNetState(getApplicationContext())) {
                        }
                        gotoSignList(result2);
                        break;
                    case MESSAGE_DELETE_ACTION:
                        String result = (String) msg.obj;
                        if (NetworkUtils.checkNetState(getApplicationContext())) {
                        }
                        JSONObject jsonObj = JSON.parseObject(result);
                        String message = jsonObj.getString("Res");
                        if (message.equals("0"))
                        //添加或修改请求结果
                        {
                            SPUtils.put(getApplicationContext(), "tab_now", 0);
                            IntentUtils.startActivity(ActionMoreActivity.this, HomeActivity.class);
                            ToastUtil.makeText(getApplicationContext(), "删除成功！");
                        } else {
                            ToastUtil.makeText(getApplicationContext(), jsonObj.getString("Msg"));

                        }
                        break;
                    case MESSAGE_UNDUE_ACTION:
                        String result3 = (String) msg.obj;
                        if (NetworkUtils.checkNetState(getApplicationContext())) {
                        }
                        JSONObject jsonObj3 = JSON.parseObject(result3);
                        String message3 = jsonObj3.getString("Res");
                        if (message3.equals("0"))
                        //添加或修改请求结果
                        {
                            ToastUtil.makeText(getApplicationContext(), "报名已截止！");
                        }

                        break;
                    case MESSAGE_INVITATION_ACTION:
                        String result4 = (String) msg.obj;
                        invitationDialog(result4);


                        break;

                    default:
                        break;
                }
            }
        };
    }

    private void getSignList(String act_id) {
        Dialog dialog = ProgressDialogUtils.showProgressDialog(this, getString(R.string.progress_title), getString(R.string.progress_message));
        AsyncSignList asyncSignList = new AsyncSignList(this, mHandler, dialog, MESSAGE_GET_SIGNLIST, act_id);
        asyncSignList.execute();
    }

    private void gotoSignList(String result) {
        Intent intent = new
                Intent(this, SignListActivity.class);
        JSONObject jsonObj = JSON.parseObject(result);
        if (result == null) {
            return;
        }
        if (jsonObj.getByte("Count") == 0) {
            ToastUtil.makeText(this, "暂无人报名");
            return;
        } else {
            JSONArray jsonArray = jsonObj.getJSONArray("Data");
            String act_id = jsonArray.getJSONObject(0).getString("ActivityID");
            SPUtils.put(this, "listup_" + act_id, result);
            //在Intent对象当中添加一个键值对
            intent.putExtra("UserInfo",bean.getBaseItem());
            intent.putExtra("ActivityFees",bean.getActivityFees());
            intent.putExtra("act_id", act_id);
            startActivity(intent);
        }
    }
    private void UmengShare(ActionBean bean,SHARE_MEDIA type ) {
        UMWeb web = new UMWeb("https://"+ShareUrl+bean.getAct_id());
        UMImage image = new UMImage(this, bean.getUrl());
        web.setTitle( bean.getAct_title());//标题
        web.setDescription(bean.getAct_starttime()+"\n活动地点： "+bean.getAddress());//描述
        web.setThumb(image);
//        new ShareAction(getActivity())
//                .withMedia(web)
//                .setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.QZONE)
//                .setCallback(mShareListener).open();
        new ShareAction(this).setPlatform(type)
                .withMedia(web)
                .setCallback(mShareListener)
                .share();
    }

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
                                        UmengShare(bean,SHARE_MEDIA.QQ);
                                        break;
                                    case R.id.iv_share_qqzone_solid:
                                        UmengShare(bean,SHARE_MEDIA.QZONE);

                                        break;
                                }
                            }
                        });
                        ImageView iv_share_wechat_solid = (ImageView) v.findViewById(R.id.iv_share_wechat_solid);
                        ImageView iv_share_weixin_friends_solid = (ImageView) v.findViewById(R.id.iv_share_weixin_friends_solid);
                        ImageView iv_share_weibo_solid= (ImageView) v.findViewById(R.id.iv_share_weibo_solid);
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



    private void actionDelete() {
        if (NetworkUtils.checkNetState(this)) {
            AsyncActionDelete asyncActivity = new AsyncActionDelete(this, mHandler, MESSAGE_DELETE_ACTION, bean.getAct_id());
            asyncActivity.execute();
        } else {
            ToastUtil.makeText(this, R.string.net_error);
        }

    }

    private void actionInvitation() {
        if (NetworkUtils.checkNetState(this)) {
            AsyncActionInvitation asyncActivity = new AsyncActionInvitation(this, mHandler, MESSAGE_INVITATION_ACTION, bean.getAct_id());
            asyncActivity.execute();
        } else {
            ToastUtil.makeText(this, R.string.net_error);
        }

    }

    private void actionUnDue() {
        if (NetworkUtils.checkNetState(this)) {
            AsyncActionUnDue asyncActivity = new AsyncActionUnDue(this, mHandler, MESSAGE_UNDUE_ACTION, bean.getAct_id());
            asyncActivity.execute();
        } else {
            ToastUtil.makeText(this, R.string.net_error);
        }

    }

    public void deleteDialog() {

        //LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化

        new AlertDialog.Builder(this)
                //对话框的标题
                .setTitle("确认要删除活动？")
                //设定显示的View
                //对话框中的“登陆”按钮的点击事件
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        actionDelete();

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

    public void undueDialog() {

        //LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化

        new AlertDialog.Builder(this)
                //对话框的标题
                .setTitle("确认要截止活动？")
                //设定显示的View
                //对话框中的“登陆”按钮的点击事件
                .setMessage("确定后，如要再次开启报名\n请通过编辑活动修改报名截止时间。")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        actionUnDue();
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

    public void QrCodeDialog() {

        //LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化
        LayoutInflater factory = LayoutInflater.from(this);
        //把activity_login中的控件定义在View中
        final View v = factory.inflate(R.layout.dialog_qrcode, null);
        ImageView iv_dialog_qrcode;
        iv_dialog_qrcode = (ImageView) v.findViewById(R.id.iv_dialog_qrcode);
        bitmap = createQrBitmap("https://m.zhundao.net/event/" + bean.getAct_id(), 500, 500);
        iv_dialog_qrcode.setImageBitmap(bitmap);
        ;
        new AlertDialog.Builder(this)
                //对话框的标题
                .setTitle(bean.getAct_title() + "二维码")
                .setView(v)
                //设定显示的View
                //对话框中的“登陆”按钮的点击事件
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        AndPermission.with(getApplicationContext())
                                .requestCode(REQUEST_CODE_PERMISSION)
                                .permission(Permission.STORAGE)
                                .callback(permissionListener)
                                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                                // 这样避免用户勾选不再提示，导致以后无法申请权限。
                                // 你也可以不设置。
                                .rationale(null)
                                .start();
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

    public void invitationDialog(final String result) {

        //LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化
        LayoutInflater factory = LayoutInflater.from(this);
        //把activity_login中的控件定义在View中
        final View v = factory.inflate(R.layout.dialog_invitation, null);
        ImageView iv_dialog_invitation;
        iv_dialog_invitation = (ImageView) v.findViewById(R.id.iv_dialog_invitation);
        for(int i=0;i<100;i++){
        Picasso.with(this).load(result).error(R.mipmap.ic_launcher).into(iv_dialog_invitation);

        Picasso.with(getApplicationContext())
                .load(result)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                        ZXingUtil.saveImageToGallery(getApplicationContext(), bitmap, bean.getAct_title()+j);
                        j++;
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });}
        ToastUtil.makeText(getApplicationContext(), "保存成功！");

//        new AlertDialog.Builder(this)
//                //对话框的标题
//                .setTitle(bean.getAct_title() + "邀请函")
//                .setView(v)
//                //设定显示的View
//                //对话框中的“登陆”按钮的点击事件
//                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
////                     由于用了帕斯卡的xml加载图片，imageview转换bitmap会失效，所以使用帕斯卡自带的方法
//                        Picasso.with(getApplicationContext())
//                                .load(result)
//                                .into(new Target() {
//                                    @Override
//                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                                        ZXingUtil.saveImageToGallery(getApplicationContext(), bitmap, bean.getAct_title());
//                                        ToastUtil.makeText(getApplicationContext(), "保存成功！");
//                                    }
//
//                                    @Override
//                                    public void onBitmapFailed(Drawable errorDrawable) {
//                                    }
//
//                                    @Override
//                                    public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                                    }
//                                });
//
//                    }
//
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//
//
//
//                // 设置dialog是否为模态，false表示模态，true表示非模态
//                .setCancelable(true)
//                //对话框的创建、显示
//                .create().show();

    }
    public void invitationDialogLocal() {

        //LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化
        LayoutInflater factory = LayoutInflater.from(this);
        //把activity_login中的控件定义在View中
        final View v = factory.inflate(R.layout.dialog_invitation_local, null);
        ImageView iv_vcode_invitation;
        iv_vcode_invitation = (ImageView) v.findViewById(R.id.iv_vcode_invitation);
        final Bitmap bitmap = createQrBitmap("https://m.zhundao.net/event/" + bean.getAct_id(), 500, 500);
        iv_vcode_invitation.setImageBitmap(bitmap);
TextView title= (TextView) v.findViewById(R.id.tv_dialog_title);
        title.setText(bean.getAct_title());
        new AlertDialog.Builder(this)
                //对话框的标题
                .setTitle(bean.getAct_title() + "邀请函")
                .setView(v)
                //设定显示的View
                //对话框中的“登陆”按钮的点击事件
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                                        ZXingUtil.saveImageToGallery(getApplicationContext(), ZXingUtil.convertViewToBitmap(v), bean.getAct_title());
                                        ToastUtil.makeText(getApplicationContext(), "保存成功！");


                    }

                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
//                .setNeutralButton("分享", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })


                // 设置dialog是否为模态，false表示模态，true表示非模态
                .setCancelable(true)
                //对话框的创建、显示
                .create().show();

    }

    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {
                case REQUEST_CODE_PERMISSION: {
                    ZXingUtil.saveImageToGallery(getApplicationContext(), bitmap, bean.getAct_title());
                    ToastUtil.makeText(getApplicationContext(), "保存成功！");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SETTING: {
                Toast.makeText(this, R.string.message_setting_back, Toast.LENGTH_LONG).show();
                break;
            }
        }
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.rl_act_more_details:
                Intent intent = new Intent(this, ActionDetailsActivity.class);

//        Intent intent = new Intent(getActivity(), EditActActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("act_id", bean.getAct_id());
                bundle.putString("act_title", bean.getAct_title());
                bundle.putSerializable("bean", bean);

                intent.putExtras(bundle);

                this.startActivity(intent);
                break;
        }
    }
}
