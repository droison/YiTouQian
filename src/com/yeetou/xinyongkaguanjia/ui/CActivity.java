package com.yeetou.xinyongkaguanjia.ui;
/**
 * 功能：收支分析页面
 */
import java.util.List;

import com.yeetou.xinyongkaguanjia.R;
import com.yeetou.xinyongkaguanjia.adapter.ExpandableAdapter_CActivity;
import com.yeetou.xinyongkaguanjia.db.service.DbStreamService;
import com.yeetou.xinyongkaguanjia.info.MonthPayments;
import com.yeetou.xinyongkaguanjia.info.YearMonthPayments;
import com.yeetou.xinyongkaguanjia.util.NumberFormateUtil;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CActivity extends AbstractActivity {
	
	/*
	 * 头部
	 */
	private LinearLayout LinearLayout_Ab_Left_Indicator; // 用于返回
	private TextView TextView_Ab_Title; // 设置标题 “信用卡详情”OR“储蓄卡详情”


	private DbStreamService dbss; 
	private List<YearMonthPayments> yearMonthPayments;
	private ExpandableAdapter_CActivity adapter;
	
	private TextView c_expend_text1;
	private TextView c_expend_text2;
	private TextView c_income_text1;
	private TextView c_income_text2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_c);
		init();
		initHead("收支分析");
	}
	public void init(){
		dbss = new DbStreamService(this);
		yearMonthPayments = dbss.getMonthPayments();
		c_expend_text1 = (TextView) findViewById(R.id.c_expend_text1);
        c_expend_text2 = (TextView) findViewById(R.id.c_expend_text2);
        c_income_text1 = (TextView) findViewById(R.id.c_income_text1);
        c_income_text2 = (TextView) findViewById(R.id.c_income_text2);
		if(yearMonthPayments!=null&&yearMonthPayments.size()!=0){
			adapter = new ExpandableAdapter_CActivity(this, yearMonthPayments);
	        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.c_list);
	        expandableListView.setAdapter(adapter);
	        expandableListView.expandGroup(0);//设置第一组张开
	        expandableListView.setGroupIndicator(null);//除去自带的箭头
	        
	        
	        int size = 0;
	        float expend = 0;
	        float income = 0;
	        for(YearMonthPayments y:yearMonthPayments){
	        	size+=y.getMonthPayments().size();
	        	for(MonthPayments m:y.getMonthPayments()){
	        		expend += m.getExpand();
	        		income += m.getIncome();
	        	}
	        }
	        if(size!=0){
	        	expend = expend/size;
	        	income = income/size;
	        }
	        
	        double expendper = 0;
	        double incomeper = 0;
	        if(expend<500){
	        	expendper= expend/500*0.04;
	        }else if(expend>21000){
	        	expendper = 0.9999;
	        }else{
	        	expendper = Math.log(expend);
	        	expendper=(Math.log(expendper)-1.82)*2+0.04;
	        }
	        if(income<100){
	        	incomeper = 0.00;
	        }else if(100<=income&&income<=4000){
	        	incomeper=(income-100)/3900*0.5;
	        }else if(4000<=income&&income<=10000){
	        	incomeper=(income-4000)/6000*0.3+0.5;
	        }else if(10000<income&&income<=50000){
	        	incomeper=(income-10000)/40000*0.15+0.8;
	        }else if(1000000>=income&&income>50000){
	        	incomeper=(Math.log(income)-10.816)/3*0.05+0.95;
	        }else{
	        	incomeper=0.9999;
	        }
	        
	        c_expend_text1.setText(NumberFormateUtil.Fromate2ex(expend));
	        c_income_text1.setText(NumberFormateUtil.Fromate2ex(income));
	        c_expend_text2.setText(NumberFormateUtil.Fromate4(expendper));
	        c_income_text2.setText(NumberFormateUtil.Fromate4(incomeper));
		}else{
	        c_expend_text1.setText("￥0.00");
	        c_income_text1.setText("￥0.00");
	        c_expend_text2.setText("0%");
	        c_income_text2.setText("0%");
		}
        
	}
	
	private void initHead(String title) {
		LinearLayout_Ab_Left_Indicator = (LinearLayout) this.findViewById(R.id.LinearLayout_Ab_Left_Indicator);
		TextView_Ab_Title = (TextView) this.findViewById(R.id.TextView_Ab_Title);
		TextView_Ab_Title.setText(title);
		this.findViewById(R.id.ImageView_Ab_Left_Indicator).setVisibility(View.INVISIBLE);
	}

}
