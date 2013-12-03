package com.yeetou.xinyongkaguanjia.ui;

import java.util.List;

import com.yeetou.xinyongkaguanjia.R;
import com.yeetou.xinyongkaguanjia.adapter.ChooseCardLinearAdapter;
import com.yeetou.xinyongkaguanjia.component.ListLinearLayout;
import com.yeetou.xinyongkaguanjia.db.base.DbLcBank;
import com.yeetou.xinyongkaguanjia.db.service.DbBankCardService;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChooseLcCardActivity extends AbstractActivity {
	
	private LinearLayout LinearLayout_Ab_Left_Indicator;
	private ListLinearLayout choose_card_layout1;
	private ListLinearLayout choose_card_layout2;
	private ListLinearLayout choose_card_layout3;
	
	private DbBankCardService dbBankCardService;
	
	private List<DbLcBank> lcBanks1;
	private List<DbLcBank> lcBanks2;
	private List<DbLcBank> lcBanks3;
	private ChooseCardLinearAdapter adapter1;
	private ChooseCardLinearAdapter adapter2;
	private ChooseCardLinearAdapter adapter3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_lc_card);
		((TextView) this.findViewById(R.id.TextView_Ab_Title)).setText("您的持卡银行");
		choose_card_layout1 = (ListLinearLayout)findViewById(R.id.choose_card_layout1);
		choose_card_layout2 = (ListLinearLayout)findViewById(R.id.choose_card_layout2);
		choose_card_layout3 = (ListLinearLayout)findViewById(R.id.choose_card_layout3);
		
		LinearLayout_Ab_Left_Indicator = (LinearLayout) findViewById(R.id.LinearLayout_Ab_Left_Indicator);
		LinearLayout_Ab_Left_Indicator.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		dbBankCardService = new DbBankCardService(this);
		lcBanks1 = dbBankCardService.getHaveBanks();
		lcBanks2 = dbBankCardService.getBigBanks();
		lcBanks3 = dbBankCardService.getOtherBanks();
		
		if(lcBanks1!=null&&lcBanks1.size()!=0){
			adapter1 = new ChooseCardLinearAdapter(this, lcBanks1);
			choose_card_layout1.setAdapter(adapter1);
		}
		
		if(lcBanks2!=null&&lcBanks2.size()!=0){
			adapter2 = new ChooseCardLinearAdapter(this, lcBanks2);
			choose_card_layout2.setAdapter(adapter2);
		}
		
		if(lcBanks3!=null&&lcBanks3.size()!=0){
			adapter3 = new ChooseCardLinearAdapter(this, lcBanks3);
			choose_card_layout3.setAdapter(adapter3);
		}
	}

}
