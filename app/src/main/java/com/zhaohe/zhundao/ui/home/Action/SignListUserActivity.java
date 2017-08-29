package com.zhaohe.zhundao.ui.home.action;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.GpCom;
import com.gprinter.command.GpUtils;
import com.gprinter.command.LabelCommand;
import com.gprinter.service.GpPrintService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.zhaohe.app.camera.PreviewImgActivity;
import com.zhaohe.app.utils.ProgressDialogUtils;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.app.utils.ZXingUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.asynctask.AsyncPayOffLine;
import com.zhaohe.zhundao.asynctask.action.AsyncSignListPeopleDelete;
import com.zhaohe.zhundao.ui.ToolBarActivity;
import com.zhaohe.zhundao.ui.ToolBarHelper;

import java.util.LinkedHashMap;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhaohe.app.utils.DensityUtil.dip2px;
import static com.zhaohe.zhundao.R.id.tv_sign_user_phone;
import static com.zhaohe.zhundao.gprinter.ListViewAdapter.DEBUG_TAG;


public class SignListUserActivity extends ToolBarActivity implements View.OnClickListener, Toolbar.OnMenuItemClickListener {
    @BindView(R.id.btn_print)
    Button btnPrint;
    private TextView tv_name, tv_phone, tv_unit, tv_sex, tv_dep, tv_industry, tv_duty,
            tv_id_card, tv_email, tv_join_num, tv_add, tv_remark, tv_amount, tv_title;
    private RelativeLayout rl_name, rl_phone, rl_sex, rl_unit, rl_dep, rl_industry, rl_duty, rl_id_card, rl_email, rl_join_num, rl_add, rl_remark, rl_amount;
    Toolbar toolbar;
    private LinearLayout ll_sign_list_user;
    private String signup_list;
    private String act_id, ID;
    private JSONObject jsonObj;
    private JSONArray jsonArray;
    private String phone, name, AdminRemark;
    private static int id = R.id.tv_code_img;
    private String url, text;//当前要保存的图片的url和标题
    private Handler mHandler;
    private GpService mGpService;
    private PrinterServiceConnection conn = null;
    private static final int MAIN_QUERY_PRINTER_STATUS = 0xfe;
    private static final int REQUEST_PRINT_LABEL = 0xfd;
    private static final int REQUEST_PRINT_RECEIPT = 0xfc;
    private int mPrinterIndex = 0;
    private int mTotalCopies = 0;
    public static final int MESSAGE_SEND_SIGNLIST_DELETE = 94;
    public static final int MESSAGE_SIGNLIST_PAY_OFF = 95;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_list_user);
        initToolBar("用户个人信息", R.layout.activity_sign_list_user);
        ButterKnife.bind(this);
        initView();
        init();

        initHandler();
        connection();
        registerReceriver();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu_signlist_user, menu);
        toolbar.setOnMenuItemClickListener(this);

        return true;
    }
    private void connection() {
        conn = new PrinterServiceConnection();
        Log.i(DEBUG_TAG, "connection");
        Intent intent = new Intent(this, GpPrintService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService
    }
    private void initToolBar(String text, int layoutResID) {
        ToolBarHelper mToolBarHelper;
        mToolBarHelper = new ToolBarHelper(this, layoutResID);
        mToolBarHelper.setTvTitle(text);
        super.setTitle("");
        setContentView(mToolBarHelper.getContentView());
        toolbar = mToolBarHelper.getToolBar();
  /*把 toolbar 设置到Activity 中*/
        setSupportActionBar(toolbar);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
            mBroadcastReceiver = null;
        }
    }


    private void initView() {
        ll_sign_list_user = (LinearLayout) findViewById(R.id.ll_sign_list_user);
//        TextView tv=new TextView(this);
//        tv.setText("ok");
//        ll_sign_list_user.addView(tv);
        tv_name = (TextView) findViewById(R.id.tv_sign_user_name);
        tv_phone = (TextView) findViewById(tv_sign_user_phone);
        tv_unit = (TextView) findViewById(R.id.tv_sign_user_unit);
        tv_industry = (TextView) findViewById(R.id.tv_sign_user_industry);
        tv_dep = (TextView) findViewById(R.id.tv_sign_user_dep);
        tv_sex = (TextView) findViewById(R.id.tv_sign_user_sex);
        tv_duty = (TextView) findViewById(R.id.tv_sign_user_duty);
        tv_id_card = (TextView) findViewById(R.id.tv_sign_user_id_card);
        tv_email = (TextView) findViewById(R.id.tv_sign_user_email);
        tv_join_num = (TextView) findViewById(R.id.tv_sign_user_join_num);
        tv_add = (TextView) findViewById(R.id.tv_sign_user_add);
        tv_remark = (TextView) findViewById(R.id.tv_sign_user_remark);
        tv_amount = (TextView) findViewById(R.id.tv_sign_user_amount);
        tv_title = (TextView) findViewById(R.id.tv_sign_user_title);
        rl_name = (RelativeLayout) findViewById(R.id.rl_name);
        rl_phone = (RelativeLayout) findViewById(R.id.rl_phone);
        rl_phone.setOnClickListener(this);
        rl_unit = (RelativeLayout) findViewById(R.id.rl_unit);
        rl_industry = (RelativeLayout) findViewById(R.id.rl_industry);
        rl_dep = (RelativeLayout) findViewById(R.id.rl_dep);
        rl_sex = (RelativeLayout) findViewById(R.id.rl_sex);
        rl_duty = (RelativeLayout) findViewById(R.id.rl_duty);
        rl_id_card = (RelativeLayout) findViewById(R.id.rl_id_card);
        rl_email = (RelativeLayout) findViewById(R.id.rl_email);
        rl_join_num = (RelativeLayout) findViewById(R.id.rl_join_num);
        rl_add = (RelativeLayout) findViewById(R.id.rl_add);
        rl_remark = (RelativeLayout) findViewById(R.id.rl_remark);
        rl_amount = (RelativeLayout) findViewById(R.id.rl_amount);
        if ((boolean)(SPUtils.get(this, "is_print", false)) == false) {
            btnPrint.setVisibility(View.GONE);
        }
    }

    private void init() {


        Intent intent = getIntent();
        //从Intent当中根据key取得value
        if (intent != null) {
            act_id = intent.getStringExtra("act_id");
            signup_list = (String) SPUtils.get(this, "listup_" + act_id, "");
            ID = intent.getStringExtra("id");


        }

        jsonObj = JSON.parseObject(signup_list);
        jsonArray = jsonObj.getJSONArray("Data");
        if (intent.getStringExtra("unit") == null) {
            rl_unit.setVisibility(View.GONE);
        }
        if (intent.getStringExtra("sex").equals("0")) {
            rl_sex.setVisibility(View.GONE);
        }
        if (intent.getStringExtra("dep") == null) {
            rl_dep.setVisibility(View.GONE);
        }
        if (intent.getStringExtra("industry") == null) {
            rl_industry.setVisibility(View.GONE);
        }
        if (intent.getStringExtra("duty") == null) {
            rl_duty.setVisibility(View.GONE);
        }
        if (intent.getStringExtra("email") == null) {
            rl_email.setVisibility(View.GONE);
        }
        if (intent.getStringExtra("id_card") == null || intent.getStringExtra("id_card").equals("")) {
            rl_id_card.setVisibility(View.GONE);
        }
        if (intent.getStringExtra("join_num").equals("0")) {
            rl_join_num.setVisibility(View.GONE);
        }
        if (intent.getStringExtra("add") == null) {
            rl_add.setVisibility(View.GONE);
        }
        if (intent.getStringExtra("remark") == null) {
            rl_remark.setVisibility(View.GONE);
        }
        if (intent.getStringExtra("title") == null) {
            rl_amount.setVisibility(View.GONE);
        }
        String FaceImg = intent.getStringExtra("face_img");
        if (FaceImg != null) {
            String[] imgurl = FaceImg.split("\\|");

            insertRL("人脸照片", imgurl);
        }
        tv_name.setText(intent.getStringExtra("name"));
        tv_phone.setText(intent.getStringExtra("phone"));
        phone = intent.getStringExtra("phone");
        tv_unit.setText(intent.getStringExtra("unit"));
        tv_dep.setText(intent.getStringExtra("dep"));
        tv_industry.setText(intent.getStringExtra("industry"));
        tv_duty.setText(intent.getStringExtra("duty"));
        tv_email.setText(intent.getStringExtra("email"));
        if (intent.getStringExtra("sex").equals("1")) {
            tv_sex.setText("男");
        }
        if (intent.getStringExtra("sex").equals("2")) {
            tv_sex.setText("女");
        }
        tv_id_card.setText(intent.getStringExtra("id_card"));
        tv_join_num.setText(intent.getStringExtra("join_num"));
        tv_add.setText(intent.getStringExtra("add"));
        tv_remark.setText(intent.getStringExtra("remark"));
        tv_title.setText(intent.getStringExtra("title"));
        tv_amount.setText("￥" + intent.getStringExtra("amount"));
        JSONArray jsonArray3 = jsonObj.getJSONArray("Option");
        String[] type = null;
        type = new String[100];
        for (int j = 0; j < jsonArray3.size(); j++) {
            System.out.println("input type" + jsonArray3.getJSONObject(j).getString("InputType"));
            String InputType = jsonArray3.getJSONObject(j).getString("InputType");
            type[j] = InputType;
            System.out.println("input type" + type[j]);
        }
        int i = 0;
        String extra = intent.getStringExtra("extra");
        if (extra != null) {
            LinkedHashMap<String, String> jsonMap = JSON.parseObject(extra, new TypeReference<LinkedHashMap<String, String>>() {
            });
            for (LinkedHashMap.Entry<String, String> entry : jsonMap.entrySet()) {
                System.out.println(entry.getKey() + ":" + entry.getValue());
                //                System.out.println(entry.getKey() + ":" + entry.getValue());
                String value = entry.getValue();
//                截取第一个key值字母
//                截取非空
                if (entry.getKey() == null) {
                    insertTextView("无", (String) entry.getValue());
                    return;
                }
//                String s = value.substring(0,1);
                int isphoto = value.indexOf("http");
//            int isWrong=value.indexOf("https://joinheadoss.oss-cn-hangzhou.aliyuncs.com/zhundao");
////                判断是否是图片
//            if (isWrong!=-1){
//                insertLongTextView(entry.getKey(), (String) entry.getValue());
//return;
//            }
                if (isphoto != -1) {
//                    insertImageView(entry.getKey(), (String) entry.getValue());
                    String url = (String) entry.getValue();
                    String[] imgurl = url.split("\\|");
                    insertRL(entry.getKey(), imgurl);

                } else if (value.length() >= 20) {
                    insertLongTextView(entry.getKey(), (String) entry.getValue());
                } else {
                    insertTextView(entry.getKey(), (String) entry.getValue());
                }
                i++;
            }
        }
    }


    public void insertTextView(String text1, String text2) {
        int margin = dip2px(this, 10);
        int h = dip2px(this, 1);
        RelativeLayout rl = new RelativeLayout(this);
        rl.setPadding(margin, margin, margin, margin);
        TextView tv1 = new TextView(this);
        tv1.setText(text1);
        tv1.setId(R.id.tv_code_title);
        TextView tv2 = new TextView(this);
        tv2.setTextIsSelectable(true);
        tv2.setText(text2);
        tv2.setId(R.id.tv_code_content);
        RelativeLayout.LayoutParams rlParams =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams tvParams1 =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvParams1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, tv1.getId());

        RelativeLayout.LayoutParams tvParams2 =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvParams2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, tv2.getId());

        rl.addView(tv1, tvParams1);
        rl.addView(tv2, tvParams2);
        ll_sign_list_user.addView(rl, rlParams);
        View view = new View(this);
        view.setBackgroundColor(getResources().getColor(R.color.line_gray));
        RelativeLayout.LayoutParams vParams =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        h);
        ll_sign_list_user.addView(view, vParams);
    }


    public void insertLongTextView(String text1, String text2) {
        int margin = dip2px(this, 10);
        int h = dip2px(this, 1);
        RelativeLayout rl = new RelativeLayout(this);
        rl.setPadding(margin, margin, margin, margin);
        TextView tv1 = new TextView(this);
        tv1.setText(text1);
        tv1.setId(R.id.tv_code_title);
        TextView tv2 = new TextView(this);
        tv2.setTextIsSelectable(true);
        tv2.setText(text2);
        tv2.setId(R.id.tv_code_content);
        RelativeLayout.LayoutParams rlParams =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams tvParams1 =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvParams1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, tv1.getId());

        RelativeLayout.LayoutParams tvParams2 =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvParams2.addRule(RelativeLayout.BELOW, tv1.getId());

        rl.addView(tv1, tvParams1);
        rl.addView(tv2, tvParams2);
        ll_sign_list_user.addView(rl, rlParams);
        View view = new View(this);
        view.setBackgroundColor(getResources().getColor(R.color.line_gray));
        RelativeLayout.LayoutParams vParams =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        h);
        ll_sign_list_user.addView(view, vParams);
    }


    //    插入多图片数组
    public void insertRL(final String text1, final String[] imgurl) {
//        10dp的默认margin
        int margin = dip2px(this, 10);
        int h = dip2px(this, 1);
        int h1 = dip2px(this, 100);
        int h2 = dip2px(this, 30);
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
//        获取当前屏幕的宽度
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
//        计算一个图片所占的宽度
        int left = (width - 4 * margin) / 3;

//图片的父控件相对布rl
        RelativeLayout rl = new RelativeLayout(this);
        rl.setPadding(margin, margin, margin, margin);
//        图片的标题
        TextView tv1 = new TextView(this);
        tv1.setText(text1);
        tv1.setId(R.id.tv_code_title);
        RelativeLayout.LayoutParams rlParams =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams tvParams1 =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvParams1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, tv1.getId());
        rl.addView(tv1, tvParams1);
