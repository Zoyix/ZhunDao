package com.zhaohe.zhundao.ui.home.sign;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.ui.ToolBarActivity;
import com.zhaohe.zhundao.ui.ToolBarHelper;

public class SignUpdateTitleActivity extends ToolBarActivity {
    private WebView webView;
    private String url = "https://m.zhundao.net/CheckIn/UpdateCheckIn?checkinId=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_update_title);
        initToolBar("修改签到信息", R.layout.activity_sign_update_title);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        //从Intent当中根据key取得value
        String mSignID = intent.getStringExtra("mSignID");
        webView = (WebView) findViewById(R.id.wv_update_title);
        String accesskey = (String) SPUtils.get(this, "accessKey", "");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBlockNetworkImage(false);
        WebSettings webSettings = webView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webView.loadUrl(url + mSignID + "&accesskey=" + accesskey);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }


        });

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

