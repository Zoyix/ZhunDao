package com.zhaohe.zhundao.ui.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zhaohe.app.utils.IntentUtils;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.ui.home.Action.EditActActivity;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2016/11/29 10:24
 */
public class GroupFragment extends Fragment implements View.OnClickListener {
    protected View rootView;
    private Button btn_test;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = getLayoutInflater(null).inflate(R.layout.fragment_grp,
                null);

        initView(rootView);
//        test();
    }
    protected void initView(View rootView){
        btn_test= (Button) rootView.findViewById(R.id.test_time);
        btn_test.setOnClickListener(this);



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 在使用这个view之前首先判断其是否存在parent view，这调用getParent()方法可以实现。
        // 如果存在parent view，那么就调用removeAllViewsInLayout()方法
        Log.i("test", "AFragment-->onCreateView");
        ViewGroup perentView = (ViewGroup) rootView.getParent();
        if (perentView != null) {
            perentView.removeAllViewsInLayout();
        }
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.test_time:
                IntentUtils.startActivity(getActivity(),EditActActivity.class);
                break;
        }

    }
}

