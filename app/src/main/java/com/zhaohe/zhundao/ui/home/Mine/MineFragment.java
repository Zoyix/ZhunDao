package com.zhaohe.zhundao.ui.home.mine;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;
import com.zhaohe.app.utils.CircleTransform;
import com.zhaohe.app.utils.IntentUtils;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.asynctask.AsyncGetUserInf;
import com.zhaohe.zhundao.asynctask.AsyncInf;
import com.zhaohe.zhundao.ui.home.mine.contacts.ContactsActivity;
import com.zhaohe.zhundao.ui.home.mine.setting.InfActivity;
import com.zhaohe.zhundao.ui.home.mine.setting.SettingActivity;
import com.zhaohe.zhundao.ui.login.BondPhoneActivity;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2016/11/29 10:23
 */
public class MineFragment extends Fragment implements View.OnClickListener {
    public static final int MESSAGE_GET_INF = 98;
    public static final int MESSAGE_GET_USERINF = 91;

    protected View rootView;
    private ImageView img_head, img_sex;
    private Handler mHandler;
    private AlertDialog dialog;
    private TextView tv_min_setting, tv_min_name, tv_min_wallet, tv_min_feedback, tv_min_phone, tv_min_vip, tv_contacts, tv_my_inf;
    private RelativeLayout view_user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_min,
                null);
        initHandler();
        initView(rootView);
        getList();

    }

    public void onResume() {
        super.onResume();
        getUserInf();
        initUserInfo();
        MobclickAgent.onResume(getActivity());
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }

    private void getUserInf() {

        AsyncGetUserInf userInf = new AsyncGetUserInf(getActivity(), mHandler, MESSAGE_GET_USERINF);
        userInf.execute();


    }


    private void initView(View rootView) {
        img_head = (ImageView) rootView.findViewById(R.id.img_head);
        img_sex = (ImageView) rootView.findViewById(R.id.iv_sex);
        tv_min_name = (TextView) rootView.findViewById(R.id.tv_min_name);
        tv_min_setting = (TextView) rootView.findViewById(R.id.tv_min_setting);
        tv_min_setting.setOnClickListener(this);
        tv_min_wallet = (TextView) rootView.findViewById(R.id.tv_my_wallet);
        tv_min_wallet.setOnClickListener(this);
        tv_min_feedback = (TextView) rootView.findViewById(R.id.tv_feedback);
        tv_min_feedback.setOnClickListener(this);
        tv_min_phone = (TextView) rootView.findViewById(R.id.tv_min_phone);
        tv_min_vip = (TextView) rootView.findViewById(R.id.tv_min_vip);
        tv_min_vip.setOnClickListener(this);
        tv_contacts = (TextView) rootView.findViewById(R.id.tv_contacts);
        tv_contacts.setOnClickListener(this);
        tv_my_inf = (TextView) rootView.findViewById(R.id.tv_my_inf);
        tv_my_inf.setOnClickListener(this);
        view_user = (RelativeLayout) rootView.findViewById(R.id.view_user);
        view_user.setOnClickListener(this);
    }

    private void initUserInfo() {
        //        帕斯卡 加载头像
        String url = (String) SPUtils.get(getActivity(), "HeadImgurl", "");
        String name = (String) SPUtils.get(getActivity(), "NickName", "");
        String phone = (String) SPUtils.get(getActivity(), "Mobile", "");
        String newPhone;
        if (phone.equals("") || phone.equals(null)) {
            newPhone = "绑定手机";
            tv_min_phone.setOnClickListener(this);
        } else {
            newPhone = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        }
        int vip = (int) SPUtils.get(getActivity(), "vip", 0);

//        int sex = (int) SPUtils.get(getActivity(), "Sex", 2);
//        if (sex == 1) {
//            Picasso.with(getActivity()).load(R.drawable.ic_sex_male).error(R.drawable.ic_sex_female).into(img_sex);
//        }
//        if (sex == 2) {
//            Picasso.with(getActivity()).load(R.drawable.ic_sex_male).error(R.drawable.ic_sex_female).into(img_sex);
//        }

//        String url="http://wx.qlogo.cn/mmopen/cdJxMia7edLt0ZywjiaFNQkOH4WXSCiaOkAAfNwaNVCp25IYX3otiaqibNGn8ib4SadtYUfMFoibYT1l5gXG1Kiamv5CVMhibQJpXjt0y/0";
        tv_min_name.setText(name);
        tv_min_phone.setText(newPhone);
        tv_min_vip.setText("V" + vip);
        TextPaint tp = tv_min_vip.getPaint();
        tp.setFakeBoldText(true);
        if (url == "") {
            Picasso.with(getActivity()).load(R.drawable.unkown_head).transform(new CircleTransform()).into(img_head);

        } else {
            Picasso.with(getActivity()).load(url).error(R.drawable.unkown_head).transform(new CircleTransform()).into(img_head);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 在使用这个view之前首先判断其是否存在parent view，这调用getParent()方法可以实现。
        // 如果存在parent view，那么就调用removeAllViewsInLayout()方法
        Log.i("test", "AFragment-->onCreateView");
        ViewGroup perentView = (ViewGroup) rootView.getParent();
        if (perentView != null) {
            perentView.removeAllViewsInLayout();
        }
        return rootView;
    }

    public void showWaiterAuthorizationDialog() {

        //LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化
        LayoutInflater factory = LayoutInflater.from(getActivity());
        //把activity_login中的控件定义在View中
        final View textEntryView = factory.inflate(R.layout.dialog_feedback, null);

        //将LoginActivity中的控件显示在对话框中
        new AlertDialog.Builder(getActivity())
                //对话框的标题
                .setTitle("您好")
                //设定显示的View
                .setView(textEntryView)
                //对话框中的“登陆”按钮的点击事件
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {


                    }

                })
                // 设置dialog是否为模态，false表示模态，true表示非模态
                .setCancelable(false)
                //对话框的创建、显示
                .create().show();

    }

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {

                    case MESSAGE_GET_USERINF:
                        String result3 = (String) msg.obj;
                        SPUtils.put(getActivity(), "UserInfo", result3);
                        System.out.println("use" + result3);
                        savaUserInf(result3);
                        break;

                    case MESSAGE_GET_INF:
                        result3 = (String) msg.obj;
                        infRead(result3);

                    default:
                        break;
                }
            }
        };
    }

    private void infRead(String result) {
        if ((result == null) || (result == "")) {
        } else {
            JSONObject jsonObj = JSON.parseObject(result);
            JSONArray jsonArray = jsonObj.getJSONArray("Data");
            if (jsonArray.size() <= 0) {
                return;
            } else if (!SPUtils.contains(getActivity(), "inf_result")) {

                setUnread();
            } else {
                String result2 = (String) SPUtils.get(getActivity(), "inf_result", "");
                JSONObject jsonObj2 = JSON.parseObject(result2);
                JSONArray jsonArray2 = jsonObj2.getJSONArray("Data");
                if (jsonArray.size() > jsonArray2.size()) {
                    setUnread();
                } else {
                    setRead();
                }
            }

        }

    }

    private void setUnread() {
        Drawable drawable = getResources().getDrawable(R.mipmap.unread);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        Drawable drawable2 = getResources().getDrawable(R.mipmap.inf108);
        drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());

        tv_my_inf.setCompoundDrawables(drawable2, null, drawable, null);
    }

    private void setRead() {
        Drawable drawable = getResources().getDrawable(R.drawable.right);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        Drawable drawable2 = getResources().getDrawable(R.mipmap.inf108);
        drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());

        tv_my_inf.setCompoundDrawables(drawable2, null, drawable, null);
    }

    private void savaUserInf(String result) {

        JSONObject jsonObj = JSON.parseObject(result);
        JSONObject jsonObject2 = JSON.parseObject(jsonObj.getString("Data"));


        SPUtils.put(getActivity(), "TrueName", jsonObject2.getString("TrueName"));
        SPUtils.put(getActivity(), "Company", jsonObject2.getString("Company"));
        SPUtils.put(getActivity(), "Industry", jsonObject2.getString("Industry"));
        SPUtils.put(getActivity(), "Duty", jsonObject2.getString("Duty"));
        SPUtils.put(getActivity(), "Email", jsonObject2.getString("Email"));
        SPUtils.put(getActivity(), "NickName", jsonObject2.getString("NickName"));

        SPUtils.put(getActivity(), "NickName", jsonObject2.getString("NickName"));
        SPUtils.put(getActivity(), "HeadImgurl", jsonObject2.getString("HeadImgurl"));
        SPUtils.put(getActivity(), "Sex", jsonObject2.getString("Sex"));

        SPUtils.put(getActivity(), "vip", jsonObject2.getInteger("GradeId"));
        int vip = (int) SPUtils.get(getActivity(), "vip", 0);
        if (null == jsonObject2.getString("Mobile")) {
        } else {
            SPUtils.put(getActivity(), "Mobile", jsonObject2.getString("Mobile"));
        }
        initUserInfo();

    }

    private void getList() {
        AsyncInf async = new AsyncInf(getActivity(), mHandler, MESSAGE_GET_INF);
        async.execute();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_min_setting:
                IntentUtils.startActivity(getActivity(), SettingActivity.class);
                break;
            case R.id.tv_my_wallet:
                IntentUtils.startActivity(getActivity(), WalletActivity.class);
                break;
            case R.id.tv_feedback:
//                IntentUtils.startActivity(getActivity(), FeedbackActivity.class);
                showWaiterAuthorizationDialog();
                break;
            case R.id.tv_min_vip:
                IntentUtils.startActivity(getActivity(), UpgradedActivity.class);
                break;
            case R.id.tv_min_phone:
                IntentUtils.startActivity(getActivity(), BondPhoneActivity.class);
                break;
            case R.id.tv_contacts:
                IntentUtils.startActivity(getActivity(), ContactsActivity.class);
                break;
            case R.id.tv_my_inf:
                IntentUtils.startActivity(getActivity(), InfActivity.class);
                setRead();
                break;
            case R.id.view_user:
                IntentUtils.startActivity(getActivity(), UpdateUserInfoActivity.class);

                break;


        }
    }

}
