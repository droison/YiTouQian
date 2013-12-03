package com.yeetou.xinyongkaguanjia.ui;

import com.yeetou.xinyongkaguanjia.R;
import com.yeetou.xinyongkaguanjia.R.layout;

import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class E5Activity extends AbstractActivity {
	private LinearLayout LinearLayout_Ab_Left_Indicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_e5);
		init();
	}

	public void init() {

		TextView TextView_Ab_Title = (TextView) findViewById(R.id.TextView_Ab_Title);
		TextView_Ab_Title.setText("关于易投");

		LinearLayout_Ab_Left_Indicator = (LinearLayout) findViewById(R.id.LinearLayout_Ab_Left_Indicator);
		LinearLayout_Ab_Left_Indicator.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

}
