package com.yeetou.xinyongkaguanjia.adapter;

import java.util.List;

import com.yeetou.xinyongkaguanjia.R;
import com.yeetou.xinyongkaguanjia.db.service.DbBankService;
import com.yeetou.xinyongkaguanjia.http.base.LccpBase.Lccp;
import com.yeetou.xinyongkaguanjia.ui.LcCardActivity;
import com.yeetou.xinyongkaguanjia.util.StringUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DListViewAdapter extends BaseAdapter {

	private Context mContext;
	private List<Lccp> lccps;
	private LayoutInflater mInflater;
	private DbBankService dbBankService;

	public DListViewAdapter(Context mContext, List<Lccp> lccps) {
		super();
		this.mContext = mContext;
		this.lccps = lccps;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lccps.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return lccps.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Lccp l = lccps.get(position);
		dbBankService = new DbBankService(mContext);
		if (position == 0) {
			convertView = mInflater.inflate(R.layout.d_item_1, null);
			TextView title = (TextView) convertView.findViewById(R.id.item_title);
			ImageView d_item_banklogo = (ImageView) convertView.findViewById(R.id.d_item_banklogo);
			TextView d_item_text1 = (TextView) convertView.findViewById(R.id.d_item_text1);
			TextView d_item_2_text_2 = (TextView) convertView.findViewById(R.id.d_item_2_text_2);
			TextView d_item_text3 = (TextView) convertView.findViewById(R.id.d_item_text3);

			title.setText(lccps.get(position).getName());
			if (dbBankService.getByName(lccps.get(position).getBank()) != null) {
				if (dbBankService.getByName((lccps.get(position).getBank())).getLogo() != null) {
					d_item_banklogo.setImageBitmap(StringUtil.getBitmapFromAssert(mContext, dbBankService.getByName((lccps.get(position).getBank())).getLogo()));
				}

			}

			d_item_text1.setText(String.valueOf(lccps.get(position).getProfit()) + "%");
			d_item_2_text_2.setText(String.valueOf(lccps.get(position).getPeriod()) + "天");
			if (lccps.get(position).getAmt() != null) {
				d_item_text3.setText(String.valueOf(lccps.get(position).getAmt()) + "万");
			}

		} else {
			convertView = mInflater.inflate(R.layout.d_item, null);
			TextView title = (TextView) convertView.findViewById(R.id.item_title);
			ImageView d_item_banklogo = (ImageView) convertView.findViewById(R.id.d_item_banklogo);
			TextView d_item_text1 = (TextView) convertView.findViewById(R.id.d_item_text1);
			TextView d_item_text2 = (TextView) convertView.findViewById(R.id.d_item_text2);

			title.setText(lccps.get(position).getName() + "%");
			if (dbBankService.getByName(lccps.get(position).getBank()) != null) {
				if (dbBankService.getByName((lccps.get(position).getBank())).getLogo() != null) {
					d_item_banklogo.setImageBitmap(StringUtil.getBitmapFromAssert(mContext, dbBankService.getByName((lccps.get(position).getBank())).getLogo()));
				}

			}

			d_item_text1.setText(String.valueOf(lccps.get(position).getProfit()) + "%");
			d_item_text2.setText(String.valueOf(lccps.get(position).getPeriod()) + "天");
		}
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent toLcCard = new Intent();
				toLcCard.setClass(mContext, LcCardActivity.class);
				Bundle b = new Bundle();
				b.putSerializable("lccp", l);
				toLcCard.putExtras(b);
				mContext.startActivity(toLcCard);
			}
		});

		return convertView;
	}

}
