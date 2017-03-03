package com.zhaohe.zhundao.adapter;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.holder.ActionHolder;
import com.zhaohe.zhundao.bean.ActionBean;

import java.util.List;

/**
 * @Description:活动列表适配器
 * @Author:邹苏隆
 * @Since:2016/12/5 15:41
 */
public class ActionAdapter extends AdapterBase<ActionBean, ActionHolder> implements View.OnClickListener {

    public interface ActionClickListener {
        public void onEditClick(ActionBean bean);

        public void onListClick(ActionBean bean);

        public void onShareClick(ActionBean bean);

        public void onMoreClick(ActionBean bean);

        public void onDetailsClick(ActionBean bean);
    }


    private LayoutInflater inflater;
    private Context mContext;
    private ActionClickListener actionClickListener;

    public ActionAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        mContext = context;
    }

    public void setActionClickListener(ActionClickListener l) {
        actionClickListener = l;
    }


    @Override
    protected void handlerData(List<ActionBean> mList2, int position, ActionHolder itemView) {
        ActionBean bean = mList2.get(position);
//        itemView.img_act.(bean.getImg_act());
        itemView.tv_act_title.setText(bean.getAct_title());
        itemView.tv_act_title.setOnClickListener(this);
        itemView.tv_act_title.setTag(position);
        itemView.tv_act_status.setText(bean.getAct_status());
        itemView.tv_act_sign_num.setText(bean.getAct_sign_num());
        itemView.tv_act_endtime.setText(bean.getAct_endtime());
        itemView.tv_act_resttime.setText(bean.getAct_resttime());
        itemView.tv_act_starttime.setText(bean.getAct_starttime());
        itemView.tv_act_resttime2.setText(bean.getAct_resttime2());
        itemView.tv_act_sign_income.setText(bean.getAct_sign_income());
        itemView.img_act.setOnClickListener(this);
        itemView.img_act.setTag(position);
        itemView.btn_act_edit.setOnClickListener(this);
        itemView.btn_act_edit.setTag(position);
        itemView.btn_act_share.setOnClickListener(this);
        itemView.btn_act_share.setTag(position);
        itemView.btn_act_list.setOnClickListener(this);
        itemView.btn_act_list.setTag(position);
        itemView.btn_act_more.setOnClickListener(this);
        itemView.btn_act_more.setTag(position);
        itemView.tv_act_edit.setOnClickListener(this);
        itemView.tv_act_edit.setTag(position);
        itemView.tv_act_share.setOnClickListener(this);
        itemView.tv_act_share.setTag(position);
        itemView.tv_act_list.setOnClickListener(this);
        itemView.tv_act_list.setTag(position);
        itemView.tv_act_more.setOnClickListener(this);
        itemView.tv_act_more.setTag(position);
//        加载活动分享小图
        Picasso.with(mContext).load(bean.getUrl()).error(R.mipmap.ic_launcher).into(itemView.img_act);


    }

    @Override
    protected ActionHolder getItemViewHolder() {
        return new ActionHolder();
    }

    @Override
    protected View initConvertView(View convertView, ActionHolder v) {
        convertView = inflater.inflate(R.layout.list_item_action_show, null);
        v.btn_act_edit = (ImageButton) convertView
                .findViewById(R.id.btn_act_edit);
        v.btn_act_list = (ImageButton) convertView
                .findViewById(R.id.btn_act_list);
        v.btn_act_share = (ImageButton) convertView
                .findViewById(R.id.btn_act_share);
        v.btn_act_more = (ImageButton) convertView
                .findViewById(R.id.btn_act_more);
        v.img_act = (ImageView) convertView.findViewById(R.id.img_act);
        v.tv_act_title = (TextView) convertView.findViewById(R.id.tv_act_title);
        TextPaint tp = v.tv_act_title.getPaint();
        tp.setFakeBoldText(true);
        v.tv_act_sign_num = (TextView) convertView.findViewById(R.id.tv_act_sign_num);
        v.tv_act_sign_income = (TextView) convertView.findViewById(R.id.tv_act_sign_income);
        v.tv_act_status = (TextView) convertView.findViewById(R.id.tv_act_status);
        v.tv_act_endtime = (TextView) convertView.findViewById(R.id.tv_act_endtime);
        v.tv_act_resttime = (TextView) convertView.findViewById(R.id.tv_act_resttime);
        v.tv_act_starttime = (TextView) convertView.findViewById(R.id.tv_act_starttime);
        v.tv_act_resttime2 = (TextView) convertView.findViewById(R.id.tv_act_resttime2);
        v.tv_act_edit = (TextView) convertView.findViewById(R.id.tv_act_edit);
        v.tv_act_list = (TextView) convertView.findViewById(R.id.tv_act_list);
        v.tv_act_share = (TextView) convertView.findViewById(R.id.tv_act_share);
        v.tv_act_more = (TextView) convertView.findViewById(R.id.tv_act_more);


        return convertView;


    }

    @Override
    protected void onReachBottom() {

    }

    @Override
    public void onClick(View view) {
        int position = (Integer) view.getTag();
        ActionBean bean = getItem(position);
        switch (view.getId()) {
            case R.id.btn_act_edit:
                if (actionClickListener != null) {
                    actionClickListener.onEditClick(bean);
                }
                break;
            case R.id.btn_act_share:
                if (actionClickListener != null) {
                    actionClickListener.onShareClick(bean);
                }
                break;
            case R.id.btn_act_list:
                if (actionClickListener != null) {
                    actionClickListener.onListClick(bean);
                }
                break;
            case R.id.btn_act_more:
                if (actionClickListener != null) {
                    actionClickListener.onMoreClick(bean);
                }
                break;
            case R.id.tv_act_title:
                if (actionClickListener != null) {
                    actionClickListener.onDetailsClick(bean);
                }
                break;
            case R.id.img_act:
                if (actionClickListener != null) {
                    actionClickListener.onDetailsClick(bean);
                }
                break;

            case R.id.tv_act_edit:
                if (actionClickListener != null) {
                    actionClickListener.onEditClick(bean);
                }
                break;
            case R.id.tv_act_share:
                if (actionClickListener != null) {
                    actionClickListener.onShareClick(bean);
                }
                break;
            case R.id.tv_act_list:
                if (actionClickListener != null) {
                    actionClickListener.onListClick(bean);
                }
                break;
            case R.id.tv_act_more:
                if (actionClickListener != null) {
                    actionClickListener.onMoreClick(bean);
                }
                break;
            default:
                break;
        }

    }

}
