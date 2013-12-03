package com.yeetou.xinyongkaguanjia.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.yeetou.xinyongkaguanjia.R;
import com.yeetou.xinyongkaguanjia.info.StreamDayInfo;
import com.yeetou.xinyongkaguanjia.info.StreamInfo;
import com.yeetou.xinyongkaguanjia.info.StreamMonthInfo;
import com.yeetou.xinyongkaguanjia.util.NumberFormateUtil;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Administrator
 * @date &{date}
 */
public class ExpandableAdapter_CardInfoActivity extends BaseExpandableListAdapter {
	private Context context;
	private List<TempDebitStreams> temps = new ArrayList<TempDebitStreams>();

	public ExpandableAdapter_CardInfoActivity(Context context, List<StreamMonthInfo> streamMonthInfos) {
		this.context = context;
		for(StreamMonthInfo smi:streamMonthInfos){
			TempDebitStreams tds = new TempDebitStreams();
			tds.setYear(smi.getYear());
			tds.setMonth(smi.getMonth());
			List<StreamInfo> sis = new ArrayList<StreamInfo>();
			for(StreamDayInfo sdi:smi.getStreamdays()){
				sis.addAll(sdi.getStreams());
			}
			tds.setStreams(sis);
			temps.add(tds);
		}
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		// return generals[groupPosition][childPosition];
		return temps.get(groupPosition).getStreams().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

		View view = View.inflate(context, R.layout.card_info_curr_debit_item, null);
		int size = temps.get(groupPosition).getStreams().size();
		StreamInfo si = temps.get(groupPosition).getStreams().get(childPosition);
		Calendar cal = Calendar.getInstance();  
		cal.setTime(new Date(si.getTrade_time()));
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		
		TextView card_info_debit_item_amount = (TextView) view.findViewById(R.id.card_info_debit_item_amount);
		TextView card_info_debit_item_date = (TextView) view.findViewById(R.id.card_info_debit_item_date);
		ImageView card_info_debit_item_name = (ImageView) view.findViewById(R.id.card_info_debit_item_name);
		
		card_info_debit_item_amount.setText("￥" + si.getAmount());
		card_info_debit_item_date.setText(month+"月"+day+"日");
		if(si.getCategory().equals("收入")){
			card_info_debit_item_name.setImageResource(R.drawable.debit_income);
			card_info_debit_item_amount.setTextColor(Color.RED);
		}else{
			card_info_debit_item_name.setImageResource(R.drawable.debit_expend);
		}
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		// return generals[groupPosition].length;
		if (temps != null) {
			return temps.get(groupPosition).getStreams().size();
		} else {
			return 0;
		}

	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		// return generalsTypes[groupPosition];
		return temps.get(groupPosition);

	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		// return generalsTypes.length;
		if (temps != null) {
			return temps.size();
		} else {
			return 0;
		}

	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LinearLayout ll = new LinearLayout(context);
		ll.setGravity(Gravity.CENTER);
		ll.setBackgroundResource(R.color.low_gray);

		TextView textView = new TextView(context);
		textView.setLayoutParams(new LinearLayout.LayoutParams(0, 40, 1));
		textView.setGravity(Gravity.CENTER);
		textView.setText(temps.get(groupPosition).getYear() + "年" + temps.get(groupPosition).getMonth()+"月");
		textView.setTextColor(context.getResources().getColor(R.color.title_blue));
		ll.addView(textView);

		float sumE = 0;
		float sumI = 0;
		List<StreamInfo> days = temps.get(groupPosition).getStreams();
		for (StreamInfo day : days) {
			if(day.getCategory().equals("收入")){
				sumI += day.getAmount();
			}else{
				sumE += day.getAmount();
			}
		}

		textView = new TextView(context);
		textView.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
		textView.setGravity(Gravity.LEFT);
		textView.setText("支出:" + NumberFormateUtil.Fromate2ex(sumE));
		textView.setTextColor(context.getResources().getColor(R.color.title_blue));
		ll.addView(textView);
		textView = new TextView(context);
		textView.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
		textView.setGravity(Gravity.LEFT);
		textView.setText("收入:" + NumberFormateUtil.Fromate2ex(sumI));
		textView.setTextColor(context.getResources().getColor(R.color.title_blue));
		
		ll.addView(textView);

		return ll;

	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	class TempDebitStreams {
		private int year;
		private int month;
		private List<StreamInfo> streams;

		public int getYear() {
			return year;
		}

		public void setYear(int year) {
			this.year = year;
		}

		public int getMonth() {
			return month;
		}

		public void setMonth(int month) {
			this.month = month;
		}

		public List<StreamInfo> getStreams() {
			return streams;
		}

		public void setStreams(List<StreamInfo> streams) {
			this.streams = streams;
		}
	}
}