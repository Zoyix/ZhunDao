package com.zhaohe.zhundao.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.zhaohe.zhundao.R;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2016/12/16 10:54
 */
public abstract class ToolBarActivity extends AppCompatActivity {
    private ToolBarHelper mToolBarHelper ;
    public Toolbar toolbar ;
    private TextView tvTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {

        mToolBarHelper = new ToolBarHelper(this,layoutResID) ;
//                mToolBarHelper.setToolbarTitle("a");
        toolbar = mToolBarHelper.getToolBar() ;

        setContentView(mToolBarHelper.getContentView());
        /*把 toolbar 设置到Activity 中*/
        setSupportActionBar(toolbar);
        /*自定义的一些操作*/
        onCreateCustomToolBar(toolbar) ;

//setTitle("高兴");
//        mToolBarHelper.setToolbarTitle("a");

    }

    public void onCreateCustomToolBar(Toolbar toolbar){
        tvTitle= (TextView)findViewById(R.id.tv_toolbar_title);
        toolbar.setContentInsetsRelative(0,0);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true ;
        }
        return super.onOptionsItemSelected(item);
    }
}