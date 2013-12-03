package com.yeetou.xinyongkaguanjia.ui;

import com.yeetou.xinyongkaguanjia.R;
import com.yeetou.xinyongkaguanjia.R.layout;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class E2Activity extends AbstractActivity implements OnClickListener {
	private LinearLayout LinearLayout_Ab_Left_Indicator;
	private Button e2_bt1;
	private Button e2_bt2;
	private Button e2_bt3;
	private Button e2_bt4;
	private Button e2_bt5;
	private Button e2_bt6;
	private Button e2_bt7;
	private Button e2_bt8;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_e2);

		TextView TextView_Ab_Title = (TextView)findViewById(R.id.TextView_Ab_Title);
		TextView_Ab_Title.setText("易投理财大全");
		LinearLayout_Ab_Left_Indicator = (LinearLayout) findViewById(R.id.LinearLayout_Ab_Left_Indicator);
		LinearLayout_Ab_Left_Indicator.setOnClickListener(this);

		e2_bt1 = (Button) findViewById(R.id.e2_bt1);
		e2_bt2 = (Button) findViewById(R.id.e2_bt2);
		e2_bt3 = (Button) findViewById(R.id.e2_bt3);
		e2_bt4 = (Button) findViewById(R.id.e2_bt4);
		e2_bt5 = (Button) findViewById(R.id.e2_bt5);
		e2_bt6 = (Button) findViewById(R.id.e2_bt6);
		e2_bt7 = (Button) findViewById(R.id.e2_bt7);
		e2_bt8 = (Button) findViewById(R.id.e2_bt8);

		e2_bt1.setOnClickListener(this);
		e2_bt2.setOnClickListener(this);
		e2_bt3.setOnClickListener(this);
		e2_bt4.setOnClickListener(this);
		e2_bt5.setOnClickListener(this);
		e2_bt6.setOnClickListener(this);
		e2_bt7.setOnClickListener(this);
		e2_bt8.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		String url;
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.LinearLayout_Ab_Left_Indicator:
			finish();

			break;
		case R.id.e2_bt1:
			url = "http://www.yeetou.com/download/market.apk";
			i.setData(Uri.parse(url));
			startActivity(i);
			break;
		case R.id.e2_bt2:
			url = "http://yeetou.com/download/fund.apk";
			i.setData(Uri.parse(url));
			startActivity(i);
			break;
		case R.id.e2_bt3:
			url = "http://www.yeetou.com/download/calculator.apk";
			i.setData(Uri.parse(url));
			startActivity(i);
			break;
		case R.id.e2_bt4:
			url = "http://www.yeetou.com/download/bank_atm.apk";
			i.setData(Uri.parse(url));
			startActivity(i);
			break;
		case R.id.e2_bt5:
			url = "http://www.yeetou.com/download/today_rate.apk";
			i.setData(Uri.parse(url));
			startActivity(i);
			break;
		case R.id.e2_bt6:
			url = "http://www.yeetou.com/download/rate_jsq.apk";
			i.setData(Uri.parse(url));
			startActivity(i);
			break;
		case R.id.e2_bt7:
			url = "http://www.yeetou.com/download/ctf_gold.apk";
			i.setData(Uri.parse(url));
			startActivity(i);
			break;
		case R.id.e2_bt8:
			url = "http://www.yeetou.com/download/today_gold.apk";
			i.setData(Uri.parse(url));
			startActivity(i);
			break;
		}

	}

}
