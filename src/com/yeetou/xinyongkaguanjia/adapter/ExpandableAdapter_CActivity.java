package com.yeetou.xinyongkaguanjia.adapter;

import java.util.List;

import com.yeetou.xinyongkaguanjia.R;
import com.yeetou.xinyongkaguanjia.info.MonthPayments;
import com.yeetou.xinyongkaguanjia.info.YearMonthPayments;
import com.yeetou.xinyongkaguanjia.ui.C2Activity;
import com.yeetou.xinyongkaguanjia.util.ViewSetUtil;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Administrator
 * @date &{date}
 */
public class ExpandableAdapter_CActivity extends BaseExpandableListAdapter {
	private Context context;
	private float max = 1;
	private List<YearMonthPayments> yearMonthPayments;

	public ExpandableAdapter_CActivity(Context context, List<YearMonthPayments> yearMonthPayments) {
		this.context = context;
		this.yearMonthPayments = yearMonthPayments;
		for(YearMonthPayments y:yearMonthPayments){
			for(MonthPayments m:y.getMonthPayments()){
				if(m.getExpand()>max){
					max = m.getExpand();
				}
				if(m.getExpand()>max){
					max = m.getIncome();
				}
			}
			
		}
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		View view = View.inflate(context, R.layout.activity_c_item, null);
		// 初始化数据
		TextView c_item_month = (TextView) view.findViewById(R.id.c_item_month);// 月份
		c_item_month.setText(yearMonthPayments.get(groupPosition).getMonthPayments().get(childPosition).getMonth() + "月");
		TextView c_cun = (TextView) view.findViewById(R.id.c_cun);// 存、超
		if (yearMonthPayments.get(groupPosition).getMonthPayments().get(childPosition).getIncome() > yearMonthPayments.get(groupPosition).getMonthPayments().get(childPosition).getExpand()) {
			c_cun.setText("存: ￥" + (int) (yearMonthPayments.get(groupPosition).getMonthPayments().get(childPosition).getIncome() - yearMonthPayments.get(groupPosition).getMonthPayments().get(childPosition).getExpand()));
			c_cun.setTextColor(context.getResources().getColor(R.color.cart_blue));
		} else {
			c_cun.setText("超: ￥" + (int) (yearMonthPayments.get(groupPosition).getMonthPayments().get(childPosition).getExpand() - yearMonthPayments.get(groupPosition).getMonthPayments().get(childPosition).getIncome()));
			c_cun.setTextColor(context.getResources().getColor(R.color.cart_orange));
		}
		TextView c_shou = (TextView) view.findViewById(R.id.c_shou);// 收入
		c_shou.setText("收: ￥" + String.valueOf((int) yearMonthPayments.get(groupPosition).getMonthPayments().get(childPosition).getIncome()));
		TextView c_zhi = (TextView) view.findViewById(R.id.c_zhi);// 支出
		c_zhi.setText("支: ￥" + String.valueOf((int) yearMonthPayments.get(groupPosition).getMonthPayments().get(childPosition).getExpand()));

		LinearLayout c_item_bar = (LinearLayout) view.findViewById(R.id.c_item_bar);
		
		ImageView c_item_red = (ImageView) view.findViewById(R.id.c_item_red);
		ImageView c_item_green = (ImageView) view.findViewById(R.id.c_item_green);
		// 添加绿色条
		setBarWidth(c_item_bar,c_item_red, (yearMonthPayments.get(groupPosition).getMonthPayments().get(childPosition).getIncome()) / max);
		// 添加红色条
		setBarWidth(c_item_bar, c_item_green, (yearMonthPayments.get(groupPosition).getMonthPayments().get(childPosition).getExpand()) / max);

		final int pPosi = groupPosition;
		final int cPosi = childPosition;
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent toBActivity = new Intent(context, C2Activity.class);
				String thedate = yearMonthPayments.get(pPosi).getYear() + "-" + yearMonthPayments.get(pPosi).getMonthPayments().get(cPosi).getMonth();
				toBActivity.putExtra("thedate", thedate);
				context.startActivity(toBActivity);
			}
		});

		return view;
	}

	public void setBarWidth(final LinearLayout linearLayout,final ImageView image,final float pencent) {
		 
		ViewTreeObserver vto = linearLayout.getViewTreeObserver();
		
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			public boolean onPreDraw() {
				float w = pencent;
				if(w==0){
					w=(float) 0.01;
				}
				image.setLayoutParams((new LinearLayout.LayoutParams((int) (linearLayout.getMeasuredWidth() * w), 35)));
				return true;
			}
		});
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return yearMonthPayments.get(groupPosition).getMonthPayments().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return yearMonthPayments.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return yearMonthPayments.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		LinearLayout ll = new LinearLayout(context);
		ll.setGravity(Gravity.CENTER);
		ll.setBackgroundResource(R.color.low_gray);

		TextView textView = new TextView(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, ViewSetUtil.dip2px(context, 35));
		
		textView.setLayoutParams(lp);
		textView.setGravity(Gravity.CENTER);
		textView.setText(yearMonthPayments.get(groupPosition).getYear() + "年");
		textView.setTextColor(context.getResources().getColor(R.color.title_blue));
		ll.addView(textView);
		return ll;

	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}