package com.zhaohe.zhundao.ui.home.mine;

import android.os.Bundle;

import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.ui.WebViewBase;

public class UpgradedActivity extends WebViewBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_base);
        initToolBarNew("会员升级",R.layout.web_view_base);
        init(setUrl());
    }

    @Override
    public String setUrl() {
        String accesskey = (String) SPUtils.get(this, "accessKey", "");

        String url="http://m.zhundao.net/Activity/Upgraded?accesskey="+accesskey;
return    url;
    }
}
