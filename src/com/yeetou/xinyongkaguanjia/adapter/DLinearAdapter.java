package com.yeetou.xinyongkaguanjia.adapter;

import java.util.ArrayList;
import java.util.List;

import com.yeetou.xinyongkaguanjia.R;
import com.yeetou.xinyongkaguanjia.db.base.DbLcBank;
import com.yeetou.xinyongkaguanjia.util.StringUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DLinearAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<DbLcBank> lcBanks;
	private Context context;


	public DLinearAdapter(Context context, List<DbLcBank> lcBanks) {
		this.context = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.lcBanks = lcBanks;
	}

	@Override
	public int getCount() {
		return lcBanks.size();
	}

	@Override
	public Object getItem(int arg0) {
		return lcBanks.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView =  mInflater.inflate(R.layout.d_item_2, null);
		DbLcBank temp = lcBanks.get(position);
		TextView d_item2_name = (TextView) convertView.findViewById(R.id.d_item2_name);
		ImageView d_item2_logo = (ImageView) convertView.findViewById(R.id.d_item2_logo);
		d_item2_name.setText(temp.getName());
		Bitmap bm = StringUtil.getBitmapFromAssert(context, temp.getLogo());
		if(bm!=null){
			d_item2_logo.setImageBitmap(bm);
		}
		return convertView;
	}

}
