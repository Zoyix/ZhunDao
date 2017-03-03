package com.zhaohe.zhundao.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaohe.zhundao.R;

public abstract class BaseActivity extends AppCompatActivity {

    protected String TAG = this.getClass().getSimpleName();
    protected Toolbar toolbar;
    protected TextView mToolBarTitleLabel;

    @Override
    protected void onCreate(Bundle saveInstance) {
        super.onCreate(saveInstance);
        setContentView();
        initializeView();
        initializeData(saveInstance);
    }


    /**
     * 1. 设置布局
     */
    protected abstract void setContentView();

    /**
     * 2. 初始化布局
     */
    protected void initializeView() {
        if (findViewById(R.id.toolbar2) != null) {
            toolbar = (Toolbar) findViewById(R.id.toolbar2);
            setSupportActionBar(toolbar);
            if (hasBackIcon()) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            if (isCenter()) {
                mToolBarTitleLabel = (TextView) findViewById(R.id.mToolBarTitleLabel);
            }
        }
    }

    /**
     * 3. 初始化ui数据
     */
    protected abstract void initializeData(Bundle saveInstance);


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finishActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    public void finishActivity() {
        finish();
    }

    /**
     * 是否有回退功能
     *
     * @return
     */
    protected boolean hasBackIcon() {
        return true;
    }

    protected boolean isCenter() {
        return true;
    }


    /**
     * 重写父类的setTitle方法根据当前标题显示是否居中做相应处理
     *
     * @param title
     */
    @Override
    public void setTitle(CharSequence title) {
        if (mToolBarTitleLabel != null && isCenter()) {
            mToolBarTitleLabel.setText(title);
            super.setTitle("");
        } else {
            super.setTitle(title);
        }
    }

    public void setContentView(int layoutId) {
        setContentView(layoutId, true);
    }

    /**
     * 容器模版
     *
     * @param layoutId         内容视图
     * @param isContainerTitle true 带有toolbar的布局容器 false无toolbar 你可以自定义标题实现复杂的title
     */
    protected void setContentView(int layoutId, boolean isContainerTitle) {
        if (isContainerTitle) {
            LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.activity_base, null);
            LayoutInflater.from(this).inflate(layoutId, root);
            super.setContentView(root);
        } else {
            super.setContentView(layoutId);
        }
    }
}
