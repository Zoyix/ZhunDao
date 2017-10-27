package com.zhaohe.zhundao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.holder.SignListHolder;
import com.zhaohe.zhundao.bean.SignListBean;

import java.util.List;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2016/12/14 10:44
 */
public class SignListAdapter extends AdapterBase<SignListBean, SignListHolder> {

    private LayoutInflater inflater;
    private Context mContext;

    public SignListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    protected void handlerData(List<SignListBean> mList2, int position, SignListHolder itemView) {
        SignListBean bean = mList2.get(position);
        itemView.tv_signlist_id.setText(mList2.size() - (position) + "");
        itemView.tv_signlist_name.setText(bean.getSign_list_name() + "(" + bean.getNickname() + ")");
        itemView.tv_signlist_time.setText(bean.getSign_list_time());
        itemView.tv_signlist_phone.setText(bean.getSign_list_phone());
        itemView.tv_signlist_status.setText(bean.getSign_list_status());


    }

    @Override
    protected SignListHolder getItemViewHolder() {
        return new SignListHolder();
    }

    @Override
    protected View initConvertView(View convertView, SignListHolder v) {
        convertView = inflater.inflate(R.layout.list_item_sign_list, null);
        v.tv_signlist_id = (TextView) convertView.findViewById(R.id.tv_inf_id);
        v.tv_signlist_name = (TextView) convertView.findViewById(R.id.tv_signlist_name);
        v.tv_signlist_time = (TextView) convertView.findViewById(R.id.tv_inf_time);
        v.tv_signlist_phone = (TextView) convertView.findViewById(R.id.tv_signlist_phone);
        v.tv_signlist_status = (TextView) convertView.findViewById(R.id.tv_signlist_status);

        return convertView;
    }

    @Override
    protected void onReachBottom() {

    }

}
