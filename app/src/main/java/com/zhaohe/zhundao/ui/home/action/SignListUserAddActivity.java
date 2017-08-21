package com.zhaohe.zhundao.ui.home.action;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhaohe.app.camera.Camera;
import com.zhaohe.app.utils.NetworkUtils;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.TimeUtil;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.asynctask.action.AsyncSignListAdd;
import com.zhaohe.zhundao.ui.ToolBarActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhaohe.app.utils.DensityUtil.dip2px;

public class SignListUserAddActivity extends ToolBarActivity {


    @BindView(R.id.tv_signlist_user_name)
    TextView tvSignlistUserName;
    @BindView(R.id.et_signlist_user_name)
    EditText etSignlistUserName;
    @BindView(R.id.rl_user_name)
    RelativeLayout rlUserName;
    @BindView(R.id.tv_signlist_user_phone)
    TextView tvSignlistUserPhone;
    @BindView(R.id.et_signlist_user_phone)
    EditText etSignlistUserPhone;
    @BindView(R.id.rl_user_phone)
    RelativeLayout rlUserPhone;
    @BindView(R.id.tv_signlist_user_sex)
    TextView tvSignlistUserSex;
    @BindView(R.id.sp_signlist_user_sex)
    Spinner spSignlistUserSex;
    @BindView(R.id.rl_user_sex)
    RelativeLayout rlUserSex;
    @BindView(R.id.tv_signlist_user_email)
    TextView tvSignlistUserEmail;
    @BindView(R.id.et_signlist_user_email)
    EditText etSignlistUserEmail;
    @BindView(R.id.rl_user_email)
    RelativeLayout rlUserEmail;
    @BindView(R.id.tv_signlist_user_unit)
    TextView tvSignlistUserUnit;
    @BindView(R.id.et_signlist_user_unit)
    EditText etSignlistUserUnit;
    @BindView(R.id.rl_user_unit)
    RelativeLayout rlUserUnit;
    @BindView(R.id.tv_signlist_user_dep)
    TextView tvSignlistUserDep;
    @BindView(R.id.et_signlist_user_dep)
    EditText etSignlistUserDep;
    @BindView(R.id.rl_user_dep)
    RelativeLayout rlUserDep;
    @BindView(R.id.tv_signlist_user_duty)
    TextView tvSignlistUserDuty;
    @BindView(R.id.et_signlist_user_duty)
    EditText etSignlistUserDuty;
    @BindView(R.id.rl_user_duty)
    RelativeLayout rlUserDuty;
    @BindView(R.id.tv_signlist_user_industry)
    TextView tvSignlistUserIndustry;
    @BindView(R.id.et_signlist_user_industry)
    EditText etSignlistUserIndustry;
    @BindView(R.id.rl_user_industry)
    RelativeLayout rlUserIndustry;
    @BindView(R.id.tv_signlist_user_join_num)
    TextView tvSignlistUserJoinNum;
    @BindView(R.id.et_signlist_user_join_num)
    EditText etSignlistUserJoinNum;
    @BindView(R.id.rl_user_join_num)
    RelativeLayout rlUserJoinNum;
    @BindView(R.id.tv_signlist_user_id_card)
    TextView tvSignlistUserIdCard;
    @BindView(R.id.et_signlist_user_id_card)
    EditText etSignlistUserIdCard;
    @BindView(R.id.rl_user_id_card)
    RelativeLayout rlUserIdCard;
    @BindView(R.id.tv_signlist_user_add)
    TextView tvSignlistUserAdd;
    @BindView(R.id.et_signlist_user_add)
    EditText etSignlistUserAdd;
    @BindView(R.id.rl_user_add)
    RelativeLayout rlUserAdd;
    @BindView(R.id.tv_signlist_user_remark)
    TextView tvSignlistUserRemark;
    @BindView(R.id.et_signlist_user_remark)
    EditText etSignlistUserRemark;
    @BindView(R.id.rl_user_remark)
    RelativeLayout rlUserRemark;

