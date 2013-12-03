package com.yeetou.xinyongkaguanjia.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

public class DialogUtil {

	private Context context;
	private OnClickListener click1;
	private OnClickListener click2;
	private String info;

	public DialogUtil(Context context, String info, OnClickListener click1, OnClickListener click2) {
		this.context = context;
		this.click1 = click1;
		this.click2 = click2;
		this.info = info;
	};

	public void creat() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("系统提示").setMessage(info).setPositiveButton("确定", click1).setNegativeButton("取消", click2);

		builder.create().show();
	}

}

/*
 * public void creatWithEditText(){ AlertDialog.Builder builder = new
 * AlertDialog.Builder(context); View view = View.inflate(context,
 * R.layout.dialog_item, null); TextView textView =
 * (TextView)view.findViewById(R.id.dialog_title); textView.setText(info);
 * EditText editText = (EditText)view.findViewById(R.id.dialog_content);
 * builder.setView(view);
 * 
 * builder.setNegativeButton("取消", click2).setPositiveButton("确定", click1);
 * builder.create().show();
 * 
 * 
 * }
 */

/*
 * private Context context; private String title; private OnClickListener
 * click_cancle; private OnClickListener click_submit;
 * 
 * public DialogUtil(Context context, String title, OnClickListener
 * click_cancle, OnClickListener click_submit) {
 * 
 * this.context = context; this.click_cancle = click_cancle; this.click_submit =
 * click_submit; this.title = title;
 * 
 * }
 * 
 * public void dialog() {
 * 
 * LayoutInflater flater = LayoutInflater.from(context); View view =
 * flater.inflate(R.layout.dialog_item, null); AlertDialog.Builder builder = new
 * AlertDialog.Builder(context); builder.setView(view); AlertDialog alert =
 * builder.create(); alert.show(); TextView dialog_title =
 * (TextView)view.findViewById(R.id.dialog_title); dialog_title.setText(title);
 * Button dialog_btn1 = (Button) view.findViewById(R.id.dialog_btn1); Button
 * dialog_btn2 = (Button) view.findViewById(R.id.dialog_btn2);
 * dialog_btn1.setOnClickListener(click_cancle);
 * dialog_btn2.setOnClickListener(click_submit);
 * 
 * }
 * 
 * public class clickListener implements OnClickListener{
 * 
 * @Override public void onClick(View v) { // TODO Auto-generated method stub
 * 
 * }
 * 
 * }
 */

