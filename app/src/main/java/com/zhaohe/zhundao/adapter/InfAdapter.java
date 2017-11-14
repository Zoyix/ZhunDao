package com.zhaohe.zhundao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.holder.InfHolder;
import com.zhaohe.zhundao.bean.InfBean;

import java.util.List;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2017/8/15 10:46
 */
public class InfAdapter extends AdapterBase<InfBean, InfHolder> {
    private LayoutInflater inflater;
    private Context mContext;

    public InfAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    protected void handlerData(List<InfBean> mList2, int position, InfHolder itemView) {
        InfBean bean = mList2.get(position);
        itemView.tv_inf_id.setText(bean.getmID());
        itemView.tv_inf_title.setText(bean.getTitle());
        itemView.tv_inf_time.setText(bean.getAddTime());
        itemView.tv_inf_type.setText(bean.getSortName() + " | ");
        if (bean.isRead()) {
            itemView.iv_inf_read.setVisibility(View.INVISIBLE);
        } else {
            itemView.iv_inf_read.setVisibility(View.VISIBLE);

        }
    }

    @Override
    protected InfHolder getItemViewHolder() {
        return new InfHolder();
    }

    @Override
    protected View initConvertView(View convertView, InfHolder v) {
        convertView = inflater.inflate(R.layout.list_item_inf, null);
        v.tv_inf_id = (TextView) convertView
                .findViewById(R.id.tv_inf_id);
        v.tv_inf_type = (TextView) convertView
                .findViewById(R.id.tv_inf_type);
        v.tv_inf_time = (TextView) convertView
                .findViewById(R.id.tv_inf_time);
        v.tv_inf_title = (TextView) convertView
                .findViewById(R.id.tv_inf_title);
        v.iv_inf_read = (ImageView) convertView
                .findViewById(R.id.iv_inf_read);
        return convertView;

    }

    @Override
    protected void onReachBottom() {

    }
}
