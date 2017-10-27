package com.zhaohe.zhundao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.holder.BeaconHolder;
import com.zhaohe.zhundao.bean.BeaconBean;

import java.util.List;

import static com.zhaohe.zhundao.R.id.iv_find_beacon_icon;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2017/2/15 10:47
 */
public class BeaconAdapter extends AdapterBase<BeaconBean, BeaconHolder> {
    private LayoutInflater inflater;
    private Context mContext;

    public BeaconAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    protected void handlerData(List<BeaconBean> mList2, int position, BeaconHolder itemView) {
        BeaconBean bean = mList2.get(position);
        itemView.tv_find_beacon_title.setText(bean.getTitle());
        itemView.tv_find_beacon_name.setText(bean.getBeaconName() + "(" + bean.getDeviceID() + ")");
        if (bean.getUrl().equals("")) {
            itemView.iv_find_beacon_icon.setImageResource(R.mipmap.ic_launcher);
        } else {
            Picasso.with(mContext).load(bean.getUrl()).error(R.mipmap.ic_launcher).into(itemView.iv_find_beacon_icon);
        }


    }

    @Override
    protected BeaconHolder getItemViewHolder() {
        return new BeaconHolder();
    }

    @Override
    protected View initConvertView(View convertView, BeaconHolder v) {
        convertView = inflater.inflate(R.layout.list_item_find_beaconlist, null);
        v.tv_find_beacon_title = (TextView) convertView.findViewById(R.id.tv_find_beacon_title);
        v.tv_find_beacon_name = (TextView) convertView.findViewById(R.id.tv_find_beacon_name);
        v.iv_find_beacon_icon = (ImageView) convertView.findViewById(iv_find_beacon_icon);
        return convertView;
    }

    @Override
    protected void onReachBottom() {

    }

}
