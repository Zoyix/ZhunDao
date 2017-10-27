package com.zhaohe.zhundao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.holder.MyGroupHolder;
import com.zhaohe.zhundao.bean.dao.MyGroupBean;

import java.util.List;

/**
 * @Description:群组列表适配器
 * @Author:邹苏隆
 * @Since:2017/5/23 15:04
 */
public class MyGroupAdapter extends AdapterBase<MyGroupBean, MyGroupHolder> {
    private LayoutInflater inflater;
    private Context mContext;

    public MyGroupAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    protected void handlerData(List<MyGroupBean> mList2, int position,
                               MyGroupHolder itemView) {
        MyGroupBean bean = mList2.get(position);
        itemView.tv_group_item.setText(bean.getName());


    }

    @Override
    protected MyGroupHolder getItemViewHolder() {
        // TODO Auto-generated method stub
        return new MyGroupHolder();
    }

    @Override
    protected View initConvertView(View convertView, MyGroupHolder v) {
        convertView = inflater.inflate(R.layout.list_item_group, null);
        v.tv_group_item = (TextView) convertView
                .findViewById(R.id.tv_group_item);


        return convertView;
    }

    @Override
    protected void onReachBottom() {
        // TODO Auto-generated method stub

    }

}
