package com.zhaohe.zhundao.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;

import java.util.LinkedList;

/*自定选项增加删除适配器
 * Created by Administrator on 2016/5/11.
 * Author jxc
 */

public class CustomSelectMutiAdapter extends BaseAdapter {

    private Context mContext;
    //是用链表的List作为数据源的类型，主要是因为该类型有set方法，
    // 可以直接修改 某位置 的数据，而且增删改的效率也比ArrayList高
    private LinkedList<String> mDatas;

    public CustomSelectMutiAdapter(Context context, LinkedList<String> data) {
        this.mContext = context;
        if (data == null) {
            data = new LinkedList<>();
        }
        this.mDatas = data;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    //给数据源添加数据
    public void addData(String item) {
        mDatas.add(item);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_item_demo, null);
        TextView tvCount = (TextView) convertView.findViewById(R.id.tv_count);
        EditText etContent = (EditText) convertView.findViewById(R.id.et_content);
        final ImageButton ibDel = (ImageButton) convertView.findViewById(R.id.ibt_delete);
        //将数据源中已保存的数据放置进去
        etContent.setText((String) getItem(position));

        tvCount.setText(String.valueOf(position + 1));

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //每当内容改变时，就更新mDatas中的数据
                mDatas.set(position, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //监听删除按钮的点击事件
        ibDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDatas.size() <= 2) {
//                    ibDel.setImageResource(R.mipmap.select_delete_off);
                    ToastUtil.makeText(mContext, "至少有2个选项！");
                } else {
                    mDatas.remove(position);
                    CustomSelectMutiAdapter.this.notifyDataSetChanged();
                }
            }
        });
        return convertView;
    }
}
