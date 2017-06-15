package com.zhaohe.zhundao.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.zhaohe.zhundao.R;


/**
 * @Description:
 * @Author: 邹苏启
 * @Since: 2016/11/28 下午10:42
 */

public class TextFragment extends Fragment {

    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 创建碎片布局
        rootView = getLayoutInflater(null).inflate(R.layout.textlayout, null);

    }
}
