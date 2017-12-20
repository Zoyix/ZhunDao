package com.zhaohe.zhundao.ui.home.find;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.ui.ToolBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InvitationProActivity extends ToolBarActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.rb_invitation_type1)
    RadioButton rbInvitationType1;
    @BindView(R.id.rb_invitation_type2)
    RadioButton rbInvitationType2;
    @BindView(R.id.rg_invitation)
    RadioGroup rgInvitation;

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
}
