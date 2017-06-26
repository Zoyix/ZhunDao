package com.zhaohe.zhundao.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.holder.SignListMoreHolder;
import com.zhaohe.zhundao.bean.SignListMoreBean;
import com.zhaohe.zhundao.ui.home.action.signlist.MyHScrollView;

import java.util.List;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2017/6/16 15:06
 */
public class SignListMoreAdapter  extends AdapterBase<SignListMoreBean, SignListMoreHolder> implements  View.OnTouchListener {
    private LayoutInflater inflater;
    private Context mContext;
   public RelativeLayout mHead;

    public SignListMoreAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    protected void handlerData(List<SignListMoreBean> mList2, int position, SignListMoreHolder itemView) {
        SignListMoreBean bean=mList2.get(position);
        itemView.txt1.setText(bean.getSex());

    }

    @Override
    protected SignListMoreHolder getItemViewHolder() {
        return new SignListMoreHolder();    }

    @Override
    protected View initConvertView(View convertView, SignListMoreHolder holder) {
        convertView = inflater.inflate(R.layout.list_item_sign_list_more, null);

        MyHScrollView scrollView1 = (MyHScrollView) convertView
                .findViewById(R.id.horizontalScrollView1);

        holder.scrollView = scrollView1;
        holder.txt1 = (TextView) convertView
                .findViewById(R.id.textView1);
        holder.txt2 = (TextView) convertView
                .findViewById(R.id.textView2);
        holder.txt3 = (TextView) convertView
                .findViewById(R.id.textView3);
        holder.txt4 = (TextView) convertView
                .findViewById(R.id.textView4);
        holder.txt5 = (TextView) convertView
                .findViewById(R.id.textView5);
        mHead = (RelativeLayout) convertView
                .findViewById(R.id.head);
        mHead.setFocusable(true);
        mHead.setClickable(true);
        mHead.setBackgroundColor(Color.parseColor("#b2d235"));
        mHead.setOnTouchListener(this);
        MyHScrollView headSrcrollView = (MyHScrollView) mHead
                .findViewById(R.id.horizontalScrollView1);
        headSrcrollView
                .AddOnScrollChangedListener(new OnScrollChangedListenerImp(
                        scrollView1));

        convertView.setTag(holder);
        return convertView;

    }

    @Override
    protected void onReachBottom() {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        HorizontalScrollView headSrcrollView = (HorizontalScrollView) mHead
                .findViewById(R.id.horizontalScrollView1);
        headSrcrollView.onTouchEvent(motionEvent);
        return false;
    }
}

class OnScrollChangedListenerImp implements MyHScrollView.OnScrollChangedListener {
    MyHScrollView mScrollViewArg;

    public OnScrollChangedListenerImp(MyHScrollView scrollViewar) {
        mScrollViewArg = scrollViewar;
    }

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        mScrollViewArg.smoothScrollTo(l, t);
    }
};
