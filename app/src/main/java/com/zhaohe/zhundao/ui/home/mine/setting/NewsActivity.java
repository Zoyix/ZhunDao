package com.zhaohe.zhundao.ui.home.mine.setting;

import android.content.Intent;
import android.os.Bundle;

import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.bean.InfBean;
import com.zhaohe.zhundao.ui.WebViewBase;

public class NewsActivity extends WebViewBase {
private InfBean bean;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_base);
        initIntent();
        initToolBarNew(bean.getTitle(),R.layout.web_view_base);
        init(setUrl());
    }

    @Override
    public String setUrl() {
        return url;
    }

    private void initIntent() {
        Intent intent = this.getIntent();
        bean = (InfBean) intent.getSerializableExtra("bean");
        url="http://www.zhundao.net/other/notice/index/"+bean.getmID();
    }
}
