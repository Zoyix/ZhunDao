package com.zhaohe.zhundao.ui.home.mine.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.InfAdapter;
import com.zhaohe.zhundao.asynctask.AsyncInf;
import com.zhaohe.zhundao.bean.InfBean;
import com.zhaohe.zhundao.ui.ToolBarActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfActivity extends ToolBarActivity implements AdapterView.OnItemClickListener{
    @BindView(R.id.lv_inf)
    ListView lvInf;
    private Handler mHandler;
    private InfAdapter adapter;
    public static final int MESSAGE_INF_LIST = 100;
private Map<String,String> map=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inf);
        initToolBarNew("准到通知", R.layout.activity_inf);
        ButterKnife.bind(this);
        initView();
        initHandler();
getList();

    }
    @Override
    public void onResume() {
        super.onResume();
        if (SPUtils.contains(this,"inf_result")){
            init((String) SPUtils.get(this,"inf_result",""));
        }
    }

    private void getList(){
        AsyncInf async=new AsyncInf(this,mHandler,MESSAGE_INF_LIST);
        async.execute();
    }

    private void initView() {
adapter= new InfAdapter(this);
lvInf.setAdapter(adapter);
        lvInf.setOnItemClickListener(this);

        map=getMap();

    }

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_INF_LIST:
                        String result = (String) msg.obj;
                        JSONObject jsonObj = JSON.parseObject(result);
                        if (jsonObj.getString("Res") == "0") {
                            init(result);
                            SPUtils.put(getApplicationContext(),"inf_result",result);
                        }


                        break;

                    default:
                        break;
                }
            }
        };
    }

    private void init(String result) {
        if ((result == null) || (result == "")) {
            ToastUtil.makeText(this, "请联网后再试");
        } else {
            JSONObject jsonObj = JSON.parseObject(result);
            JSONArray jsonArray = jsonObj.getJSONArray("Data");
            List<InfBean> list = new ArrayList<InfBean>();
            for (int i = 0; i < jsonArray.size(); i++) {
                InfBean bean=new InfBean();
                bean.setTitle(jsonArray.getJSONObject(i).getString("Title"));
                String time=jsonArray.getJSONObject(i).getString("AddTime");
               String newtime = time.replace("T", " ");
             String   newtime1 = newtime.substring(2, newtime.length() - 3);
                bean.setAddTime(newtime1);
                bean.setSortName(jsonArray.getJSONObject(i).getString("SortName"));
                bean.setDetail(jsonArray.getJSONObject(i).getString("Detail"));
                bean.setmID(jsonArray.getJSONObject(i).getString("ID"));
                if (getMap().containsKey(jsonArray.getJSONObject(i).getString("ID"))){
                    bean.setRead(true);
                }
                list.add(bean);
            }
            adapter.refreshData(list);

        }
        ;
    }

    private void saveMap(Map<String,String> map){
        String result=JSONObject.toJSONString(map);
        ToastUtil.print("map"+result);
        SPUtils.put(this,"map_inf",result);
    }

    private Map<String,String> getMap(){
        String result= (String) SPUtils.get(this,"map_inf","{\"x\":\"y\"}");
        map=JSONObject.parseObject(result,Map.class);

        return map;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

       InfBean bean= adapter.getItem(position);
        map.put(bean.getmID(),"1");
        saveMap(map);
        Intent intent = new Intent();
        intent.setClass(this, NewsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", bean);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }
}
