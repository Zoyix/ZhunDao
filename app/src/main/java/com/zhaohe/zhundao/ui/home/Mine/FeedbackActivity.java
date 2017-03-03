package com.zhaohe.zhundao.ui.home.mine;

import android.os.Bundle;
import android.webkit.WebView;

import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.ui.ToolBarActivity;
import com.zhaohe.zhundao.ui.ToolBarHelper;

public class FeedbackActivity extends ToolBarActivity {
    private WebView webView;
    private String url = "https://baoming.app.joinhead.com/SignUp/?WXID=9C1017ABB0C8A2FE&SignNo=4a05e6215b829240";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initToolBar("意见反馈", R.layout.activity_feedback);
        initView();
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.wv_feedback);
//        String accesskey= (String) SPUtils.get(this,"ACCESSKEY","");
        webView.loadUrl(url);
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
}
