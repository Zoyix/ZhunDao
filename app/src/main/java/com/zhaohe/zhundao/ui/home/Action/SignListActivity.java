package com.zhaohe.zhundao.ui.home.action;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wevey.selector.dialog.NormalSelectionDialog;
import com.zhaohe.app.utils.NetworkUtils;
import com.zhaohe.app.utils.ProgressDialogUtils;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.SignListAdapter;
import com.zhaohe.zhundao.asynctask.AsyncSignList;
import com.zhaohe.zhundao.asynctask.action.AsyncSignlistEmail;
import com.zhaohe.zhundao.bean.SignListBean;
import com.zhaohe.zhundao.bean.ToolUserBean;
import com.zhaohe.zhundao.dao.MySignListDao;
import com.zhaohe.zhundao.ui.ToolBarActivity;
import com.zhaohe.zhundao.ui.ToolBarHelper;
import com.zhaohe.zhundao.ui.home.action.signlist.InvitationPersonActivity;

import java.util.ArrayList;
import java.util.List;

import static com.zhaohe.zhundao.ui.login.BondPhoneActivity.MESSAGE_GET_CODE;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2016/12/14 10:52
 */
public class SignListActivity extends ToolBarActivity implements AdapterView.OnItemClickListener,Toolbar.OnMenuItemClickListener,AdapterView.OnItemLongClickListener{
    private SignListAdapter adapter;
    private List<SignListBean> list_act;
    private ListView lv_signlist;
    private String signup_list;
    private String act_id;
    private JSONObject jsonObj;
    private JSONArray jsonArray;
    private int count=0;
    private MySignListDao dao;
    private EditText et_signlist_search;
    private Handler mHandler;
    public static final int MESSAGE_SEND_SIGNLIST_EMAIL = 94;
    public static final int MESSAGE_GET_SIGNLIST = 93;
    public static final int MESSAGE_GET_SIGNLIST_NO_DIALOG = 92;

    NormalSelectionDialog dialog1;//底部对话框
    SignListBean bean;
    public static final int PAGE_SIZE = 200000;
  private String  ActivityFees;
    private String    UserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_list);
        initToolBar("报名用户信息", R.layout.activity_sign_list);
        initHandler();
        initView();
        init();
