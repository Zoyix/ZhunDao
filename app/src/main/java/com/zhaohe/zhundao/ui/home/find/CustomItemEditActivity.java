package com.zhaohe.zhundao.ui.home.find;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhaohe.app.utils.IntentUtils;
import com.zhaohe.app.utils.NetworkUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.CustomSelectAdapter;
import com.zhaohe.zhundao.adapter.CustomSelectMutiAdapter;
import com.zhaohe.zhundao.asynctask.AsyncUpdateOrAddCustom;
import com.zhaohe.zhundao.bean.CustomSelectBean;
import com.zhaohe.zhundao.ui.ToolBarActivity;
import com.zhaohe.zhundao.ui.ToolBarHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CustomItemEditActivity extends ToolBarActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private Spinner sp_find_custom_type;
    private EditText et_find_custom_name, et_find_custom_option;
    //    自定义项修改提醒
    private TextView tv_find_custom_add_edit;
    private String sp_result;
    private Handler mHandler;
    private String ID;
    //    intent接受的选项
    private String option = null;
    private RelativeLayout rl_find_custom_option;
    private Button btn_find_custom_submit;
    private Switch sw_find_custom_required;
    public static final int MESSAGE_UPDATE_OR_ADD_CUSTOM = 94;
    private List<CustomSelectBean> list_custom_select;
    private ListView lv_custom_select;
    private CustomSelectAdapter adapter;
    private LinkedList<String> mDatas;
    private CustomSelectMutiAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_item);
        initToolBar("编辑自定义选项", R.layout.activity_custom_item);
        initView();
        initIntent();
        initData();


//        初始化从列表传来的数据
        initHandler();

