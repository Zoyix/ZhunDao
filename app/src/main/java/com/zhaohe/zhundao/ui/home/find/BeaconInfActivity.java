package com.zhaohe.zhundao.ui.home.find;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.squareup.picasso.Picasso;
import com.zhaohe.app.utils.NetworkUtils;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.asynctask.AsyncBeaconBond;
import com.zhaohe.zhundao.asynctask.AsyncBeaconUpdateAction;
import com.zhaohe.zhundao.ui.ToolBarActivity;
import com.zhaohe.zhundao.ui.ToolBarHelper;

import java.util.ArrayList;
import java.util.List;

public class BeaconInfActivity extends ToolBarActivity implements View.OnClickListener,Toolbar.OnMenuItemClickListener {
private String IconUrl,BeaconName,BeaconID,DeviceID,NickName,AddTime;
    private TextView tv_beacon_name,tv_beacon_id,tv_beacon_device_id,tv_beacon_nickname,tv_beacon_time;
    private ImageView iv_beacon_icon;
    private RelativeLayout rl_beacon_bind;
    private List<String> list_act=new ArrayList<String>() ;
    private List<String> list_sign=new ArrayList<String>() ;

    public static final int MESSAGE_UPDATE_BEACON = 97;
    public static final int MESSAGE_ADD_BEACON = 88;


    private String[] act_id=new String[1000];
    private String[] sign_id=new String[1000];
private String name;
    private ArrayAdapter<String> adapter;
    private Handler mHandler;
    private String ID;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_inf);
        initToolBar("设备详情",R.layout.activity_beacon_inf);
        initHandler();
        initIntent();
        initView();
        initlist();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu_beacon_delete, menu);
        toolbar.setOnMenuItemClickListener(this);

        return true;
    }


    private void initlist() {
        String result=(String) SPUtils.get(this, "act_result_on", "");
        JSONObject jsonObj = JSON.parseObject(result);
        JSONArray jsonArray = jsonObj.getJSONArray("Data");


        for (int i = 0; i < jsonArray.size(); i++){
            list_act.add(jsonArray.getJSONObject(i).getString("Title"));
            act_id[i]=jsonArray.getJSONObject(i).getString("ID");
        }

        String result2=(String) SPUtils.get(this, "sign_result", "");
        JSONObject jsonObj2 = JSON.parseObject(result2);
        JSONArray jsonArray2 = jsonObj2.getJSONArray("Data");


        for (int i = 0; i < jsonArray2.size(); i++){
            if (jsonArray2.getJSONObject(i).getString("Name")==null){
                list_sign.add("暂无");

            }
            else
            {list_sign.add(jsonArray2.getJSONObject(i).getString("Name"));}

            sign_id[i]=jsonArray2.getJSONObject(i).getString("ID");

        }

    }

    private void initView() {
        tv_beacon_name = (TextView) findViewById(R.id.tv_beacon_name);
        tv_beacon_name.setText(BeaconName);
        tv_beacon_id = (TextView) findViewById(R.id.tv_beacon_id);
        tv_beacon_id.setText(BeaconID);
        tv_beacon_device_id = (TextView) findViewById(R.id.tv_beacon_device_id);
        tv_beacon_device_id.setText(DeviceID);
        tv_beacon_nickname = (TextView) findViewById(R.id.tv_beacon_nickname);
        tv_beacon_nickname.setText(NickName);
        tv_beacon_time = (TextView) findViewById(R.id.tv_beacon_time);
        tv_beacon_time .setText(AddTime);
        iv_beacon_icon = (ImageView) findViewById(R.id.iv_beacon_icon);
        Picasso.with(this).load(IconUrl).error(R.mipmap.ic_launcher).into(iv_beacon_icon);
        rl_beacon_bind= (RelativeLayout) findViewById(R.id.rl_beacon_bind);
        rl_beacon_bind.setOnClickListener(this);
        button= (Button) findViewById(R.id.btn_find_beacon_delete);
        button.setOnClickListener(this);


    }

    private void initIntent() {
        Intent intent = getIntent();
        IconUrl = intent.getStringExtra("IconUrl");
        BeaconName = intent.getStringExtra("BeaconName");
        BeaconID = intent.getStringExtra("BeaconID");
        DeviceID = intent.getStringExtra("DeviceId");
        NickName = intent.getStringExtra("NickName");
        ID=intent.getStringExtra("ID");
        AddTime=intent.getStringExtra("AddTime");

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
    private void updateBeacon(String mParam ){
        AsyncBeaconUpdateAction async=new AsyncBeaconUpdateAction(this, mHandler, MESSAGE_UPDATE_BEACON, mParam);
        async.execute();
    }

    private void beaconBond(String result,String type)
    {
//     type   0绑定摇一摇设备 1解除绑定摇一摇  result deviceID
        AsyncBeaconBond Async = new AsyncBeaconBond(this, mHandler, MESSAGE_ADD_BEACON, result, type);
        Async.execute();
    }
    public void selectDialog() {

        //LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化

        new AlertDialog.Builder(this)
                //对话框的标题
                .setTitle("选择类别")
                //设定显示的View
                //对话框中的“登陆”按钮的点击事件
                .setPositiveButton("活动", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        actDialog();

                    }

                })
                .setNegativeButton("签到", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        signDialog();
                    }
                })
                // 设置dialog是否为模态，false表示模态，true表示非模态
                .setCancelable(true)
                //对话框的创建、显示
                .create().show();

    }
