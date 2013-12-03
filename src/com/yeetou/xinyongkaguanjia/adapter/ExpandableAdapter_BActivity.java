package com.yeetou.xinyongkaguanjia.adapter;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yeetou.xinyongkaguanjia.R;
import com.yeetou.xinyongkaguanjia.info.StreamDayInfo;
import com.yeetou.xinyongkaguanjia.info.StreamInfo;
import com.yeetou.xinyongkaguanjia.info.StreamMonthInfo;
import com.yeetou.xinyongkaguanjia.util.NumberFormateUtil;
import com.yeetou.xinyongkaguanjia.util.StringUtil;
import com.yeetou.xinyongkaguanjia.util.ViewSetUtil;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * @author Administrator
 * @date &{date}
 */
public class ExpandableAdapter_BActivity extends BaseExpandableListAdapter{
    private Context context;
    private List<StreamMonthInfo> streamMonthInfos;
    private Map<String, Integer> category_img;
    
    
    public ExpandableAdapter_BActivity(Context context, List<StreamMonthInfo> streamMonthInfos){
        this.context = context;
        this.streamMonthInfos = streamMonthInfos;
        category_img = new HashMap<String, Integer>();
        category_img.put("吃喝", R.drawable.food);
        category_img.put("购物", R.drawable.shoping);
        category_img.put("网购", R.drawable.ol_shoping);
        category_img.put("出行", R.drawable.travel);
        category_img.put("生活", R.drawable.life);
        category_img.put("玩乐", R.drawable.game);
        category_img.put("爱车", R.drawable.car);

    }
    
    public void setDatas(List<StreamMonthInfo> streamMonthInfos){
    	this.streamMonthInfos = streamMonthInfos;
    }
    
    //int[] logos = new int[] { R.drawable.wei, R.drawable.shu,R.drawable.wu};
    //设置组视图的显示文字
    //private String[] generalsTypes = new String[] { "2012年", "2011年", "2009年" };
    //子视图显示文字
/*    private String[][] generals = new String[][] {
            { "6日", "7日"},
            { "1日", "2日", "3日", "4日", "5日"},
            { "1日", "2日", "3日", "4日", "5日"}
    };*/
    //二级子视图显示文字
/*    private String[][] generals2 = new String[][] {
            { "超市", "吃饭"},
            { "超市", "吃饭","超市", "吃饭"},
            { "超市", "吃饭","逛街"}
    };*/


    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
       // return generals[groupPosition][childPosition];
    	return streamMonthInfos.get(groupPosition).getStreamdays().get(childPosition).getDay();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
    //	parent.setBackgroundResource(R.drawable.expend_you_yinying);
    	View view = View.inflate(context, R.layout.activity_b_item, null);
    	TextView b_item_day = (TextView)view.findViewById(R.id.b_item_day);
    	TextView b_item_weekday = (TextView)view.findViewById(R.id.b_item_weekday);
    	
    	int year = streamMonthInfos.get(groupPosition).getStreamdays().get(childPosition).getYear();
    	int month = streamMonthInfos.get(groupPosition).getStreamdays().get(childPosition).getMonth();
    	int day = streamMonthInfos.get(groupPosition).getStreamdays().get(childPosition).getDay();
    	b_item_weekday.setText(getWeekDay(year,month-1,day));
    	String str ="";
    	if(day>9){
    		str = day+"日";
    	}else{
    		str = "0"+day+"日";
    	}
    	b_item_day.setText(str);
  
    	LinearLayout b_item_layout = (LinearLayout)view.findViewById(R.id.b_item_layout);
    	