//        test();
    }

    private void test() {
        List<CustomSelectBean> list = new ArrayList<CustomSelectBean>();
        for (int i = 1; i <= 20; i++) {
            CustomSelectBean bean = new CustomSelectBean();
            bean.setID("" + i);
            bean.setContent("选项" + i);
            list.add(bean);
        }
        adapter.refreshData(list);
    }

    private void initData() {
        //设置数据源
        if (option != null) {
            if (option.indexOf("|") != -1) {
                String[] mOption = option.split("\\|");
                mDatas = new LinkedList<>();
                for (int i = 0; i < mOption.length; i++) {
                    mAdapter.addData(mOption[i]);
                }
            } else {
                mDatas = new LinkedList<>();
                mAdapter.addData("");
                mAdapter.addData("");
            }

        } else {
            mDatas = new LinkedList<>();
            mAdapter.addData("");
            mAdapter.addData("");
        }
//        mDatas = new LinkedList<>();
//        mDatas.add("");
//        mDatas.add("");

    }

    //点击事件--添加
    public void addClick(View view) {
        mAdapter.addData("");
    }

    private void initIntent() {
        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");
        String Title = intent.getStringExtra("Title");
        option = intent.getStringExtra("Option");

        et_find_custom_name.setText(Title);
        String InputType = intent.getStringExtra("InputType");
        sp_find_custom_type.setSelection(Integer.parseInt(InputType));
        String Required = intent.getStringExtra("Required");
        if (Required.equals("是")) {
            sw_find_custom_required.setChecked(true);
        } else {
            sw_find_custom_required.setChecked(false);

        }


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
        sp_find_custom_type = (Spinner) findViewById(R.id.sp_find_custom_type);
        sp_find_custom_type.setOnItemSelectedListener(this);
        et_find_custom_name = (EditText) findViewById(R.id.et_find_custom_name);
        et_find_custom_option = (EditText) findViewById(R.id.et_find_custom_option);
        rl_find_custom_option = (RelativeLayout) findViewById(R.id.rl_find_custom_option);
        btn_find_custom_submit = (Button) findViewById(R.id.btn_find_custom_submit);
        btn_find_custom_submit.setOnClickListener(this);
        sw_find_custom_required = (Switch) findViewById(R.id.sw_find_custom_required);
        lv_custom_select = (ListView) findViewById(R.id.lv_custom_select);
        tv_find_custom_add_edit = (TextView) findViewById(R.id.tv_find_custom_add_edit);
        tv_find_custom_add_edit.setVisibility(View.VISIBLE);
//        adapter = new CustomSelectAdapter(this);
//        lv_custom_select.setAdapter(adapter);
        mAdapter = new CustomSelectMutiAdapter(this, mDatas);
        lv_custom_select.setAdapter(mAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        sp_result = sp_find_custom_type.getSelectedItem().toString();
        if (sp_find_custom_type.getSelectedItem().toString().equals("输入框")) {
            sp_result = "0";
            rl_find_custom_option.setVisibility(View.GONE);
        }
        if (sp_find_custom_type.getSelectedItem().toString().equals("多文本")) {
            sp_result = "1";
            rl_find_custom_option.setVisibility(View.GONE);

        }
        if (sp_find_custom_type.getSelectedItem().toString().equals("下拉")) {
            sp_result = "2";
            rl_find_custom_option.setVisibility(View.VISIBLE);


        }
        if (sp_find_custom_type.getSelectedItem().toString().equals("多选")) {
            sp_result = "3";
            rl_find_custom_option.setVisibility(View.VISIBLE);

        }
        if (sp_find_custom_type.getSelectedItem().toString().equals("图片")) {
            sp_result = "4";
            rl_find_custom_option.setVisibility(View.GONE);

        }
        if (sp_find_custom_type.getSelectedItem().toString().equals("单选")) {
            sp_result = "5";
            rl_find_custom_option.setVisibility(View.VISIBLE);

        }
        if (sp_find_custom_type.getSelectedItem().toString().equals("日期控件")) {
            sp_result = "6";
            rl_find_custom_option.setVisibility(View.GONE);

        }
        if (sp_find_custom_type.getSelectedItem().toString().equals("数字输入框")) {
            sp_result = "7";
            rl_find_custom_option.setVisibility(View.GONE);

        }
    }

    public void UpdateOrAddCustom(String param) {
        AsyncUpdateOrAddCustom async = new AsyncUpdateOrAddCustom(this, mHandler, MESSAGE_UPDATE_OR_ADD_CUSTOM, param);
        async.execute();
    }

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_UPDATE_OR_ADD_CUSTOM:
                        String result = (String) msg.obj;
                        JSONObject jsonObj = JSON.parseObject(result);
                        String message = jsonObj.getString("Res");
                        if (message.equals("0"))
                        //添加或修改请求结果
                        {
                            ToastUtil.makeText(getApplicationContext(), "修改成功");
                            IntentUtils.startActivity(CustomItemEditActivity.this, CustomActivity.class);
                            finish();
                        }

                        break;
                    default:
                        break;
                }
            }
        };


    }

    public String setOption() {
        String s = "";
        if (mAdapter.getCount() > 0) {
            for (int i = 0; i < (mAdapter.getCount() - 1); i++) {
                int count = i + 1;
                if (mAdapter.getItem(i).equals("")) {
                    ToastUtil.makeText(getApplicationContext(), "选项" + count + "不得为空");
                    return "0";

                }
                s = s + mAdapter.getItem(i) + "|";

            }
            if (mAdapter.getItem(mAdapter.getCount() - 1).equals("")) {
                ToastUtil.makeText(getApplicationContext(), "选项" + mAdapter.getCount() + "不得为空");
                return "0";

            } else {
                s = s + mAdapter.getItem(mAdapter.getCount() - 1);
                return s;
            }

        } else {
            return "0";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        sp_result = "0";
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_find_custom_submit:
                if (NetworkUtils.checkNetState(this)) {
                    String Option = null;
                    String Title = et_find_custom_name.getText().toString();
                    if (Title.equals("")) {
                        ToastUtil.makeText(getApplicationContext(), "请输入自定义选项名称");
                        return;
                    }
                    if (sp_result.equals("2") || sp_result.equals("5") || sp_result.equals("3")) {
//                String Option=et_find_custom_option.getText().toString();
                        Option = setOption();
//                    ToastUtil.makeText(getApplicationContext(), "结果" + Option);

                        if (Option.equals("0")) {

                            return;
                        }

                        int isok = Option.indexOf("|");
                        {
                            if (isok == -1) {
                                ToastUtil.makeText(getApplicationContext(), "请输入2个或以上自定义选择项");
                                return;
                            }
                        }
                    } else {
                        Option = null;
                    }
                    String Required = "" + sw_find_custom_required.isChecked();
                    String mParam = "Title=" + Title + "&Option=" + Option + "&Required=" + Required + "&InputType=" + sp_result + "&ID=" + ID;
                    UpdateOrAddCustom(mParam);
                } else {
                    ToastUtil.makeText(getApplicationContext(), "请联网后再试");

                }
                break;
            default:
                break;
        }
    }
}
