package com.yeetou.xinyongkaguanjia.adapter;

import java.util.List;

import com.yeetou.xinyongkaguanjia.R;
import com.yeetou.xinyongkaguanjia.constants.AppConstant;
import com.yeetou.xinyongkaguanjia.db.base.DbBank;
import com.yeetou.xinyongkaguanjia.db.base.DbBankCard;
import com.yeetou.xinyongkaguanjia.db.service.DbAccountService;
import com.yeetou.xinyongkaguanjia.db.service.DbBankService;
import com.yeetou.xinyongkaguanjia.db.service.DbStreamService;
import com.yeetou.xinyongkaguanjia.http.service.SyncData;
import com.yeetou.xinyongkaguanjia.ui.CardInfoActivity;
import com.yeetou.xinyongkaguanjia.ui.ChangeCardActivity;
import com.yeetou.xinyongkaguanjia.util.NumberFormateUtil;
import com.yeetou.xinyongkaguanjia.util.StringUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DebitCardAdapter_AActivity extends BaseAdapter {

	private Context mContext;
	private float max = 0;
	private List<DbBankCard> cards;
	private DbBankService dbBankService;
	private DbStreamService dbStreamService;
	private LayoutInflater mInflater;
	private DbAccountService dbas;
	private TextView bank_card_text1;
	private int position1;
	
	public DebitCardAdapter_AActivity(Context mContext, List<DbBankCard> cards) {
		this.mContext = mContext;
		this.cards = cards;
		dbBankService = new DbBankService(mContext);
		dbStreamService = new DbStreamService(mContext);
		dbas = new DbAccountService(mContext);
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

		View view =  mInflater.inflate(R.layout.debit_card_item, null);
		ImageView bank_card_logo = (ImageView) view.findViewById(R.id.bank_card_logo);
		TextView bank_card_name = (TextView) view.findViewById(R.id.bank_card_name);
		TextView bank_card_number = (TextView) view.findViewById(R.id.bank_card_number);
		TextView bank_card_text3 = (TextView) view.findViewById(R.id.bank_card_text3);
		bank_card_text1 = (TextView) view.findViewById(R.id.bank_card_text1);

		bank_card_logo.setImageBitmap(StringUtil.getBitmapFromAssert(mContext,bank.getLogo()));
		bank_card_name.setText(bank.getName());
		bank_card_number.setText("(" + card.getNumber() + ")");
		bank_card_text3.setTextColor(Color.BLUE);
		bank_card_text3.setText(Html.fromHtml("<u>修改</u>"));
		final float balance = dbStreamService.getBalanceByCard(bank_card_id);
		bank_card_text1.setText("余额：￥"+ NumberFormateUtil.Fromate2(balance));
		bank_card_text3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent toChangeCard = new Intent(mContext, ChangeCardActivity.class);
				toChangeCard.putExtra("isCredit", false);
				toChangeCard.putExtra("bank_card_id", bank_card_id);
				//ExitApplication.getInstance().addCardInfoActivity(CardInfoActivity.this);
				mContext.startActivity(toChangeCard);
				
				
				
/*				position1 = position;
				
				Toast.makeText(mContext, "余额：￥"+ NumberFormateUtil.Fromate2(balance), 1).show();
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				View view = View.inflate(mContext, R.layout.dialog_item, null);
				TextView textView = (TextView)view.findViewById(R.id.dialog_title);
				textView.setText("请输入余额");
				final EditText editText = (EditText)view.findViewById(R.id.dialog_content);
				editText.setText(NumberFormateUtil.Fromate2(balance) + "");
				//设置输入框在末尾
				Editable etext = editText.getText();
				Selection.setSelection(etext, etext.length());
				builder.setView(view);
				
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Card card = new Card();
						card.setId(bank_card_id);
						card.setCard_type("debit");
						card.setBalance(editText.getText().toString());
						new Thread(new UpdateCard(mContext, mHandler2, dbas.get().getSecret(), dbas.get().getEmail(), card)).start();
						
					}
				});
				builder.create().show();*/
				
				
				
				
				
			}
		});
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent toCardInfoActivity = new Intent(mContext, CardInfoActivity.class);

				toCardInfoActivity.putExtra("isCredit", false);
				toCardInfoActivity.putExtra("bank_card_id", bank_card_id);

				mContext.startActivity(toCardInfoActivity);
			}
		});
		
		return view;
	}
	
	public Handler mHandler2 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//dismissProgressDialog();
			super.handleMessage(msg);
			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				new Thread(new SyncData(mContext, SyncDatahandler2, dbas.get().getEmail(), dbas.get().getSecret(), 0)).start();

				break;
			case AppConstant.HANDLER_MESSAGE_NONETWORK:
				break;
			case AppConstant.HANDLER_MESSAGE_TIMEOUT:
				break;
			case AppConstant.HANDLER_HTTPSTATUS_ERROR:
				break;
			}
		}

	};
	

	public Handler SyncDatahandler2 = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				// 余额成功保存
				Toast.makeText(mContext, "余额修改成", Toast.LENGTH_SHORT).show();
				dbStreamService = new DbStreamService(mContext);
				bank_card_text1.setText(dbStreamService.getBalanceByCard(cards.get(position1).getBank_id()+"") + "");

				break;
			case AppConstant.HANDLER_MESSAGE_NONETWORK:
				break;
			case AppConstant.HANDLER_MESSAGE_TIMEOUT:
				break;
			case AppConstant.HANDLER_HTTPSTATUS_ERROR:
				break;
			}

		}

	};

	
}