//for循环插入不同位置的图片
        for (int j = 0; j < imgurl.length; j++) {
            String newimgurl = imgurl[j].replace("800", "200");
            final int num = j;
//            计算图片在第几层
            int x = (int) Math.floor(j / 3);
//            计算图片在第几个位置
            int y = j % 3;
            ImageView img = new ImageView(this);
            img.setId(R.id.tv_code_img + j);
            Picasso.with(this).load(newimgurl).error(R.mipmap.ic_launcher).into(img);
            RelativeLayout.LayoutParams imgParams1 =
                    new RelativeLayout.LayoutParams(
                            h1, h1);
//            img.setScaleType(ImageView.ScaleType.FIT_START);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);

            imgParams1.addRule(RelativeLayout.BELOW, tv1.getId());
//            设置图片相对父控件的位置
            imgParams1.leftMargin = (y + 1) * margin + y * left;
            imgParams1.topMargin = (x + 2) * margin + x * left;
            //注册上下文菜单
            registerForContextMenu(img);
//            将图片设置到RL父控件中去
            rl.addView(img, imgParams1);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra("imgpath", imgurl[num]);
                    intent.setClass(getApplication(), PreviewImgActivity.class);
                    startActivity(intent);
                }
            });

            img.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
//                    savePhoto(imgurl[num], text1);

                    url = imgurl[num];
                    text = text1;
                    return false;
                }
            });

        }
