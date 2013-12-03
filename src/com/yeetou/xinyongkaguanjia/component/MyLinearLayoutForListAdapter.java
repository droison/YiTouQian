package com.yeetou.xinyongkaguanjia.component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.achartengine.GraphicalView;

import com.yeetou.xinyongkaguanjia.R;
import com.yeetou.xinyongkaguanjia.adapter.AdapterForLinearLayout;
import com.yeetou.xinyongkaguanjia.constants.AppConstant;
import com.yeetou.xinyongkaguanjia.db.service.DbStreamService;
import com.yeetou.xinyongkaguanjia.info.MonthPayments;
import com.yeetou.xinyongkaguanjia.util.AchartUtil;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyLinearLayoutForListAdapter extends LinearLayout {

	public AdapterForLinearLayout adapter;
	public OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int i = (Integer) v.getTag();
			setItemOnClick(i);
		}
	};
	public OnLongClickListener mOnLongClickListener = null;
	private Context context;
	private List<MonthPayments> monthPayments;
	private Map<String, Float> expends;
	private DbStreamService dbStreamService;
	private LinearLayout linearLayout;
	private String bankname;
	private String number;

	public MyLinearLayoutForListAdapter(Context context) {
		super(context);
		this.context = context;

	}

	public void setInputDatas(Context context, List<MonthPayments> monthPayments, LinearLayout pie, String bankname, String number) {
		this.context = context;
		this.linearLayout = pie;
		this.monthPayments = monthPayments;
		this.bankname = bankname;
		this.number = number;
		dbStreamService = new DbStreamService(context);
	}

	public MyLinearLayoutForListAdapter(Context context, AttributeSet attrs) {
		super(context, attrs);
		dbStreamService = new DbStreamService(context);
	}

	public void bindLinearLayout() {
		int count = adapter.getCount();
		for (int i = 0; i < count; i++) {
			View v = adapter.getDropDownView(i, null, null);
			v.setTag(i);
			v.setOnClickListener(this.mOnClickListener);
			// v.setOnLongClickListener(this.mOnLongClickListener);

			this.addView(v, i);
		}
		setItemOnClick(0);
	}

	public void setItemOnClick(int j) {

		int count = adapter.getCount();
		for (int i = 0; i < count; i++) {
			View v = this.getChildAt(i);
			TextView card_info_bar_item_amount = (TextView) v.findViewById(R.id.card_info_bar_item_amount);
			ImageView c_color_expend = (ImageView) v.findViewById(R.id.a_bar_item_expend);
			TextView card_info_bar_item_date = (TextView) v.findViewById(R.id.card_info_bar_item_date);
			if (i == j) {
				card_info_bar_item_amount.setTextColor(Color.BLUE);
				card_info_bar_item_date.setTextColor(Color.BLUE);
				c_color_expend.setBackgroundColor(Color.BLUE);
			} else {
				card_info_bar_item_amount.setTextColor(Color.GRAY);
				card_info_bar_item_date.setTextColor(Color.GRAY);
				c_color_expend.setBackgroundColor(Color.GRAY);
			}
		}
		MonthPayments mpm = monthPayments.get(j);
		String thedate = mpm.getYear() + "-" + mpm.getMonth();
		TextView card_info_pie_date = (TextView) linearLayout.findViewById(R.id.card_info_pie_date);
		RelativeLayout card_info_pie = (RelativeLayout) linearLayout.findViewById(R.id.card_info_pie);
		LinearLayout card_info_pie_item_item = (LinearLayout) linearLayout.findViewById(R.id.card_info_pie_item_item);
		card_info_pie_date.setText(thedate + "月消费分类图");
		setupPie(card_info_pie_item_item, card_info_pie, mpm.getYear(), mpm.getMonth());

	}

	public AdapterForLinearLayout getAdapter() {
		return adapter;
	}

	public void setAdapter(AdapterForLinearLayout adapter) {
		this.adapter = adapter;
		bindLinearLayout();
	}

	@Override
	public void setOnClickListener(OnClickListener mOnClickListener) {
		this.mOnClickListener = mOnClickListener;
	}

	public OnClickListener getOnClickListener() {
		return mOnClickListener;
	}

	@Override
	public void setOnLongClickListener(OnLongClickListener mOnLongClickListener) {
		this.mOnLongClickListener = mOnLongClickListener;
	}

	public OnLongClickListener getOnLongClickListener() {
		return mOnLongClickListener;
	}

	public void setupPie(LinearLayout card_info_pie_item_item, RelativeLayout card_info_pie, int year,int month) {
		expends = dbStreamService.getExpandByCategory(year,month,bankname,number);
		Iterator iter = expends.keySet().iterator();
		int i = 0;
		float sum = 0;
		card_info_pie_item_item.removeAllViews();
		while (iter.hasNext()) {
			sum += expends.get(iter.next());
		}
		iter = expends.keySet().iterator();
		while (iter.hasNext()) {
			final String key = (String) iter.next();
			View view = View.inflate(context, R.layout.card_info_pie_item_item, null);

			ImageView card_info_item_item_imag = (ImageView) view.findViewById(R.id.card_info_item_item_imag);
			TextView card_info_item_item_text1 = (TextView) view.findViewById(R.id.card_info_item_item_text1);
			TextView card_info_item_item_text2 = (TextView) view.findViewById(R.id.card_info_item_item_text2);
			card_info_item_item_text1.setText(key);
			card_info_item_item_text2.setText("￥" + expends.get(key) + "[" + Math.round(100 * expends.get(key) / sum) + "%" + "]");
			card_info_item_item_imag.setBackgroundColor(context.getResources().getColor(AppConstant.cart_color[i]));

			LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);

			view.setLayoutParams(param);

			card_info_pie_item_item.addView(view);

			i++;
		}

		GraphicalView view = AchartUtil.getPieGraphicalView(context, expends);
		card_info_pie.addView(view);

	}
}
