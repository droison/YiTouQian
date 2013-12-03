package com.yeetou.xinyongkaguanjia.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yeetou.xinyongkaguanjia.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

/** 
 * 我的SpinnerButton 
 * @author chaisong 
 * 
 */  
public class MySpinnerButton extends Button {
  
    private Context context;
    private MyListItemOnClickListener myListItemOnClickListener;
    private List<String> currContent;
    private boolean isContain = true;
      
    public MySpinnerButton(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        this.context = context;  
        // 设置监听事件   
        setOnClickListener(new MySpinnerButtonOnClickListener());  
    }  
  
    public MySpinnerButton(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        this.context = context;  
        // 设置监听事件   
        setOnClickListener(new MySpinnerButtonOnClickListener());  
    }  
  
    public MySpinnerButton(Context context) {  
        super(context);  
        this.context = context;  
        // 设置监听事件   
        setOnClickListener(new MySpinnerButtonOnClickListener());  
    }  
      
    /** 
     * MySpinnerButton的点击事件 
     * @author chaisong 
     * 
     */  
    class MySpinnerButtonOnClickListener implements View.OnClickListener{
  
        @Override  
        public void onClick(View v) {  
              
            final MySpinnerDropDownItems mSpinnerDropDrownItems = new MySpinnerDropDownItems(context,currContent);  
            if (!mSpinnerDropDrownItems.isShowing()) {   
                mSpinnerDropDrownItems.showAsDropDown(MySpinnerButton.this);  
            }   
        }  
    }  
      
    /** 
     * MySpinnerButton的下拉列表 
     * @author chaisong 
     * 
     */  
    class MySpinnerDropDownItems extends PopupWindow{  
          
        private Context context;  
        private LinearLayout mLayout;  // 下拉列表的布局   
        private ListView mListView;    // 下拉列表控件   
        private ArrayList<HashMap<String, String>> mData;  
        
        
        public MySpinnerDropDownItems(Context context,List<String> currContent){
            super(context);  
              
            this.context = context;  
            // 下拉列表的布局   
            mLayout = new LinearLayout(context);  
            mLayout.setOrientation(LinearLayout.VERTICAL);  
            // 下拉列表控件   
            mListView = new ListView(context);  
            mListView.setLayoutParams(new LayoutParams(MySpinnerButton.this.getWidth(), LayoutParams.WRAP_CONTENT));  
            mListView.setCacheColorHint(Color.TRANSPARENT);  
            mData = new ArrayList<HashMap<String,String>>();
            HashMap<String, String> mHashmap;
            if(isContain){
            	mHashmap = new HashMap<String, String>();
            	mHashmap.put("spinner_dropdown_item_textview", "全部");
                mData.add(mHashmap);
            }
            for(int i=currContent.size()-1; i>=0; i--){  
                mHashmap = new HashMap<String, String>();  
                mHashmap.put("spinner_dropdown_item_textview", currContent.get(i));  
                mData.add(mHashmap);  
            }  
            // 为listView设置适配器   
            mListView.setAdapter(new MyAdapter(context,   
                    mData, R.layout.spinner_dropdown_item,   
                    new String[]{"spinner_dropdown_item_textview"}, new int[]{R.id.spinner_dropdown_item_textview}));  
            // 设置listView的点击事件   
            mListView.setOnItemClickListener(new MyListViewOnItemClickedListener());  
            // 把下拉列表添加到layout中。   
            mLayout.addView(mListView);  
              
            setWidth(LayoutParams.WRAP_CONTENT);  
            setHeight(LayoutParams.WRAP_CONTENT);  
            setContentView(mLayout);  
            setFocusable(true);  
              
            mLayout.setFocusableInTouchMode(true);  
        }  
          
        /**  
         * 我的适配器  
         * @author chaisong  
         *  
         */    
        public class MyAdapter extends BaseAdapter {    
            
            private Context context;    
            private List<? extends Map<String, ?>> mData;    
            private int mResource;    
            private String[] mFrom;    
            private int[] mTo;    
            private LayoutInflater mLayoutInflater;    
                
            public MyAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to){    
                    
                this.context = context;    
                this.mData = data;    
                this.mResource = resource;    
                this.mFrom = from;    
                this.mTo = to;    
                this.mLayoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);    
            }    
                
            public int getCount() {    
                    
                return this.mData.size();    
            }    
            
            public Object getItem(int position) {    
                    
                return this.mData.get(position);    
            }    
            
            public long getItemId(int position) {    
                    
                return position;    
            }    
            
            public View getView(int position, View contentView, ViewGroup parent) {    
                    
                contentView = this.mLayoutInflater.inflate(this.mResource, parent, false);      
            
                for(int index=0; index<this.mTo.length; index++){    
                    TextView textView = (TextView) contentView.findViewById(this.mTo[index]);    
                    textView.setText(this.mData.get(position).get(this.mFrom[index]).toString());    
                }    
            
                return contentView;    
            }    
        }   
          
        /** 
         * listView的点击事件 
         * @author chaisong 
         * 
         */  
        class MyListViewOnItemClickedListener implements AdapterView.OnItemClickListener{  
              
            @Override  
            public void onItemClick(AdapterView<?> parent, View view, int position,  
                    long id) {  
                  
                TextView mTextView = (TextView) view.findViewById(R.id.spinner_dropdown_item_textview);  
                String content = mTextView.getText().toString();  
                MySpinnerButton.this.setText(content);  
                MySpinnerDropDownItems.this.dismiss();
                if(myListItemOnClickListener!=null)
                	myListItemOnClickListener.onClick(content);
            }  
        }  
    }
    
    public void setListContent(List<String> listcontent){
    	currContent = listcontent;
    }
    
    public void setMyListItemOnClick(MyListItemOnClickListener myListItemOnClickListener){
    	this.myListItemOnClickListener = myListItemOnClickListener;
    }
    
    public void setContainAll(boolean isContain){
    	this.isContain = isContain;
    }
    
    public interface MyListItemOnClickListener{
    	public void onClick(String text);
    }
}  