    @BindView(R.id.btn_signlist_user_sumbit)
    Button btnSignlistUserSumbit;
    @BindView(R.id.ll_signlist_add_user)
    LinearLayout llSignlistAddUser;
    private String signup_list;
    private String act_id;
    private String ID;
    private JSONObject jsonObj;
    private JSONArray jsonArray;
    private Handler mHandler;
    private String param="";
    private String FaceImg;
    LinkedHashMap<String, String> jsonMap= new LinkedHashMap<>();
    LinkedHashMap<String, String> jsonFeeGroup= new LinkedHashMap<>();

    private HashMap<String,Camera> cameraHashMap=new HashMap<>();
    public static final int MESSAGE_SIGNLIST_USER_ADD = 94;
    public static final int MESSAGE_UPLOAD_COMPLETE= 1000;
    public static final int MESSAGE_DELETE_COMPLETE= 999;
    String json;
    private Camera camera;
    private String PhotoTitle;
    String ActivityFees;
    String UserInfo;
    String activityFeeid="0";
    String paramBase="";
    String sex="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_list_user_add);
        initToolBarNew("新增报名", R.layout.activity_sign_list_user_add);
        ButterKnife.bind(this);

        initHandler();
        initIntent();

    }
    public void add(String param) {
        if (NetworkUtils.checkNetState(this)) {
            AsyncSignListAdd async = new AsyncSignListAdd(this, mHandler, MESSAGE_SIGNLIST_USER_ADD,activityFeeid,param);
            async.execute();
        } else {
            ToastUtil.makeText(this, R.string.net_error);
        }
    }

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_SIGNLIST_USER_ADD:
                        String result = (String) msg.obj;
                        JSONObject jsonObj = JSON.parseObject(result);
                        String message = jsonObj.getString("Res");
                        if (message.equals("0"))
                        //添加或修改请求结果
                        {
                            ToastUtil.makeText(getApplicationContext(), "添加成功！");
                            finish();
                        }
                        else{
                            ToastUtil.makeText(getApplicationContext(),  jsonObj.getString("Msg"));

                        }
                        break;
                    case MESSAGE_UPLOAD_COMPLETE:

                        String cameraUrl="";

                        ArrayList<String> a = camera.getUploadUrl();
                        for (int i = 0; i < a.size(); i++) {
                            cameraUrl=cameraUrl+a.get(i)+"|";

                        }
                        ToastUtil.print("PhotoTitle"+PhotoTitle+cameraUrl);
                        if(PhotoTitle.equals("人脸照片")){
                       FaceImg=cameraUrl;
                        }
                        else{
                        jsonMap.put(PhotoTitle, cameraUrl);}
                        ToastUtil.makeText(getApplicationContext(), "图片上传成功！");
                        break;
                    case MESSAGE_DELETE_COMPLETE:

                        cameraUrl="";
                        PhotoTitle = (String) msg.obj;
                        camera=cameraHashMap.get(PhotoTitle);
                        a = camera.getUploadUrl();
                        for (int i = 0; i < a.size(); i++) {
                            cameraUrl=cameraUrl+a.get(i)+"|";
                        }
                        if(PhotoTitle.equals("人脸照片")){
                            FaceImg=cameraUrl;

                        }
                        else{
                        ToastUtil.print("PhotoTitle"+PhotoTitle+cameraUrl);
                        jsonMap.put(PhotoTitle, cameraUrl);}
                        ToastUtil.print("图片删除成功");
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
            UserInfo= (String) SPUtils.get(this,"UserInfo" + act_id,"");
            ActivityFees=(String) SPUtils.get(this,"ActivityFees" + act_id,"");
            ToastUtil.print("基本选项:"+UserInfo+"费用"+ActivityFees);
        }


        jsonObj = JSON.parseObject(signup_list);
        jsonArray = jsonObj.getJSONArray("Data");
