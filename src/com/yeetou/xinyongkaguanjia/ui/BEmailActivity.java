package com.yeetou.xinyongkaguanjia.ui;

import java.util.List;

import com.yeetou.xinyongkaguanjia.R;
import com.yeetou.xinyongkaguanjia.constants.AppConstant;
import com.yeetou.xinyongkaguanjia.db.base.DbEmail;
import com.yeetou.xinyongkaguanjia.db.service.DbAccountService;
import com.yeetou.xinyongkaguanjia.db.service.DbEmailService;
import com.yeetou.xinyongkaguanjia.http.service.EmailUnbind;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BEmailActivity extends AbstractActivity {

	private LinearLayout b_email_bg;
	private LinearLayout b_email_add;
	private DbEmailService dbes;
	private List<DbEmail> dbEmails;
	private DbAccountService dbAccountService;
	private String email_delet;
	private LinearLayout LinearLayout_Ab_Left_Indicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_b_email);

		b_email_bg = (LinearLayout) findViewById(R.id.b_email_bg);

		dbes = new DbEmailService(this);
		dbEmails = dbes.getAllEmail();
		dbAccountService = new DbAccountService(this);

		if (dbEmails.size() != 0) {
			b_email_bg.setVisibility(View.VISIBLE);
		}

		this.findViewById(R.id.ImageView_Ab_Left_Indicator).setVisibility(View.VISIBLE);
		
		b_email_add = (LinearLayout) findViewById(R.id.b_email_add);
		b_email_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent toEmailAdd = new Intent(BEmailActivity.this, BEmialAddActivity.class);
				startActivityForResult(toEmailAdd, 1);
			}
		});
		
		LinearLayout_Ab_Left_Indicator = (LinearLayout)findViewById(R.id.LinearLayout_Ab_Left_Indicator);
		LinearLayout_Ab_Left_Indicator.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		initEmail();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode != RESULT_OK) {
				return;
			}
			dbEmails = dbes.getAllEmail();
			// 设置其为显示
			b_email_bg.setVisibility(View.VISIBLE);
			b_email_bg.removeAllViews();
			initEmail();

		}

	}

	public void initEmail() {
		for (final DbEmail dbEmail : dbEmails) {
			View view = View.inflate(BEmailActivity.this, R.layout.login_add_email_item, null);
			TextView email = (TextView) view.findViewById(R.id.login_add_email_name);
			email.setText(dbEmail.getEmail()); // 添加邮箱
			view.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View arg0) {
					// TODO Auto-generated method stub
					Log.d("alert", "alert");
					Dialog dialog = new AlertDialog.Builder(BEmailActivity.this).setTitle("系统提示").setMessage("是否确认解绑该邮箱").setPositiveButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							new Thread(new EmailUnbind(BEmailActivity.this, mHandler, dbAccountService.get().getEmail(), dbAccountService.get().getSecret(), dbEmail.getEmail())).start();
							email_delet = dbEmail.getEmail();
						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					}).create();
					dialog.show();

					return true;
				}
			});
			
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.d("BEmailActivity", "clicked");
				}
			});

			b_email_bg.addView(view);
		}
	}

	public Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				Toast.makeText(BEmailActivity.this, "解绑成功", Toast.LENGTH_SHORT).show();
				dbes.deleteOne(email_delet);
				dbEmails = dbes.getAllEmail();
				b_email_bg.removeAllViews();

				initEmail();

				if (b_email_bg.getChildCount() == 0) {
					b_email_bg.setVisibility(View.GONE); // 将布局隐藏不占用上面空间
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

}
