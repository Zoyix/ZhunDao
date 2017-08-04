package com.zhaohe.zhundao.ui.home.action;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zhaohe.app.utils.NetworkUtils;
import com.zhaohe.app.utils.ProgressDialogUtils;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.TimeUtil;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.ActionAdapter;
import com.zhaohe.zhundao.asynctask.action.AsyncAction;
import com.zhaohe.zhundao.asynctask.AsyncSignList;
import com.zhaohe.zhundao.bean.ActionBean;
import com.zhaohe.zhundao.constant.Constant;
import com.zhaohe.zhundao.ui.home.action.more.ActionMoreActivity;
import com.zhaohe.zhundao.ui.home.action.more.ActionSignActivity;

import java.util.ArrayList;
import java.util.List;

import me.shaohui.bottomdialog.BottomDialog;

import static android.view.View.GONE;
import static com.zhaohe.zhundao.constant.Constant.Url.ShareUrl;
import static com.zhaohe.zhundao.ui.home.action.ActionOnFragment.MESSAGE_ACT_ALL;
import static com.zhaohe.zhundao.ui.home.action.ActionOnFragment.MESSAGE_GET_SIGNLIST;
import static com.zhaohe.zhundao.ui.home.action.ActionOnFragment.REFRESH_COMPLETE;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2016/12/12 14:42
 */
public class ActionOffFrgment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, ActionAdapter.ActionClickListener {
    //            单页显示的数据数目
    public static final int PAGE_SIZE = 100000;
    protected View rootView;
    private IWXAPI api;
    private ActionAdapter adapter;
    private List<ActionBean> list_act;
    private ListView lv_act;
    private Handler mHandler;
    private ImageView img_share;
    private String url;
    private SwipeRefreshLayout mSwipeLayout;
    private TextView tv_actoff_suggest;
    private UMShareListener mShareListener;
    private String UserInfo;
    private String ActivityFees;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_actoff,
                null);
        initHandler();
        initWx();
        initView();
        init();

