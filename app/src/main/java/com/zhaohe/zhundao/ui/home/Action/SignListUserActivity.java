package com.zhaohe.zhundao.ui.home.action;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.app.utils.ZXingUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.ui.ToolBarActivity;
import com.zhaohe.zhundao.ui.ToolBarHelper;

import java.util.Map;

import static com.zhaohe.app.utils.DensityUtil.dip2px;
import static com.zhaohe.zhundao.R.id.tv_sign_user_phone;

public class SignListUserActivity extends ToolBarActivity implements View.OnClickListener {
    private TextView tv_name, tv_phone, tv_unit, tv_sex, tv_dep, tv_industry, tv_duty,
            tv_id_card, tv_email, tv_join_num, tv_add, tv_remark, tv_amount, tv_title;
    private RelativeLayout rl_name, rl_phone, rl_sex, rl_unit, rl_dep, rl_industry, rl_duty, rl_id_card, rl_email, rl_join_num, rl_add, rl_remark, rl_amount;
    Toolbar toolbar;
    private LinearLayout ll_sign_list_user;
    private String signup_list;
    private String act_id;
    private JSONObject jsonObj;
    private JSONArray jsonArray;
    private String phone;
    private static int id=R.id.tv_code_img;
    private String url,text;//当前要保存的图片的url和标题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_list_user);
        initToolBar("用户个人信息", R.layout.activity_sign_list_user);
        initView();
        init();
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

    }

    private void init() {


        Intent intent = getIntent();
        //从Intent当中根据key取得value
        if (intent != null) {
            act_id = intent.getStringExtra("act_id");
            signup_list = (String) SPUtils.get(this, "listup_" + act_id, "");
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
        if (intent.getStringExtra("id_card") == null) {
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
        JSONObject jsonObject2 = JSON.parseObject(extra);

        if (extra != null) {
            for (Map.Entry<String, Object> entry : jsonObject2.entrySet()) {
                System.out.println(entry.getKey() + ":" + entry.getValue());
                String value = entry.getValue().toString();
//                截取第一个key值字母
//                截取非空
                if (value == null) {
                    insertTextView(entry.getKey(), (String) entry.getValue());
                }
//                String s = value.substring(0,1);
                int isphoto = value.indexOf("http");
//                判断是否是图片
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

    public void insertImageView(String text1, String url) {
        int margin = dip2px(this, 10);
        int h = dip2px(this, 1);
        int h1 = dip2px(this, 100);

        RelativeLayout rl = new RelativeLayout(this);
        rl.setPadding(margin, margin, margin, margin);
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
        ImageView img = new ImageView(this);
        img.setId(R.id.tv_code_img);
        Picasso.with(this).load(url).error(R.mipmap.ic_launcher).into(img);
        RelativeLayout.LayoutParams imgParams1 =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        h1);
        img.setScaleType(ImageView.ScaleType.FIT_START);
        imgParams1.addRule(RelativeLayout.BELOW, tv1.getId());
        imgParams1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, img.getId());
        rl.addView(img, imgParams1);
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
            final int num=j;
//            计算图片在第几层
            int x = (int) Math.floor(j / 3);
//            计算图片在第几个位置
            int y = j % 3;
            ImageView img = new ImageView(this);
            img.setId(R.id.tv_code_img+j);

            Picasso.with(this).load(newimgurl).error(R.mipmap.ic_launcher).into(img);
            RelativeLayout.LayoutParams imgParams1 =
                    new RelativeLayout.LayoutParams(
                            h1, h1);
            img.setScaleType(ImageView.ScaleType.FIT_START);
            imgParams1.addRule(RelativeLayout.BELOW, tv1.getId());
//            设置图片相对父控件的位置
            imgParams1.leftMargin = (y + 1) * margin + y * left;
            imgParams1.topMargin = (x + 2) * margin + x * left;
            //注册上下文菜单
            registerForContextMenu(img);
//            将图片设置到RL父控件中去
            rl.addView(img, imgParams1);
            img.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
//                    savePhoto(imgurl[num], text1);

url=imgurl[num];
                    text=text1;
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
                        ZXingUtil.saveImageToGallery(getApplicationContext(), bitmap,text1);
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

    public void insertImageViewMuti(String text1) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_phone:
                if (phone.equals("") && phone.equals(null)) {
                    ToastUtil.makeText(this, "号码不得为空");
                } else {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
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


}
