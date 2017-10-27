package com.zhaohe.zhundao.ui.home.mine.contacts.group;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class textActivity extends AppCompatActivity {

    @BindView(R.id.tv_teset)
    TextView tvTeset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        ButterKnife.bind(this);
        tvTeset.setText("2");
    }

    @OnClick(R.id.tv_teset)
    public void onViewClicked() {
        ToastUtil.makeText(this, "5");
    }
}
