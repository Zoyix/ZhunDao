package com.zhaohe.zhundao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhaohe.app.utils.CircleTransform;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.holder.AccountHolder;
import com.zhaohe.zhundao.bean.AccountBean;

import java.util.List;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2017/8/15 10:46
 */
public class AccountAdapter extends AdapterBase<AccountBean, AccountHolder> {
    private LayoutInflater inflater;
    private Context mContext;

    public AccountAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    protected void handlerData(List<AccountBean> mList2, int position, AccountHolder itemView) {
        AccountBean bean = mList2.get(position);
        Picasso.with(mContext).load(bean.getHead()).error(R.drawable.unkown_head).transform(new CircleTransform()).into(itemView.head);

        itemView.name.setText(bean.getName());
        String newPhone = bean.getPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        itemView.phone.setText(newPhone);
        if (bean.getStatus().equals("true")) {
            itemView.status.setVisibility(View.VISIBLE);
        } else {
            itemView.status.setVisibility(View.GONE);

        }
    }

    @Override
    protected AccountHolder getItemViewHolder() {
        return new AccountHolder();
    }

    @Override
    protected View initConvertView(View convertView, AccountHolder v) {
        convertView = inflater.inflate(R.layout.list_item_account, null);
        v.name = (TextView) convertView
                .findViewById(R.id.tv_name);
        v.phone = (TextView) convertView
                .findViewById(R.id.tv_phone);
        v.status = (ImageView) convertView
                .findViewById(R.id.iv_select);
        v.head = (ImageView) convertView
                .findViewById(R.id.iv_head);

        return convertView;

    }

    @Override
    protected void onReachBottom() {

    }
}
