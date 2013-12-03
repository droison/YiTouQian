package com.yeetou.xinyongkaguanjia.ui;
/**
 * 功能：理财分析页面
 */
import java.util.List;

import com.yeetou.xinyongkaguanjia.R;
import com.yeetou.xinyongkaguanjia.adapter.DLinearAdapter;
import com.yeetou.xinyongkaguanjia.adapter.DListViewAdapter;
import com.yeetou.xinyongkaguanjia.component.ListLinearLayout;
import com.yeetou.xinyongkaguanjia.constants.AppConstant;
import com.yeetou.xinyongkaguanjia.db.base.DbLcBank;
import com.yeetou.xinyongkaguanjia.db.service.DbBankCardService;
import com.yeetou.xinyongkaguanjia.http.base.LccpBase;
import com.yeetou.xinyongkaguanjia.http.base.LccpBase.Lccp;
import com.yeetou.xinyongkaguanjia.http.service.LccpService;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class DActivity extends AbstractActivity {

	private ListLinearLayout d_layout1;
	private Button d_button;
	private ListView d_listview;
	
	private DListViewAdapter adapter1;
	private DLinearAdapter adapter2;
	
	private DbBankCardService dbBankCardService;
	private List<DbLcBank> lcBanks;
	private long lcChangeTime = 0;
	private  List<Lccp> lccps;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_d);
		setUpView();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		
		long curChangeTime = dbBankCardService.getLcChangeTime();
		List<DbLcBank> temp = dbBankCardService.getAllLcBanks();
		if(curChangeTime>lcChangeTime||temp.size()!=lcBanks.size()){
			d_layout1.removeAllViews();
			lcBanks = temp;
			
			if(lcBanks!=null&&lcBanks.size()!=0){
				adapter2 = new DLinearAdapter(this, lcBanks);
				d_layout1.setAdapter(adapter2);
			}
					
			new Thread(new LccpService(this, lcListHandler,lcBanks)).start();
			showProgressDialog("正在获取今日理财数据");
			lcChangeTime = curChangeTime;
		}
	}

	private void setUpView(){
		d_layout1 = (ListLinearLayout) this.findViewById(R.id.d_layout1);
		d_button = (Button) this.findViewById(R.id.d_button);
		d_listview = (ListView) this.findViewById(R.id.d_listview);
		
		dbBankCardService = new DbBankCardService(this);
		lcBanks = dbBankCardService.getAllLcBanks();
		if(lcBanks!=null && lcBanks.size()!=0){
			adapter2 = new DLinearAdapter(this, lcBanks);
			d_layout1.setAdapter(adapter2);
			lcChangeTime = dbBankCardService.getLcChangeTime();
		}
		
		d_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent toChooseLcCard = new Intent(DActivity.this, ChooseLcCardActivity.class);
				startActivity(toChooseLcCard);
			}
		});
		
		new Thread(new LccpService(this, lcListHandler,lcBanks)).start();
		showProgressDialog("正在获取今日理财数据");
	}
	
	private Handler lcListHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			dismissProgressDialog();
			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				LccpBase lccpBase = (LccpBase) msg.obj;
				lccps = lccpBase.getLccps();
				if(adapter1==null){
					adapter1 = new DListViewAdapter(DActivity.this, lccps);
					d_listview.setAdapter(adapter1);
				}else{
					adapter1 = new DListViewAdapter(DActivity.this, lccps);
					d_listview.setAdapter(adapter1);
/*					adapter1.notifyDataSetChanged();
					d_listview.setAdapter(adapter1);*/
				}
				break;
			default:
				displayResponse("网络错误。请稍候重试!");
				break;
			}
		}

	};

}
