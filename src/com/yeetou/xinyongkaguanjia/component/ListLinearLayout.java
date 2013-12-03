package com.yeetou.xinyongkaguanjia.component;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

public class ListLinearLayout extends LinearLayout {

	public BaseAdapter adapter;
	public OnClickListener mOnClickListener = null;
	public OnLongClickListener mOnLongClickListener = null;

	public ListLinearLayout(Context context) {
		super(context);
	}

	public ListLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void bindLinearLayout() {
		int count = adapter.getCount();
		for (int i = 0; i < count; i++) {
			View v = adapter.getDropDownView(i, null, null);

		//	v.setOnClickListener(this.mOnClickListener);
		//	v.setOnLongClickListener(this.mOnLongClickListener);
			
			this.addView(v, i);
		}
		Log.v("countTAG", "" + count);
	}

	public BaseAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(BaseAdapter adapter) {
		ListLinearLayout.this.removeAllViews();
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

}
