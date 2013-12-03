package com.yeetou.xinyongkaguanjia.ui;

import com.yeetou.xinyongkaguanjia.R;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TabHost;

public class MainTabActivity extends TabActivity implements android.widget.CompoundButton.OnCheckedChangeListener {
	private TabHost mTabHost;
	private Intent mAIntent;
	private Intent mBIntent;
	private Intent mCIntent;
	private Intent mDIntent;
	private Intent mEIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_tab);

		this.mAIntent = new Intent(this, AActivity.class);
		this.mBIntent = new Intent(this, BActivity.class);
		this.mCIntent = new Intent(this, CActivity.class);
		this.mDIntent = new Intent(this, DActivity.class);
		this.mEIntent = new Intent(this, EActivity.class);

		((RadioButton) findViewById(R.id.radio_button0)).setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.radio_button1)).setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.radio_button2)).setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.radio_button3)).setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.radio_button4)).setOnCheckedChangeListener(this);

		setupIntent();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if (isChecked) {
			switch (buttonView.getId()) {
			case R.id.radio_button0:
				this.mTabHost.setCurrentTabByTag("A_TAB");
				break;
			case R.id.radio_button1:
				this.mTabHost.setCurrentTabByTag("B_TAB");
				break;
			case R.id.radio_button2:
				this.mTabHost.setCurrentTabByTag("C_TAB");
				break;
			case R.id.radio_button3:
				this.mTabHost.setCurrentTabByTag("D_TAB");
				break;
			case R.id.radio_button4:
				this.mTabHost.setCurrentTabByTag("E_TAB");
				break;
			}
		}

	}

	private void setupIntent() {
		this.mTabHost = getTabHost();
		TabHost localTabHost = this.mTabHost;
		localTabHost.addTab(buildTabSpec("A_TAB", R.string.main_tab_a, R.drawable.bar_1, this.mAIntent));

		localTabHost.addTab(buildTabSpec("B_TAB", R.string.main_tab_b, R.drawable.bar_2, this.mBIntent));

		localTabHost.addTab(buildTabSpec("C_TAB", R.string.main_tab_c, R.drawable.bar_3, this.mCIntent));

		localTabHost.addTab(buildTabSpec("D_TAB", R.string.main_tab_d, R.drawable.bar_4, this.mDIntent));

		localTabHost.addTab(buildTabSpec("E_TAB", R.string.main_tab_e, R.drawable.bar_5, this.mEIntent));

	}

	private TabHost.TabSpec buildTabSpec(String tag, int resLabel, int resIcon, final Intent content) {
		return this.mTabHost.newTabSpec(tag).setIndicator(getString(resLabel), getResources().getDrawable(resIcon)).setContent(content);
	}

}
