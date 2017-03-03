package com.zhaohe.zhundao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaohe.app.utils.StringUtils;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.holder.ActionMoreHolder;
import com.zhaohe.zhundao.bean.ActionMoreBean;

import java.util.List;

/**
 * @Description:活动更多选项GirdViewS适配器
 * @Author:邹苏隆
 * @Since:2017/2/20 9:42
 */
public class ActionMoreAdapter extends AdapterBase<ActionMoreBean, ActionMoreHolder> {

    private LayoutInflater inflater;

    public  ActionMoreAdapter(Context context) {
        inflater = LayoutInflater.from (context);
    }


    @Override
    protected void handlerData(List< ActionMoreBean> mList2, int position,  ActionMoreHolder itemView){
        ActionMoreBean bean = mList2.get (position);

        //空白
        if(StringUtils.isEmpty (bean.name)&&bean.imageRes==-1){
            itemView.image.setVisibility (View.INVISIBLE);
            itemView.tvName.setVisibility (View.INVISIBLE);
            return;
        }

        itemView.image.setImageResource (bean.imageRes);
        itemView.tvName.setText (bean.name);



    }

    @Override
    protected  ActionMoreHolder getItemViewHolder(){
        return new  ActionMoreHolder ();
    }

    @Override
    protected View initConvertView(View convertView, ActionMoreHolder v){
        convertView = inflater.inflate (R.layout.gridview_item, null);
        v.image = (ImageView) convertView.findViewById (R.id.iv_gridview);
        v.tvName = (TextView) convertView.findViewById (R.id.tv_nameGridview);
        return convertView;
    }

    @Override
    protected void onReachBottom(){

    }

}

