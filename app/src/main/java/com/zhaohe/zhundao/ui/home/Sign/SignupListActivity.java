package com.zhaohe.zhundao.ui.home.sign;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.SignupListAdapter;
import com.zhaohe.zhundao.bean.SignupListBean;
import com.zhaohe.zhundao.ui.ToolBarActivity;
import com.zhaohe.zhundao.ui.ToolBarHelper;

import java.util.ArrayList;
import java.util.List;


public class SignupListActivity extends ToolBarActivity implements View.OnClickListener {
    private String sign_id;
    private SignupListAdapter adapter, adapter2, adapter3;
    private ListView lv_signup, lv_signon, lv_signoff;
    private String signup_list;
    private TextView tv_signup_all, tv_signup_on, tv_signup_off;
    private JSONObject jsonObj;
    private JSONArray jsonArray;
    private int numShould,numFact,numRest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_list);
        initToolBar("用户签到信息",R.layout.activity_signup_list);
        initView();
        init();
    }
    private void initToolBar(String text,int layoutResID){
        ToolBarHelper mToolBarHelper ;
        mToolBarHelper = new ToolBarHelper(this,layoutResID) ;
        mToolBarHelper.setTvTitle(text);
        super.setTitle("");
        setContentView(mToolBarHelper.getContentView());
        toolbar = mToolBarHelper.getToolBar() ;
  /*把 toolbar 设置到Activity 中*/
        setSupportActionBar(toolbar);
    }
    private void initView() {
        lv_signup = (ListView) findViewById(R.id.lv_signuplist_all);
        adapter = new SignupListAdapter(this);
        lv_signup.setAdapter(adapter);
        tv_signup_all = (TextView) findViewById(R.id.tv_signup_all);
        tv_signup_all.setOnClickListener(this);
        tv_signup_on = (TextView) findViewById(R.id.tv_signup_on);
        tv_signup_on.setOnClickListener(this);
        tv_signup_off = (TextView) findViewById(R.id.tv_signup_off);
        tv_signup_off.setOnClickListener(this);


    }

    public void init() {

        Intent intent = getIntent();
        //从Intent当中根据key取得value
        sign_id = intent.getStringExtra("sign_id");
        String result = intent.getStringExtra("result");
//        String numShould=intent.getStringExtra("NumShould");
//        String numFact=intent.getStringExtra("NumFact");
//        int numRest=Integer.valueOf(numShould).intValue()-Integer.valueOf(numFact).intValue();
       numFact=0;
        numRest=0;
        numShould=0;
        signup_list = (String) SPUtils.get(this, "listup_" + sign_id, "");
        System.out.println("new" + SPUtils.get(this, "signup_" + sign_id, ""));
        jsonObj = JSON.parseObject(result);
         jsonArray = jsonObj.getJSONArray("Data");
//        tv_signup_all.setText();
        List<SignupListBean> list = new ArrayList<SignupListBean>();
        for (int i = 0; i < jsonArray.size(); i++) {
            SignupListBean bean = new SignupListBean();
            bean.setSignup_list_name(jsonArray.getJSONObject(i).getString("TrueName"));
            bean.setSignup_list_phone(jsonArray.getJSONObject(i).getString("Mobile"));
            String time=jsonArray.getJSONObject(i).getString("SignTime");
//            去除json传回来的时间中的T字符

            bean.setSignup_list_time(time);

//          Status  true已签到，false未签到
            bean.setSignup_list_status(jsonArray.getJSONObject(i).getString("Status"));
            if(jsonArray.getJSONObject(i).getString("Status")=="true"){
                numFact=numFact+1;
            }
            if(jsonArray.getJSONObject(i).getString("Status")=="false"){
                numRest=numRest+1;
            }
            numShould=numShould+1;
            list.add(bean);

        }
        adapter.refreshData(list);
        tv_signup_all.setText("全部（"+numShould+"）");
        tv_signup_on.setText("已签（"+numFact+"）");
        tv_signup_off.setText("未签（"+numRest+"）");
    }
    private void signon(){
        List<SignupListBean> list = new ArrayList<SignupListBean>();
        for (int i = 0; i < jsonArray.size(); i++) {
            SignupListBean bean = new SignupListBean();
            bean.setSignup_list_name(jsonArray.getJSONObject(i).getString("TrueName"));
            bean.setSignup_list_phone(jsonArray.getJSONObject(i).getString("Mobile"));
            bean.setSignup_list_time(jsonArray.getJSONObject(i).getString("SignTime"));

//          Status  true已签到，false未签到
            if(jsonArray.getJSONObject(i).getString("Status")=="true"){
            bean.setSignup_list_status(jsonArray.getJSONObject(i).getString("Status"));
            list.add(bean);}

        }
        adapter.refreshData(list);

    }

    private void signoff(){
        List<SignupListBean> list = new ArrayList<SignupListBean>();
        for (int i = 0; i < jsonArray.size(); i++) {
            SignupListBean bean = new SignupListBean();
            bean.setSignup_list_name(jsonArray.getJSONObject(i).getString("TrueName"));
            bean.setSignup_list_phone(jsonArray.getJSONObject(i).getString("Mobile"));
            bean.setSignup_list_time(jsonArray.getJSONObject(i).getString("SignTime"));

//          Status  true已签到，false未签到
            if(jsonArray.getJSONObject(i).getString("Status")=="false"){
                bean.setSignup_list_status(jsonArray.getJSONObject(i).getString("Status"));
                list.add(bean);}

        }
        adapter.refreshData(list);

    }

    public void resetColor() {
        tv_signup_all.setTextColor(Color.rgb(56, 56, 56));
        tv_signup_off.setTextColor(Color.rgb(56, 56, 56));
        tv_signup_on.setTextColor(Color.rgb(56, 56, 56));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_signup_all:
                resetColor();
                tv_signup_all.setTextColor(Color.rgb(87,153,8));
init();
                break;

            case R.id.tv_signup_on:
                resetColor();
                tv_signup_on.setTextColor(Color.rgb(87,153,8));
signon();
                break;
            case R.id.tv_signup_off:
                resetColor();
                signoff();
                tv_signup_off.setTextColor(Color.rgb(87,153,8));
                break;

        }
    }
}