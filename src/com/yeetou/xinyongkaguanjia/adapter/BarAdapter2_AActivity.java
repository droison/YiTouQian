package com.yeetou.xinyongkaguanjia.adapter;

import java.util.ArrayList;
import java.util.List;

import com.yeetou.xinyongkaguanjia.R;
import com.yeetou.xinyongkaguanjia.constants.AppConstant;
import com.yeetou.xinyongkaguanjia.info.MonthPayments;
import com.yeetou.xinyongkaguanjia.util.NumberFormateUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BarAdapter2_AActivity extends BaseAdapter {

	private Context mContext;
	private float max = 0;
	private List<MonthPayments> monthPayments = new ArrayList<MonthPayments>();
	private int barmax = 140;

	public BarAdapter2_AActivity(Context mContext, List<MonthPayments> monthPayments, int barmax) {
		this.mContext = mContext;
		this.monthPayments = monthPayments;
		this.barmax = barmax;
		for (MonthPayments mp : monthPayments) {
			if (mp.getExpand() > max) {
				max = mp.getExpand();
			}
			if (mp.getIncome() > max) {
				max = mp.getIncome();
			}
		}
	}

	@Override
	public int getCount() {
		return monthPayments.size();
	}

	@Override
	public Object getItem(int position) {
		return monthPayments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		MonthPayments temp = monthPayments.get(position);
		convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_info_bar_item2, null);
		TextView title = (TextView) convertView.findViewById(R.id.a_bar_item2_title);
		TextView card_info_bar_item2_amount1 = (TextView) convertView.findViewById(R.id.card_info_bar_item2_amount1);
		ImageView a_bar_item2_expend1 = (ImageView) convertView.findViewById(R.id.a_bar_item2_expend1);
		TextView card_info_bar_item2_amount2 = (TextView) convertView.findViewById(R.id.card_info_bar_item2_amount2);
		ImageView a_bar_item2_expend2 = (ImageView) convertView.findViewById(R.id.a_bar_item2_expend2);
		title.setText(temp.getYear() + "-" + temp.getMonth());

		int high = (int) (barmax * (temp.getExpand()) / max);
		if (high == 0)
			high = 1;
		a_bar_item2_expend1.setLayoutParams(new LinearLayout.LayoutParams(50, high));
		a_bar_item2_expend1.setBackgroundColor(mContext.getResources().getColor(AppConstant.bar_expend));
		card_info_bar_item2_amount1.setText(NumberFormateUtil.Fromate0(temp.getExpand()));

		high = (int) (barmax * (temp.getIncome()) / max);
		if (high == 0)
			high = 1;
		a_bar_item2_expend2.setLayoutParams(new LinearLayout.LayoutParams(50, high));
		a_bar_item2_expend2.setBackgroundColor(mContext.getResources().getColor(AppConstant.bar_income));
		card_info_bar_item2_amount2.setText(NumberFormateUtil.Fromate0(temp.getIncome()));
		convertView.setTag(temp.getYear() + "-" + temp.getMonth());

		return convertView;
	}

}
