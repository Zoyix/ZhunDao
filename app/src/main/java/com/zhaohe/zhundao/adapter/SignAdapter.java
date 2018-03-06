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

import java.text.DecimalFormat;
import java.util.List;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2016/12/13 14:08
 */
public class SignAdapter extends AdapterBase<SignBean, SignHolder> implements View.OnClickListener {

    private ImageView ImageView;

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
    protected void handlerData(List<SignBean> mList2, int position, final SignHolder itemView) {
        final SignBean bean = mList2.get(position);
//        itemView.img_act.(bean.getImg_act());

        itemView.tv_sign_title.setText(bean.getSign_title());
        itemView.tv_act_title.setText(bean.getAct_title());
        itemView.tv_sign_type.setText(bean.getSign_type());
        itemView.tv_sign_stoptime.setText(bean.getStoptime());
        itemView.tv_sign_num.setText(bean.getSign_num());
        itemView.tv_signup_num.setText(bean.getSignup_num());
        double NumShould = Integer.parseInt(bean.getSign_num());
        double NubFact = Integer.parseInt(bean.getSignup_num());

        int NumUnsign = (int) (NumShould - NubFact);
        if (NumUnsign < 0) {
            NumUnsign = 0;
        }
        double result;
        itemView.tv_unsignup_num.setText(NumUnsign + "");
        if (NumShould != 0) {
            result = NubFact / NumShould;

        } else {
            result = 0;
        }

        DecimalFormat df = new DecimalFormat("0.0%");
        String r = df.format(result);
        itemView.tv_signed_percent.setText(r);
//        itemView.img_sign_scan.setOnClickListener(this);
//        itemView.img_sign_scan.setTag(position);

        itemView.iv_sign_right2.setOnClickListener(this);
        itemView.iv_sign_right2.setTag(position);
        itemView.iv_sign_right.setOnClickListener(this);


        itemView.iv_sign_right.setTag(position);
//        itemView.tv_sign_scan.setOnClickListener(this);
//        itemView.tv_sign_scan.setTag(position);
        itemView.sw_sign_status.setOnClickListener(this);
        itemView.sw_sign_status.setTag(position);
        ImageView = itemView.iv_list_status;
//        if(bean.getList_status()!=null){
        if (bean.getList_status().equals("true")) {
            itemView.iv_list_status.setVisibility(View.GONE);
        } else {
            itemView.iv_list_status.setVisibility(View.VISIBLE);

        }
//        }
//        else{
//            itemView.iv_list_status.setVisibility(View.VISIBLE);
//
//        }
//        if (SPUtils.contains(mContext, "signup_" + bean.getSign_id()) == true){
//                itemView.iv_list_status.setVisibility(View.GONE);
//        }
//        else{
//            itemView.iv_list_status.setVisibility(View.VISIBLE);

//        }
        itemView.iv_sign_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // itemView.iv_list_status是绿色的小球
                itemView.iv_list_status.setVisibility(View.GONE);
                if (signClickListener != null) {
                    signClickListener.onGetList(bean);
                }

            }
        });
        if (bean.getSign_status().equals("false")) {
            itemView.sw_sign_status.setChecked(false);
        }

        if (bean.getSign_status().equals("true")) {
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
//        v.img_sign_scan = (ImageView) convertView.findViewById(R.id.img_sign_scan);
        v.tv_sign_title = (TextView) convertView.findViewById(R.id.tv_sign_title);
        v.tv_act_title = (TextView) convertView.findViewById(R.id.tv_act_title);
        v.tv_sign_stoptime = (TextView) convertView.findViewById(R.id.tv_sign_stoptime);
        v.tv_sign_num = (TextView) convertView.findViewById(R.id.tv_sign_num);
        v.tv_signup_num = (TextView) convertView.findViewById(R.id.tv_signup_num);
        v.tv_sign_type = (TextView) convertView.findViewById(R.id.tv_sign_type);
        v.sw_sign_status = (Switch) convertView.findViewById(R.id.sw_sign_status);
        v.tv_sign_scan = (TextView) convertView.findViewById(R.id.tv_sign_scan);
        v.iv_list_status = (ImageView) convertView.findViewById(R.id.iv_list_status);
        v.tv_unsignup_num = (TextView) convertView.findViewById(R.id.tv_unsignup_num);
        v.tv_signed_percent = (TextView) convertView.findViewById(R.id.tv_signed_percent);

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
//            case R.id.iv_sign_right:
//
//                if (signClickListener != null) {
//                    signClickListener.onGetList(bean);
//                }
//                break;
            default:
                break;
        }

    }


}
