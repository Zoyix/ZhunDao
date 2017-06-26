package com.zhaohe.zhundao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.holder.CustomHolder;
import com.zhaohe.zhundao.bean.CustomBean;

import java.util.List;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2017/1/16 16:07
 */
public class CustomAdapter extends AdapterBase<CustomBean, CustomHolder> {

    private LayoutInflater inflater;
    private Context mContext;

    public CustomAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    protected void handlerData(List<CustomBean> mList2, int position, CustomHolder itemView) {
        CustomBean bean = mList2.get(position);
        itemView.tv_custom_title.setText(bean.getTitle());
        String InputType = bean.getType();
        String type = "[输入框]";
        switch (InputType) {
            case "0":
                type = "[输入框]";
                break;
            case "1":
                type = "[多文本]";
                break;
            case "2":
                type = "[下    拉]";
                break;
            case "3":
                type = "[多    选]";
                break;
            case "4":
                type = "[图    片]";
                break;
            case "5":
                type = "[单    选]";
                break;
            case "6":
                type = "[日    期]";
                break;
            case "7":
                type = "[数    字]";
                break;

            default:
                type = "[输入框]";

                break;

        }
        itemView.tv_custom_type.setText(type);

        itemView.tv_custom_required.setText(bean.getRequired());


    }

    @Override
    protected CustomHolder getItemViewHolder() {
        return new CustomHolder();
    }

    @Override
    protected View initConvertView(View convertView, CustomHolder v) {
        convertView = inflater.inflate(R.layout.list_item_custom_show, null);
        v.tv_custom_title = (TextView) convertView.findViewById(R.id.tv_custom_title);
        v.tv_custom_type = (TextView) convertView.findViewById(R.id.tv_custom_type);
        v.tv_custom_required = (TextView) convertView.findViewById(R.id.tv_custom_required);
        return convertView;
    }

    @Override
    protected void onReachBottom() {

    }

}
