package com.yeetou.xinyongkaguanjia.ui;

import com.yeetou.xinyongkaguanjia.R;
import com.yeetou.xinyongkaguanjia.constants.AppConstant;
import com.yeetou.xinyongkaguanjia.db.service.DbAccountService;
import com.yeetou.xinyongkaguanjia.http.base.Update_pwdBase;
import com.yeetou.xinyongkaguanjia.http.service.Update_pwd;
import com.yeetou.xinyongkaguanjia.util.Crypt;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class E1PasswdActivity extends AbstractActivity {

	private EditText passwd_1;
	private EditText passwd_2;
	private EditText passwd_3;
	private DbAccountService dbas;
	private String iv;
	private TextView e1_tishi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_e1_passwd);

		passwd_1 = (EditText) findViewById(R.id.passwd_1);
		passwd_2 = (EditText) findViewById(R.id.passwd_2);
		passwd_3 = (EditText) findViewById(R.id.passwd_3);
		dbas = new DbAccountService(this);
		iv = dbas.get().getIv();
		e1_tishi = (TextView) findViewById(R.id.e1_tishi);
		e1_tishi.setText("默认密码:" + dbas.get().getPhone());
	}

	public void btn_summit(View v) {
		if (passwd_1.getText().toString().equals("") || passwd_2.getText().toString().equals("") || passwd_3.getText().toString().equals("")) {
			Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
		} else if (!passwd_1.getText().toString().equals(passwd_1.getText().toString())) {
			Toast.makeText(this, "新密码和旧密码不能相同", Toast.LENGTH_SHORT).show();
		} else if (!passwd_2.getText().toString().equals(passwd_3.getText().toString())) {
			Toast.makeText(this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
		} else if (passwd_1.getText().toString().length() < 6 || passwd_1.getText().toString().length() > 12) {
			Toast.makeText(this, "密码长度需要6-12位", Toast.LENGTH_SHORT).show();
			;
		} else {
			try {
				showProgressDialog("正在修改");
				new Thread(new Update_pwd(this, mHandler, Crypt.encrypt(passwd_1.getText().toString(), iv), Crypt.encrypt(passwd_2.getText().toString(), iv), dbas.get().getSecret(), dbas.get().getEmail(), iv)).start();
			} catch (Exception e) {
				Toast.makeText(E1PasswdActivity.this, "出错，请稍候重试", Toast.LENGTH_SHORT).show();
				finish();
			}
		}

	}

	public void btn_cancel(View v) {
		finish();
	}

	public Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			dismissProgressDialog();
			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				Update_pwdBase update_pwdBase = (Update_pwdBase) msg.obj;
				if(update_pwdBase.getSecret()!=null){
					new DbAccountService(E1PasswdActivity.this).saveSecret(update_pwdBase.getSecret());
				}
				switch (update_pwdBase.getCode()) {
				case 101: // 修改成功
					Toast.makeText(E1PasswdActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
					finish();
					break;
				case 231: // 旧密码不正确
					Toast.makeText(E1PasswdActivity.this, "旧密码不正确", Toast.LENGTH_SHORT).show();
					break;
				case 999:// 更新失败
					Toast.makeText(E1PasswdActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
					break;
				case 230: // 密码长度
					Toast.makeText(E1PasswdActivity.this, "密码长度不对", Toast.LENGTH_SHORT).show();
					break;
				}
				break;
			default:
				Toast.makeText(E1PasswdActivity.this, "网络错误。请稍候重试", Toast.LENGTH_SHORT).show();
				finish();
				break;

			}
		}

	};

}
