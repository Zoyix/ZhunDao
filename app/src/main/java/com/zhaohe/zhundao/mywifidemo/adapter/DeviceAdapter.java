package com.zhaohe.zhundao.mywifidemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.mywifidemo.activity.AdjustActivity;
import com.zhaohe.zhundao.mywifidemo.utils.ToastUtis;
import com.zhaohe.zhundao.mywifidemo.utils.WifiUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by caojun on 2017/8/26.
 */

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.PersonViewHolder> {
    private Context mContext;
    private List<String> mList;
    private WifiConfiguration configuration;

    public DeviceAdapter(Context context, List<String> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_device, parent, false);
        PersonViewHolder viewHolder = new PersonViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder holder, final int position) {
        holder.tv_item_device.setText("序列号: " + mList.get(position));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtis.showLoading(mContext, "正在连接设备");
                configuration = WifiUtils.createWifiConfiguration(mContext, mList.get(position), "", 1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtis.hideLoading();
                        boolean success = false;
                        if (Build.VERSION.SDK_INT >= 23) {
                            try {
                                success = true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            success = WifiUtils.connect2Wifi(mContext, configuration);
                        }
                        if (success) {
                            Intent intent = new Intent(mContext, AdjustActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            mContext.startActivity(intent);
                        } else {
                            ToastUtis.showToast(mContext, "连接设备wifi失败,请重试!");
                        }
                    }
                }, 3000);


            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class PersonViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_item_device;
        private View mView;

        public PersonViewHolder(View itemView) {
            super(itemView);
            tv_item_device = (TextView) itemView.findViewById(R.id.tv_item_device);
            mView = itemView;
        }
    }

    //通过反射的方式去判断wifi是否已经连接上，并且可以开始传输数据
    private boolean checkWiFiConnectSuccess() {
        Class classType = WifiInfo.class;
        try {
            Object invo = classType.newInstance();
            Object result = invo.getClass().getMethod("getMeteredHint").invoke(invo);
            return (boolean) result;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
    }
}
