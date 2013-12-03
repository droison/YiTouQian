package com.yeetou.xinyongkaguanjia.ui;

/**
 * 
 * 功能：登录页面
 *
 */

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.yeetou.xinyongkaguanjia.R;
import com.yeetou.xinyongkaguanjia.component.ResizeLayout;
import com.yeetou.xinyongkaguanjia.constants.AppConstant;
import com.yeetou.xinyongkaguanjia.db.base.DbAccount;
import com.yeetou.xinyongkaguanjia.db.service.DbAccountService;
import com.yeetou.xinyongkaguanjia.db.service.DbBankService;
import com.yeetou.xinyongkaguanjia.db.service.DbCategoryService;
import com.yeetou.xinyongkaguanjia.db.service.DbSMSService;
import com.yeetou.xinyongkaguanjia.http.base.EmailCheckBase;
import com.yeetou.xinyongkaguanjia.http.base.IVBase;
import com.yeetou.xinyongkaguanjia.http.base.MsgUploadBase;
import com.yeetou.xinyongkaguanjia.http.base.UserLoginBase;
import com.yeetou.xinyongkaguanjia.http.service.BankSynHttp;
import com.yeetou.xinyongkaguanjia.http.service.EmailCheck;
import com.yeetou.xinyongkaguanjia.http.service.IV_Get;
import com.yeetou.xinyongkaguanjia.http.service.MsgUpload;
import com.yeetou.xinyongkaguanjia.http.service.SyncData;
import com.yeetou.xinyongkaguanjia.http.service.UserLogin;
import com.yeetou.xinyongkaguanjia.info.MsgInfo;
import com.yeetou.xinyongkaguanjia.util.AnimationUtil;
import com.yeetou.xinyongkaguanjia.util.CheckSMS;
import com.yeetou.xinyongkaguanjia.util.Crypt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AbstractActivity implements OnClickListener {

	private ResizeLayout keyboardLayout1;
	private ImageView loginCart;
	private LinearLayout cart;
	private TextView duanxin;
	private TextView login_yinhang;
	private List<MsgInfo> msgInfos;
	private ProgressBar login_progressbar;
	private Handler progressHandler;
	private ImageView login_progressbar1;
	private ImageView login_progressbar2;
	private ImageView login_finish1;
	private DbSMSService dbsmss;
	private LinearLayout login_linearlayout_3;
	private Animation translateAnimation;
	private DbAccountService dbas;
	private DbAccount dbaccout;
	private String phone = "";
	private String token = "2123123123";
	private ImageView login_finish2;
	// 邮箱登陆
	private EditText login_email_name;
	private EditText login_email_passwd;
	private Button login_submit;
	/*
	 * private String email = "tti12345@126.com"; private String passwd =
	 * "tt123456789";
	 */
	private String email = "";
	private String passwd = "";
	private TextView login_skip;

	// 邮箱登陆三个linearlayout
	private LinearLayout login_linearlayout_insert;
	private LinearLayout login_linearlayout_check;
	private LinearLayout login_linearlayout_add;

	// 邮箱登陆验证页面
	private TextView login_check_text1;
	private TextView login_check_text2;
	private TextView login_check_text3;
	private TextView login_reinsert;
	private ImageView login_check_warning;
	private Handler textHandler;
	private TextView login_email_text;
	private ScrollView login_scroll;
	private ImageView login_check_finish1;
	private ImageView login_check_finish2;
	private ImageView login_check_finish3;

	// 邮箱添加完成页面
	private RelativeLayout login_re_add;
	private ImageView login_tomain;
	private LinearLayout login_add_email;

	private String secret = "";

	private long scanSmsTime = 0l;

	private static final String TAG = "LoginActivity";

	private String iv = ""; // 暂时将iv存全局变量

	private Boolean flag = true;
	private InputMethodManager imm;

	private TextView login_xinyongka;
	private TextView login_youjian;
	private boolean isPop3 = false;

	// private KeyboardListenRelativeLayout keyboardLayout1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		init();

	}

	public void init() {
		// 初始化手机号
		TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm.getLine1Number() != null && !tm.getLine1Number().equals("")) {
			phone = tm.getLine1Number();
			phone = phone.replace("+86", "");
			System.out.println(tm.getLine1Number());
		}

		translateAnimation = new TranslateAnimation(0f, 0f, 200f, 0f);
		translateAnimation.setDuration(1000);
		translateAnimation.setStartOffset(500);

		loginCart = (ImageView) findViewById(R.id.loginCart);

		duanxin = (TextView) findViewById(R.id.duanxin);
		login_yinhang = (TextView) findViewById(R.id.login_yinhang);
		msgInfos = new ArrayList<MsgInfo>();

		login_progressbar = (ProgressBar) findViewById(R.id.login_progressbar);
		login_scroll = (ScrollView) this.findViewById(R.id.login_scroll);

		login_progressbar.setProgress(10);
		login_progressbar1 = (ImageView) findViewById(R.id.login_progressbar1);
		AnimationUtil.setRoundAtimation(login_progressbar1); // 设置动画
		login_progressbar2 = (ImageView) findViewById(R.id.login_progressbar2);

		login_finish1 = (ImageView) findViewById(R.id.login_finish1);
		login_linearlayout_3 = (LinearLayout) findViewById(R.id.login_linearlayout_3);
		dbas = new DbAccountService(this);
		dbaccout = dbas.get();
		dbsmss = new DbSMSService(this);

		login_email_name = (EditText) findViewById(R.id.login_email_name);
		login_email_name.setText(email);
		login_email_name.setOnClickListener(this);

		login_email_passwd = (EditText) findViewById(R.id.login_email_passwd);
		login_email_passwd.setText(passwd);
		login_email_passwd.setOnClickListener(this);
		login_submit = (Button) findViewById(R.id.login_submit);
		login_submit.setOnClickListener(this);

		login_linearlayout_insert = (LinearLayout) findViewById(R.id.login_linearlayout_insert);
		login_linearlayout_check = (LinearLayout) findViewById(R.id.login_linearlayout_check);
		login_linearlayout_add = (LinearLayout) findViewById(R.id.login_linearlayout_add);

		login_check_text1 = (TextView) findViewById(R.id.login_check_text1);
		login_check_text2 = (TextView) findViewById(R.id.login_check_text2);
		login_check_text3 = (TextView) findViewById(R.id.login_check_text3);
		login_check_finish1 = (ImageView) this.findViewById(R.id.login_check_finish1);
		login_check_finish2 = (ImageView) this.findViewById(R.id.login_check_finish2);
		login_check_finish3 = (ImageView) this.findViewById(R.id.login_check_finish3);
		login_check_warning = (ImageView) this.findViewById(R.id.login_check_warning);
		login_reinsert = (TextView) findViewById(R.id.login_reinsert);
		login_reinsert.setOnClickListener(this);
		login_email_text = (TextView) findViewById(R.id.login_email_text);

		login_re_add = (RelativeLayout) findViewById(R.id.login_re_add);
		login_re_add.setOnClickListener(this);
		login_tomain = (ImageView) findViewById(R.id.login_tomain);
		login_tomain.setOnClickListener(this);
		login_add_email = (LinearLayout) findViewById(R.id.login_add_email);
		login_finish2 = (ImageView) findViewById(R.id.login_finish2);

		login_skip = (TextView) findViewById(R.id.login_skip);
		login_skip.setOnClickListener(this);

		login_xinyongka = (TextView) this.findViewById(R.id.login_xinyongka);
		login_youjian = (TextView) this.findViewById(R.id.login_youjian);

		progressHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					login_progressbar.setProgress(40);
					break;
				}
			}
		};

		textHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					login_check_text1.setTextColor(getResources().getColor(R.color.gray));
					login_check_text2.setTextColor(getResources().getColor(R.color.black));
					login_check_finish1.setImageResource(R.drawable.login_dian_last);
					login_check_finish2.setImageResource(R.drawable.login_dian_cur);

					login_progressbar.setProgress(80);
					break;
				case 2:
					login_linearlayout_check.setVisibility(View.GONE);
					login_linearlayout_add.setVisibility(View.VISIBLE);

					login_check_text3.setTextColor(getResources().getColor(R.color.gray));
					login_check_text1.setTextColor(getResources().getColor(R.color.black));
					login_check_finish1.setImageResource(R.drawable.login_dian_next);
					login_check_finish2.setImageResource(R.drawable.login_dian_next);
					login_check_finish3.setImageResource(R.drawable.login_dian_next);
					login_progressbar2.clearAnimation();
					login_progressbar2.setVisibility(View.GONE);
					login_finish2.setVisibility(View.VISIBLE);
					login_progressbar.setProgress(100);

					View view = View.inflate(LoginActivity.this, R.layout.login_add_email_item, null);
					TextView email = (TextView) view.findViewById(R.id.login_add_email_name);
					
					email.setText((String) msg.obj); // 添加邮箱
					if(login_add_email.getChildCount()==0){
						view.findViewById(R.id.login_add_margin).setVisibility(View.GONE);
					}
					login_add_email.addView(view);
					break;
				}

			}

		};

		if (dbaccout != null && dbaccout.getSecret() != null) {
			Intent begin = new Intent(this, MainTabActivity.class);
			startActivity(begin);
			finish();
		} else {
			initDatabase();

			if (dbaccout != null && dbaccout.getPhone().equals(phone)) {
				scanSmsTime = dbaccout.getMsgscan_at();
				phone = dbaccout.getPhone();
			}
			dbaccout = new DbAccount();

			new Thread(new IV_Get(this, IVHandler)).start();
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		flag = false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_submit:
			// 点击登录按钮 获取iv
			// new Thread(new IV_Get(LoginActivity.this, IVHandler)).start();
			if (login_add_email.getChildCount() > 0) {
				for (int i = 0; i < login_add_email.getChildCount(); i++) {
					View view = login_add_email.getChildAt(i);
					TextView textView = (TextView) view.findViewById(R.id.login_add_email_name);
					if (login_email_name.getText().toString().equals(textView.getText().toString())) {
						Toast.makeText(LoginActivity.this, "该邮箱已经存在", Toast.LENGTH_SHORT).show();
						return;
					}
				}
			}

			if (login_email_name.getText().toString().equals("") || login_email_passwd.getText().toString().equals("")) {
				Toast.makeText(this, "邮箱账号或密码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}

			try {
				new Thread(new EmailCheck(this, emailAuthlHandler, dbaccout.getEmail(), secret, login_email_name.getText().toString(), Crypt.encrypt(login_email_passwd.getText().toString(), iv), AppConstant.HTTPURL.emails_auth, iv)).start();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 登录按钮 显示验证页面，
			login_linearlayout_insert.setVisibility(View.GONE);
			login_linearlayout_check.setVisibility(View.VISIBLE);
			login_check_finish1.setImageResource(R.drawable.login_dian_cur);
			// 验证页面 邮箱
			login_email_text.setText(login_email_name.getText().toString());
			login_progressbar2.setVisibility(View.VISIBLE);
			AnimationUtil.setRoundAtimation(login_progressbar2);// 设置动画
			// 跳过按钮失效
			login_skip.setVisibility(View.INVISIBLE);
			break;
		case R.id.login_reinsert: // 重新输入则跳转到输入页面， 验证页面初始化。

			login_linearlayout_insert.setVisibility(View.VISIBLE);
			login_linearlayout_check.setVisibility(View.GONE);
			login_check_text1.setTextColor(getResources().getColor(R.color.black));
			login_check_text2.setTextColor(getResources().getColor(R.color.gray));
			login_progressbar2.setVisibility(View.VISIBLE);
			login_check_warning.setVisibility(View.GONE);
			login_reinsert.setVisibility(View.GONE);
			login_finish2.setVisibility(View.GONE);
			login_skip.setVisibility(View.VISIBLE);

			if(isPop3){
				Intent toHelp = new Intent(LoginActivity.this, E3Activity.class);
				toHelp.putExtra("isFromLogin", true);
				startActivity(toHelp);
				isPop3 = false;
			}
			break;
		case R.id.login_tomain:
			new Thread(new BankSynHttp(this, synBankHandler)).start();
			showProgressDialog("正在同步银行");
			break;
		case R.id.login_re_add:
			// 跳过按钮激活
			login_skip.setVisibility(View.VISIBLE);
			login_linearlayout_add.setVisibility(View.GONE);
			login_linearlayout_insert.setVisibility(View.VISIBLE);
			login_progressbar2.clearAnimation();
			login_progressbar2.setVisibility(View.VISIBLE);
			login_email_name.setText("");
			login_email_passwd.setText("");
			login_finish2.setVisibility(View.GONE);

			break;
		case R.id.login_skip:
			login_skip.setVisibility(View.INVISIBLE);
			login_linearlayout_insert.setVisibility(View.GONE);
			login_linearlayout_add.setVisibility(View.VISIBLE);
			login_progressbar2.setVisibility(View.GONE);

			login_finish2.setVisibility(View.VISIBLE);
			break;
		}

	}

	public Handler emailAuthlHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				EmailCheckBase eCheckBase = (EmailCheckBase) msg.obj;
				switch (eCheckBase.getCode()) {
				case 101: // 验证成功 , 则添加邮箱
					secret = eCheckBase.getSecret();// 更新secret
					Log.d(TAG, "emailAuthlHandler  secret=" + secret);
					try {
						new Thread(new EmailCheck(LoginActivity.this, emailAddlHandler, dbaccout.getEmail(), secret, login_email_name.getText().toString(), Crypt.encrypt(login_email_passwd.getText().toString(), iv), AppConstant.HTTPURL.emails_add, iv)).start();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					login_progressbar.setProgress(50);
					textHandler.sendMessageDelayed(textHandler.obtainMessage(1), 1000);
					break;
				case 200: // 验证成功 , 则添加邮箱
					login_progressbar2.clearAnimation();
					login_check_warning.setVisibility(View.VISIBLE);
					login_reinsert.setVisibility(View.VISIBLE);
					if(eCheckBase.getMsg().contains("POP3")){
						login_reinsert.setText(Html.fromHtml("<u>查看帮助</u>"));
						isPop3 = true;
					}else{
						login_reinsert.setText(Html.fromHtml("<u>重新输入账号</u>"));
					}
					break;
				default:// 认证邮箱失败，请稍后再试
					login_progressbar2.clearAnimation();
					login_check_warning.setVisibility(View.VISIBLE);
					login_reinsert.setVisibility(View.VISIBLE);
					login_reinsert.setText(Html.fromHtml("<u>重新输入账号</u>"));
					break;
				}

				break;
			default:
				displayResponse("邮件验证时网络错误。请稍候重试!");
				finish();
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
				EmailCheckBase eCheckBase = (EmailCheckBase) msg.obj;
				switch (eCheckBase.getCode()) {
				case 101: // 添加邮箱成功
					// 同步数据，暂时写在这里
					secret = eCheckBase.getSecret(); // 更新secret
					// new Thread(new SyncData(LoginActivity.this,
					// SyncDatahandler, dbaccout.getEmail(),
					// dbaccout.getSecret(), 1)).start();
					Log.d(TAG, "eCheckBase.getFlag=" + eCheckBase.getFlag());
					switch (eCheckBase.getFlag()) {
					case 0:// 新的邮箱
						login_check_text2.setTextColor(getResources().getColor(R.color.gray));
						login_check_text3.setTextColor(getResources().getColor(R.color.black));
						login_check_finish2.setImageResource(R.drawable.login_dian_last);
						login_check_finish3.setImageResource(R.drawable.login_dian_cur);
						login_xinyongka.setText(eCheckBase.getCard_cnt());
						login_youjian.setText(eCheckBase.getBill_cnt());
						textHandler.sendMessageDelayed(textHandler.obtainMessage(2, eCheckBase.getC_email().toString()), 2000);
						break;
					case 1:// 存在,弹出提示是否解绑
						login_check_text2.setTextColor(getResources().getColor(R.color.gray));
						login_check_text3.setTextColor(getResources().getColor(R.color.black));
						login_check_finish2.setImageResource(R.drawable.login_dian_last);
						login_check_finish3.setImageResource(R.drawable.login_dian_cur);
						login_xinyongka.setText(eCheckBase.getCard_cnt());
						login_youjian.setText(eCheckBase.getBill_cnt());
						textHandler.sendMessageDelayed(textHandler.obtainMessage(2, eCheckBase.getC_email().toString()), 2000);

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
				// new Thread(new BankSynHttp(LoginActivity.this,
				// synBankHandler)).start();
				// 数据库保存secret 下次判断是否第一次登录
				secret = (String) msg.obj;
				dbaccout.setSecret(secret);
				dbaccout.setSyn_at(System.currentTimeMillis());
				dbas.saveOrUpdate(dbaccout);
				// 保存完成跳转到首页
				Intent tomain = new Intent(LoginActivity.this, MainTabActivity.class);
				startActivity(tomain);
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

	public Handler handler3 = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			MsgUploadBase msgUploadBase = (MsgUploadBase) msg.obj;
			secret = msgUploadBase.getSecret();
			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				Toast.makeText(LoginActivity.this, "成功读取短信账单", Toast.LENGTH_SHORT).show();

				login_progressbar.setProgress(30);
				// 更新secret
				progressHandler.sendMessageDelayed(progressHandler.obtainMessage(1), 2000);
				duanxin.setText(String.valueOf(msgUploadBase.getSms_cnt()));
				login_yinhang.setText(String.valueOf(msgUploadBase.getCard_cnt()));
				Log.d(TAG, String.valueOf(msgUploadBase.getCard_cnt()));
				dbaccout.setMsgscan_at(scanSmsTime);
				dbas.saveOrUpdate(dbaccout);
				break;
			default:
				displayResponse("短线扫描识失败，请稍候手动扫描");
				break;
			}
			
			// 键盘的初始化
			keyboardLayout1 = (ResizeLayout) findViewById(R.id.keyboardLayout1);
			keyboardLayout1.setOnResizeListener(new ResizeLayout.OnResizeListener() {

				public void OnResize(int w, int h, int oldw, int oldh) {
					int change = BIGGER;
					if (h < oldh) {
						change = SMALLER;
					}

					Message msg = new Message();
					msg.what = 1;
					msg.arg1 = change;
					inputSizeHandler.sendMessage(msg);
				}
			});
			
			login_progressbar1.clearAnimation();
			login_progressbar1.setVisibility(View.GONE);
			login_finish1.setVisibility(View.VISIBLE);
			login_linearlayout_3.setVisibility(View.VISIBLE);
			login_linearlayout_3.startAnimation(translateAnimation);
			login_scroll.post(new Runnable() {
				public void run() {
					login_scroll.fullScroll(ScrollView.FOCUS_DOWN);
					login_email_name.requestFocus();
					login_email_name.setFocusableInTouchMode(true);
				}

			});
			
		}

	};

	public Handler handler2 = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case AppConstant.CHECK_MESSAGE_FINISH:
				msgInfos = (List<MsgInfo>) msg.obj;
				if (msgInfos != null && msgInfos.size() != 0) {// 当查询的短信不为空的时候，保存并上传
					// 上传短信
					new Thread(new MsgUpload(LoginActivity.this, handler3, msgInfos, secret, dbaccout.getEmail())).start();
				} else {
					
					// 键盘的初始化
					keyboardLayout1 = (ResizeLayout) findViewById(R.id.keyboardLayout1);
					keyboardLayout1.setOnResizeListener(new ResizeLayout.OnResizeListener() {

						public void OnResize(int w, int h, int oldw, int oldh) {
							int change = BIGGER;
							if (h < oldh) {
								change = SMALLER;
							}

							Message msg = new Message();
							msg.what = 1;
							msg.arg1 = change;
							inputSizeHandler.sendMessage(msg);
						}
					});
					
					dbaccout.setMsgscan_at(scanSmsTime);
					dbas.saveOrUpdate(dbaccout);
					login_progressbar1.clearAnimation();
					login_progressbar1.setVisibility(View.GONE);
					login_finish1.setVisibility(View.VISIBLE);
					login_linearlayout_3.setVisibility(View.VISIBLE);
					login_linearlayout_3.startAnimation(translateAnimation);
					login_scroll.post(new Runnable() {
						public void run() {
							login_scroll.fullScroll(ScrollView.FOCUS_DOWN);
							login_email_name.requestFocus();
							login_email_name.setFocusableInTouchMode(true);
						}

					});
				}

				break;
			default:
				displayResponse("扫描失败，可以稍候手动扫描");
				break;

			}
		}

	};

	/**
	 * 登录的handler。 功能：查看服务器用户是否存在1）存在则查看本地是否存在，存在则按时间扫描；不存在全部扫描并同步。2） 不存在全部扫描并同步
	 */
	public Handler handler1 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				UserLoginBase userLoginBase = (UserLoginBase) msg.obj;

				if (userLoginBase.getExisting() == 0) {// 0表示已经注册。

				} else if ((userLoginBase.getExisting() == 1)) {// 1表示第一次注册登录，全部扫描

				}

				secret = userLoginBase.getSecret();
				// dbaccout.setSecret(userLoginBase.getSecret()); // 更新secret
				dbaccout.setEmail(userLoginBase.getEmail());
				dbaccout.setMsgscan_at(scanSmsTime);
				dbaccout.setSyn_at(System.currentTimeMillis());
				dbaccout.setToken(token);
				dbaccout.setPhone(userLoginBase.getNumber());
				dbaccout.setIv(iv);
				dbas.saveOrUpdate(dbaccout);
				new Thread(new CheckSMS(LoginActivity.this, handler2, scanSmsTime)).start();
				scanSmsTime = System.currentTimeMillis();
				login_progressbar.setProgress(15);
				break;
			default:
				displayResponse("登陆时网络错误，请稍候重试");
				finish();
				break;
			}
		}

	};

	// 获取iv的handler
	public Handler IVHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				IVBase ivBase = (IVBase) msg.obj;
				switch (ivBase.getCode()) {
				case 101: // 验证成功 , 则添加邮箱
					iv = ivBase.getIv();
					new Thread(new UserLogin(LoginActivity.this, handler1, phone)).start();// 用户登录
					break;
				default:
					displayResponse(ivBase.getMsg());
					finish();
					break;
				}

				break;
			default:
				displayResponse("获取密钥网络错误，请稍候重试");
				finish();
				break;
			}
		}

	};

	public Handler synBankHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			dismissProgressDialog();
			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				new Thread(new SyncData(LoginActivity.this, SyncDatahandler, dbaccout.getEmail(), secret, 1)).start();
				showProgressDialog("正在同步数据");
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

	public void initDatabase() {
		DbCategoryService dbCategoryService = new DbCategoryService(LoginActivity.this);
		DbBankService dbBankService = new DbBankService(LoginActivity.this);
		if (dbCategoryService.getAllCategory().size() == 0) {
			List<String> categorys = new ArrayList<String>();
			categorys.add("吃喝");
			categorys.add("购物");
			categorys.add("网购");
			categorys.add("出行");
			categorys.add("生活");
			categorys.add("玩乐");
			categorys.add("爱车");

			dbCategoryService.save(categorys);
		}
		if (dbBankService.getAllBank().size() == 0) {

		}
	}

	public String inputStream2String(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}

	private static final int BIGGER = 1;
	private static final int SMALLER = 2;
	private static final int MSG_RESIZE = 1;

	private Handler inputSizeHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_RESIZE: {
				if (msg.arg1 == BIGGER) {
					loginCart.setVisibility(View.VISIBLE);
				} else {
					loginCart.setVisibility(View.GONE);
				}
			}
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

}
