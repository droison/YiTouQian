package com.yeetou.xinyongkaguanjia.ui;

import java.util.List;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.yeetou.xinyongkaguanjia.R;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class E4Activity extends AbstractActivity implements OnClickListener {
	private static final String APP_ID = "wx7b277d5fd684a435";
	IWXAPI api = null;
	private ImageView e4_weibo;
	private ImageView e4_duanxin;
	private ImageView e4_weixin;
	private ImageView e4_pengyou;
	String text = null;
	private LinearLayout LinearLayout_Ab_Left_Indicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_e4);
		TextView TextView_Ab_Title = (TextView) findViewById(R.id.TextView_Ab_Title);
		TextView_Ab_Title.setText("分享给朋友");
		text = getString(R.string.e4_share_content);
		api = WXAPIFactory.createWXAPI(this, APP_ID, true);
		api.registerApp(APP_ID);
		e4_weibo = (ImageView) findViewById(R.id.e4_weibo);
		e4_duanxin = (ImageView) findViewById(R.id.e4_duanxin);
		e4_weixin = (ImageView) findViewById(R.id.e4_weixin);
		e4_pengyou = (ImageView) findViewById(R.id.e4_pengyou);

		e4_weibo.setOnClickListener(this);
		e4_duanxin.setOnClickListener(this);
		e4_weixin.setOnClickListener(this);
		e4_pengyou.setOnClickListener(this);

		LinearLayout_Ab_Left_Indicator = (LinearLayout) findViewById(R.id.LinearLayout_Ab_Left_Indicator);
		LinearLayout_Ab_Left_Indicator.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	private void sendWeiXin() {
		WXTextObject textObj = new WXTextObject();
		textObj.text = text;

		WXMediaMessage msg = new WXMediaMessage(textObj);
		msg.mediaObject = textObj;
		msg.description = text;

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;

		api.sendReq(req);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.e4_weibo:
			sendWeibo();
			break;
		case R.id.e4_duanxin:

			Uri smsToUri = Uri.parse("smsto:");
			Intent sendIntent = new Intent(Intent.ACTION_VIEW, smsToUri);
			// sendIntent.putExtra("address", "123456"); // 电话号码，这行去掉的话，默认就没有电话
			sendIntent.putExtra("sms_body", text);
			sendIntent.setType("vnd.android-dir/mms-sms");
			startActivityForResult(sendIntent, 1002);

			break;
		case R.id.e4_weixin:
			sendWeiXin();
			break;
		case R.id.e4_pengyou:
			sendPengyou();
			break;
		}

	}

	private void sendPengyou() {

		WXTextObject textObj = new WXTextObject();
		textObj.text = text;

		// 用WXTextObject对象初始化一个WXMediaMessage对象
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = textObj;
		// 发送文本类型的消息时，title字段不起作用
		// msg.title = "Will be ignored";
		msg.description = text;

		// 构造一个Req
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
		req.message = msg;
		req.scene = true ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;

		// 调用api接口发送数据到微信
		api.sendReq(req);

	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}

	private void sendWeibo() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/*");
		intent.putExtra(Intent.EXTRA_SUBJECT, "分享我的应用");
		intent.putExtra(Intent.EXTRA_TEXT, text);
		intent.putExtra(Intent.EXTRA_TITLE, "分享我的应用");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 

		PackageManager pm = getPackageManager();
		List<ResolveInfo> list = pm.queryIntentActivities(intent, 0);
		List<ResolveInfo> matches = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		String packageName = "com.sina.weibo";
		ResolveInfo info = null;
		for (ResolveInfo each : matches) {
			String pkgName = each.activityInfo.applicationInfo.packageName;
			if (packageName.equals(pkgName)) {
				info = each;
				break;
			}
		}
		if (info == null) {
			displayResponse("还没装微博");
			return;
		} else {
			intent.setClassName(packageName, info.activityInfo.name);
		}

		startActivity(intent);
	}
}
