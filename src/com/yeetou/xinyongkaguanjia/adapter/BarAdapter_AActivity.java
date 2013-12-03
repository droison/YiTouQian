package com.yeetou.xinyongkaguanjia.adapter;

import java.util.ArrayList;
import java.util.List;

import com.yeetou.xinyongkaguanjia.R;
import com.yeetou.xinyongkaguanjia.constants.AppConstant;
import com.yeetou.xinyongkaguanjia.info.MonthPayments;
import com.yeetou.xinyongkaguanjia.info.YearMonthPayments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BarAdapter_AActivity extends BaseAdapter {

	private Context mContext;
	private float max = 0;
	private List<MonthPayments> monthPayments = new ArrayList<MonthPayments>();
	private int barmax = 160;

	public BarAdapter_AActivity(Context mContext, List<YearMonthPayments> yearMonthPayments) {
		this.mContext = mContext;
		for (YearMonthPayments yearMonthPayment : yearMonthPayments) {
			monthPayments.addAll(yearMonthPayment.getMonthPayments());
		}
		for (MonthPayments mp : monthPayments) {
			if (mp.getExpand() > max) {
				max = mp.getExpand();
			}
			if (mp.getIncome() > max) {
				max = mp.getIncome();
			}
		}
	}
	
	public BarAdapter_AActivity(Context mContext, List<MonthPayments> monthPayments, int barmax) {
		this.mContext = mContext;
		this.monthPayments =monthPayments;
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
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_a_bar_item, null);
		TextView title = (TextView) retval.findViewById(R.id.a_bar_item_title);
		LinearLayout a_bar_item_expend = (LinearLayout) retval.findViewById(R.id.a_bar_item_expend);
		LinearLayout a_bar_item_income = (LinearLayout) retval.findViewById(R.id.a_bar_item_income);
		if(monthPayments.get(position).getMonth()==1){
			title.setText(monthPayments.get(position).getYear()+"");
		}else{
			title.setText(monthPayments.get(position).getMonth()+"月");
		}
		

		ImageView c_color_expend = new ImageView(mContext);
		int high = (int) (barmax * (monthPayments.get(position).getExpand())/max);
		if(high==0)
			high=1;
		c_color_expend.setLayoutParams(new LinearLayout.LayoutParams(30, high));
		c_color_expend.setBackgroundColor(mContext.getResources().getColor(AppConstant.bar_expend));
		
		ImageView c_color_income = new ImageView(mContext);
		high = (int) (barmax * (monthPayments.get(position).getIncome())/max);
		if(high==0)
			high=1;
		c_color_income.setLayoutParams(new LinearLayout.LayoutParams(30, high));
		c_color_income.setBackgroundColor(mContext.getResources().getColor(AppConstant.bar_income));
		
		a_bar_item_expend.addView(c_color_expend);
		a_bar_item_income.addView(c_color_income);
		retval.setTag(monthPayments.get(position).getYear()+"-"+monthPayments.get(position).getMonth());
		
		return retval;
	}

}