//        test();
    }


    @Override
    public void onResume() {
        super.onResume();
        if ((boolean) SPUtils.get(this, "updateSignList", false)) {
            updateNoDialog();
            SPUtils.put(this, "updateSignList", false);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu_signlist_email, menu);
        toolbar.setOnMenuItemClickListener(this);

        return true;
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


    public void init() {

        Intent intent = getIntent();
        //从Intent当中根据key取得value
        if (intent != null) {
            act_id = intent.getStringExtra("act_id");
            UserInfo=intent.getStringExtra("UserInfo");
            ActivityFees=intent.getStringExtra("ActivityFees");
            signup_list = (String) SPUtils.get(this, "listup_" + act_id, "");

        }
        jsonObj = JSON.parseObject(signup_list);
        jsonArray = jsonObj.getJSONArray("Data");
        List<SignListBean> list = new ArrayList<SignListBean>();
        for (int i = 0; i < jsonArray.size(); i++) {
            SignListBean bean = new SignListBean();
            bean.setSign_list_id(jsonArray.getJSONObject(i).getString("ID"));
            bean.setSign_list_name(jsonArray.getJSONObject(i).getString("UserName"));
            bean.setSign_list_time(jsonArray.getJSONObject(i).getString("AddTime"));
            bean.setSign_list_phone(jsonArray.getJSONObject(i).getString("Mobile"));
            bean.setNickname(jsonArray.getJSONObject(i).getString("NickName"));
            bean.setAdminRemark(jsonArray.getJSONObject(i).getString("AdminRemark"));
            bean.setVCode(jsonArray.getJSONObject(i).getString("VCode"));

            bean.setmIndex(i);
            bean.setAct_id(act_id);
//          Status  -1取消报名，0报名成功，1待缴费
            if (jsonArray.getJSONObject(i).getString("Status") .equals("0") ) {
                bean.setSign_list_status("报名成功");
            }
            if (jsonArray.getJSONObject(i).getString("Status") .equals("1")) {
                bean.setSign_list_status("待缴费");
            }
            if (jsonArray.getJSONObject(i).getString("Status").equals("-1")) {
                bean.setSign_list_status("取消报名");
            }
            if (jsonArray.getJSONObject(i).getString("Status").equals("2")) {
                bean.setSign_list_status("待审核");
            }
            if (jsonArray.getJSONObject(i).getString("Status").equals("3")) {
                bean.setSign_list_status("审核失败");
            }


            list.add(bean);
        }
//        count=jsonArray.size()-1;
        dao.save(list);
        if (intent.getStringExtra("phone")!=null){
//            et_signlist_search.setText(intent.getStringExtra("phone"));
            list_act=  dao.queryListActIDAndPhoneOrName(act_id,intent.getStringExtra("phone"));
           SignListBean bean_act= list_act.get(0);
            StartList(bean_act);
            finish();

        }
        else{
        list_act=dao.queryListActID(act_id);
        adapter.refreshData(list_act);}


    }
    private void getSignList(String param) {
        Dialog dialog = ProgressDialogUtils.showProgressDialog(this, getString(R.string.progress_title), getString(R.string.progress_message));
        AsyncSignList asyncSignList = new AsyncSignList(this, mHandler, dialog, MESSAGE_GET_SIGNLIST, param);
        asyncSignList.execute();
    }
    private void getSignListNoDialog(String param) {
        AsyncSignList asyncSignList = new AsyncSignList(this, mHandler,  MESSAGE_GET_SIGNLIST_NO_DIALOG, param);
        asyncSignList.execute();
    }

    private void test() {
        List<SignListBean> list = new ArrayList<SignListBean>();
        for (int i = 1; i <= 20; i++) {
            SignListBean bean = new SignListBean();
            bean.setSign_list_id("" + i);
            bean.setSign_list_name("小明" + i);
            bean.setSign_list_phone("1896666661" + i);
            bean.setSign_list_time("2001" + i);
            bean.setSign_list_status("报名成功");
            list.add(bean);
        }
        adapter.refreshData(list);
    }


    private void initView() {
        dao = new MySignListDao(this);

        lv_signlist = (ListView) findViewById(R.id.lv_signlist);
        adapter = new SignListAdapter(this);
        lv_signlist.setAdapter(adapter);
        lv_signlist.setOnItemClickListener(this);
        lv_signlist.setOnItemLongClickListener(this);
        et_signlist_search= (EditText) findViewById(R.id.et_signlist_search);
et_signlist_search.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //TODO:
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        list_act=  dao.queryListActIDAndPhoneOrName(act_id,et_signlist_search.getText().toString());
        adapter.refreshData(list_act);
    }

    @Override
    public void afterTextChanged(Editable s) {
        //TODO:
    }
});

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
                .setOnItemListener(new com.wevey.selector.dialog.DialogInterface.OnItemClickListener<NormalSelectionDialog>() {
                    @Override
                    public void onItemClick(NormalSelectionDialog dialog, View button, int position) {
                        switch (position){
                            case 0:
                                Intent intent = new Intent();
                                intent.setClass(getApplicationContext(), InvitationPersonActivity.class);
//                intent.setClass(this, InvitationUserActivity.class);

                                Bundle bundle = new Bundle();
                                bundle.putSerializable("bean", bean);
                                intent.putExtras(bundle);
                                startActivity(intent);
                              dialog1.dismiss();

                            break;

                        }
                    }  //监听item点击事件

                })
                .setCanceledOnTouchOutside(true)  //设置是否可点击其他地方取消dialog
                .build();

        ArrayList<String> s = new ArrayList<>();
        s.add("专属邀请函");
        dialog1.setDatas(s);
    }

        @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
      SignListBean bean= adapter.getItem(i);
            StartList(bean);


    }

    private void StartList(SignListBean bean) {
        int m=  bean.getmIndex();
        String name = jsonArray.getJSONObject(m).getString("UserName");
        String phone = jsonArray.getJSONObject(m).getString("Mobile");
        String unit = jsonArray.getJSONObject(m).getString("Company");
        String sex = jsonArray.getJSONObject(m).getString("Sex");
        String dep = jsonArray.getJSONObject(m).getString("Depart");
        String industry = jsonArray.getJSONObject(m).getString("Industry");
        String duty = jsonArray.getJSONObject(m).getString("Duty");
        String id_card = jsonArray.getJSONObject(m).getString("IDcard");
        String email = jsonArray.getJSONObject(m).getString("Email");
        String join_num = jsonArray.getJSONObject(m).getString("Num");
        String add = jsonArray.getJSONObject(m).getString("Address");
        String remark = jsonArray.getJSONObject(m).getString("Remark");
        String amount = jsonArray.getJSONObject(m).getString("Amount");
        String title = jsonArray.getJSONObject(m).getString("Title");
        String face_img = jsonArray.getJSONObject(m).getString("FaceImg");
        String VCode= jsonArray.getJSONObject(m).getString("VCode");
       String AdminRemark= jsonArray.getJSONObject(m).getString("AdminRemark");
        String Payment = jsonArray.getJSONObject(m).getString("Payment");

        Intent intent = new
            Intent(this, SignListUserActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("phone", phone);
        intent.putExtra("unit", unit);
        intent.putExtra("sex", sex);
        intent.putExtra("dep", dep);
        intent.putExtra("industry", industry);
        intent.putExtra("duty", duty);
        intent.putExtra("id_card", id_card);
        intent.putExtra("email", email);
        intent.putExtra("join_num", join_num);
        intent.putExtra("add", add);
        intent.putExtra("remark", remark);
        intent.putExtra("amount", amount);
        intent.putExtra("title", title);
        intent.putExtra("act_id", act_id);
        intent.putExtra("face_img", face_img);
        intent.putExtra("Payment", Payment);

        if(VCode!=null){
SPUtils.put(getApplicationContext(),"print_Vcode",VCode);}
        if(AdminRemark!=null){
            intent.putExtra("AdminRemark", AdminRemark);

            SPUtils.put(getApplicationContext(), "print_AdminRemark", AdminRemark);
        }
        SPUtils.put(getApplicationContext(),"print_name",bean.getSign_list_name());


        intent.putExtra("id", bean.getSign_list_id());

        JSONObject jsonObject2 = null;
        if (JSON.parseObject(jsonArray.getJSONObject(m).getString("ExtraInfo")) != null) {
            jsonObject2 = JSON.parseObject(jsonArray.getJSONObject(m).getString("ExtraInfo"));
            ToastUtil.print(            jsonArray.getJSONObject(m).getString("ExtraInfo"));

            String extra = jsonObject2.toString();
            intent.putExtra("extra", jsonArray.getJSONObject(m).getString("ExtraInfo"));
        }
        startActivity(intent);
    }

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {

                    case MESSAGE_SEND_SIGNLIST_EMAIL:
                        String result2 = (String) msg.obj;
                        ToolUserBean bean = (ToolUserBean) JSON.parseObject(result2, ToolUserBean.class);
                        Toast.makeText(getApplicationContext(), bean.getMsg(), Toast.LENGTH_LONG).show();
                        break;

                    case MESSAGE_GET_SIGNLIST:
                        String result = (String) msg.obj;
                        JSONObject jsonObj = JSON.parseObject(result);
                        String message = jsonObj.getString("Res");
                        if (message.equals("0")){
    SPUtils.put(getApplicationContext(), "listup_" + act_id, result);
                            dao.deleteTable();
    init();
                        ToastUtil.makeText(getApplicationContext(),"刷新成功！");
                        }

                        break;

                    case MESSAGE_GET_SIGNLIST_NO_DIALOG:
                         result = (String) msg.obj;
                         jsonObj = JSON.parseObject(result);
                         message = jsonObj.getString("Res");
                        if (message.equals("0")){
                            SPUtils.put(getApplicationContext(), "listup_" + act_id, result);
                            dao.deleteTable();
                            init();}
                    default:
                        break;
                }
            }
        };     }
    private void sendSignListByEmail(String email,String act_id ){
        Dialog dialog = ProgressDialogUtils.showProgressDialog(this, getString(R.string.progress_title), getString(R.string.progress_message));
        AsyncSignlistEmail getCode = new AsyncSignlistEmail(this, mHandler,dialog, MESSAGE_GET_CODE, email,act_id);
        getCode.execute();

    }
    public void sendEmail() {

        //LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化
        LayoutInflater factory = LayoutInflater.from(this);
        //把activity_login中的控件定义在View中
        final View textEntryView = factory.inflate(R.layout.dialog_email, null);

        //将LoginActivity中的控件显示在对话框中
        new AlertDialog.Builder(this)
                //对话框的标题
                .setTitle("发送报名名单到邮箱")
                //设定显示的View
                .setView(textEntryView)
                //对话框中的“登陆”按钮的点击事件
                .setPositiveButton("发送", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        //获取用户输入的“用户名”，“密码”
                        //注意：textEntryView.findViewById很重要，因为上面factory.inflate(R.layout.activity_login, null)将页面布局赋值给了textEntryView了
                        final EditText etPassword = (EditText) textEntryView.findViewById(R.id.et_dialog_password);

                        //将页面输入框中获得的“用户名”，“密码”转为字符串
                        String email = etPassword.getText().toString();
if (email==null||email.equals("")){
    ToastUtil.makeText(getApplicationContext(),"邮箱不得为空！");
    return;
}
else{
    sendSignListByEmail(email,act_id);
}
                        //现在为止已经获得了字符型的用户名和密码了，接下来就是根据自己的需求来编写代码了
                        //这里做一个简单的测试，假定输入的用户名和密码都是1则进入其他操作页面（OperationActivity）

                    }
                })
                //对话框的“退出”单击事件
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                // 设置dialog是否为模态，false表示模态，true表示非模态
                .setCancelable(false)
                //对话框的创建、显示
                .create().show();
    }
    public void addSign(){

        Intent intent = new
                Intent(this, SignListUserAddActivity.class);

        intent.putExtra("act_id", act_id);
        intent.putExtra("UserInfo",UserInfo);
        intent.putExtra("ActivityFees",ActivityFees);
        JSONObject jsonObject2 = null;

        startActivity(intent);

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_signlist_email:
                sendEmail();
                break;
            case R.id.menu_signlist_upload:
                update();
                break;
            case R.id.menu_signlist_add:
                if (NetworkUtils.checkNetState(this)){
                addSign();}
                else ToastUtil.makeText(this,R.string.net_error);
                break;
        }
        return false;
    }

    private void update() {
        if (act_id == null) {
            act_id = ((String) SPUtils.get(this, "Act_id_now", ""));
        }
        String mParam = "ActivityID=" + act_id + "&pageSize=" + PAGE_SIZE;
        getSignList(mParam);
    }
    private void updateNoDialog() {
        if (act_id == null) {
            act_id = ((String) SPUtils.get(this, "Act_id_now", ""));
        }
        String mParam = "ActivityID=" + act_id + "&pageSize=" + PAGE_SIZE;
        getSignListNoDialog(mParam);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
         bean= adapter.getItem(position);
        int m=  bean.getmIndex();
        bean.setVCode(jsonArray.getJSONObject(m).getString("VCode"));
        ToastUtil.print("vcode"+bean.getVCode());

        dialog1.show();

        return true;
    }
}