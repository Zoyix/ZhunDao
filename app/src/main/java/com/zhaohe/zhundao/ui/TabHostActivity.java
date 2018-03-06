package com.zhaohe.zhundao.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaohe.zhundao.R;

/**
 * @Description: TabHost Activity
 * @Author: 邹苏启
 * @Since: 2016/11/28 下午9:04
 */

/**
 * 该类是来实现底部导航栏的封装，是HomeActivity的基类
 */
public abstract class TabHostActivity extends FragmentActivity {

    protected FragmentTabHost mTabHost;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabhost);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(getApplicationContext(), getSupportFragmentManager(), R.id.realtabcontent);

        Class<?>[] tabFrag = getTabFragment();
        for (int i = 0, len = tabFrag.length; i < len; i++) {
            //第一个参数是设置TabSpec 即给每个Tab按钮设置图标、文字和内容
            //第二个参数是设置对应的fragment
            //TODO 第三个参数是啥？
            mTabHost.addTab(mTabHost.newTabSpec(getString(getTabwidgetTag()[i])).setIndicator(getTabItemView(i)), tabFrag[i], bundle);
        }
        mTabHost.setCurrentTab(0);
    }

    /**
     * 与tabwidget相对应的 碎片 数组
     *
     * @return
     */
    protected abstract Class<?>[] getTabFragment();

    /**
     * 与tabwidget相对应的 图片 数组
     *
     * @return
     */
    protected abstract int[] getTabwidgetDrawable();

    /**
     * 与tabwidget相对应的 Tag 数组
     *
     * @return Tab选项卡的文字
     */
    protected abstract int[] getTabwidgetTag();


    /**
     * 返回每个选项卡的view
     * @param index
     * @return
     */
    private View getTabItemView(int index) {
        View tabView = View.inflate(this, R.layout.tab_item_view, null);
        ImageView imgView = (ImageView) tabView.findViewById(R.id.tab_img);
        imgView.setBackgroundResource(getTabwidgetDrawable()[index]);
        TextView textView = (TextView) tabView.findViewById(R.id.tab_text);
        textView.setText(getTabwidgetTag()[index]);
        return tabView;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTabHost = null;
    }
}