if (UserInfo!=null){
String[] baseItem= UserInfo.split("\\,");

        for (int i=0;i<baseItem.length;i++){
            ToastUtil.print("基本选项:"+baseItem[i]);
hideView(baseItem[i]);
        }}
        if (ActivityFees!=("")) {

            createSpinnerFee("缴费","");
        }


        JSONArray jsonArray3 = jsonObj.getJSONArray("Option");
        if (jsonArray3.size()!=0) {
            json = JSON.toJSONString(jsonMap);
            ToastUtil.print("json数据" + json);
            for (int i = 0; i < jsonArray3.size(); i++) {
                String InputType = jsonArray3.getJSONObject(i).getString("InputType");
                String Title = jsonArray3.getJSONObject(i).getString("Title");
                String Option = jsonArray3.getJSONObject(i).getString("Option");
                String Content = "";
                createView(InputType, Title, Option, Content, i);

            }

        }

    }

    private void hideView(String s) {
        switch (s){
            case "102":
//                rlUserSex.setVisibility(View.VISIBLE);
                createSpinnerBase("性别","Sex","未选择|男|女");
                break;
            case"103":
//                rlUserUnit.setVisibility(View.VISIBLE);
                createTextViewBase("单位","Company");

                break;
            case"104":
//                rlUserDep.setVisibility(View.VISIBLE);
                createTextViewBase("部门","Depart");

                break;
            case"105":
//                rlUserDuty.setVisibility(View.VISIBLE);
                createTextViewBase("职务","Duty");

                break;
            case"106":
//                rlUserIndustry.setVisibility(View.VISIBLE);
                createTextViewBase("行业","Industry");

                break;
            case"107":
//                rlUserEmail.setVisibility(View.VISIBLE);
                createTextViewBase("邮箱","Email");
                break;
            case"108":
//                rlUserJoinNum.setVisibility(View.VISIBLE);
                createTextViewBase("参与人数","Num");

                break;
            case"109":
//                rlUserRemark.setVisibility(View.VISIBLE);
                createTextViewBase("备注","Remark");

                break;
            case"110":
//                rlUserIdCard.setVisibility(View.VISIBLE);
                createTextViewBase("身份证","IDcard");

                break;
            case"111":
//                rlUserAdd.setVisibility(View.VISIBLE);
                createTextViewBase("地址","Address");

                break;
            case"112":
                createPhotoFace("人脸照片");

                break;


        }
    }

    private void createView(String InputType, String Title, String Option, String Content, int i) {
        switch (InputType) {
            case "0":
                createTextView(Title, Content, i);
                break;
            case "1":

                createTextView(Title, Content, i);
                break;
            case "2":
                createSpinner(Title, Content, Option, i);
                break;
            case "3":
                createCheckBox(Title, Content, Option, i);
                break;

            case "4":
                createPhoto(Title, Content, Option, i);

                break;
            case "5":
                createRadioButton(Title, Content, Option, i);
                break;
            case "6":
                createDateView(Title, Content, i);

                break;

            case "7":
                createTextNumView(Title, Content, i);
                break;
            default:
                break;
        }
    }

    private void createTextView(final String title, String Content, final int i) {
        int margin = dip2px(this, 10);
        int etWidth = dip2px(this, 300);
        int h = dip2px(this, 1);
        RelativeLayout rl = new RelativeLayout(this);
        TextView tv1 = new TextView(this);
        tv1.setText(title);
        tv1.setId(R.id.tv_code_title);
        final EditText et = new EditText(this);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //TODO:
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                jsonMap.put(title, et.getText().toString());
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
        llSignlistAddUser.addView(rl, rlParams);
        llSignlistAddUser.setTag(et);
    }
    private void createTextViewBase(final String title, final String mParam) {
        int margin = dip2px(this, 10);
        int etWidth = dip2px(this, 300);
        int h = dip2px(this, 1);
        RelativeLayout rl = new RelativeLayout(this);
        TextView tv1 = new TextView(this);
        tv1.setText(title);
        tv1.setId(R.id.tv_code_title);
        final EditText et = new EditText(this);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //TODO:
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                paramBase=paramBase+"&"+mParam+"="+et.getText().toString();

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
        llSignlistAddUser.addView(rl, rlParams);
        llSignlistAddUser.setTag(et);
    }

    private void createDateView(final String title, String Content, final int i) {
        int margin = dip2px(this, 10);
        int etWidth = dip2px(this, 300);
        int h = dip2px(this, 1);
        final DatePicker datePicker = new DatePicker(this);
        final EditText et = new EditText(this);
        RelativeLayout rl = new RelativeLayout(this);

        String[] date = TimeUtil.getNowTimeNew().split("\\-");
//        ToastUtil.print(date[0] + date[1] + date[2]);
//        datePicker.updateDate(Integer.valueOf(date[0]).intValue(), Integer.valueOf(date[1]).intValue() - 1, Integer.valueOf(date[2]).intValue());

        final DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1++;
                jsonMap.put(title, i + "-" + i1 + "-" + i2);
                et.setText(i + "-" + i1 + "-" + i2);


            }
        }, Integer.valueOf(date[0]).intValue(), Integer.valueOf(date[1]).intValue() - 1, Integer.valueOf(date[2]).intValue());
        TextView tv1 = new TextView(this);
        tv1.setText(title);
        et.setText(Content);
        et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    dialog.show();
                    return true;
                }
                return false;
            }

        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    dialog.show();

                }
            }
        });


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

        RelativeLayout.LayoutParams tvParams2 =
                new RelativeLayout.LayoutParams(
                        etWidth,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvParams2.addRule(RelativeLayout.RIGHT_OF, tv1.getId());
        tvParams2.addRule(RelativeLayout.ALIGN_BASELINE, tv1.getId());

        tv1.setPadding(margin, margin, margin, margin);


        rl.addView(tv1, tvParams1);
        rl.addView(et, tvParams2);
        llSignlistAddUser.addView(rl, rlParams);

    }

    private void createTextNumView(final String title, String Content, final int i) {
        int margin = dip2px(this, 10);
        int etWidth = dip2px(this, 300);
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
                jsonMap.put(title, et.getText().toString());
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
        llSignlistAddUser.addView(rl, rlParams);
        llSignlistAddUser.setTag(et);
    }

    private void createSpinner(final String title, String Content, String Option, final int i) {
        int margin = dip2px(this, 10);
        int etWidth = dip2px(this, 300);
        int h = dip2px(this, 1);
        RelativeLayout rl = new RelativeLayout(this);
        TextView tv1 = new TextView(this);
        tv1.setText(title);
        tv1.setId(R.id.tv_code_title);
        final Spinner sp = new Spinner(this);

        sp.setId(R.id.tv_code_content);
        sp.setPrompt(title);
        String[] s = Option.split("\\|");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, s);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
