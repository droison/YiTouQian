package com.yeetou.xinyongkaguanjia.ui;

import com.yeetou.xinyongkaguanjia.R;
import com.yeetou.xinyongkaguanjia.constants.AppConstant;
import com.yeetou.xinyongkaguanjia.db.base.DbAccount;
import com.yeetou.xinyongkaguanjia.db.service.DbAccountService;
import com.yeetou.xinyongkaguanjia.http.base.EmailCheckBase;
import com.yeetou.xinyongkaguanjia.http.service.EmailCheck;
import com.yeetou.xinyongkaguanjia.http.service.SyncData;
import com.yeetou.xinyongkaguanjia.util.Crypt;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BEmialAddActivity extends AbstractActivity {

	private EditText user;
	private EditText passwd;

	private String email_input = "";//"tti12345@126.com";
	private String passwd_input = "";//"tt123456789";

	private Button passwd_cancel;
	private Button passwd_submit;

	private DbAccountService dbas;
	private DbAccount dbaccout;
	private String secret = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bemial_add);

		user = (EditText) findViewById(R.id.user);
		passwd = (EditText) findViewById(R.id.passwd);
		user.setText(email_input);
		passwd.setText(passwd_input);
		passwd_cancel = (Button) findViewById(R.id.passwd_cancel);
		passwd_submit = (Button) findViewById(R.id.passwd_submit);
		dbas = new DbAccountService(this);
		dbaccout = dbas.get();

		passwd_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResult(RESULT_CANCELED);
				finish();

			}
		});

		passwd_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showProgressDialog("正在添加邮箱");

				try {
					new Thread(new EmailCheck(BEmialAddActivity.this, emailAuthlHandler, dbaccout.getEmail(), dbaccout.getSecret(), user.getText().toString(), Crypt.encrypt(passwd.getText().toString(), dbaccout.getIv()), AppConstant.HTTPURL.emails_add, dbaccout.getIv())).start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

	}

	public Handler emailAuthlHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				EmailCheckBase eCheckBase = (EmailCheckBase) msg.obj;
				secret = eCheckBase.getSecret();
				dbas.saveSecret(secret);
				switch (eCheckBase.getCode()) {
				case 101: // 验证成功 , 则添加邮箱
					Log.d(TAG, "emailAuthlHandler .secret=" + secret);
					try {
						new Thread(new EmailCheck(BEmialAddActivity.this, emailAddlHandler, dbaccout.getEmail(), secret, user.getText().toString(), Crypt.encrypt(passwd.getText().toString(), dbaccout.getIv()), AppConstant.HTTPURL.emails_add, dbaccout.getIv())).start();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					break;
				case 999:// 认证邮箱失败，请稍后再试
					dismissProgressDialog();
					Toast.makeText(BEmialAddActivity.this, "认证邮箱失败，请稍后再试", Toast.LENGTH_SHORT).show();
					break;
				case 200:// 用户邮箱或者密码错误。或者POP3服务没有开启
					dismissProgressDialog();
					Toast.makeText(BEmialAddActivity.this, " 用户邮箱或者密码错误", Toast.LENGTH_SHORT).show();
					break;
				case 201:// 参数错误
					dismissProgressDialog();
					Toast.makeText(BEmialAddActivity.this, "认证邮箱失败，请稍后再试", Toast.LENGTH_SHORT).show();
					break;
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

	public Handler emailAddlHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				dismissProgressDialog();
				EmailCheckBase eCheckBase = (EmailCheckBase) msg.obj;
				switch (eCheckBase.getCode()) {
				case 101: // 添加邮箱成功
					// 同步数据，暂时写在这里
					secret = eCheckBase.getSecret(); // 更新secret
					dbas.saveSecret(secret);
					// new Thread(new SyncData(LoginActivity.this,
					// SyncDatahandler, dbaccout.getEmail(),
					// dbaccout.getSecret(), 1)).start();
					new Thread(new SyncData(BEmialAddActivity.this, SyncDatahandler, dbaccout.getEmail(), secret, 0)).start();
					Log.d(TAG, "eCheckBase.getFlag=" + eCheckBase.getFlag());
					switch (eCheckBase.getFlag()) {
					case 0:// 新的邮箱

						break;
					case 1:// 存在,弹出提示是否解绑

						break;
					case 2:// 激活？？

						break;
					}

					break;
				case 999:// 认证邮箱失败，请稍后再试
					break;
				case 200:// 用户邮箱或者密码错误。或者POP3服务没有开启
					break;
				case 201:// 参数错误
					break;
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

	public Handler SyncDatahandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			dismissProgressDialog();
			super.handleMessage(msg);

			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:

				setResult(RESULT_OK);
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

}
