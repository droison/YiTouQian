package com.yeetou.xinyongkaguanjia.adapter;

import java.util.List;

import com.yeetou.xinyongkaguanjia.R;
import com.yeetou.xinyongkaguanjia.db.base.DbLcBank;
import com.yeetou.xinyongkaguanjia.db.service.DbBankCardService;
import com.yeetou.xinyongkaguanjia.util.StringUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ChooseCardLinearAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<DbLcBank> lcBanks;
	private Context context;
	private DbBankCardService dbBankCardService;

	public ChooseCardLinearAdapter(Context context, List<DbLcBank> lcBanks) {
		this.context = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.lcBanks = lcBanks;
		dbBankCardService = new DbBankCardService(context);
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
		convertView = mInflater.inflate(R.layout.choose_card_item, null);
		final DbLcBank temp = lcBanks.get(position);
		TextView choose_card_item_name = (TextView) convertView.findViewById(R.id.choose_card_item_name);
		ImageView choose_card_item_logo = (ImageView) convertView.findViewById(R.id.choose_card_item_logo);
		CheckBox choose_card_item_checkbox = (CheckBox) convertView.findViewById(R.id.choose_card_item_checkbox);
		choose_card_item_name.setText(temp.getName());
		Bitmap bm = StringUtil.getBitmapFromAssert(context, temp.getLogo());
		if (bm != null)
			choose_card_item_logo.setImageBitmap(bm);
		
		choose_card_item_checkbox.setChecked(temp.isIs_choose());
		choose_card_item_checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				dbBankCardService.setLcBank(temp.getName(), temp.getLogo(),isChecked);
			}
		});
		
		return convertView;
	}

}
