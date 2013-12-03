package com.yeetou.xinyongkaguanjia.ui;

import com.yeetou.xinyongkaguanjia.R;

import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class E3Activity extends AbstractActivity implements OnClickListener {
	private LinearLayout LinearLayout_Ab_Left_Indicator;

	private TextView e3_text1, e3_text2, e3_text3, e3_text4;
	private TextView e3_text1_1, e3_text2_1, e3_text3_1;
	private LinearLayout e3_text4_1;
	
	private boolean isFromLogin =false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_e3);
		isFromLogin = getIntent().getBooleanExtra("isFromLogin", false);
		e3_text1 = (TextView) findViewById(R.id.e3_text1);
		e3_text2 = (TextView) findViewById(R.id.e3_text2);
		e3_text3 = (TextView) findViewById(R.id.e3_text3);
		e3_text4 = (TextView) findViewById(R.id.e3_text4);

		e3_text1.setOnClickListener(this);
		e3_text2.setOnClickListener(this);
		e3_text3.setOnClickListener(this);
		e3_text4.setOnClickListener(this);

		e3_text1_1 = (TextView) findViewById(R.id.e3_text1_1);
		e3_text2_1 = (TextView) findViewById(R.id.e3_text2_1);
		e3_text3_1 = (TextView) findViewById(R.id.e3_text3_1);
		e3_text4_1 = (LinearLayout) findViewById(R.id.e3_text4_1);

		if(isFromLogin){
			e3_text1_1.setVisibility(View.GONE);
			e3_text2_1.setVisibility(View.GONE);
			e3_text3_1.setVisibility(View.GONE);
			e3_text4_1.setVisibility(View.VISIBLE);
		}
		
		TextView TextView_Ab_Title = (TextView) findViewById(R.id.TextView_Ab_Title);
		TextView_Ab_Title.setText("常见问题");
		LinearLayout_Ab_Left_Indicator = (LinearLayout) findViewById(R.id.LinearLayout_Ab_Left_Indicator);
		LinearLayout_Ab_Left_Indicator.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.e3_text1:
			if (e3_text1_1.getVisibility() == View.VISIBLE) {
				e3_text1_1.setVisibility(View.GONE);
			} else {
				e3_text1_1.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.e3_text2:
			if (e3_text2_1.getVisibility() == View.VISIBLE) {
				e3_text2_1.setVisibility(View.GONE);
			} else {
				e3_text2_1.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.e3_text3:
			if (e3_text3_1.getVisibility() == View.VISIBLE) {
				e3_text3_1.setVisibility(View.GONE);
			} else {
				e3_text3_1.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.e3_text4:
			if (e3_text4_1.getVisibility() == View.VISIBLE) {
				e3_text4_1.setVisibility(View.GONE);
			} else {
				e3_text4_1.setVisibility(View.VISIBLE);
			}
			break;
		}

	}

}
