package com.yeetou.xinyongkaguanjia.ui;

/**
 * 功能：设置页面
 */
import com.yeetou.xinyongkaguanjia.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class EActivity extends AbstractActivity implements OnClickListener {
	private RelativeLayout e_layout1;
	private RelativeLayout e_layout2;
	private RelativeLayout e_layout3;
	private RelativeLayout e_layout4;
	private RelativeLayout e_layout5;
	private RelativeLayout e_layout6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_e);
		init();
	}

	public void init() {
		e_layout1 = (RelativeLayout) findViewById(R.id.e_layout1);
		e_layout1.setOnClickListener(this);
		e_layout2 = (RelativeLayout) findViewById(R.id.e_layout2);
		e_layout2.setOnClickListener(this);
		e_layout3 = (RelativeLayout) findViewById(R.id.e_layout3);
		e_layout3.setOnClickListener(this);
		e_layout4 = (RelativeLayout) findViewById(R.id.e_layout4);
		e_layout4.setOnClickListener(this);
		e_layout5 = (RelativeLayout) findViewById(R.id.e_layout5);
		e_layout5.setOnClickListener(this);
		e_layout6 = (RelativeLayout) findViewById(R.id.e_layout6);
		e_layout6.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.e_layout1:
			Intent toE1Activity = new Intent(EActivity.this, E1Activity.class);
			startActivity(toE1Activity);
			break;
		case R.id.e_layout2:
			Intent toE2Activity = new Intent(EActivity.this, E2Activity.class);
			startActivity(toE2Activity);
			break;
		case R.id.e_layout3:
			Intent toE3Activity = new Intent(EActivity.this, E3Activity.class);
			startActivity(toE3Activity);
			break;
		case R.id.e_layout4:
			Intent toE4Activity = new Intent(EActivity.this, E4Activity.class);
			startActivity(toE4Activity);
			break;
		case R.id.e_layout5:
			Intent toE5Activity = new Intent(EActivity.this, E5Activity.class);
			startActivity(toE5Activity);
			break;
		case R.id.e_layout6:
			Intent toE6Activity = new Intent(EActivity.this, E6Activity.class);
			startActivity(toE6Activity);
			break;
		}
	}

}
