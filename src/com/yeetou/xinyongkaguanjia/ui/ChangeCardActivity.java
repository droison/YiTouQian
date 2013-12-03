package com.yeetou.xinyongkaguanjia.ui;

import java.util.ArrayList;
import java.util.List;

import com.yeetou.xinyongkaguanjia.R;
import com.yeetou.xinyongkaguanjia.component.MySpinnerButton;
import com.yeetou.xinyongkaguanjia.component.MySpinnerButton.MyListItemOnClickListener;
import com.yeetou.xinyongkaguanjia.constants.AppConstant;
import com.yeetou.xinyongkaguanjia.db.base.DbBank;
import com.yeetou.xinyongkaguanjia.db.base.DbBankCard;
import com.yeetou.xinyongkaguanjia.db.service.DbAccountService;
import com.yeetou.xinyongkaguanjia.db.service.DbBankCardService;
import com.yeetou.xinyongkaguanjia.db.service.DbBankService;
import com.yeetou.xinyongkaguanjia.db.service.DbStreamService;
import com.yeetou.xinyongkaguanjia.http.service.SyncData;
import com.yeetou.xinyongkaguanjia.http.service.UpdateCard;
import com.yeetou.xinyongkaguanjia.info.Card;
import com.yeetou.xinyongkaguanjia.util.ExitApplication;
import com.yeetou.xinyongkaguanjia.util.NumberFormateUtil;
import com.yeetou.xinyongkaguanjia.util.StringUtil;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class ChangeCardActivity extends AbstractActivity {

	private MySpinnerButton change_spinner;
	private Boolean isCredit = false;
	private String bank_card_id;
	private DbBankCardService dbbcs;
	private DbBankCard dBankCard;
	private DbBankService dbBankService;
	private DbBank dbBank;
	private LinearLayout change_yue;
	private EditText change_text;
	private DbAccountService dbas;
	private Card card;
	private String temp_type;
	private ImageView change_logo;
	private TextView change_bankname;
	private TextView change_banknumber;
	private TextView TextView_Ab_Title;
	private LinearLayout LinearLayout_Ab_Left_Indicator;
	private DbStreamService dbss;
	private int flag = 0;
	private Button change_submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_card);
		init();
	}

	public void init() {
		change_spinner = (MySpinnerButton) findViewById(R.id.change_spinner);
		Bundle b = getIntent().getExtras();
		isCredit = b.getBoolean("isCredit");
		bank_card_id = b.getString("bank_card_id");
		change_yue = (LinearLayout) findViewById(R.id.change_yue);
		change_text = (EditText) findViewById(R.id.change_text);
		dbas = new DbAccountService(this);
		card = new Card();
		LinearLayout_Ab_Left_Indicator = (LinearLayout) findViewById(R.id.LinearLayout_Ab_Left_Indicator);

		change_logo = (ImageView) findViewById(R.id.change_logo);
		change_bankname = (TextView) findViewById(R.id.change_bankname);
		change_banknumber = (TextView) findViewById(R.id.change_banknumber);
		TextView_Ab_Title = (TextView) findViewById(R.id.TextView_Ab_Title);

		change_submit = (Button) findViewById(R.id.change_submit);

		dbbcs = new DbBankCardService(this);
		dBankCard = dbbcs.getById(bank_card_id);
		dbBankService = new DbBankService(this);
		dbBank = dbBankService.getById(dBankCard.getBank_id() + "");
		dbss = new DbStreamService(this);
		change_text.setText(NumberFormateUtil.Fromate2(dbss.getBalanceByCard(bank_card_id))); // 流水中获取余额
		// 设置输入框在末尾
		Editable etext = change_text.getText();
		Selection.setSelection(etext, etext.length());
		change_logo.setImageBitmap(StringUtil.getBitmapFromAssert(this, dbBank.getLogo()));

		change_banknumber.setText("(" + dBankCard.getNumber() + ")");

		change_yue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		change_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				card.setId(bank_card_id);

				if (change_spinner.getText().toString().equals("信用卡")) {
					card.setCard_type("credit");
				} else {
					card.setCard_type("debit");
				}
				card.setBalance(change_text.getText().toString());
				new Thread(new UpdateCard(ChangeCardActivity.this, mHandler2, dbas.get().getSecret(), dbas.get().getEmail(), card)).start();
				showProgressDialog("正在修改储蓄卡余额");
				InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(ChangeCardActivity.INPUT_METHOD_SERVICE);
				if (imm.isActive()) {
					imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
				}
			}
		});

		/*
		 * change_text.setOnKeyListener(new OnKeyListener() {
		 * 
		 * @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
		 * 
		 * if (keyCode == KeyEvent.KEYCODE_ENTER) { // 在这里编写自己想要实现的功能
		 * card.setId(bank_card_id);
		 * 
		 * if (change_spinner.getText().toString().equals("信用卡")) {
		 * card.setCard_type("credit"); } else { card.setCard_type("debit"); }
		 * card.setBalance(change_text.getText().toString()); new Thread(new
		 * UpdateCard(ChangeCardActivity.this, mHandler2,
		 * dbas.get().getSecret(), dbas.get().getEmail(), card)).start();
		 * showProgressDialog("正在修改储蓄卡余额"); InputMethodManager imm =
		 * (InputMethodManager)
		 * v.getContext().getSystemService(ChangeCardActivity
		 * .INPUT_METHOD_SERVICE); if (imm.isActive()) {
		 * imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0); }
		 * return true; } // TODO Auto-generated method stub return false; } });
		 */
		/*
		 * change_text.setOnEditorActionListener(new OnEditorActionListener() {
		 * 
		 * @Override public boolean onEditorAction(TextView v, int actionId,
		 * KeyEvent event) { // TODO Auto-generated method stub
		 * 
		 * if (actionId == KeyEvent.KEYCODE_ENTER) { // 在这里编写自己想要实现的功能
		 * card.setBand_id(bank_card_id); card.setNumber(dBankCard.getNumber());
		 * card.setCard_type(change_spinner.getText().toString());
		 * card.setBalance(change_text.getText().toString()); new Thread(new
		 * UpdateCard(ChangeCardActivity.this, mHandler, dbas.get().getSecret(),
		 * dbas.get().getEmail(), card)); InputMethodManager imm =
		 * (InputMethodManager)
		 * v.getContext().getSystemService(ChangeCardActivity
		 * .INPUT_METHOD_SERVICE); if (imm.isActive()) {
		 * imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0); }
		 * return true; } return false;
		 * 
		 * } });
		 */

		if (isCredit) {
			change_spinner.setText("信用卡");
			change_spinner.setBackgroundColor(Color.TRANSPARENT);
			
			change_submit.setVisibility(View.GONE);
			change_spinner.setClickable(false);
			change_yue.setVisibility(View.GONE);
			temp_type = "信用卡";
			change_bankname.setText(dbBank.getName() + "信用卡");
			TextView_Ab_Title.setText(dbBank.getName() + "信用卡" + " " + dBankCard.getNumber());

		} else {
			change_spinner.setText("储蓄卡");
			change_yue.setVisibility(View.VISIBLE);
			temp_type = "储蓄卡";
			change_bankname.setText(dbBank.getName() + "储蓄卡");
			TextView_Ab_Title.setText(dbBank.getName() + "储蓄卡" + " " + dBankCard.getNumber());
		}

		List<String> list = new ArrayList<String>();
		list.add("储蓄卡");
		list.add("信用卡");
		change_spinner.setListContent(list);
		change_spinner.setContainAll(false);
		change_spinner.setMyListItemOnClick(new MyListItemOnClickListener() {

			@Override
			public void onClick(String text) {
				// TODO Auto-generated method stub
				Log.d("ChangeCardActivity", change_spinner.getText().toString() + text.toString());
				if (!text.equals(temp_type)) {

					card.setId(bank_card_id);
					if (text.equals("信用卡")) {
						card.setCard_type("credit");
					} else {
						card.setCard_type("debit");
					}

					card.setBalance(change_text.getText().toString());

					new Thread(new UpdateCard(ChangeCardActivity.this, mHandler1, dbas.get().getSecret(), dbas.get().getEmail(), card)).start();
					showProgressDialog("正在更改储蓄卡类型");
					/*
					 * if (text.equals("储蓄卡")) { isCredit = false; } else {
					 * isCredit = true; }
					 */
				}
			}
		});

		LinearLayout_Ab_Left_Indicator.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	public Handler mHandler1 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			dismissProgressDialog();
			super.handleMessage(msg);
			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				new Thread(new SyncData(ChangeCardActivity.this, SyncDatahandler1, dbas.get().getEmail(), dbas.get().getSecret(), 0)).start();

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

	public Handler mHandler2 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			dismissProgressDialog();
			super.handleMessage(msg);
			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				new Thread(new SyncData(ChangeCardActivity.this, SyncDatahandler2, dbas.get().getEmail(), dbas.get().getSecret(), 0)).start();

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

	public Handler SyncDatahandler1 = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			dismissProgressDialog();
			super.handleMessage(msg);

			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				// 成功后转换类型。
				flag = 1;
				isCredit = !isCredit;

				Toast.makeText(ChangeCardActivity.this, "类型修改成功", Toast.LENGTH_SHORT).show();
				if (isCredit) {
					change_spinner.setText("信用卡");
					change_yue.setVisibility(View.GONE);
					temp_type = "信用卡";
					change_bankname.setText(dbBank.getName() + "信用卡");
					TextView_Ab_Title.setText(dbBank.getName() + "信用卡" + " " + dBankCard.getNumber());
					change_submit.setVisibility(View.GONE);

					// 如果是信用卡则 不能点击，且没有右下角箭头
					change_spinner.setClickable(false);
					change_spinner.setBackgroundColor(Color.TRANSPARENT);

				} else {
					change_spinner.setText("储蓄卡");
					change_yue.setVisibility(View.VISIBLE);
					temp_type = "储蓄卡";
					change_bankname.setText(dbBank.getName() + "储蓄卡");
					TextView_Ab_Title.setText(dbBank.getName() + "储蓄卡" + " " + dBankCard.getNumber());

				}

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
			dismissProgressDialog();
			super.handleMessage(msg);
			flag = 1;
			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				// 余额成功保存
				Toast.makeText(ChangeCardActivity.this, "余额修改成", Toast.LENGTH_SHORT).show();
				dbss = new DbStreamService(ChangeCardActivity.this);
				change_text.setText(NumberFormateUtil.Fromate2(dbss.getBalanceByCard(bank_card_id)));
				finish();
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (flag == 1) {
			ExitApplication.getInstance().finishCardInfoActivity();
		}
	}

}
