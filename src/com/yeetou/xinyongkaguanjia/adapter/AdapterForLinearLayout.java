package com.yeetou.xinyongkaguanjia.adapter;

import java.util.ArrayList;
import java.util.List;

import com.yeetou.xinyongkaguanjia.R;
import com.yeetou.xinyongkaguanjia.info.MonthPayments;
import com.yeetou.xinyongkaguanjia.util.ViewSetUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdapterForLinearLayout extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<MonthPayments> monthPayments = new ArrayList<MonthPayments>();
	private float max = 0;
	private Context context;
	private int maxBarHigh;
	private int maxBarWidth;

	public AdapterForLinearLayout(Context context, List<MonthPayments> monthPayments) {
		this.context = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.monthPayments = monthPayments;
		for (MonthPayments mpm : monthPayments) {
			if (mpm.getExpand() > max) {
				max = mpm.getExpand();
			}
		}
		maxBarHigh = ViewSetUtil.dip2px(context, 96);
		maxBarWidth = ViewSetUtil.dip2px(context, 35);
	}

	@Override
	public int getCount() {
		return monthPayments.size();
	}

	@Override
	public Object getItem(int arg0) {
		return monthPayments.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView =  mInflater.inflate(R.layout.card_info_bar_item, null);
		MonthPayments mpm = monthPayments.get(position);
		TextView card_info_bar_item_amount = (TextView) convertView.findViewById(R.id.card_info_bar_item_amount);
		ImageView c_color_expend = (ImageView) convertView.findViewById(R.id.a_bar_item_expend);
		TextView card_info_bar_item_date = (TextView) convertView.findViewById(R.id.card_info_bar_item_date);
		card_info_bar_item_amount.setText(mpm.getExpand() + "");
		card_info_bar_item_date.setText(mpm.getYear() + "-" + mpm.getMonth());
		int high = (int) (maxBarHigh * (mpm.getExpand()) / max);
		if (high == 0)
			high = 1;
		c_color_expend.setLayoutParams(new LinearLayout.LayoutParams(maxBarWidth, high));

		return convertView;
	}

}
