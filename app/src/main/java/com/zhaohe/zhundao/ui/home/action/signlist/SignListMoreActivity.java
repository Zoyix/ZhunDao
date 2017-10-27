package com.zhaohe.zhundao.ui.home.action.signlist;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.adapter.AdapterBase;
import com.zhaohe.zhundao.adapter.holder.SignListMoreHolder;
import com.zhaohe.zhundao.bean.SignListMoreBean;

import java.util.ArrayList;
import java.util.List;

public class SignListMoreActivity extends Activity {
    TextView tv1;
    ListView mListView1;
    MyAdapter myAdapter;
    SignListMoreAdapter adapter;
    RelativeLayout mHead;
    LinearLayout main;
    List<SignListMoreBean> list = new ArrayList<>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_list_more);

        mHead = (RelativeLayout) findViewById(R.id.head);
        mHead.setFocusable(true);
        mHead.setClickable(true);
        mHead.setBackgroundColor(Color.parseColor("#b2d235"));
        mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

        mListView1 = (ListView) findViewById(R.id.listView1);
        mListView1.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
        adapter = new SignListMoreAdapter(this);
        myAdapter = new MyAdapter(this, R.layout.list_item_sign_list_more);
        mListView1.setAdapter(adapter);
        initData();
    }

    private void initData() {
        SignListMoreBean bean = new SignListMoreBean();
        for (int i = 0; i < 30; i++) {
            bean.setSex("1");
            list.add(bean);
        }
        adapter.refreshData(list);

    }

    class ListViewAndHeadViewTouchLinstener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {
            HorizontalScrollView headSrcrollView = (HorizontalScrollView) mHead
                    .findViewById(R.id.horizontalScrollView1);
            headSrcrollView.onTouchEvent(arg1);
            return false;
        }
    }

    public class SignListMoreAdapter extends AdapterBase<SignListMoreBean, SignListMoreHolder> implements View.OnTouchListener {
        private LayoutInflater inflater;
        private Context mContext;


        public SignListMoreAdapter(Context context) {
            inflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        protected void handlerData(List<SignListMoreBean> mList2, int position, SignListMoreHolder itemView) {
            SignListMoreBean bean = mList2.get(position);
            itemView.txt1.setText(bean.getSex());
            tv1.setText(bean.getRemark());


        }

        @Override
        protected SignListMoreHolder getItemViewHolder() {
            return new SignListMoreHolder();
        }

        @Override
        protected View initConvertView(View convertView, SignListMoreHolder holder) {
            LinearLayout ll = (LinearLayout) findViewById(R.id.ll_signlist_item);
            tv1 = new TextView(getApplicationContext());
            LinearLayout.LayoutParams tvParam =
                    new LinearLayout.LayoutParams(80, ViewGroup.LayoutParams.WRAP_CONTENT);
            ll.addView(tv1, tvParam);

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
    }

    ;

    public class MyAdapter extends BaseAdapter {
        public List<ViewHolder> mHolderList = new ArrayList<ViewHolder>();

        int id_row_layout;
        LayoutInflater mInflater;

        public MyAdapter(Context context, int id_row_layout) {
            super();
            this.id_row_layout = id_row_layout;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 250;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parentView) {
            ViewHolder holder = null;
            if (convertView == null) {
                synchronized (SignListMoreActivity.this) {
                    convertView = mInflater.inflate(id_row_layout, null);
                    holder = new ViewHolder();

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

                    MyHScrollView headSrcrollView = (MyHScrollView) mHead
                            .findViewById(R.id.horizontalScrollView1);
                    headSrcrollView
                            .AddOnScrollChangedListener(new OnScrollChangedListenerImp(
                                    scrollView1));

                    convertView.setTag(holder);
                    mHolderList.add(holder);
                }
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txt1.setText(position + "" + 1);
            holder.txt2.setText(position + "" + 2);
            holder.txt3.setText(position + "" + 3);
            holder.txt4.setText(position + "" + 4);
            holder.txt5.setText(position + "" + 5);

            return convertView;
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
        }

        ;

        class ViewHolder {
            TextView txt1;
            TextView txt2;
            TextView txt3;
            TextView txt4;
            TextView txt5;
            HorizontalScrollView scrollView;
        }
    }// end class my

}