//        把RL父控件加到总控件ll中去
        ll_sign_list_user.addView(rl, rlParams);
        View view = new View(this);
        view.setBackgroundColor(getResources().getColor(R.color.line_gray));
        RelativeLayout.LayoutParams vParams =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        h);
        ll_sign_list_user.addView(view, vParams);
    }

    private void savePhoto(String path, final String text1) {
        //                     由于用了帕斯卡的xml加载图片，imageview转换bitmap会失效，所以使用帕斯卡自带的方法
        Picasso.with(getApplicationContext())
                .load(path)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        ZXingUtil.saveImageToGallery(getApplicationContext(), bitmap, text1);
                        ToastUtil.makeText(getApplicationContext(), "保存成功！");
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_phone:
                if (phone.equals("") && phone.equals(null)) {
                    ToastUtil.makeText(this, "号码不得为空");
                } else {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, 1, 0, "保存图片");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                savePhoto(url, text);
                break;

        }
        return true;
    }

    private void SignListDelete(String id) {
        Dialog dialog = ProgressDialogUtils.showProgressDialog(this, getString(R.string.progress_title), getString(R.string.progress_message));
        AsyncSignListPeopleDelete getCode = new AsyncSignListPeopleDelete(this, mHandler, dialog, MESSAGE_SEND_SIGNLIST_DELETE, id);
        getCode.execute();

    }

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {

                    case MESSAGE_SEND_SIGNLIST_DELETE:
                        String result = (String) msg.obj;
                        JSONObject jsonObj = JSON.parseObject(result);
                        String message = jsonObj.getString("Res");
                        if (message.equals("0")) {
                            ToastUtil.makeText(getApplicationContext(), "删除成功！");
                            finish();
                        } else {
                            ToastUtil.makeText(getApplicationContext(), jsonObj.getString("Msg"));

                        }


                        break;
                    case MESSAGE_SIGNLIST_PAY_OFF:
                        result = (String) msg.obj;
                    jsonObj = JSON.parseObject(result);
                        message = jsonObj.getString("Res");
                        if (message.equals("0")) {
                            ToastUtil.makeText(getApplicationContext(), "修改成功！");
                            finish();
                        } else {
                            ToastUtil.makeText(getApplicationContext(), jsonObj.getString("Msg"));

                        }

                    default:
                        break;
                }
            }
        };
    }

    public void deleteDialog() {

        //LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化

        new AlertDialog.Builder(this)
                //对话框的标题
                .setTitle("确认要删除报名人员吗？")
                //设定显示的View
                //对话框中的“登陆”按钮的点击事件
                .setMessage("删除后将导致用户二维码凭证失效，如果有签到记录也将被删除，是否继续？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        SignListDelete(ID);


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

    public void payoffDialog() {

        //LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化

        new AlertDialog.Builder(this)
                //对话框的标题
                .setTitle("确认转为线下支付吗？")
                //设定显示的View
                //对话框中的“登陆”按钮的点击事件
                .setMessage("修改后，用户直接报名成功？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
payoff(ID);

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
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_signlist_user_delete:
                deleteDialog();
                break;
            case R.id.menu_signlist_user_edit:
                Intent intent = getIntent();
                intent.setClass(this, SignListUserEditActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.menu_signlist_user_payoff:
                payoffDialog();
                break;

        }

        return false;
    }
    public void payoff(String param) {

        AsyncPayOffLine async = new AsyncPayOffLine(this, mHandler, MESSAGE_SIGNLIST_PAY_OFF, ID);
        async.execute();

    }

    @OnClick(R.id.btn_print)
    public void onViewClicked() {
        print();

    }
    private void print() {
        try {

            mGpService.queryPrinterStatus(0, 1000, REQUEST_PRINT_LABEL);

        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }

    private void registerReceriver() {
        registerReceiver(mBroadcastReceiver, new IntentFilter(GpCom.ACTION_DEVICE_REAL_STATUS));
        /**
         * 票据模式下，可注册该广播，在需要打印内容的最后加入addQueryPrinterStatus()，在打印完成后会接收到
         * action为GpCom.ACTION_DEVICE_STATUS的广播，特别用于连续打印，
         * 可参照该sample中的sendReceiptWithResponse方法与广播中的处理
         **/
        registerReceiver(mBroadcastReceiver, new IntentFilter(GpCom.ACTION_RECEIPT_RESPONSE));
        /**
         * 标签模式下，可注册该广播，在需要打印内容的最后加入addQueryPrinterStatus(RESPONSE_MODE mode)
         * ，在打印完成后会接收到，action为GpCom.ACTION_LABEL_RESPONSE的广播，特别用于连续打印，
         * 可参照该sample中的sendLabelWithResponse方法与广播中的处理
         **/
        registerReceiver(mBroadcastReceiver, new IntentFilter(GpCom.ACTION_LABEL_RESPONSE));
    }
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("TAG", action);
            // GpCom.ACTION_DEVICE_REAL_STATUS 为广播的IntentFilter
            if (action.equals(GpCom.ACTION_DEVICE_REAL_STATUS)) {

                // 业务逻辑的请求码，对应哪里查询做什么操作
                int requestCode = intent.getIntExtra(GpCom.EXTRA_PRINTER_REQUEST_CODE, -1);
                // 判断请求码，是则进行业务操作
                if (requestCode == MAIN_QUERY_PRINTER_STATUS) {

                    int status = intent.getIntExtra(GpCom.EXTRA_PRINTER_REAL_STATUS, 16);
                    String str;
                    if (status == GpCom.STATE_NO_ERR) {
                        str = "打印机正常";
                    } else {
                        str = "打印机 ";
                        if ((byte) (status & GpCom.STATE_OFFLINE) > 0) {
                            str += "脱机";
                        }
                        if ((byte) (status & GpCom.STATE_PAPER_ERR) > 0) {
                            str += "缺纸";
                        }
                        if ((byte) (status & GpCom.STATE_COVER_OPEN) > 0) {
                            str += "打印机开盖";
                        }
                        if ((byte) (status & GpCom.STATE_ERR_OCCURS) > 0) {
                            str += "打印机出错";
                        }
                        if ((byte) (status & GpCom.STATE_TIMES_OUT) > 0) {
                            str += "查询超时";
                        }
                    }

                    Toast.makeText(getApplicationContext(), "打印机：" + mPrinterIndex + " 状态：" + str, Toast.LENGTH_SHORT)
                            .show();
                } else if (requestCode == REQUEST_PRINT_LABEL) {
                    int status = intent.getIntExtra(GpCom.EXTRA_PRINTER_REAL_STATUS, 16);
                    if (status == GpCom.STATE_NO_ERR) {
                        switch (SPUtils.get(getApplicationContext(),"model",0)+""){
                            case "0":
                                sendLabelVocde();
                                break;
                            case "1":
                                sendLabelVocdeAndName();
                                break;
                            case "2":
                                sendLabelNameAndRemark();
                                break;

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "query printer status error", Toast.LENGTH_SHORT).show();
                    }
                } else if (requestCode == REQUEST_PRINT_RECEIPT) {
                    int status = intent.getIntExtra(GpCom.EXTRA_PRINTER_REAL_STATUS, 16);
                    if (status == GpCom.STATE_NO_ERR) {
//                        sendReceipt();
                    } else {
                        Toast.makeText(getApplicationContext(), "query printer status error", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (action.equals(GpCom.ACTION_RECEIPT_RESPONSE)) {
                if (--mTotalCopies > 0) {
//                    sendReceiptWithResponse();
                }
            } else if (action.equals(GpCom.ACTION_LABEL_RESPONSE)) {
                byte[] data = intent.getByteArrayExtra(GpCom.EXTRA_PRINTER_LABEL_RESPONSE);
                int cnt = intent.getIntExtra(GpCom.EXTRA_PRINTER_LABEL_RESPONSE_CNT, 1);
                String d = new String(data, 0, cnt);
                /**
                 * 这里的d的内容根据RESPONSE_MODE去判断返回的内容去判断是否成功，具体可以查看标签编程手册SET
                 * RESPONSE指令
                 * 该sample中实现的是发一张就返回一次,这里返回的是{00,00001}。这里的对应{Status,######,ID}
                 * 所以我们需要取出STATUS
                 */
                Log.d("LABEL RESPONSE", d);

                if (--mTotalCopies > 0 && d.charAt(1) == 0x00) {
//                    sendLabelWithResponse();
                }
            }
        }
    };
    void sendLabelVocdeAndName() {
        LabelCommand tsc = new LabelCommand();
//        tsc.addSize(60, 60); // 设置标签尺寸，按照实际尺寸设置
        tsc.addGap(0); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
        tsc.addReference((Integer) SPUtils.get(this, "x", 0), (Integer) SPUtils.get(this, "y", 0));// 设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
        tsc.addCls();// 清除打印缓冲区


        switch (SPUtils.get(this,"size",0)+""){
            case"0":
                tsc.addQRCode(55, 60, LabelCommand.EEC.LEVEL_L, 4, LabelCommand.ROTATION.ROTATION_0, (String) SPUtils.get(this,"print_Vcode","q298387"));
                tsc.addText(55,160 , LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                        (String) SPUtils.get(this,"print_name","测试"));
                break;
            case "1":
                tsc.addQRCode(35, 40, LabelCommand.EEC.LEVEL_L, 6, LabelCommand.ROTATION.ROTATION_0, (String) SPUtils.get(this,"print_Vcode","q298387"));
                tsc.addText(55,180 , LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                        (String) SPUtils.get(this,"print_name","测试"));
                break;
            case "2":

                tsc.addQRCode(10, 30, LabelCommand.EEC.LEVEL_L, 8, LabelCommand.ROTATION.ROTATION_0, (String) SPUtils.get(this,"print_Vcode","q298387"));
                tsc.addText(55,210 , LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                        (String) SPUtils.get(this,"print_name","测试"));
                break;

        }


//		tsc.addQRCode(17, 20, LabelCommand.EEC.LEVEL_L, 8, LabelCommand.ROTATION.ROTATION_0, " 55555");

//        tsc.addText(20, 30, FONTTYPE.KOREAN, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
//                "조선말");
//        tsc.addText(100, 30, FONTTYPE.SIMPLIFIED_CHINESE, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
//                "简体字");
//        tsc.addText(180, 30, FONTTYPE.TRADITIONAL_CHINESE, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
//                "繁體字");

        // 绘制图片
//        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.gprinter);
//        tsc.addBitmap(20, 60, BITMAP_MODE.OVERWRITE, b.getWidth(), b);
//	    //绘制二维码
//        tsc.addQRCode(105, 75, EEC.LEVEL_L, 5, ROTATION.ROTATION_0, " www.smarnet.cc");
//        // 绘制一维条码
//        tsc.add1DBarcode(50, 350, BARCODETYPE.CODE128, 100, READABEL.EANBEL, ROTATION.ROTATION_0, "SMARNET");
        tsc.addPrint(1, 1); // 打印标签
//		tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        Vector<Byte> datas = tsc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
        try {
            rel = mGpService.sendLabelCommand(mPrinterIndex, str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    void sendLabelNameAndRemark() {
        LabelCommand tsc = new LabelCommand();
//        tsc.addSize(60, 60); // 设置标签尺寸，按照实际尺寸设置
        tsc.addGap(0); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
        tsc.addReference((Integer) SPUtils.get(this, "x", 0), (Integer) SPUtils.get(this, "y", 0));// 设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
        tsc.addCls();// 清除打印缓冲区


        switch (SPUtils.get(this,"size",0)+""){
            case"0":
                tsc.addText(30,40 , LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "姓名："+ (String) SPUtils.get(this,"print_name","测试"));
                tsc.addText(30,100 , LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                        "备注："+(String) SPUtils.get(this,"print_AdminRemark","无"));
                break;
            case "1":
                tsc.addText(20,30 , LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
                        (String) SPUtils.get(this,"print_name","测试"));
                tsc.addText(20,110 , LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
                        (String) SPUtils.get(this,"print_AdminRemark","无"));
                break;
            case "2":

                tsc.addText(10,20 , LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_3, LabelCommand.FONTMUL.MUL_3,
                        (String) SPUtils.get(this,"print_name","测试"));
                tsc.addText(10,130 , LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_3, LabelCommand.FONTMUL.MUL_3,
                        (String) SPUtils.get(this,"print_AdminRemark","无"));
                break;

        }


//		tsc.addQRCode(17, 20, LabelCommand.EEC.LEVEL_L, 8, LabelCommand.ROTATION.ROTATION_0, " 55555");

//        tsc.addText(20, 30, FONTTYPE.KOREAN, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
//                "조선말");
//        tsc.addText(100, 30, FONTTYPE.SIMPLIFIED_CHINESE, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
//                "简体字");
//        tsc.addText(180, 30, FONTTYPE.TRADITIONAL_CHINESE, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
//                "繁體字");

        // 绘制图片
//        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.gprinter);
//        tsc.addBitmap(20, 60, BITMAP_MODE.OVERWRITE, b.getWidth(), b);
//	    //绘制二维码
//        tsc.addQRCode(105, 75, EEC.LEVEL_L, 5, ROTATION.ROTATION_0, " www.smarnet.cc");
//        // 绘制一维条码
//        tsc.add1DBarcode(50, 350, BARCODETYPE.CODE128, 100, READABEL.EANBEL, ROTATION.ROTATION_0, "SMARNET");
        tsc.addPrint(1, 1); // 打印标签
//		tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        Vector<Byte> datas = tsc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
        try {
            rel = mGpService.sendLabelCommand(mPrinterIndex, str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    void sendLabelVocde() {
        LabelCommand tsc = new LabelCommand();
//        tsc.addSize(60, 60); // 设置标签尺寸，按照实际尺寸设置
        tsc.addGap(0); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
        tsc.addReference((Integer) SPUtils.get(this, "x", 0), (Integer) SPUtils.get(this, "y", 0));// 设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
        tsc.addCls();// 清除打印缓冲区
        switch (SPUtils.get(this,"size",0)+""){
            case"0":
                tsc.addQRCode(55, 80, LabelCommand.EEC.LEVEL_L, 4, LabelCommand.ROTATION.ROTATION_0, (String) SPUtils.get(this,"print_Vcode","q298387"));

                break;
            case "1":
                tsc.addQRCode(35, 50, LabelCommand.EEC.LEVEL_L, 6, LabelCommand.ROTATION.ROTATION_0, (String) SPUtils.get(this,"print_Vcode","q298387"));

                break;
            case "2":
                tsc.addQRCode(10, 30, LabelCommand.EEC.LEVEL_L, 8, LabelCommand.ROTATION.ROTATION_0, (String) SPUtils.get(this,"print_Vcode","q298387"));

                break;

        }



//		tsc.addQRCode(17, 20, LabelCommand.EEC.LEVEL_L, 8, LabelCommand.ROTATION.ROTATION_0, " 55555");

//        tsc.addText(20, 30, FONTTYPE.KOREAN, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
//                "조선말");
//        tsc.addText(100, 30, FONTTYPE.SIMPLIFIED_CHINESE, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
//                "简体字");
//        tsc.addText(180, 30, FONTTYPE.TRADITIONAL_CHINESE, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
//                "繁體字");

        // 绘制图片
//        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.gprinter);
//        tsc.addBitmap(20, 60, BITMAP_MODE.OVERWRITE, b.getWidth(), b);
//	    //绘制二维码
//        tsc.addQRCode(105, 75, EEC.LEVEL_L, 5, ROTATION.ROTATION_0, " www.smarnet.cc");
//        // 绘制一维条码
//        tsc.add1DBarcode(50, 350, BARCODETYPE.CODE128, 100, READABEL.EANBEL, ROTATION.ROTATION_0, "SMARNET");
        tsc.addPrint(1, 1); // 打印标签
//		tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        Vector<Byte> datas = tsc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
        try {
            rel = mGpService.sendLabelCommand(mPrinterIndex, str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    class PrinterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {

            Log.i(DEBUG_TAG, "onServiceDisconnected() called");
            mGpService = null;
        }


        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mGpService = GpService.Stub.asInterface(service);
        }
    }
}
