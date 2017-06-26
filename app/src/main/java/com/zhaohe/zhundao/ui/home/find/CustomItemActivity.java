package com.zhaohe.zhundao.ui.home.find;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhaohe.app.utils.NetworkUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.CustomSelectMutiAdapter;
import com.zhaohe.zhundao.asynctask.AsyncUpdateOrAddCustom;
import com.zhaohe.zhundao.ui.ToolBarActivity;
import com.zhaohe.zhundao.ui.ToolBarHelper;

import java.util.LinkedList;

public class CustomItemActivity extends ToolBarActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private Spinner sp_find_custom_type;
    private EditText et_find_custom_name, et_find_custom_option;
    private String sp_result;
    private Handler mHandler;
    private RelativeLayout rl_find_custom_option;
    private Button btn_find_custom_submit;
    private Switch sw_find_custom_required;
    public static final int MESSAGE_UPDATE_OR_ADD_CUSTOM = 94;
    private LinkedList<String> mDatas;
    private CustomSelectMutiAdapter mAdapter;
    private ListView lv_custom_select;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_item);
        initToolBar("添加自定义选项", R.layout.activity_custom_item);
        initView();
        initData();
        initHandler();
    }

    private void initData() {
        mDatas = new LinkedList<>();
        mAdapter.addData("");
        mAdapter.addData("");
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
        mAdapter = new CustomSelectMutiAdapter(this, mDatas);
        lv_custom_select.setAdapter(mAdapter);
    }

    //点击事件--添加
    public void addClick(View view) {
        mAdapter.addData("");
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

    public String setOption() {
        String s = "";
        if (mAdapter.getCount() > 0) {
            for (int i = 0; i < (mAdapter.getCount() - 1); i++) {
                int count = i + 1;
                if (mAdapter.getItem(i).equals("")) {
                    ToastUtil.makeText(getApplicationContext(), "选项" + count + "不得为空");
//                    O选项为空
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
            return null;
        }
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
                            ToastUtil.makeText(getApplicationContext(), "添加成功");
                            finish();
                            ;
                        }
                        break;
                    default:
                        break;
                }
            }
        };


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
                        Option = setOption();
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
                    }
                    String Required = "" + sw_find_custom_required.isChecked();
                    String mParam = "Title=" + Title + "&Option=" + Option + "&Required=" + Required + "&InputType=" + sp_result;
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
