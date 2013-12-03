package com.yeetou.xinyongkaguanjia.adapter;

import java.util.List;

import com.yeetou.xinyongkaguanjia.R;
import com.yeetou.xinyongkaguanjia.constants.AppConstant;
import com.yeetou.xinyongkaguanjia.db.base.DbBank;
import com.yeetou.xinyongkaguanjia.db.base.DbBankCard;
import com.yeetou.xinyongkaguanjia.db.base.DbCardBills;
import com.yeetou.xinyongkaguanjia.db.service.DbBankService;
import com.yeetou.xinyongkaguanjia.db.service.DbBillsService;
import com.yeetou.xinyongkaguanjia.http.service.CreditPayoffService;
import com.yeetou.xinyongkaguanjia.ui.AbstractActivity;
import com.yeetou.xinyongkaguanjia.ui.CardInfoActivity;
import com.yeetou.xinyongkaguanjia.util.DialogUtil;
import com.yeetou.xinyongkaguanjia.util.StringUtil;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CreditCardAdapter_AActivity extends BaseAdapter {

	private Context mContext;
	private float max = 0;
	private List<DbBankCard> cards;
	private DbBillsService dbBillsService;
	private DbBankService dbBankService;
	private LayoutInflater mInflater;
	private DbCardBills dbCardBills;

	public CreditCardAdapter_AActivity(Context mContext, List<DbBankCard> cards) {
		this.mContext = mContext;
		this.cards = cards;
		dbBankService = new DbBankService(mContext);
		dbBillsService = new DbBillsService(mContext);
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return cards.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		DbBankCard card = cards.get(position);
		final String bank_card_id = String.valueOf(card.getId());
		DbBank bank = dbBankService.getById(String.valueOf(card.getBank_id()));

		View view = mInflater.inflate(R.layout.credit_card_item, null);
		ImageView bank_card_logo = (ImageView) view.findViewById(R.id.bank_card_logo);
		TextView bank_card_name = (TextView) view.findViewById(R.id.bank_card_name);
		TextView bank_card_number = (TextView) view.findViewById(R.id.bank_card_number);
		final ImageView b = (ImageView) view.findViewById(R.id.bank_card_btn);
		final TextView bank_card_text1 = (TextView) view.findViewById(R.id.bank_card_text1);
		TextView bank_card_text3 = (TextView) view.findViewById(R.id.bank_card_text3);
		bank_card_logo.setImageBitmap(StringUtil.getBitmapFromAssert(mContext, bank.getLogo()));
		bank_card_name.setText(bank.getName());
		bank_card_number.setText("(" + card.getNumber() + ")");
		bank_card_text3.setText(card.getName());

		dbCardBills = dbBillsService.getLast(bank_card_id);

		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new DialogUtil(mContext, "改为已还状态，则默认您还清了信用卡，本期账单将不再提示", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						((AbstractActivity)mContext).showProgressDialog("正在修改...");
						new Thread(new CreditPayoffService(mContext, creditPayoffHandler,dbCardBills.getId())).start();
					}
				}, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).creat();
			}
			
			Handler creditPayoffHandler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					((AbstractActivity)mContext).dismissProgressDialog();
					switch (msg.what) {
					case AppConstant.HANDLER_MESSAGE_NORMAL:
						Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
						dbBillsService.updateLast(bank_card_id);
						setUpCreditBill(dbCardBills, bank_card_text1, b);
						break;
					default:
						Toast.makeText(mContext, "失败，请稍候重试", Toast.LENGTH_SHORT).show();
						break;
					}
				}

			};
			
		});

		// view.setTag(card.getId());
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent toCardInfoActivity = new Intent(mContext, CardInfoActivity.class);

				toCardInfoActivity.putExtra("isCredit", true);
				toCardInfoActivity.putExtra("bank_card_id", bank_card_id);

				mContext.startActivity(toCardInfoActivity);

			}
		});

		setUpCreditBill(dbCardBills, bank_card_text1, b);

		return view;
	}

	private void setUpCreditBill(DbCardBills dbCardBills, TextView bank_card_text1, ImageView b) {

		if (dbCardBills == null) {
			bank_card_text1.setVisibility(View.GONE);
			b.setImageResource(R.drawable.weichu);
			b.setClickable(false);
		} else {

			if (dbCardBills.getPay_state() == 0) { // 未还
				if (dbCardBills.getBilling_date() > dbCardBills.getDue_date()) { // 账单日>还款日
																					// ，逾期
					if ((int) (dbCardBills.getBilling_date() - dbCardBills.getDue_date()) / (1000 * 24 * 60 * 60) <= 99) { // 逾期不大于99天
						bank_card_text1.setText((dbCardBills.getBilling_date() - dbCardBills.getDue_date()) / (1000 * 24 * 60 * 60) + "天");
					} else {
						bank_card_text1.setText("99天");
					}
					b.setImageResource(R.drawable.yuqi);
				} else {// 账单日<还款日 逾期 ========未还
					bank_card_text1.setText(-(dbCardBills.getBilling_date() - dbCardBills.getDue_date()) / (1000 * 24 * 60 * 60) + "天");
					b.setImageResource(R.drawable.daoqi);
				}

			} else if (dbCardBills.getPay_state() == 1) { // 部分还

			} else { // 还清
				bank_card_text1.setVisibility(View.GONE);
				b.setImageResource(R.drawable.yihuan);
				b.setClickable(false);
			}
		}
	}

}
