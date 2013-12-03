package com.yeetou.xinyongkaguanjia.ui;

/**
 * 
 * 功能：收支汇总页面
 *
 */

import java.io.File;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.yeetou.xinyongkaguanjia.R;
import com.yeetou.xinyongkaguanjia.adapter.BarAdapter_AActivity;
import com.yeetou.xinyongkaguanjia.adapter.CreditCardAdapter_AActivity;
import com.yeetou.xinyongkaguanjia.adapter.DebitCardAdapter_AActivity;
import com.yeetou.xinyongkaguanjia.component.ASpinnerImageButton;
import com.yeetou.xinyongkaguanjia.component.HorizontalListView;
import com.yeetou.xinyongkaguanjia.component.ListLinearLayout;
import com.yeetou.xinyongkaguanjia.constants.AppConstant;
import com.yeetou.xinyongkaguanjia.db.base.DbBankCard;
import com.yeetou.xinyongkaguanjia.db.service.DbBankCardService;
import com.yeetou.xinyongkaguanjia.db.service.DbStreamService;
import com.yeetou.xinyongkaguanjia.http.service.ApkDownloadService;
import com.yeetou.xinyongkaguanjia.http.service.AutoSyncData;
import com.yeetou.xinyongkaguanjia.http.service.CheckVersionService;
import com.yeetou.xinyongkaguanjia.info.MonthPayments;
import com.yeetou.xinyongkaguanjia.util.AnimationUtil;
import com.yeetou.xinyongkaguanjia.util.NumberFormateUtil;
import com.yeetou.xinyongkaguanjia.util.ViewSetUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class AActivity extends AbstractActivity {

	private TextView a_expend;
	private TextView a_income;
	private ListLinearLayout cardsLinearLayout1;
	private ListLinearLayout cardsLinearLayout2;
	private HorizontalListView a_cart;
	private BarAdapter_AActivity baradapter;
	private CreditCardAdapter_AActivity cardadapter1;
	private DebitCardAdapter_AActivity cardadapter2;

	private DbBankCardService dbBankCardService;
	private DbStreamService dbStreamService;
	private ASpinnerImageButton title_right2;
	private ImageView title_right1;
	private ImageView a_cart_demo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_a);
		setUpView();
		UpdateHandler uhandler = new UpdateHandler(this);
		new Thread(new CheckVersionService(this, uhandler)).start();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		setUpView();
	}

	private void setUpView() {

		a_expend = (TextView) this.findViewById(R.id.a_expend);
		a_income = (TextView) this.findViewById(R.id.a_income);
		a_cart_demo = (ImageView) this.findViewById(R.id.a_cart_demo);
		title_right1 = (ImageView) this.findViewById(R.id.title_right1);
		title_right2 = (ASpinnerImageButton) this.findViewById(R.id.title_right2);

		dbBankCardService = new DbBankCardService(AActivity.this);
		dbStreamService = new DbStreamService(AActivity.this);
		a_cart_demo.setVisibility(View.GONE);
		MonthPayments mp = dbStreamService.getCurMonthPayments(null);
		a_expend.setText(NumberFormateUtil.Fromate2ex(mp.getExpand()));
		a_income.setText(NumberFormateUtil.Fromate2ex(mp.getIncome()));

		List<MonthPayments> monthPayments = dbStreamService.getMonthPayments(2013);

		a_cart = (HorizontalListView) this.findViewById(R.id.a_cart);
		if (dbStreamService.getMonthPayments().size() != 0) {
			baradapter = new BarAdapter_AActivity(AActivity.this, dbStreamService.getMonthPayments());
			a_cart.setAdapter(baradapter);
			a_cart.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					Intent toBActivity = new Intent(AActivity.this, BActivity.class);
					toBActivity.putExtra("thedate", arg1.getTag().toString());
					AActivity.this.startActivity(toBActivity);
				}
			});
		} else {
			
			a_cart_demo.setVisibility(View.VISIBLE);
		}

		title_right1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				title_right1.setClickable(false);
				AnimationUtil.setRoundAtimation(title_right1);
				new AutoSyncData(AActivity.this, autoSynHanler);

			}
		});
		cardsLinearLayout1 = (ListLinearLayout) this.findViewById(R.id.cardsLinearLayout1);
		cardsLinearLayout2 = (ListLinearLayout) this.findViewById(R.id.cardsLinearLayout2);
		cardsLinearLayout1.removeAllViews();
		cardsLinearLayout2.removeAllViews();

		if (dbBankCardService.getAllCreditCard().size() != 0) {
			List<DbBankCard> cards = dbBankCardService.getAllCreditCard();
			cardadapter1 = new CreditCardAdapter_AActivity(this, cards);
			cardsLinearLayout1.setAdapter(cardadapter1);
		} else {
			ImageView imv = new ImageView(this);
			imv.setImageResource(R.drawable.credit_demo);
			LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewSetUtil.dip2px(AActivity.this, 120), ViewSetUtil.dip2px(AActivity.this, 90));
			lp2.setMargins(ViewSetUtil.dip2px(AActivity.this, 10), 0, 0, 0);
			imv.setLayoutParams(lp2);
			
			cardsLinearLayout1.addView(imv);
		}
		if (dbBankCardService.getAllDebitCard().size() != 0) {
			List<DbBankCard> cards = dbBankCardService.getAllDebitCard();
			cardadapter2 = new DebitCardAdapter_AActivity(this, cards);
			cardsLinearLayout2.setAdapter(cardadapter2);
		} else {
			ImageView imv2 = new ImageView(this);
			imv2.setImageResource(R.drawable.debit_demo);
			LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewSetUtil.dip2px(AActivity.this, 120), ViewSetUtil.dip2px(AActivity.this, 90));
			lp2.setMargins(ViewSetUtil.dip2px(AActivity.this, 10), 0, 0, 0);
			imv2.setLayoutParams(lp2);

			cardsLinearLayout2.addView(imv2);
		}
	}

	private Handler autoSynHanler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			title_right1.clearAnimation();
			setUpView();
			title_right1.setClickable(true);
		};
	};

	public static class UpdateHandler extends Handler {

		/**
		 * 更新版本使用的URL
		 */
		private String downloadUrl;

		/**
		 * 更新进度
		 */
		private ProgressBar mProgress;

		/**
		 * 下载提示框
		 */
		private Dialog downloadDialog;

		private Activity context;

		public UpdateHandler(Activity context) {
			this.context = context;
		}

		protected void installApk(File file) {

			Intent intent = new Intent();

			intent.setAction(Intent.ACTION_VIEW);

			intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");

			context.startActivity(intent);
		}

		@Override
		public void handleMessage(Message mes) {
			switch (mes.what) {
			case AppConstant.HANDLER_APK_STOP:
				Toast.makeText(context, "您的应用被禁止", 1).show();
				context.finish();
				break;
			case AppConstant.HANDLER_VERSION_UPDATE:
				String result = (String) mes.obj;
				JSONObject jo;
				String info = "";
				try {
					jo = new JSONObject(result);
					JSONObject data = jo.getJSONObject("data");
					info = jo.getString("info");
					downloadUrl = data.getString("url");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				AlertDialog.Builder builer = new Builder(context);
				builer.setTitle("升级提示");
				builer.setMessage(info.equals("") ? "新版本发布了，强烈建议更新" : info);

				builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						AlertDialog.Builder builder = new Builder(context);
						builder.setTitle("早客新版本下载更新中");
						final LayoutInflater inflater = LayoutInflater.from(context);
						View v = inflater.inflate(R.layout.update_progress, null);
						mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
						builder.setView(v);
						downloadDialog = builder.create();
						downloadDialog.setCancelable(false);
						downloadDialog.show();
						new Thread(new ApkDownloadService(downloadUrl, UpdateHandler.this)).start();
					}
				});

				builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						context.finish();
					}
				});
				AlertDialog dialog = builer.create();
				dialog.show();
				break;
			case AppConstant.HANDLER_APK_DOWNLOAD_PROGRESS:
				mProgress.setProgress((Integer) mes.obj);
				break;
			case AppConstant.HANDLER_APK_DOWNLOAD_FINISH:
				File file = new File(AppConstant.BASE_DIR_PATH, AppConstant.APK_NAME);
				installApk(file);
				break;
			case AppConstant.HANDLER_HTTPSTATUS_ERROR:
				Log.v("update", "检查失败");
				break;

			}
		}

	}

}
