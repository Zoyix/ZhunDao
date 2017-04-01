package com.zhaohe.zhundao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.holder.SignupListHolder;
import com.zhaohe.zhundao.bean.dao.MySignListupBean;

import java.util.List;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2016/12/21 10:18
 */
public class SignupListAdapter extends AdapterBase<MySignListupBean, SignupListHolder>implements View.OnClickListener {
    public interface SignupListClickListener{
     public void   signupClick(MySignListupBean bean);
    }
    private LayoutInflater inflater;
    private Context mContext;
    private SignupListClickListener signupListClickListener;

    public SignupListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        mContext = context;
    }
  public void setSignupListClickListener(SignupListClickListener l){
      signupListClickListener=l;
  }

    @Override
    protected void handlerData(List<MySignListupBean> mList2, int position, SignupListHolder itemView) {
        MySignListupBean bean = mList2.get(position);

        itemView.tv_signuplist_phone.setText(bean.getPhone());
        itemView.tv_signuplist_name.setText(bean.getName());
        itemView.tv_signuplist_time.setText(bean.getSignTime());
        itemView.btn_signuplist_signup.setOnClickListener(this);
        itemView.btn_signuplist_signup.setTag(position);
        if (bean.getStatus().equals("true")) {
            itemView.img_signuplist_status.setImageResource(R.drawable.check_on);
            itemView.btn_signuplist_signup.setVisibility(View.GONE);
//            if (bean.getSignTime().equals("")){
//                bean.setSignTime("2017-3-13");
//            }
        }
//        if (bean.getStatus() == "true") {
//            itemView.img_signuplist_status.setImageResource(R.drawable.check_on);
//        }
        else {
            itemView.img_signuplist_status.setImageResource(R.drawable.check_off);
            itemView.btn_signuplist_signup.setVisibility(View.VISIBLE);

        }

    }

    @Override
    protected SignupListHolder getItemViewHolder() {
        return new SignupListHolder();
    }

    @Override
    protected View initConvertView(View convertView, SignupListHolder v) {
        convertView = inflater.inflate(R.layout.list_item_signup_list, null);
        v.tv_signuplist_name = (TextView) convertView.findViewById(R.id.tv_signuplist_name);
        v.tv_signuplist_phone = (TextView) convertView.findViewById(R.id.tv_signuplist_phone);
        v.tv_signuplist_time = (TextView) convertView.findViewById(R.id.tv_signuplist_time);
        v.btn_signuplist_signup = (Button) convertView.findViewById(R.id.btn_signuplist_signup);
        v.img_signuplist_status = (ImageView) convertView.findViewById(R.id.img_signuplist_status);

        return convertView;
    }

    @Override
    protected void onReachBottom() {

    }


    @Override
    public void onClick(View view) {
        int position = (Integer) view.getTag();
        MySignListupBean bean = getItem(position);
        switch (view.getId()) {
            case R.id.btn_signuplist_signup:
                if (signupListClickListener != null) {
                    signupListClickListener.signupClick(bean);
                }
                break;

            default:
                break;
        }

    }
}
