package com.zhaohe.zhundao.ui.home.find;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.ui.ToolBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InvitationProActivity extends ToolBarActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.rb_invitation_type1)
    RadioButton rbInvitationType1;
    @BindView(R.id.rb_invitation_type2)
    RadioButton rbInvitationType2;
    @BindView(R.id.rg_invitation)
    RadioGroup rgInvitation;
    @BindView(R.id.tv_question)
    TextView tvQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBarNew("专属邀请函", R.layout.activity_invitation_pro);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        rgInvitation.setOnCheckedChangeListener(this);

        setcheck((int) SPUtils.get(this, "invitation_type", 1));
    }


    private void setcheck(int invitation_type) {
        switch (invitation_type) {
            case 1:
                rbInvitationType1.setChecked(true);
                break;
            case 2:
                rbInvitationType2.setChecked(true);


                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rb_invitation_type1:
                SPUtils.put(this, "invitation_type", 1);
                break;
            case R.id.rb_invitation_type2:
                SPUtils.put(this, "invitation_type", 2);

                break;
        }
    }

    private void questionDialog() {
        new android.support.v7.app.AlertDialog.Builder(this)
                //对话框的标题
                .setTitle("什么是专属邀请函？")
                //设定显示的View
                //对话框中的“登陆”按钮的点击事件
                .setMessage("关注准到微信公众号（微信号izhundao），发送关键词“专属邀请函”，了解准到相关功能")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }

                })

                // 设置dialog是否为模态，false表示模态，true表示非模态
                .setCancelable(true)
                //对话框的创建、显示
                .create().show();
    }

    @OnClick(R.id.tv_question)
    public void onViewClicked() {
        questionDialog();

    }
}
