package com.yeetou.xinyongkaguanjia.ui;

import com.yeetou.xinyongkaguanjia.R;
import com.yeetou.xinyongkaguanjia.constants.AppConstant;
import com.yeetou.xinyongkaguanjia.db.service.DbAccountService;
import com.yeetou.xinyongkaguanjia.http.service.Feedback;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class E6Activity extends AbstractActivity {

	private ImageView e6_send;
	private EditText e6_text_feedback;
	private DbAccountService dbas;
	private LinearLayout LinearLayout_Ab_Left_Indicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_e6);
		
		TextView TextView_Ab_Title = (TextView)findViewById(R.id.TextView_Ab_Title);
		TextView_Ab_Title.setText("给我们提意见");

		e6_send = (ImageView) findViewById(R.id.e6_send);
		e6_text_feedback = (EditText) findViewById(R.id.e6_text_feedback);
		dbas = new DbAccountService(this);
		
		e6_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(e6_text_feedback.getText().toString().equals("")){
					Toast.makeText(E6Activity.this, "内容不能为空哦~~", Toast.LENGTH_SHORT).show();
					return;
				}
				new Thread(new Feedback(E6Activity.this, mhandler, dbas.get().getEmail(), dbas.get().getSecret(), e6_text_feedback.getText().toString())).start();
				showProgressDialog("正在发表反馈信息");
			}
		});
		
		LinearLayout_Ab_Left_Indicator = (LinearLayout) findViewById(R.id.LinearLayout_Ab_Left_Indicator);
		LinearLayout_Ab_Left_Indicator.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	public Handler mhandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			dismissProgressDialog();
			super.handleMessage(msg);
			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				Toast.makeText(E6Activity.this, "反馈成功", Toast.LENGTH_SHORT).show();
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
