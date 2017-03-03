package com.zhaohe.zhundao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.holder.SignHolder;
import com.zhaohe.zhundao.bean.SignBean;

import java.util.List;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2016/12/13 14:08
 */
public class SignAdapter extends AdapterBase<SignBean, SignHolder> implements View.OnClickListener {


    public interface SignClickListener {
        public void onSignscanClick(SignBean bean);

        public void onSignSwitch(SignBean bean);

        public void onEditTitle(SignBean bean);

        public void onGetList(SignBean bean);


    }

    private LayoutInflater inflater;
    private Context mContext;
    private SignClickListener signClickListener;

    public SignAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        mContext = context;
    }

    public void setSignClickListener(SignAdapter.SignClickListener l) {
        signClickListener = l;
    }

    @Override
    protected void handlerData(List<SignBean> mList2, int position, SignHolder itemView) {
        SignBean bean = mList2.get(position);
//        itemView.img_act.(bean.getImg_act());

        itemView.tv_sign_title.setText(bean.getSign_title());
        itemView.tv_act_title.setText(bean.getAct_title());
        itemView.tv_sign_type.setText(bean.getSign_type());
        itemView.tv_sign_stoptime.setText(bean.getStoptime());
        itemView.tv_sign_num.setText(bean.getSign_num());
        itemView.tv_signup_num.setText(bean.getSignup_num());
        itemView.img_sign_scan.setOnClickListener(this);
        itemView.img_sign_scan.setTag(position);
        itemView.iv_sign_right2.setOnClickListener(this);
        itemView.iv_sign_right2.setTag(position);
        itemView.iv_sign_right.setOnClickListener(this);
        itemView.iv_sign_right.setTag(position);
        itemView.tv_sign_scan.setOnClickListener(this);
        itemView.tv_sign_scan.setTag(position);
        itemView.sw_sign_status.setOnClickListener(this);
        itemView.sw_sign_status.setTag(position);
        if (bean.getSign_status() == "false") {
            itemView.sw_sign_status.setChecked(false);
        }

        if (bean.getSign_status() == "true") {
            itemView.sw_sign_status.setChecked(true);
        }

    }

    @Override
    protected SignHolder getItemViewHolder() {
        return new SignHolder();
    }

    @Override
    protected View initConvertView(View convertView, SignHolder v) {
        convertView = inflater.inflate(R.layout.list_item_sign_show, null);
        v.iv_sign_right2 = (ImageView) convertView.findViewById(R.id.iv_sign_right2);
        v.iv_sign_right = (ImageView) convertView.findViewById(R.id.iv_sign_right);
        v.img_sign_scan = (ImageView) convertView.findViewById(R.id.img_sign_scan);
        v.tv_sign_title = (TextView) convertView.findViewById(R.id.tv_sign_title);
        v.tv_act_title = (TextView) convertView.findViewById(R.id.tv_act_title);
        v.tv_sign_stoptime = (TextView) convertView.findViewById(R.id.tv_sign_stoptime);
        v.tv_sign_num = (TextView) convertView.findViewById(R.id.tv_sign_num);
        v.tv_signup_num = (TextView) convertView.findViewById(R.id.tv_signup_num);
        v.tv_sign_type = (TextView) convertView.findViewById(R.id.tv_sign_type);
        v.sw_sign_status = (Switch) convertView.findViewById(R.id.sw_sign_status);
        v.tv_sign_scan = (TextView) convertView.findViewById(R.id.tv_sign_scan);
        return convertView;
    }

    @Override
    protected void onReachBottom() {

    }

    @Override
    public void onClick(View view) {
        int position = (Integer) view.getTag();
        SignBean bean = getItem(position);
        switch (view.getId()) {
            case R.id.img_sign_scan:
                if (signClickListener != null) {
                    signClickListener.onSignscanClick(bean);
                }
                break;
            case R.id.sw_sign_status:
                if (signClickListener != null) {
                    signClickListener.onSignSwitch(bean);
                }
                break;
            case R.id.tv_sign_scan:
                if (signClickListener != null) {
                    signClickListener.onSignscanClick(bean);
                }
                break;
            case R.id.iv_sign_right2:
                if (signClickListener != null) {
                    signClickListener.onEditTitle(bean);
                }
                break;
            case R.id.iv_sign_right:
                if (signClickListener != null) {
                    signClickListener.onGetList(bean);
                }
                break;
            default:
                break;
        }

    }


}
