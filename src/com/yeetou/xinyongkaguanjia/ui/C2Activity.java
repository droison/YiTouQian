package com.yeetou.xinyongkaguanjia.ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.achartengine.GraphicalView;

import com.yeetou.xinyongkaguanjia.R;
import com.yeetou.xinyongkaguanjia.component.MySpinnerButton;
import com.yeetou.xinyongkaguanjia.component.MySpinnerButton.MyListItemOnClickListener;
import com.yeetou.xinyongkaguanjia.constants.AppConstant;
import com.yeetou.xinyongkaguanjia.db.service.DbStreamService;
import com.yeetou.xinyongkaguanjia.util.AchartUtil;
import com.yeetou.xinyongkaguanjia.util.NumberFormateUtil;

import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class C2Activity extends AbstractActivity {

	/*
	 * 头部
	 */
	private LinearLayout LinearLayout_Ab_Left_Indicator; // 用于返回
	private TextView TextView_Ab_Title; // 设置标题 “信用卡详情”OR“储蓄卡详情”

	private MySpinnerButton c2_pick_date;
	private RelativeLayout c2_cart;
	private GraphicalView view;
	private TextView c2_total_expend;
	private LinearLayout c2_pie_legend;
	private LinearLayout c2_bar_legend;
	private LinearLayout c2_pie_bt;
	private LinearLayout c2_bar_bt;
	private RelativeLayout c2_pie_layout;
	private RelativeLayout c2_bar_layout;
	private DbStreamService dbStreamService;
	private String thedate;
	private Map<String, Float> expends;
	private Map<String, Integer> category_img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_c2);
		category_img = new HashMap<String, Integer>();
		category_img.put("吃喝", R.drawable.food);
		category_img.put("购物", R.drawable.shoping);
		category_img.put("网购", R.drawable.ol_shoping);
		category_img.put("出行", R.drawable.travel);
		category_img.put("生活", R.drawable.life);
		category_img.put("玩乐", R.drawable.game);
		category_img.put("爱车", R.drawable.car);

		init();
		initHead("收支分析");
	}

	public void init() {
		dbStreamService = new DbStreamService(C2Activity.this);
		thedate = this.getIntent().getStringExtra("thedate");
		expends = dbStreamService.getExpandByCategory(thedate);

		c2_pick_date = (MySpinnerButton) findViewById(R.id.c2_pick_date);
		c2_pick_date.setText(thedate);
		List<String> datas = dbStreamService.getAllDate();
		c2_pick_date.setListContent(datas);
		c2_pick_date.setContainAll(false);
		c2_pick_date.setMyListItemOnClick(new MyListItemOnClickListener() {

			@Override
			public void onClick(String text) {
				thedate = text;
				expends = dbStreamService.getExpandByCategory(thedate);
				view = AchartUtil.getPieGraphicalView(C2Activity.this, expends);
				c2_cart.removeAllViews();
				c2_cart.addView(view);

				setupPie();
				setupXY();
			}
		});

		c2_total_expend = (TextView) this.findViewById(R.id.c2_total_expend);

		c2_pie_layout = (RelativeLayout) findViewById(R.id.c2_pie_layout);
		c2_bar_layout = (RelativeLayout) findViewById(R.id.c2_bar_layout);
		c2_pie_bt = (LinearLayout) findViewById(R.id.c2_pie_bt);
		c2_bar_bt = (LinearLayout) findViewById(R.id.c2_bar_bt);
		c2_pie_bt.setBackgroundColor(Color.GRAY);
		c2_pie_bt.getChildAt(0).setVisibility(View.GONE);
		c2_pie_bt.getChildAt(1).setBackgroundResource(R.drawable.c2_pie_click);
		((TextView) c2_pie_bt.getChildAt(2)).setTextColor(Color.WHITE);
		c2_pie_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				c2_pie_layout.setVisibility(View.VISIBLE);
				c2_bar_layout.setVisibility(View.GONE);

				c2_pie_bt.setBackgroundColor(Color.GRAY);
				c2_pie_bt.getChildAt(0).setVisibility(View.GONE);
				c2_pie_bt.getChildAt(1).setBackgroundResource(R.drawable.c2_pie_click);
				((TextView) c2_pie_bt.getChildAt(2)).setTextColor(Color.WHITE);

				c2_bar_bt.setBackgroundDrawable(null);
				c2_bar_bt.getChildAt(0).setBackgroundResource(R.drawable.c2_bar_unclick);
				((TextView) c2_bar_bt.getChildAt(1)).setTextColor(Color.GRAY);
			}
		});

		c2_bar_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				c2_pie_layout.setVisibility(View.GONE);
				c2_bar_layout.setVisibility(View.VISIBLE);

				c2_bar_bt.setBackgroundColor(Color.GRAY);
				c2_bar_bt.getChildAt(0).setBackgroundResource(R.drawable.c2_bar_click);
				((TextView) c2_bar_bt.getChildAt(1)).setTextColor(Color.WHITE);

				c2_pie_bt.setBackgroundDrawable(null);
				c2_pie_bt.getChildAt(0).setVisibility(View.VISIBLE);
				c2_pie_bt.getChildAt(1).setBackgroundResource(R.drawable.c2_pie_unclick);
				((TextView) c2_pie_bt.getChildAt(2)).setTextColor(Color.GRAY);

			}
		});

		// 初始化饼状图下面的
		c2_cart = (RelativeLayout) findViewById(R.id.c2_cart);
		view = AchartUtil.getPieGraphicalView(this, expends);
		c2_cart.addView(view);
		/*
		 * text_zhi = new TextView(this); text_zhi.setText("123");
		 * text_zhi.setTextColor(getResources().getColor(R.color.black));
		 * c2_cart.addView(text_zhi);
		 */
		c2_pie_legend = (LinearLayout) findViewById(R.id.c2_pie_legend);

		setupPie();
		// 初始化柱状图
		c2_bar_legend = (LinearLayout) findViewById(R.id.c2_bar_legend);
		setupXY();
	}

	// 获取长度的回调函数
	/*
	 * public void getMaxWidth(final View view){ ViewTreeObserver vto =
	 * view.getViewTreeObserver(); vto.addOnPreDrawListener(new
	 * ViewTreeObserver.OnPreDrawListener() { public boolean onPreDraw() {
	 * max_width = view.getMeasuredWidth(); System.out.println(max_width);
	 * return true; } }); }
	 */

	// 使用数组形式操作
	class SpinnerSelectedListener implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}

	}

	// 初始化饼状图下边标注
	public void setupPie() {
		Iterator iter = expends.keySet().iterator();
		int i = 0;
		float sum = 0;
		c2_pie_legend.removeAllViews();
		while (iter.hasNext()) {
			sum += expends.get(iter.next());
		}
		iter = expends.keySet().iterator();
		while (iter.hasNext()) {
			final String key = (String) iter.next();
			View view = View.inflate(this, R.layout.activity_c2_pie_item, null);
			// view.setBackgroundResource(R.color.white);
			ImageView c2_color = (ImageView) view.findViewById(R.id.c2_color);
			TextView c2_pie_category = (TextView) view.findViewById(R.id.c2_pie_category);
			TextView c2_money = (TextView) view.findViewById(R.id.c2_money);
			TextView c2_percent = (TextView) view.findViewById(R.id.c2_percent);
			c2_pie_category.setText(key);
			c2_money.setText(NumberFormateUtil.Fromate2ex(expends.get(key)));
			c2_percent.setText(NumberFormateUtil.Fromate3(expends.get(key) / sum));
			c2_color.setBackgroundColor(getResources().getColor(AppConstant.cart_color[i]));
			c2_pie_legend.addView(view);
			view.setTag(key);
			final String thedate = c2_pick_date.getText().toString();
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent toBActivity = new Intent(C2Activity.this, BActivity.class);
					toBActivity.putExtra("category", key);
					toBActivity.putExtra("thedate", thedate);
					toBActivity.putExtra("activityType", "C2Activity");
					startActivity(toBActivity);

				}
			});
			i++;
		}
		c2_total_expend.setText(NumberFormateUtil.Fromate2ex(sum));
	}

	// 初始化柱状图
	public void setupXY() {
		Iterator iter = expends.keySet().iterator();
		int i = 0;
		float max = 1;
		c2_bar_legend.removeAllViews();
		while (iter.hasNext()) {
			float cur = expends.get(iter.next());
			if (cur > max)
				max = cur;
		}
		iter = expends.keySet().iterator();
		while (iter.hasNext()) {

			final String key = (String) iter.next();
			View view = View.inflate(this, R.layout.activity_c2_bar_item, null);
			/*
			 * if(i==0){ ImageView c2_bar_item_divide = (ImageView)
			 * view.findViewById(R.id.c2_bar_item_divide);
			 * c2_bar_item_divide.setVisibility(View.GONE); }
			 */

			ImageView c2_color = (ImageView) view.findViewById(R.id.c2_bar_color);
			TextView c2_bar_item_category = (TextView) view.findViewById(R.id.c2_bar_item_category);
			c2_bar_item_category.setText(key);
			c2_color.setImageResource(category_img.get(key));

			// 添加横向柱状图
			ImageView c2_color_l = (ImageView) view.findViewById(R.id.c2_color_l);
			c2_color_l.setLayoutParams(new LinearLayout.LayoutParams((int) (400 * expends.get(key) / max), 30));
			c2_color_l.setBackgroundColor(getResources().getColor(AppConstant.cart_color[i]));

			// 添加文字
			TextView c2_money = (TextView) view.findViewById(R.id.c2_money);
			c2_money.setText(NumberFormateUtil.Fromate2ex(expends.get(key)));

			c2_bar_legend.addView(view);
			view.setTag(key);

			final String thedate = c2_pick_date.getText().toString();
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent toBActivity = new Intent(C2Activity.this, BActivity.class);
					toBActivity.putExtra("category", key);
					toBActivity.putExtra("thedate", thedate);
					toBActivity.putExtra("activityType", "C2Activity");
					startActivity(toBActivity);
				}
			});
			i++;
		}
	}

	private void initHead(String title) {
		LinearLayout_Ab_Left_Indicator = (LinearLayout) this.findViewById(R.id.LinearLayout_Ab_Left_Indicator);
		TextView_Ab_Title = (TextView) this.findViewById(R.id.TextView_Ab_Title);
		TextView_Ab_Title.setText(title);
		LinearLayout_Ab_Left_Indicator.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

}
