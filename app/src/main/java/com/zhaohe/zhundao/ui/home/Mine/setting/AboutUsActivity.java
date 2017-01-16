package com.zhaohe.zhundao.ui.home.mine.setting;

import android.os.Bundle;
import android.webkit.WebView;

import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.ui.ToolBarActivity;
import com.zhaohe.zhundao.ui.ToolBarHelper;

public class AboutUsActivity extends ToolBarActivity {
    private WebView webView;
    private String url = "https://m.zhundao.net/html/aboutus.html";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initToolBar("关于准到",R.layout.activity_about_us);
        initView();
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.wv_about_us);
//        String accesskey= (String) SPUtils.get(this,"ACCESSKEY","");
        webView.loadUrl(url);

    }
    private void initToolBar(String text,int layoutResID){
        ToolBarHelper mToolBarHelper ;
        mToolBarHelper = new ToolBarHelper(this,layoutResID) ;
        mToolBarHelper.setTvTitle(text);
        super.setTitle("");
        setContentView(mToolBarHelper.getContentView());
        toolbar = mToolBarHelper.getToolBar() ;
  /*把 toolbar 设置到Activity 中*/
        setSupportActionBar(toolbar);
    }
}
