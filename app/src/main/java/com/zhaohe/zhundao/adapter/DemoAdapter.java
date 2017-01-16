package com.zhaohe.zhundao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.bean.DemoBean;
import com.zhaohe.zhundao.adapter.holder.DemoHolder;

import java.util.List;

public class DemoAdapter extends AdapterBase<DemoBean, DemoHolder> {
	private LayoutInflater inflater;
	private Context mContext;

	public DemoAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mContext = context;
	}

	@Override
	protected void handlerData(List<DemoBean> mList2, int position,
							   DemoHolder itemView) {
		DemoBean bean = mList2.get(position);
		itemView.btn_act_edit1.setText(bean.getEdit());
		itemView.btn_act_list1.setText(bean.getList());
		itemView.btn_act_share1.setText(bean.getShare());
		itemView.btn_act_more1.setText(bean.getMore());

	}

	@Override
	protected DemoHolder getItemViewHolder() {
		// TODO Auto-generated method stub
		return new DemoHolder();
	}

	@Override
	protected View initConvertView(View convertView, DemoHolder v) {
		convertView = inflater.inflate(R.layout.list_item_demo, null);
		v.btn_act_edit1 = (Button) convertView
				.findViewById(R.id.btn_act_edit1);
		v.btn_act_list1 = (Button) convertView
				.findViewById(R.id.btn_act_list1);
		v.btn_act_share1 = (Button) convertView
				.findViewById(R.id.btn_act_share1);
		v.btn_act_more1 = (Button) convertView
				.findViewById(R.id.btn_act_more1);

		return convertView;
	}

	@Override
	protected void onReachBottom() {
		// TODO Auto-generated method stub

	}

}
