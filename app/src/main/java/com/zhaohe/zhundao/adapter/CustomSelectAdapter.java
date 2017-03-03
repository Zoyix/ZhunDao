package com.zhaohe.zhundao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.holder.CustomSelectHolder;
import com.zhaohe.zhundao.bean.CustomSelectBean;

import java.util.List;

/**
 * @Description:自定义选择
 * @Author:邹苏隆
 * @Since:2017/1/16 16:07
 */
public class CustomSelectAdapter extends AdapterBase<CustomSelectBean, CustomSelectHolder> {

    private LayoutInflater inflater;
    private Context mContext;

    public CustomSelectAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    protected void handlerData(List<CustomSelectBean> mList2, int position, final CustomSelectHolder itemView) {
        CustomSelectBean bean = mList2.get(position);
        itemView.et_custom_select.setText(bean.getContent());


    }

    @Override
    protected CustomSelectHolder getItemViewHolder() {
        return new CustomSelectHolder();
    }

    @Override
    protected View initConvertView(View convertView, CustomSelectHolder v) {
        convertView = inflater.inflate(R.layout.list_item_custom_select, null);
        v.et_custom_select = (EditText) convertView.findViewById(R.id.et_custom_select);
        v.iv_custom_select = (ImageView) convertView.findViewById(R.id.iv_custom_select);
        return convertView;
    }

    @Override
    protected void onReachBottom() {

    }

}
