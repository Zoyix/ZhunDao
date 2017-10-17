package com.zhaohe.zhundao.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.swipebackactivity.app.SwipeBackActivity;
import com.umeng.analytics.MobclickAgent;
import com.zhaohe.zhundao.R;


/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2016/12/16 10:54
 */
public abstract class ToolBarActivity extends SwipeBackActivity {
    private ToolBarHelper mToolBarHelper;
    public Toolbar toolbar;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void setContentView(int layoutResID) {

        mToolBarHelper = new ToolBarHelper(this, layoutResID);
//                mToolBarHelper.setToolbarTitle("a");
        toolbar = mToolBarHelper.getToolBar();

        setContentView(mToolBarHelper.getContentView());
        /*把 toolbar 设置到Activity 中*/
        setSupportActionBar(toolbar);
        /*自定义的一些操作*/
        onCreateCustomToolBar(toolbar);

//setTitle("高兴");
//        mToolBarHelper.setToolbarTitle("a");

    }

    public void onCreateCustomToolBar(Toolbar toolbar) {
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        toolbar.setContentInsetsRelative(0, 0);
        toolbar.inflateMenu(R.menu.toolbar_act_details);

    }

    @Override
    protected void onResume() {
        /**
         * 设置为横屏
         */

        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onResume();
        MobclickAgent.onResume(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    public void initToolBarNew(String text, int layoutResID) {
        ToolBarHelper mToolBarHelper;
        mToolBarHelper = new ToolBarHelper(this, layoutResID);
        mToolBarHelper.setTvTitle(text);
        super.setTitle("");
        setContentView(mToolBarHelper.getContentView());
        toolbar = mToolBarHelper.getToolBar();
  /*把 toolbar 设置到Activity 中*/
        setSupportActionBar(toolbar);
    }

    public void initToolBarNew(String text, @ColorInt int colorId, int layoutResID) {
        ToolBarHelper mToolBarHelper;
        mToolBarHelper = new ToolBarHelper(this, layoutResID);
        mToolBarHelper.setTvTitle(text);
        mToolBarHelper.setToolBarColor(colorId);
        setStatusBarColor(colorId);

        super.setTitle("");
        setContentView(mToolBarHelper.getContentView());
        toolbar = mToolBarHelper.getToolBar();
  /*把 toolbar 设置到Activity 中*/
        setSupportActionBar(toolbar);
    }


}