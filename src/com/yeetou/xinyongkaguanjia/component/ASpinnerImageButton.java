package com.yeetou.xinyongkaguanjia.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yeetou.xinyongkaguanjia.R;
import com.yeetou.xinyongkaguanjia.http.service.AutoSyncData;
import com.yeetou.xinyongkaguanjia.ui.BEmailActivity;
import com.yeetou.xinyongkaguanjia.util.ViewSetUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 我的SpinnerButton
 * 
 * @author chaisong
 * 
 */
public class ASpinnerImageButton extends Button {

	private Context context;

	public ASpinnerImageButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		// 设置监听事件
		setOnClickListener(new MySpinnerButtonOnClickListener());
	}

	public ASpinnerImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		// 设置监听事件
		setOnClickListener(new MySpinnerButtonOnClickListener());
	}

	public ASpinnerImageButton(Context context) {
		super(context);
		this.context = context;
		// 设置监听事件
		setOnClickListener(new MySpinnerButtonOnClickListener());
	}

	/**
	 * MySpinnerButton的点击事件
	 * 
	 * @author chaisong
	 * 
	 */
	class MySpinnerButtonOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {

			final MySpinnerDropDownItems mSpinnerDropDrownItems = new MySpinnerDropDownItems(context);
			if (!mSpinnerDropDrownItems.isShowing()) {
				mSpinnerDropDrownItems.showAsDropDown(ASpinnerImageButton.this);
			}
		}
	}

	/**
	 * MySpinnerButton的下拉列表
	 * 
	 * @author chaisong
	 * 
	 */
	class MySpinnerDropDownItems extends PopupWindow {

		private Context context;
		private LinearLayout mLayout; // 下拉列表的布局
		private ListView mListView; // 下拉列表控件
		private ArrayList<HashMap<String, String>> mData;

		public MySpinnerDropDownItems(Context context) {
			super(context);

			this.context = context;
			// 下拉列表的布局
			mLayout = new LinearLayout(context);
			mLayout.setOrientation(LinearLayout.VERTICAL);
			mLayout.setBackgroundColor(Color.WHITE);
			// 下拉列表控件
			mListView = new ListView(context);
			mListView.setBackgroundColor(Color.WHITE);
			mListView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		//	mListView.setCacheColorHint(Color.TRANSPARENT);
			// 为listView设置适配器
			mListView.setAdapter(new AToolAdapter(context));
			// 把下拉列表添加到layout中。
			mLayout.addView(mListView);

			setWidth(ViewSetUtil.dip2px(context, 140));
			setHeight(LayoutParams.WRAP_CONTENT);
			setContentView(mLayout);
			setFocusable(true);

			mLayout.setFocusableInTouchMode(true);
		}

		/**
		 * 我的适配器
		 * 
		 * @author chaisong
		 * 
		 */
		public class AToolAdapter extends BaseAdapter {

			private Context context;
			private LayoutInflater mLayoutInflater;
			private String[] content = { "导入邮件账单", "扫描短信账单" };

			public AToolAdapter(Context context) {
				this.context = context;
				this.mLayoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

			}

			public int getCount() {

				return content.length;
			}

			public Object getItem(int position) {

				return content[position];
			}

			public long getItemId(int position) {

				return position;
			}

			public View getView(int position, View contentView, ViewGroup parent) {

				contentView = this.mLayoutInflater.inflate(R.layout.a_spinner_dropdown_item, null);
				TextView a_spinner_item_text = (TextView) contentView.findViewById(R.id.a_spinner_item_text);
				ImageView a_spinner_item_img = (ImageView) contentView.findViewById(R.id.a_spinner_item_img);
				a_spinner_item_text.setText(content[position]);

				if (position == 0) {
					a_spinner_item_img.setImageResource(R.drawable.scan_email_img);
					contentView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent toBEmail = new Intent(ASpinnerImageButton.this.context, BEmailActivity.class);
							ASpinnerImageButton.this.context.startActivity(toBEmail);
							MySpinnerDropDownItems.this.dismiss();
						}
					});
				} else {
					a_spinner_item_img.setImageResource(R.drawable.scan_sms_img);
					contentView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							new AutoSyncData(ASpinnerImageButton.this.context, new Handler());
							MySpinnerDropDownItems.this.dismiss();
						}
					});
				}

				return contentView;
			}
		}
	}
}
