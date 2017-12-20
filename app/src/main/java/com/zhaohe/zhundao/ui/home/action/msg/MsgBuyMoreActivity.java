package com.zhaohe.zhundao.ui.home.action.msg;

import android.os.Bundle;

import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.ui.WebViewBase;

public class MsgBuyMoreActivity extends WebViewBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBarNew("短信充值明细", R.layout.web_view_base);
        init(setUrl());
    }

    @Override
    public String setUrl() {
        String accesskey = (String) SPUtils.get(this, "accessKey", "");

        String url = "http://sms.zhundao.com.cn/wx/android/" + accesskey + "#/charged";
        return url;
    }
}
