package com.zhaohe.zhundao.ui.home.action;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.zhaohe.app.utils.NetworkUtils;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.asynctask.action.AsyncSignListEdit;
import com.zhaohe.zhundao.ui.ToolBarActivity;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhaohe.app.utils.DensityUtil.dip2px;

public class SignListUserEditActivity extends ToolBarActivity {

    @BindView(R.id.tv_signlist_user_name)
    TextView tvSignlistUserName;
    @BindView(R.id.et_signlist_user_name)
    EditText etSignlistUserName;
    @BindView(R.id.rl_user_name)
    RelativeLayout rlUserName;
    @BindView(R.id.et_signlist_user_phone)
    EditText etSignlistUserPhone;
    @BindView(R.id.rl_user_phone)
    RelativeLayout rlUserPhone;
    @BindView(R.id.sp_signlist_user_sex)
    Spinner spSignlistUserSex;
    @BindView(R.id.rl_user_sex)
    RelativeLayout rlUserSex;
    @BindView(R.id.et_signlist_user_unit)
    EditText etSignlistUserUnit;
    @BindView(R.id.rl_user_unit)
    RelativeLayout rlUserUnit;
    @BindView(R.id.et_signlist_user_dep)
    EditText etSignlistUserDep;
    @BindView(R.id.rl_user_dep)
    RelativeLayout rlUserDep;
    @BindView(R.id.et_signlist_user_duty)
    EditText etSignlistUserDuty;
    @BindView(R.id.rl_user_duty)
    RelativeLayout rlUserDuty;
    @BindView(R.id.et_signlist_user_industry)
    EditText etSignlistUserIndustry;
    @BindView(R.id.rl_user_industry)
    RelativeLayout rlUserIndustry;
    @BindView(R.id.et_signlist_user_join_num)
    EditText etSignlistUserJoinNum;
    @BindView(R.id.rl_user_join_num)
    RelativeLayout rlUserJoinNum;
    @BindView(R.id.et_signlist_user_id_card)
    EditText etSignlistUserIdCard;
    @BindView(R.id.rl_user_id_card)
    RelativeLayout rlUserIdCard;
    @BindView(R.id.et_signlist_user_add)
    EditText etSignlistUserAdd;
    @BindView(R.id.rl_user_add)
    RelativeLayout rlUserAdd;
    @BindView(R.id.et_signlist_user_remark)
    EditText etSignlistUserRemark;
    @BindView(R.id.rl_user_remark)
    RelativeLayout rlUserRemark;
    @BindView(R.id.et_signlist_user_email)
    EditText etSignlistUserEmail;
    @BindView(R.id.rl_user_email)
    RelativeLayout rlUserEmail;
    @BindView(R.id.btn_signlist_user_sumbit)
    Button btnSignlistUserSumbit;
    @BindView(R.id.ll_signlist_user)
    LinearLayout llSignListUser;
    LinkedHashMap<String, String> jsonMap;
    private String signup_list;
    private String act_id;
    private String ID;
    private JSONObject jsonObj;
    private JSONArray jsonArray;
    private Handler mHandler;
    public static final int MESSAGE_SIGNLIST_USER_EDIT = 94;
    private String param;
private int size;
    private String[] item=new String[100];
    String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_list_user_edit);
        initToolBarNew("修改用户信息", R.layout.activity_sign_list_user_edit);
        ButterKnife.bind(this);
        initIntent();
        initHandler();
    }

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {

                    case MESSAGE_SIGNLIST_USER_EDIT:
                        String result = (String) msg.obj;
                        JSONObject jsonObj = JSON.parseObject(result);
                        String message = jsonObj.getString("Res");
                        System.out.println("people_add_result:" + result);
                        if (message.equals("0"))
                        //添加或修改请求结果
                        {
                            ToastUtil.makeText(getApplicationContext(), "修改成功！");
                            finish();
                        }
                        break;


                    default:
                        break;
                }
            }
        };
    }

    private void initIntent() {
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
            rlUserUnit.setVisibility(View.GONE);
        }
        if (intent.getStringExtra("sex").equals("0")) {
            rlUserSex.setVisibility(View.GONE);
        }
        if (intent.getStringExtra("dep") == null) {
            rlUserDep.setVisibility(View.GONE);
        }
        if (intent.getStringExtra("industry") == null) {
            rlUserIndustry.setVisibility(View.GONE);
        }
        if (intent.getStringExtra("duty") == null) {
            rlUserDuty.setVisibility(View.GONE);
        }
        if (intent.getStringExtra("email") == null) {
            rlUserEmail.setVisibility(View.GONE);
        }
        if (intent.getStringExtra("id_card") == null) {
            rlUserIdCard.setVisibility(View.GONE);
        }
        if (intent.getStringExtra("join_num").equals("0")) {
            rlUserJoinNum.setVisibility(View.GONE);
        }
        if (intent.getStringExtra("add") == null) {
            rlUserAdd.setVisibility(View.GONE);
        }
        if (intent.getStringExtra("remark") == null) {
            rlUserRemark.setVisibility(View.GONE);
        }
        etSignlistUserName.setText(intent.getStringExtra("name"));
        etSignlistUserPhone.setText(intent.getStringExtra("phone"));

        etSignlistUserUnit.setText(intent.getStringExtra("unit"));
        etSignlistUserDep.setText(intent.getStringExtra("dep"));
        etSignlistUserIndustry.setText(intent.getStringExtra("industry"));
        etSignlistUserDuty.setText(intent.getStringExtra("duty"));
        etSignlistUserEmail.setText(intent.getStringExtra("email"));
        if (intent.getStringExtra("sex").equals("1")) {
            spSignlistUserSex.setSelection(1);
        }
        if (intent.getStringExtra("sex").equals("2")) {
            spSignlistUserSex.setSelection(2);
        }
        etSignlistUserIdCard.setText(intent.getStringExtra("id_card"));
        etSignlistUserJoinNum.setText(intent.getStringExtra("join_num"));
        etSignlistUserAdd.setText(intent.getStringExtra("add"));
        etSignlistUserRemark.setText(intent.getStringExtra("remark"));
        JSONArray jsonArray3 = jsonObj.getJSONArray("Option");
        String extra = intent.getStringExtra("extra");
        if (extra != null){
            jsonMap = JSON.parseObject(extra, new TypeReference<LinkedHashMap<String, String>>() {
            });
             json=JSON.toJSONString(jsonMap);
            ToastUtil.print("json数据"+json);
        for (int i = 0; i < jsonArray3.size(); i++) {
            String InputType = jsonArray3.getJSONObject(i).getString("InputType");
            String Title = jsonArray3.getJSONObject(i).getString("Title");
            String Option = jsonArray3.getJSONObject(i).getString("Option");
            String Content="";
            for (LinkedHashMap.Entry<String, String> entry : jsonMap.entrySet()){
                if(entry.getKey().equals(Title)){
                    Content=entry.getValue();
                }
            }
            createView(InputType, Title, Option,Content,i);

        }

    }

    }

    private void createView(String InputType, String Title, String Option,String Content, int i) {
        switch (InputType) {
            case "0":
                createTextView(Title,Content,i);
                break;
            case"1":

                createTextView(Title,Content,i);
            break;
            case "2":
                createSpinner(Title,Content,Option,i);
                break;
            case"3":
                createCheckBox(Title,Content,Option,i);
                break;
            case"5":
                createRadioButton(Title,Content,Option,i);
                break;
            case"7":
                createTextNumView(Title,Content,i);
break;
            default:
                break;
        }
    }

    private void createTextView(final String title, String Content , final int i) {
        int margin = dip2px(this, 10);
        int etWidth=dip2px(this, 300);
        int h = dip2px(this, 1);
        RelativeLayout rl = new RelativeLayout(this);
        TextView tv1 = new TextView(this);
        tv1.setText(title);
        tv1.setId(R.id.tv_code_title);
        final EditText et = new EditText(this);
        et.setText(Content);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //TODO:
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                jsonMap.put(title,et.getText().toString());
         }

            @Override
            public void afterTextChanged(Editable s) {
                //TODO:
            }
        });
        et.setId(R.id.tv_code_content);
        tv1.setPadding(margin, margin, margin, margin);

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
                        etWidth,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvParams2.addRule(RelativeLayout.RIGHT_OF, tv1.getId());
        tvParams2.addRule(RelativeLayout.ALIGN_BASELINE, tv1.getId());

        rl.addView(tv1, tvParams1);
        rl.addView(et, tvParams2);
        llSignListUser.addView(rl, rlParams);
        llSignListUser.setTag(et);
    }
    private void createTextNumView(final String title, String Content , final int i) {
        int margin = dip2px(this, 10);
        int etWidth=dip2px(this, 300);
        int h = dip2px(this, 1);
        RelativeLayout rl = new RelativeLayout(this);
        TextView tv1 = new TextView(this);
        tv1.setText(title);
        tv1.setId(R.id.tv_code_title);
        final EditText et = new EditText(this);
        et.setText(Content);
        et.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //TODO:
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                jsonMap.put(title,et.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //TODO:
            }
        });
        et.setId(R.id.tv_code_content);
        tv1.setPadding(margin, margin, margin, margin);

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
                        etWidth,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvParams2.addRule(RelativeLayout.RIGHT_OF, tv1.getId());
        tvParams2.addRule(RelativeLayout.ALIGN_BASELINE, tv1.getId());

        rl.addView(tv1, tvParams1);
        rl.addView(et, tvParams2);
        llSignListUser.addView(rl, rlParams);
        llSignListUser.setTag(et);
    }
    private void createSpinner(final String title, String Content ,String Option, final int i) {
        int margin = dip2px(this, 10);
        int etWidth=dip2px(this, 300);
        int h = dip2px(this, 1);
        RelativeLayout rl = new RelativeLayout(this);
        TextView tv1 = new TextView(this);
        tv1.setText(title);
        tv1.setId(R.id.tv_code_title);
        final Spinner sp = new Spinner(this);

        sp.setId(R.id.tv_code_content);
        sp.setPrompt(title);
        String[] s = Option.split("\\|");

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,s);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
sp.setAdapter(adapter);
        for(int j=0;j<s.length;j++){
            if (s[j].equals(Content)){
                sp.setSelection(j);
            }
        }
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                jsonMap.put(title,sp.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tv1.setPadding(margin, margin, margin, margin);

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
        tvParams2.addRule(RelativeLayout.RIGHT_OF, tv1.getId());
        tvParams2.addRule(RelativeLayout.ALIGN_BASELINE, tv1.getId());

        rl.addView(tv1, tvParams1);
        rl.addView(sp, tvParams2);
        llSignListUser.addView(rl, rlParams);

    }
    private void createRadioButton(final String title, String Content ,String Option, final int i) {
        int margin = dip2px(this, 10);
        int etWidth=dip2px(this, 300);
        int h = dip2px(this, 1);
        RelativeLayout rl = new RelativeLayout(this);
        TextView tv1 = new TextView(this);
        tv1.setText(title);
        tv1.setId(R.id.tv_code_title);
        final RadioGroup radioGroup = new RadioGroup(this);

        radioGroup.setId(R.id.tv_code_content);

        String[] s = Option.split("\\|");
for(int j=0;j<s.length;j++){
    final RadioButton radioButtonj=new RadioButton(this);
    radioButtonj.setText(s[j]);
    radioGroup.addView(radioButtonj);
    radioGroup.setSelected(true);
    if (s[j].equals(Content)){
        radioButtonj.setChecked(true);
    }


}

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                for(int j = 0 ;j < radioGroup.getChildCount();j++){
                    RadioButton rb = (RadioButton)radioGroup.getChildAt(j);
                    if(rb.isChecked()){
                        jsonMap.put(title,rb.getText().toString());

                        break;
                    }
                }

            }
        });


        tv1.setPadding(margin, margin, margin, margin);

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
//        tvParams2.addRule(RelativeLayout.ALIGN_BASELINE, tv1.getId());

        rl.addView(tv1, tvParams1);
        rl.addView(radioGroup, tvParams2);
        llSignListUser.addView(rl, rlParams);

    }
    private void createCheckBox(final String title, String Content , String Option, final int i) {
        int margin = dip2px(this, 10);
        int etWidth=dip2px(this, 300);
        int h = dip2px(this, 1);
        RelativeLayout rl = new RelativeLayout(this);
        LinearLayout ll=new LinearLayout(this);
        TextView tv1 = new TextView(this);
        tv1.setText(title);
        tv1.setId(R.id.tv_code_title);

//        radioGroup.setId(R.id.tv_code_content);





        tv1.setPadding(margin, margin, margin, margin);

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

        LinearLayout.LayoutParams layoutParams=
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

//        rl.addView(tv1, tvParams1);
//        rl.addView(ll, layoutParams);
        llSignListUser.addView(tv1, tvParams1);

        final String[] s = Option.split("\\|");
        String[] c = Content.split("\\|");
        final boolean[] booleen=new boolean[s.length];

        for(int j=0;j<s.length;j++){
            final CheckBox checkBox=new CheckBox(this);
            checkBox.setText(s[j]);
            for(int k=0;k<c.length;k++){
                if (s[j].equals(c[k])){
                    booleen[j]=true;
                    checkBox.setChecked(true);
                }

            }
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(compoundButton.isChecked()){
                        ToastUtil.print("选择:"+compoundButton.getText().toString());

                        for(int l=0;l<s.length;l++){
                            if(compoundButton.getText().toString().equals(s[l])){
                                booleen[l]=true;}
                        }
                    }
                    else {
                        ToastUtil.print("取消:"+compoundButton.getText().toString());


                        for(int l=0;l<s.length;l++){
                            if(compoundButton.getText().toString().equals(s[l])){
                                booleen[l]=false;}
                        }
                    }
                    String result="";
                    for(int m=0;m<s.length;m++){
                        if(booleen[m]){
                            result=result+s[m]+"|";
                        }
                    }
                    ToastUtil.print("多选结果:"+result);

                    jsonMap.put(title,result);


                }
            });
            llSignListUser.addView(checkBox);

        }

    }

    public void edit(String param) {
        if (NetworkUtils.checkNetState(this)) {
            AsyncSignListEdit async = new AsyncSignListEdit(this, mHandler, MESSAGE_SIGNLIST_USER_EDIT, param);
            async.execute();
        } else {
            ToastUtil.makeText(this, R.string.net_error);
        }
    }


    @OnClick(R.id.btn_signlist_user_sumbit)
    public void onViewClicked() {
        String Name = etSignlistUserName.getText().toString();
        String Mobile = etSignlistUserPhone.getText().toString();
        if (Name.length() == 0) {
            ToastUtil.makeText(this, "姓名不得为空，请重新输入~");
            return;
        }
        if (Mobile.length() != 11) {
            ToastUtil.makeText(this, "请输入11位电话号码，请检查后重新输入~");
            return;
        }
        json=JSON.toJSONString(jsonMap);
        ToastUtil.print("json数据"+json);

        param = "UserName=" + Name + "&Mobile=" + Mobile + "&ID=" + ID+"&ExtraInfo="+json;
        edit(setParam(param));
    }

    public String setParam(String param) {
        this.param = param;
        if (etSignlistUserRemark.getText().toString()!=null){
            param=param+"&Remark="+etSignlistUserRemark.getText().toString();
        }
        if (etSignlistUserAdd.getText().toString()!=null){
            param=param+"&Address="+etSignlistUserAdd.getText().toString();
        }
        if (etSignlistUserUnit.getText().toString()!=null){
            param=param+"&Company="+etSignlistUserUnit.getText().toString();
        }
        if (spSignlistUserSex.getSelectedItemPosition()!=0){
            param=param+"&Sex="+spSignlistUserSex.getSelectedItemPosition();
        }    if (etSignlistUserDep.getText().toString()!=null){
            param=param+"&Depart="+etSignlistUserDep.getText().toString();
        }    if (etSignlistUserIndustry.getText().toString()!=null){
            param=param+"&Industry="+etSignlistUserIndustry.getText().toString();
        }    if (etSignlistUserIdCard.getText().toString()!=null){
            param=param+"&IDcard="+etSignlistUserIdCard.getText().toString();
        }    if (etSignlistUserEmail.getText().toString()!=null){
            param=param+"&Email="+etSignlistUserEmail.getText().toString();
        }
           if (etSignlistUserJoinNum.getText().toString()!=null){
            param=param+"&Num="+etSignlistUserJoinNum.getText().toString();
        }

        if (etSignlistUserEmail.getText().toString()!=null){
            param=param+"&Email="+etSignlistUserEmail.getText().toString();
        }

        return param;
    }
}