//        test();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }

    private void init() {
        if (SPUtils.contains(getActivity(),"act_result")) {
            jsonconver((String) SPUtils.get(getActivity(), "act_result", ""));
            getActionListNoneDialog();

        } else {
//            getActionList();
            getActionListNoneDialog();

        }
    }

    private void getActionList() {
        Dialog dialog = ProgressDialogUtils.showProgressDialog(getActivity(), getString(R.string.progress_title), getString(R.string.progress_message));
        AsyncAction asyncActivity = new AsyncAction(getActivity(), mHandler, dialog, MESSAGE_ACT_ALL);
        asyncActivity.execute();
    }
    private void getActionListNoneDialog() {
        AsyncAction asyncActivity = new AsyncAction(getActivity(), mHandler, MESSAGE_ACT_ALL);
        asyncActivity.execute();
    }

    private void initWx() {
        api = WXAPIFactory.createWXAPI(getActivity(), Constant.APP_ID, true);
        api.registerApp(Constant.APP_ID);
    }

    private void jsonconver(String result) {
        JSONObject jsonObj = JSON.parseObject(result);
        JSONArray jsonArray = jsonObj.getJSONArray("Data");
        List<ActionBean> list = new ArrayList<ActionBean>();
        for (int i = 0; i < jsonArray.size(); i++) {
            ActionBean bean = new ActionBean();
            bean.setAct_title(jsonArray.getJSONObject(i).getString("Title"));
            bean.setClick_num(jsonArray.getJSONObject(i).getString("ClickNo"));

            String time = jsonArray.getJSONObject(i).getString("TimeStop");
//            去除json传回来的时间中的T字符
            String newtime = time.replace("T", " ");
            bean.setAddress(jsonArray.getJSONObject(i).getString("Address"));

//             去除秒
            String newtime1 = newtime.substring(2, newtime.length() - 3);
            bean.setAct_endtime("报名截止：" + newtime1);
            String comparetime = TimeUtil.getTimeDifference(newtime, TimeUtil.getNowTime());
            if(comparetime.indexOf("-")!=-1){
                String newtime3=comparetime.replace("-","");
                bean.setAct_resttime("(还剩" + newtime3 + ")");

            }
            else{bean.setAct_resttime("(结束" + comparetime + ")");}
            time = jsonArray.getJSONObject(i).getString("TimeStart");
            newtime = time.replace("T", " ");
            newtime1 = newtime.substring(2, newtime.length() - 3);
            bean.setAct_starttime("活动结束：" + newtime1);
            comparetime = TimeUtil.getTimeDifference(newtime, TimeUtil.getNowTime());
            if(comparetime.indexOf("-")!=-1){
                String newtime3=comparetime.replace("-","");
                bean.setAct_resttime2("(还剩" + newtime3 + ")");
            }
            else{bean.setAct_resttime2("(结束" + comparetime + ")");}
            bean.setAct_sign_num(jsonArray.getJSONObject(i).getString("HasJoinNum"));
            bean.setAct_sign_income(jsonArray.getJSONObject(i).getString("Amount"));
            bean.setAct_status("  报名截止");
            bean.setAct_content(jsonArray.getJSONObject(i).getString("Content"));
            bean.setUrl(jsonArray.getJSONObject(i).getString("ShareImgurl"));
            bean.setAct_id(jsonArray.getJSONObject(i).getString("ID"));
            bean.setBaseItem(jsonArray.getJSONObject(i).getString("UserInfo"));
            bean.setActivityFees(jsonArray.getJSONObject(i).getString("ActivityFees"));
////            获取活动报名人数不为0的活动名单
//            if (jsonArray.getJSONObject(i).getString("HasJoinNum")!="0") {
////            获取报名列表活动ID
//                String mParam = "ActivityID=" + bean.getAct_id();
//                getSignupList(mParam);
//            }
            if (jsonArray.getJSONObject(i).getString("Status") .equals("2")) {
                list.add(bean);
            } else {

            }
        }
        showSuggest(list);
        adapter.refreshData(list);
    }

    private void showSuggest(List<ActionBean> list) {
        if (list.size() == 0) {
            lv_act.setVisibility(GONE);
            tv_actoff_suggest.setVisibility(View.VISIBLE);
        } else {
            lv_act.setVisibility(View.VISIBLE);
            tv_actoff_suggest.setVisibility(GONE);
        }

    }

    private void getSignList(String act_id) {
        Dialog dialog = ProgressDialogUtils.showProgressDialog(getActivity(), getString(R.string.progress_title), getString(R.string.progress_message));
        AsyncSignList asyncSignList = new AsyncSignList(getActivity(), mHandler, dialog, MESSAGE_GET_SIGNLIST, act_id);
        asyncSignList.execute();
    }

    private void wxShare(int flag, ActionBean bean) {
        String actid = bean.getAct_id();
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = Constant.Url.ShareUrl + actid;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = bean.getAct_title();
        msg.description = bean.getAct_starttime()+"\n活动地点： "+bean.getAddress();
        //这里替换一张自己工程里的图片资源
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.logo_multi);
        msg.setThumbImage(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

    private void gotoSignList(String result) {
        Intent intent = new
                Intent(getActivity(), SignListActivity.class);
        JSONObject jsonObj = JSON.parseObject(result);
        if (result == null) {
            return;
        }
        if (jsonObj.getByte("Count") == 0) {
            ToastUtil.makeText(getActivity(), "暂无人报名");
            return;
        } else {
            JSONArray jsonArray = jsonObj.getJSONArray("Data");
            String act_id = jsonArray.getJSONObject(0).getString("ActivityID");
            SPUtils.put(getActivity(), "listup_" + act_id, result);
            //在Intent对象当中添加一个键值对
            intent.putExtra("act_id", act_id);
            intent.putExtra("UserInfo",UserInfo);
            intent.putExtra("ActivityFees",ActivityFees);
            startActivity(intent);
        }
    }

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_ACT_ALL:
                        String result = (String) msg.obj;

                        System.out.println("Activity result:  " + result);
                        //活动列表结果
                        if (NetworkUtils.checkNetState(getActivity())) {
                            SPUtils.put(getActivity(), "act_result", result);
                            jsonconver((String) SPUtils.get(getActivity(), "act_result", ""));
                        }

                        break;
                    case REFRESH_COMPLETE:
                        mSwipeLayout.setRefreshing(false);
                        break;
                    case MESSAGE_GET_SIGNLIST:
                        String result2 = (String) msg.obj;
                        System.out.println("SignupList result:  " + result2);
                        if (NetworkUtils.checkNetState(getActivity())) {
                        }
                        gotoSignList(result2);
                        break;
//                        savelistup(result2);


                    default:
                        break;
                }
            }
        };
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

    public void initView() {
//        btn_test= (Button) rootView.findViewById(btn_test);
//        btn_test.setOnClickListener(this);
        lv_act = (ListView) rootView.findViewById(R.id.lv_act2);
        adapter = new ActionAdapter(getActivity());
        adapter.setActionClickListener(this);
        lv_act.setAdapter(adapter);
        mSwipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_ly2);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        tv_actoff_suggest = (TextView) rootView.findViewById(R.id.tv_actoff_suggest);
        mShareListener = new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {
                //分享开始的回调
            }
            @Override
            public void onResult(SHARE_MEDIA platform) {
                Log.d("plat","platform"+platform);

                Toast.makeText(getActivity(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                Toast.makeText(getActivity(),platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                if(t!=null){
                    Log.d("throw","throw:"+t.getMessage());
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                Toast.makeText(getActivity(),platform + " 分享取消了", Toast.LENGTH_SHORT).show();
            }
        };

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case btn_test:
//                IntentUtils.startActivity(getActivity(), LoginActivity.class);
//                break;
            default:
                break;
        }


    }

    @Override
    public void onRefresh() {
        if (NetworkUtils.checkNetState(getActivity())) {
            init();
            System.out.print("成功刷新");
            mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
        } else {
            ToastUtil.makeText(getActivity(), "请联网后再试");
            mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
        }
    }

    @Override
    public void onSignClick(ActionBean bean) {
        Intent intent = new Intent(getActivity(), ActionSignActivity.class);
        Bundle  bundle = new Bundle();
        bundle.putString("act_id", bean.getAct_id());
        bundle.putString("act_title", bean.getAct_title());
        intent.putExtras(bundle);
        if (SPUtils.contains(getActivity(),"sign_result")==false) {
            if (NetworkUtils.checkNetState(getActivity())==false) {
                ToastUtil.makeText(getActivity(), R.string.net_error);
                return;
            }
        }
        this.startActivity(intent);
    }

    @Override
    public void onListClick(ActionBean bean) {
        ActivityFees=bean.getActivityFees();
        UserInfo=bean.getBaseItem();
//       这里开始写 判断网络状况
        if (NetworkUtils.checkNetState(getActivity())) {
            String mParam = "ActivityID=" + bean.getAct_id() + "&pageSize=" + PAGE_SIZE;
            getSignList(mParam);
        } else if (SPUtils.contains(getActivity(), "listup_" + bean.getAct_id()) == true) {
            Intent intent = new
                    Intent(getActivity(), SignListActivity.class);
            //在Intent对象当中添加一个键值对
            intent.putExtra("act_id", bean.getAct_id());
            startActivity(intent);

        } else {
            ToastUtil.makeText(getActivity(), "请联网后再试");
            return;
        }

    }

    @Override
    public void onShareClick(final ActionBean bean) {
        showDialog(bean);

    }

    //    微信分享底部对话框
    private void showDialog(final ActionBean bean) {
        BottomDialog.create(getFragmentManager())
                .setViewListener(new BottomDialog.ViewListener() {
                    @Override
//                    自定义事件
                    public void bindView(View v) {
                        View.OnClickListener onclick = (new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch (v.getId()) {
                                    case R.id.iv_share_wechat_solid:
                                        UmengShare(bean,SHARE_MEDIA.WEIXIN);
                                        break;
                                    case R.id.iv_share_weixin_friends_solid:
                                        UmengShare(bean,SHARE_MEDIA.WEIXIN_CIRCLE);
                                        break;
                                    case R.id.iv_share_weibo_solid:
                                        UmengShare(bean, SHARE_MEDIA.SINA);
                                        break;
                                    case R.id.iv_share_qq_solid:
                                        UmengShare(bean,SHARE_MEDIA.QQ);
                                        break;
                                    case R.id.iv_share_qqzone_solid:
                                        UmengShare(bean,SHARE_MEDIA.QZONE);

                                        break;
                                }
                            }
                        });
                        ImageView iv_share_wechat_solid = (ImageView) v.findViewById(R.id.iv_share_wechat_solid);
                        ImageView iv_share_weixin_friends_solid = (ImageView) v.findViewById(R.id.iv_share_weixin_friends_solid);
                        ImageView iv_share_weibo_solid= (ImageView) v.findViewById(R.id.iv_share_weibo_solid);
                        ImageView iv_share_qq_solid = (ImageView) v.findViewById(R.id.iv_share_qq_solid);
                        ImageView iv_share_qqzone_solid = (ImageView) v.findViewById(R.id.iv_share_qqzone_solid);

                        iv_share_weixin_friends_solid.setOnClickListener(onclick);
                        iv_share_wechat_solid.setOnClickListener(onclick);
                        iv_share_weibo_solid.setOnClickListener(onclick);
                        iv_share_qq_solid.setOnClickListener(onclick);
                        iv_share_qqzone_solid.setOnClickListener(onclick);
                    }
                })
                .setLayoutRes(R.layout.dialog_layout)
                .setDimAmount(0.2f)
                .setTag("BottomDialog")
                .show();
    }
    private void UmengShare(ActionBean bean,SHARE_MEDIA type ) {
        try {

            UMWeb web = new UMWeb("https://"+ShareUrl+bean.getAct_id());
            UMImage image = new UMImage(getActivity(), bean.getUrl());
            web.setTitle( bean.getAct_title());//标题
            web.setDescription(bean.getAct_starttime()+"\n活动地点： "+bean.getAddress());//描述
            web.setThumb(image);
//        new ShareAction(getActivity())
//                .withMedia(web)
//                .setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.QZONE)
//                .setCallback(mShareListener).open();
            new ShareAction(getActivity()).setPlatform(type)
                    .withMedia(web)
                    .setCallback(mShareListener)
                    .share();
        }
        catch (Exception e){
            ToastUtil.makeText(getActivity(),type.toString()+"异常，请暂时先使用更多选项中的复制链接进行手动分享");
            return;
        }
    }
    @Override
    public void onMoreClick(ActionBean bean) {
        ActivityFees=bean.getActivityFees();
        UserInfo=bean.getBaseItem();
        Intent intent = new Intent();
        intent.setClass(getActivity(), ActionMoreActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", bean);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

    @Override
    public void onDetailsClick(ActionBean bean) {
        Intent intent = new Intent(getActivity(), ActionDetailsActivity.class);

//        Intent intent = new Intent(getActivity(), EditActActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("act_id", bean.getAct_id());
        bundle.putString("act_title", bean.getAct_title());
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }
}