//        for (int j = 0; j < s.length; j++) {
//            if (s[j].equals(Content)) {
//                sp.setSelection(j);
//            }
//        }
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                jsonMap.put(title, sp.getSelectedItem().toString());

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
        llSignlistAddUser.addView(rl, rlParams);

    }
    private void createSpinnerBase(final String title, final String mParam, String Option) {
        int margin = dip2px(this, 10);
        int etWidth = dip2px(this, 300);
        int h = dip2px(this, 1);
        RelativeLayout rl = new RelativeLayout(this);
        TextView tv1 = new TextView(this);
        tv1.setText(title);
        tv1.setId(R.id.tv_code_title);
        final Spinner sp = new Spinner(this);

        sp.setId(R.id.tv_code_content);
        sp.setPrompt(title);
        String[] s = Option.split("\\|");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, s);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
//        for (int j = 0; j < s.length; j++) {
//            if (s[j].equals(Content)) {
//                sp.setSelection(j);
//            }
//        }
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
sex=sp.getSelectedItemPosition()+"";
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
        llSignlistAddUser.addView(rl, rlParams);

    }
    private void createSpinnerFee(final String title, String Option                                                                                                                                                        ) {
        int margin = dip2px(this, 10);
        int etWidth = dip2px(this, 300);
        int h = dip2px(this, 1);
        RelativeLayout rl = new RelativeLayout(this);
        TextView tv1 = new TextView(this);
        tv1.setText(title);
        tv1.setId(R.id.tv_code_title);
        final Spinner sp = new Spinner(this);
        JSONArray jsonFee = JSON.parseArray(ActivityFees);
        if (jsonFee.size()==0){
            return;
        }
        activityFeeid=jsonFee.getJSONObject(0).getString("ID");
        for (int i=0;i<jsonFee.size();i++){
            Option+=jsonFee.getJSONObject(i).getString("Title")+":"+jsonFee.getJSONObject(i).getString("Amount")+"|";
        }
        sp.setId(R.id.tv_code_content);
        sp.setPrompt(title);
        String[] s = Option.split("\\|");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, s);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
