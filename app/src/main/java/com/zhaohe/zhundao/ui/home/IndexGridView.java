package com.zhaohe.zhundao.ui.home;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @Description:首页的gridview
 * @Author:xuhaijiang
 * @Since:2014年9月22日下午3:39:32
 */
public class IndexGridView extends GridView {

    public IndexGridView(Context context) {
        super(context);
    }

    public IndexGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IndexGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
