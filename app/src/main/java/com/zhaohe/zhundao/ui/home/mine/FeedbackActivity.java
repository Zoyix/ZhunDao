package com.zhaohe.zhundao.ui.home.mine;

import android.os.Bundle;
import android.webkit.WebView;

import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.ui.WebViewBase;

public class FeedbackActivity extends WebViewBase {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_base);
        initToolBarNew("在线客服", R.layout.web_view_base);
        init(setUrl());


    }

    @Override
    public String setUrl() {
        String url = "http://p.qiao.baidu.com/cps/chat?siteId=9902661&userId=22227709";
        return url;
    }


}