    	int size = streamMonthInfos.get(groupPosition).getStreamdays().get(childPosition).getStreams().size();
    	for(int i=0; i<size; i++){
    		StreamInfo si = streamMonthInfos.get(groupPosition).getStreamdays().get(childPosition).getStreams().get(i);
        	View view_item = View.inflate(context, R.layout.activity_b_item_item, null);
    		TextView textView = (TextView)view_item.findViewById(R.id.b_item_item_name);
    		textView.setBackgroundResource(category_img.get(si.getCategory()));
    		TextView b_item_item_content = (TextView)view_item.findViewById(R.id.b_item_item_content);
    		TextView b_item_item_money = (TextView)view_item.findViewById(R.id.b_item_item_money);
    		TextView bank_card_bankname = (TextView)view_item.findViewById(R.id.bank_card_bankname);
    		ImageView bank_card_logo = (ImageView)view_item.findViewById(R.id.bank_card_logo);
    		b_item_item_content.setText(si.getDes());
    		b_item_item_money.setText(NumberFormateUtil.Fromate2ex(si.getAmount()));
    		bank_card_bankname.setText(si.getBank()+"("+si.getCard_num()+")");
    		bank_card_logo.setImageBitmap(StringUtil.getBitmapFromAssert(context, si.getBank_logo()));
    		
    		if(i+1==size){
    			view_item.findViewById(R.id.b_item_item_divide_layout).setVisibility(View.GONE);
    		}
    		
    		b_item_layout.addView(view_item);
    	}
    	ImageView b_item_left = (ImageView) view.findViewById(R.id.b_item_left);
    	b_item_left.setLayoutParams(new RelativeLayout.LayoutParams(ViewSetUtil.dip2px(context, 73.68f), ViewSetUtil.dip2px(context, size*112)));
    	return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        //return generals[groupPosition].length;
    	if(streamMonthInfos!=null){
    		return streamMonthInfos.get(groupPosition).getStreamdays().size();
    	}else{
    		return 0;
    	}
    	
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        //return generalsTypes[groupPosition];
    	return streamMonthInfos.get(groupPosition);
    	
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        //return generalsTypes.length;
    	if(streamMonthInfos != null){
    		return streamMonthInfos.size();
    	}else{
    		return 0;
    	}
    	
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
       LinearLayout ll = new LinearLayout(context);
      // ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 30));
       ll.setGravity(Gravity.CENTER);
       ll.setBackgroundResource(R.color.low_gray);
       ll.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT,ViewSetUtil.dip2px(context, 35)));
       TextView textView = new TextView(context);
       LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,ViewSetUtil.dip2px(context, 35));
       lp1.setMargins(ViewSetUtil.dip2px(context, 55), 0, 0, 0);
       textView.setLayoutParams(lp1);
       textView.setGravity(Gravity.CENTER);
       textView.setTextSize(14);
       textView.setText(streamMonthInfos.get(groupPosition).getYear()+"-"+streamMonthInfos.get(groupPosition).getMonth());
       textView.setTextColor(context.getResources().getColor(R.color.title_blue));
       ll.addView(textView);
       
       float sum = 0;
       List<StreamDayInfo> days = streamMonthInfos.get(groupPosition).getStreamdays();
       for(StreamDayInfo day:days){
    	   for(StreamInfo si:day.getStreams()){
    		   sum += si.getAmount();
    	   }
       }
       
       textView = new TextView(context);
       LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,ViewSetUtil.dip2px(context, 35));
       lp2.setMargins(ViewSetUtil.dip2px(context, 40), 0, 0, 0);
       textView.setLayoutParams(lp2);
       textView.setGravity(Gravity.CENTER);
       textView.setTextSize(14);
       textView.setText("总共消费：￥"+NumberFormateUtil.Fromate2(sum));
       textView.setTextColor(context.getResources().getColor(R.color.title_blue));
       ll.addView(textView);
       
       return ll;

    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }
    
    public String getWeekDay(int year,int month,int day){

    	String[] wee = { "",  "天", "一", "二","三", "四", "五", "六" };
		Calendar cal = Calendar.getInstance();

		cal.set(year, month, day);// 2002-03-28 星期四

		return "星期" + wee[cal.get(Calendar.DAY_OF_WEEK)];
		


    }

}