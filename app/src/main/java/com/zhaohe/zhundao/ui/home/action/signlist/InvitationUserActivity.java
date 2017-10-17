package com.zhaohe.zhundao.ui.home.action.signlist;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaohe.app.utils.DragViewUtil;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;

public class InvitationUserActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REFRESH_VIEW = 1000;
    ImageView ivInvitationPhoto;
    TextView tvInvitationPhoto;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_user);
        initHandler();
        initView();
    }


    private void initView() {
        tvInvitationPhoto = (TextView) findViewById(R.id.tv_invitation_photo);
        tvInvitationPhoto.setText("炸弹");
        ivInvitationPhoto = (ImageView) findViewById(R.id.iv_invitation_photo);
//        ivInvitationPhoto.setOnClickListener(this);
        DragViewUtil.drag(ivInvitationPhoto, "ivInvitationPhoto");
        DragViewUtil.drag(tvInvitationPhoto, "tvInvitationPhoto");
//        mHandler.sendEmptyMessageDelayed(REFRESH_VIEW, 0);
        mHandler.sendEmptyMessageDelayed(100, 0);
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                DragViewUtil.set(ivInvitationPhoto, "ivInvitationPhoto");
                DragViewUtil.set(tvInvitationPhoto, "tvInvitationPhoto");

            }
        }, 100);
    }

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case REFRESH_VIEW:
                        ivInvitationPhoto.requestFocus();
                        ivInvitationPhoto.layout(554, 698, 698, 1158);
                        ToastUtil.makeText(getApplicationContext(), "炸弹");
                        break;

                    default:
                        break;
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_invitation_photo:
                ivInvitationPhoto.layout(554, 698, 698, 1158);
                break;
        }

    }
}