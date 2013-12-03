package com.yeetou.xinyongkaguanjia.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yeetou.xinyongkaguanjia.R;
import com.yeetou.xinyongkaguanjia.R.layout;
import com.yeetou.xinyongkaguanjia.db.service.DbAccountService;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class E1Activity extends AbstractActivity {

	private LinearLayout LinearLayout_Ab_Left_Indicator;
	private TextView e1_username;
	private TextView e1_passwd;
	private TextView e1_changePwd;


	private DbAccountService abas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_e1);
		e1_username = (TextView) findViewById(R.id.e1_username);
		e1_passwd = (TextView) findViewById(R.id.e1_passwd);
		abas = new DbAccountService(this);
		e1_changePwd = (TextView) findViewById(R.id.e1_changePwd);
		e1_changePwd.setText(Html.fromHtml("<u>修改</u>"));


		LinearLayout_Ab_Left_Indicator = (LinearLayout) findViewById(R.id.LinearLayout_Ab_Left_Indicator);
		LinearLayout_Ab_Left_Indicator.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		e1_username.setText(abas.get().getEmail().toString());

/*		if (abas.get().getEmail().toString().substring(0, 1).equals("u") && abas.get().getPhone().equals(abas.get().getEmail().toString().substring(0, abas.get().getEmail().toString().indexOf("@")))) {
			e1_passwd.setText(abas.get().getPhone());
			e1_passwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD); // 明文
		} else {
			e1_passwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); // 暗文
		}*/

		e1_changePwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent toPasswd = new Intent(E1Activity.this, E1PasswdActivity.class);
				startActivity(toPasswd);
			}
		});
		// e1_passwd.

	}

}
