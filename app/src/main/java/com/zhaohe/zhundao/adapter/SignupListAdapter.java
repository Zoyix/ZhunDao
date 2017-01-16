package com.zhaohe.zhundao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.holder.SignupListHolder;
import com.zhaohe.zhundao.bean.SignupListBean;

import java.util.List;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2016/12/21 10:18
 */
public class SignupListAdapter extends AdapterBase<SignupListBean,SignupListHolder> {

    private LayoutInflater inflater;
    private Context mContext;
    public SignupListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    protected void handlerData(List<SignupListBean> mList2, int position, SignupListHolder itemView) {
        SignupListBean bean = mList2.get(position);
        itemView.tv_signuplist_phone.setText(bean.getSignup_list_phone());
        itemView.tv_signuplist_name.setText(bean.getSignup_list_name());
        itemView.tv_signuplist_time.setText(bean.getSignup_list_time());

        if(bean.getSignup_list_status()=="true"){
            itemView.img_signuplist_status.setImageResource(R.drawable.check_on);
        }
        else {
            itemView.img_signuplist_status.setImageResource(R.drawable.check_off);

        }
    }
    @Override
    protected SignupListHolder getItemViewHolder() {
        return new SignupListHolder();
    }

    @Override
    protected View initConvertView(View convertView, SignupListHolder v) {
        convertView = inflater.inflate(R.layout.list_item_signup_list, null);
        v.tv_signuplist_name= (TextView) convertView.findViewById(R.id.tv_signuplist_name);
        v.tv_signuplist_phone= (TextView) convertView.findViewById(R.id.tv_signuplist_phone);
        v.tv_signuplist_time= (TextView) convertView.findViewById(R.id.tv_signuplist_time);

        v.img_signuplist_status= (ImageView) convertView.findViewById(R.id.img_signuplist_status);

        return convertView;
    }

    @Override
    protected void onReachBottom() {

    }


}
