package com.zhaohe.zhundao.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhaohe.app.utils.IntentUtils;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.constant.Constant;
import com.zhaohe.zhundao.ui.TabHostActivity;
import com.zhaohe.zhundao.ui.home.Action.ActionAddActivity;
import com.zhaohe.zhundao.ui.home.Action.ActionFragment;
import com.zhaohe.zhundao.ui.home.Mine.MineFragment;
import com.zhaohe.zhundao.ui.home.Sign.SignFragment;
import com.zhaohe.zhundao.ui.home.Sign.SignupAddActivity;

/**
 * @Description:
 * @Author: 邹苏启
 * @Since: 2016/11/28 下午10:38
 */

public class HomeActivity extends TabHostActivity implements Toolbar.OnMenuItemClickListener{

    protected Class<?>[] tabFragments  = { ActionFragment.class, SignFragment.class, MineFragment.class};
    // Tab选项卡的文字
    private int[]        tabwidgetTags = { R.string.tab_act, R.string.tab_sig, R.string.tab_min };
    // Tab选项卡的按钮图片
    private int[]        tabItemViews  = { R.drawable.selector_home_tab_action, R.drawable.selector_home_tab_sign,R.drawable.selector_home_tab_min};

    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate (savedInstanceState);
        initToolbar();
        regToWechat();
    }




    private void regToWechat() {
        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID, true);
        api.registerApp(Constant.APP_ID);
    }
    //自定义toolbar 可以设置图标和功能
    protected void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolbar.setOnMenuItemClickListener(this);
//        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        toolbar.setTitle("");
//        返回
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }
    /**
     * 设置返回键不关闭应用,回到桌面
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //启动一个意图,回到桌面
            Intent backHome = new Intent(Intent.ACTION_MAIN);
            backHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            backHome.addCategory(Intent.CATEGORY_HOME);
            startActivity(backHome);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected Class<?>[] getTabFragment(){
        return tabFragments;
    }

    @Override
    protected int[] getTabwidgetDrawable() {
        return tabItemViews;
    }

    @Override
    protected int[] getTabwidgetTag(){
        return tabwidgetTags;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_action:
                IntentUtils.startActivity(this, ActionAddActivity.class);
                break;
            case R.id.menu_add_signup:
                IntentUtils.startActivity(this, SignupAddActivity.class);
                break;


        }
        return false;
    }
}