//        for (int j = 0; j < s.length; j++) {
//            if (s[j].equals(Content)) {
//                sp.setSelection(j);
//            }
//        }
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int k, long l) {
                JSONArray jsonFee = JSON.parseArray(ActivityFees);

                for (int i=0;i<jsonFee.size();i++){
                   if ((jsonFee.getJSONObject(i).getString("Title")+":"+jsonFee.getJSONObject(i).getString("Amount")) .equals(sp.getSelectedItem().toString()))     {
                       activityFeeid=jsonFee.getJSONObject(i).getString("ID");
                       ToastUtil.print(activityFeeid);
                   }
                }


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
        llSignlistAddUser.addView(rl, rlParams);

    }


    private void createRadioButton(final String title, String Content, String Option, final int i) {
        int margin = dip2px(this, 10);
        int etWidth = dip2px(this, 300);
        int h = dip2px(this, 1);
        RelativeLayout rl = new RelativeLayout(this);
        TextView tv1 = new TextView(this);
        tv1.setText(title);
        tv1.setId(R.id.tv_code_title);
        final RadioGroup radioGroup = new RadioGroup(this);

        radioGroup.setId(R.id.tv_code_content);

        String[] s = Option.split("\\|");
        for (int j = 0; j < s.length; j++) {
            final RadioButton radioButtonj = new RadioButton(this);
            radioButtonj.setText(s[j]);
            radioGroup.addView(radioButtonj);
            radioGroup.setSelected(true);
//            if (s[j].equals(Content)) {
//                radioButtonj.setChecked(true);
//            }


        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                for (int j = 0; j < radioGroup.getChildCount(); j++) {
                    RadioButton rb = (RadioButton) radioGroup.getChildAt(j);
                    if (rb.isChecked()) {
                        jsonMap.put(title, rb.getText().toString());

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
        llSignlistAddUser.addView(rl, rlParams);

    }

    private void createCheckBox(final String title, String Content, String Option, final int i) {
        int margin = dip2px(this, 10);
        int etWidth = dip2px(this, 300);
        int h = dip2px(this, 1);
        RelativeLayout rl = new RelativeLayout(this);
        LinearLayout ll = new LinearLayout(this);
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

        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

//        rl.addView(tv1, tvParams1);
//        rl.addView(ll, layoutParams);
        llSignlistAddUser.addView(tv1, tvParams1);

        final String[] s = Option.split("\\|");
        String[] c = Content.split("\\|");
        final boolean[] booleen = new boolean[s.length];

        for (int j = 0; j < s.length; j++) {
            final CheckBox checkBox = new CheckBox(this);

            checkBox.setText(s[j]);
//            for (int k = 0; k < c.length; k++) {
//                if (s[j].equals(c[k])) {
//                    booleen[j] = true;
//                    checkBox.setChecked(true);
//                }
//
//            }
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (compoundButton.isChecked()) {
                        ToastUtil.print("选择:" + compoundButton.getText().toString());

                        for (int l = 0; l < s.length; l++) {
                            if (compoundButton.getText().toString().equals(s[l])) {
                                booleen[l] = true;
                            }
                        }
                    } else {
                        ToastUtil.print("取消:" + compoundButton.getText().toString());


                        for (int l = 0; l < s.length; l++) {
                            if (compoundButton.getText().toString().equals(s[l])) {
                                booleen[l] = false;
                            }
                        }
                    }
                    String result = "";
                    for (int m = 0; m < s.length; m++) {
                        if (booleen[m]) {
                            result = result + s[m] + "|";
                        }
                    }
                    ToastUtil.print("多选结果:" + result);

                    jsonMap.put(title, result);


                }
            });
            llSignlistAddUser.addView(checkBox);

        }

    }

    private void createPhoto(final String title, String Content, String Option, final int i) {
        int margin = dip2px(this, 10);
        int etWidth = dip2px(this, 300);
        int h = dip2px(this, 1);
        RelativeLayout rl = new RelativeLayout(this);
        LinearLayout ll = new LinearLayout(this);
        TextView tv1 = new TextView(this);
        tv1.setText(title);
        tv1.setId(R.id.tv_code_title);
        GridLayout gl_camara = new GridLayout(this);
        final Camera camera1 = new Camera(this, gl_camara, false, 9, mHandler, title);
        PhotoTitle = title;
        cameraHashMap.put(title,camera1);

        gl_camara.requestDisallowInterceptTouchEvent(true);
        gl_camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.print("onclick");
                camera = camera1;
                PhotoTitle = title;


            }
        });
        gl_camara.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                ToastUtil.print("ontouch");
                camera = camera1;
                PhotoTitle = title;

                return false;
            }
        });
        gl_camara.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ToastUtil.print("onlongclick");

                camera = camera1;
                PhotoTitle = title;

                return false;
            }
        });


        tv1.setPadding(margin, margin, margin, margin);
        gl_camara.setPadding(2 * margin, margin, margin, margin);
        llSignlistAddUser.addView(tv1);
        llSignlistAddUser.addView(gl_camara);


    }
    private void createPhotoFace(final String title) {
        int margin = dip2px(this, 10);
        int etWidth = dip2px(this, 300);
        int h = dip2px(this, 1);
        RelativeLayout rl = new RelativeLayout(this);
        LinearLayout ll = new LinearLayout(this);
        TextView tv1 = new TextView(this);
        tv1.setText(title);
        tv1.setId(R.id.tv_code_title);
        GridLayout gl_camara = new GridLayout(this);
        final Camera camera1 = new Camera(this, gl_camara, false, 3, mHandler, title);
        PhotoTitle = title;
        cameraHashMap.put(title,camera1);

        gl_camara.requestDisallowInterceptTouchEvent(true);
        gl_camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.print("onclick");
                camera = camera1;
                PhotoTitle = title;


            }
        });
        gl_camara.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                ToastUtil.print("ontouch");
                camera = camera1;
                PhotoTitle = title;

                return false;
            }
        });
        gl_camara.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ToastUtil.print("onlongclick");

                camera = camera1;
                PhotoTitle = title;

                return false;
            }
        });


        tv1.setPadding(margin, margin, margin, margin);
        gl_camara.setPadding(2 * margin, margin, margin, margin);
        llSignlistAddUser.addView(tv1);
        llSignlistAddUser.addView(gl_camara);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        camera.onActivityResult(requestCode, resultCode, data);


    }
    @OnClick(R.id.btn_signlist_user_sumbit)
    public void onViewClicked() {
        String Name = etSignlistUserName.getText().toString();
        String Mobile = etSignlistUserPhone.getText().toString();
        if (Name.length() == 0) {
            ToastUtil.makeText(this, "姓名不得为空，请重新输入~");
            return;
        }
        if (Mobile.length() == 0) {
            ToastUtil.makeText(this, "手机号不得为空，请重新输入~");
            return;
        }

        json=JSON.toJSONString(jsonMap);
        ToastUtil.print("json数据"+json);
        param = "UserName=" + Name + "&Mobile=" + Mobile+ "&ExtraInfo="+json+"&ActivityID="+act_id+paramBase;

//        activityFeeid="0";
        add(setParam(param));
        ToastUtil.print(setParam(param));


    }

    public String setParam(String param) {
        this.param = param;
//        if (etSignlistUserRemark.getText().toString()!=null){
//            param=param+"&Remark="+etSignlistUserRemark.getText().toString();
//        }
//        if (etSignlistUserAdd.getText().toString()!=null){
//            param=param+"&Address="+etSignlistUserAdd.getText().toString();
//        }
//        if (etSignlistUserUnit.getText().toString()!=null){
//            param=param+"&Company="+etSignlistUserUnit.getText().toString();
//        }
        if (sex!="0"){
            param=param+"&Sex="+sex;
        }
//        if (etSignlistUserDep.getText().toString()!=null){
//            param=param+"&Depart="+etSignlistUserDep.getText().toString();
//        }    if (etSignlistUserIndustry.getText().toString()!=null){
//            param=param+"&Industry="+etSignlistUserIndustry.getText().toString();
//        }    if (etSignlistUserIdCard.getText().toString()!=null){
//            param=param+"&IDcard="+etSignlistUserIdCard.getText().toString();
//        }    if (etSignlistUserEmail.getText().toString()!=null){
//            param=param+"&Email="+etSignlistUserEmail.getText().toString();
//        }
//        if (etSignlistUserJoinNum.getText().toString()!=null){
//            param=param+"&Num="+etSignlistUserJoinNum.getText().toString();
//        }


        if (FaceImg!=null){
            param=param+"&FaceImg="+FaceImg;

        }

        return param;
    }

}
