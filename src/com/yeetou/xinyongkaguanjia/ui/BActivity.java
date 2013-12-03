package com.yeetou.xinyongkaguanjia.ui;

/**
 * 功能：消费流水页面
 */
import java.util.List;

import com.yeetou.xinyongkaguanjia.R;
import com.yeetou.xinyongkaguanjia.adapter.ExpandableAdapter_BActivity;
import com.yeetou.xinyongkaguanjia.component.MySpinnerButton;
import com.yeetou.xinyongkaguanjia.component.MySpinnerButton.MyListItemOnClickListener;
import com.yeetou.xinyongkaguanjia.db.base.DbCategory;
import com.yeetou.xinyongkaguanjia.db.service.DbCategoryService;
import com.yeetou.xinyongkaguanjia.db.service.DbStreamService;
import com.yeetou.xinyongkaguanjia.info.StreamMonthInfo;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;

public class BActivity extends AbstractActivity {
	/*
	 * 头部
	 */
	private LinearLayout LinearLayout_Ab_Left_Indicator; // 用于返回
	private TextView TextView_Ab_Title; // 设置标题 “信用卡详情”OR“储蓄卡详情”

	private ExpandableAdapter_BActivity adapter;
	private MySpinnerButton b_thedate;
	private MySpinnerButton b_category;

	private DbStreamService dbss;
	private DbStreamService dbStreamService;
	private DbCategoryService dbCategoryService;
	private List<String> dates;
	private List<String> categorys;
	private List<StreamMonthInfo> streamMonthInfos;
	private boolean isFromTab = true; // 保存是否是mainHost启动
	private String thedate; // 日期，格式：2013-9 或者 2013-10
	private String category;
	
	private ExpandableListView expandableListView ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_b);
		initData();
		init();
		initHead("消费流水");
	}

	public void initData() {
		thedate = getIntent().getStringExtra("thedate");
		category = getIntent().getStringExtra("category");

		dbStreamService = new DbStreamService(BActivity.this);
		dbCategoryService = new DbCategoryService(BActivity.this);
		dbss = new DbStreamService(BActivity.this);
		if (category == null) {
			category = "全部";
		} else {
			isFromTab = false;
		}
		if (thedate == null) {
			thedate = "全部";
		} else {
			isFromTab = false;
		}

		dates = dbStreamService.getAllDate();
		categorys = dbCategoryService.getAllCategory();
		streamMonthInfos = dbss.getStreams(thedate, category);
		expandableListView = (ExpandableListView) findViewById(R.id.b_list);
	}

	public void init() {
		b_thedate = (MySpinnerButton) this.findViewById(R.id.b_thedate);
		b_category = (MySpinnerButton) this.findViewById(R.id.b_category);
		
		if (dates != null && dates.size() != 0)
			b_thedate.setListContent(dates);
		else
			b_thedate.setClickable(false);
		
		if (categorys != null && categorys.size() != 0)
			b_category.setListContent(categorys);
		else
			b_category.setClickable(false);
		
		b_thedate.setText(thedate);
		b_category.setText(category);
		b_thedate.setMyListItemOnClick(new MyListItemOnClickListener() {

			@Override
			public void onClick(String text) {
				expandableListView.setVisibility(View.GONE);
				thedate = text;
				streamMonthInfos = dbStreamService.getStreams(thedate, category);
				if (streamMonthInfos != null && streamMonthInfos.size() != 0) {
					expandableListView.removeAllViewsInLayout();
					adapter = new ExpandableAdapter_BActivity(BActivity.this, streamMonthInfos);
					expandableListView.setAdapter(adapter);
					expandableListView.expandGroup(0);// 设置第一组张开
					expandableListView.setGroupIndicator(null);// 除去自带的箭头
					expandableListView.setVisibility(View.VISIBLE);
				}
			}
		});
		b_category.setMyListItemOnClick(new MyListItemOnClickListener() {

			@Override
			public void onClick(String text) {
				expandableListView.setVisibility(View.GONE);
				category = text;

				streamMonthInfos = dbStreamService.getStreams(thedate, category);
				if (streamMonthInfos != null && streamMonthInfos.size() != 0) {
					adapter = new ExpandableAdapter_BActivity(BActivity.this, streamMonthInfos);
					expandableListView.setAdapter(adapter);
					expandableListView.expandGroup(0);// 设置第一组张开
					expandableListView.setGroupIndicator(null);// 除去自带的箭头
					expandableListView.setVisibility(View.VISIBLE);
				}
			}
		});

		if (streamMonthInfos != null && streamMonthInfos.size() != 0) {
			adapter = new ExpandableAdapter_BActivity(this, streamMonthInfos);
			expandableListView.setAdapter(adapter);
			expandableListView.expandGroup(0);// 设置第一组张开
			expandableListView.setGroupIndicator(null);// 除去自带的箭头
		}
	}

	private void initHead(String title) {
		LinearLayout_Ab_Left_Indicator = (LinearLayout) this.findViewById(R.id.LinearLayout_Ab_Left_Indicator);
		TextView_Ab_Title = (TextView) this.findViewById(R.id.TextView_Ab_Title);
		TextView_Ab_Title.setText(title);
		if(isFromTab){
			this.findViewById(R.id.ImageView_Ab_Left_Indicator).setVisibility(View.INVISIBLE);
		}
		LinearLayout_Ab_Left_Indicator.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isFromTab) {

				} else {
					finish();
				}
			}
		});
	}

}