//    private void actDialog() {
//        BottomDialog.create(getSupportFragmentManager())
//                .setViewListener(new BottomDialog.ViewListener() {
//                    @Override
////                    自定义事件
//                    public void bindView(View v) {
//                        final Spinner sp= (Spinner) v.findViewById(R.id.sp_beacon);
//                        adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,list_act);
//                        sp.setAdapter(adapter);
//                        View.OnClickListener onclick = (new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                switch (v.getId()) {
//                                    case R.id.btn_beacon_submit:
//
//                                     int i=sp.getSelectedItemPosition();
//                                     name= sp.getSelectedItem().toString();
//                                        String PageUrl="https://m.zhundao.net/event/"+act_id[i];
//                                        String mParam = "ID=" + ID + "&PageUrl=" + PageUrl + "&NickName=" + name;
//                                        System.out.println(mParam);
//
//                                        updateBeacon(mParam);
//                                        break;
//                                    default:
//                                        break;
//                                }
//                            }
//
//                        });
//
//                        Button btn_beacon_submit= (Button) v.findViewById(R.id.btn_beacon_submit);
//                        btn_beacon_submit.setOnClickListener(onclick);
//
//                    }
//                })
//                .setLayoutRes(R.layout.dialog_spinner)
//                .setDimAmount(0.9f)
//                .setTag("SelectDialog")
//                .show();
//    }
    public void actDialog() {

        //LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化
        LayoutInflater factory = LayoutInflater.from(this);
        //把activity_login中的控件定义在View中
        final View v = factory.inflate(R.layout.dialog_spinner, null);
        final Spinner sp= (Spinner) v.findViewById(R.id.sp_beacon);
        adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dialog_item_beacon,list_act);
        //将LoginActivity中的控件显示在对话框中
        sp.setAdapter(adapter);

        new AlertDialog.Builder(this)
                //对话框的标题
                .setTitle("绑定活动事件")
                //设定显示的View
                .setView(v)
                //对话框中的“登陆”按钮的点击事件
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        int i=sp.getSelectedItemPosition();
                                     name= sp.getSelectedItem().toString();
                                        String PageUrl="https://m.zhundao.net/event/"+act_id[i];
                                        String mParam = "ID=" + ID + "&PageUrl=" + PageUrl + "&NickName=" + name;
                                        System.out.println(mParam);

                                        updateBeacon(mParam);
                    }

                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                // 设置dialog是否为模态，false表示模态，true表示非模态
                .setCancelable(false)
                //对话框的创建、显示
                .create().show();

    }

    public void signDialog() {

        //LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化
        LayoutInflater factory = LayoutInflater.from(this);
        //把activity_login中的控件定义在View中
        final View v = factory.inflate(R.layout.dialog_spinner, null);
        final Spinner sp= (Spinner) v.findViewById(R.id.sp_beacon);
        adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dialog_item_beacon,list_sign);
        //将LoginActivity中的控件显示在对话框中
        sp.setAdapter(adapter);

        new AlertDialog.Builder(this)
                //对话框的标题
                .setTitle("绑定签到事件")
                //设定显示的View
                .setView(v)
                //对话框中的“登陆”按钮的点击事件
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        int i=sp.getSelectedItemPosition();
                                         name= sp.getSelectedItem().toString();
                                        String PageUrl="https://m.zhundao.net/inwechat/CheckInForBeacon?checkInId="+sign_id[i]+"&checkInWay=1";
                                        String mParam = "ID=" + ID + "&PageUrl=" + PageUrl + "&NickName=" + name;
                                        System.out.println(mParam);
                                        updateBeacon(mParam);
                    }

                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
                    case MESSAGE_UPDATE_BEACON:
                        String result = (String) msg.obj;
                        JSONObject jsonObj = JSON.parseObject(result);
                        String message = jsonObj.getString("Res");
                        if (message.equals("0"))
                        //添加或修改请求结果
                        {
                            tv_beacon_nickname.setText(name);
                            ToastUtil.makeText(getApplication(), "绑定成功");
                        }

                        break;

                    case MESSAGE_ADD_BEACON:
                        String result3 = (String) msg.obj;
                        JSONObject jsonObj2 = JSON.parseObject(result3);
                        String message2 = jsonObj2.getString("Msg");
                        ToastUtil.makeText(getApplicationContext(),message2);
                        if (jsonObj2.getString("Res").equals("0")){
                            BeaconInfActivity.this.finish();
                        }

                        break;
                    default:
                        break;
                }
            }
        };
    }
    public void beaconUnBondDialog() {

        //LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化

        new AlertDialog.Builder(this)
                //对话框的标题
                .setTitle("确认要解除摇一摇设备的绑定吗？")
                //设定显示的View
                //对话框中的“登陆”按钮的点击事件
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        beaconBond(DeviceID,"1");

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_beacon_bind:
                if (NetworkUtils.checkNetState(this))
                { selectDialog();}
                else
                {ToastUtil.makeText(this,"暂无网络，请确认后再试！");
                return;
                }

                break;
//           case R.id.btn_find_beacon_delete:
//               beaconUnBondDialog();
            default:
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
switch (item.getItemId()) {
    case R.id.menu_beacon_delete:
        beaconUnBondDialog();
        break;
}
        return false;
    }
}
