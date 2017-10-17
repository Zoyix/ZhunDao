package com.zhaohe.zhundao.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * @param <T> List 数据类型
 * @param <V> ItemView - 对象类型，且对象中只需要提供 public 的属性即可
 * @Description: 公用适配器
 * @Author:杨攀
 * @Since:2014年7月31日下午3:46:27
 */
public abstract class AdapterBase<T, V> extends BaseAdapter {

    private final List<T> mList = new LinkedList<T>();

    public List<T> getList() {
        return mList;
    }

    /**
     * @param list
     * @Description: 刷新
     * @Author: 杨攀
     * @Since: 2014年8月11日下午4:05:15
     */
    public void refreshData(List<T> list) {
        mList.clear();
        appendToList(list);
    }

    public void appendToList(List<T> list) {
        if (list == null) {
            return;
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void appendToTopList(List<T> list) {
        if (list == null) {
            return;
        }
        mList.addAll(0, list);
        notifyDataSetChanged();
    }

    /**
     * @Description: 清除数据
     * @Author:杨攀
     * @Since: 2014年7月31日下午3:ic_launcher:02
     */
    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    /**
     * @param location
     * @return
     * @Description: 删除指定的行
     * @Author:杨攀
     * @Since: 2014年7月31日下午3:51:00
     */
    public T remove(int location) {
        T t = mList.remove(location);
        notifyDataSetChanged();
        return t;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        if (position > mList.size() - 1) {
            return null;
        }
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == getCount() - 1) {
            onReachBottom();
        }
        return getExView(position, convertView, parent);
    }

    /**
     * @param position
     * @param convertView
     * @param parent
     * @return
     * @Description: 显示 View
     * @Author:杨攀
     * @Since: 2014年7月31日下午4:30:50
     */
    protected View getExView(int position, View convertView, ViewGroup parent) {
        V itemView = null; // 通用的 Item 界面控制器
        if (null == convertView) {// 初始化
            itemView = getItemViewHolder();
            convertView = initConvertView(convertView, itemView);
            convertView.setTag(itemView);
        } else {
            itemView = (V) convertView.getTag();
        }
        // 处理数据
        handlerData(mList, position, itemView);
        return convertView;
    }

    /**
     * @param mList2
     * @param position
     * @param itemView
     * @Description: 处理数据： 把数据set到Item 界面的控件上面
     * @Author:杨攀
     * @Since: 2014年7月31日下午4:29:07
     */
    protected abstract void handlerData(List<T> mList2, int position, V itemView);

    /**
     * @return
     * @Description: 获取 通用的 Item 界面布局
     * @Author:杨攀
     * @Since: 2014年7月31日下午4:14:43
     */
    protected abstract V getItemViewHolder();

    /**
     * @return
     * @Description: 初始化    通用的 Item 界面布局
     * @Author:杨攀
     * @Since: 2014年7月31日下午4:17:36
     */
    protected abstract View initConvertView(View convertView, V v);

    /**
     * @Description: 到达底部后的动作--显示完所有数据
     * @Author:杨攀
     * @Since: 2014年7月31日下午3:54:06
     */
    protected abstract void onReachBottom();

}
