package com.zhaohe.zhundao.mywifidemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.mywifidemo.activity.WifiConfigActivity;

import java.util.List;


/**
 * Created by caojun on 2017/8/26.
 */

public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.PersonViewHolder> {
    private Context mContext;
    private List<String> mList;
    private String deviceKey;

    public WifiAdapter(Context context, List<String> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_wifi, parent, false);
        PersonViewHolder viewHolder = new PersonViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder holder, final int position) {
        holder.tv_item_wifi.setText(mList.get(position));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击跳转
                Intent intent = new Intent(mContext, WifiConfigActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.putExtra("wifiName", mList.get(position));
                intent.putExtra("flag", 2);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class PersonViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_item_wifi;
        private View mView;

        public PersonViewHolder(View itemView) {
            super(itemView);
            tv_item_wifi = (TextView) itemView.findViewById(R.id.tv_item_wifi);
            mView = itemView;
        }
    }
}
