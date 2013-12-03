package com.yeetou.xinyongkaguanjia.ui;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.MyLocationOverlay;
import com.baidu.mapapi.PoiOverlay;
import com.yeetou.xinyongkaguanjia.R;
import com.yeetou.xinyongkaguanjia.adapter.AdapterForLinearLayout;
import com.yeetou.xinyongkaguanjia.adapter.BarAdapter2_AActivity;
import com.yeetou.xinyongkaguanjia.adapter.ExpandableAdapter_BActivity;
import com.yeetou.xinyongkaguanjia.adapter.ExpandableAdapter_CardInfoActivity;
import com.yeetou.xinyongkaguanjia.component.HorizontalListView;
import com.yeetou.xinyongkaguanjia.component.MyLinearLayoutForListAdapter;
import com.yeetou.xinyongkaguanjia.db.base.DbBank;
import com.yeetou.xinyongkaguanjia.db.base.DbBankCard;
import com.yeetou.xinyongkaguanjia.db.base.DbCardBills;
import com.yeetou.xinyongkaguanjia.db.service.DbBankCardService;
import com.yeetou.xinyongkaguanjia.db.service.DbBankService;
import com.yeetou.xinyongkaguanjia.db.service.DbBillsService;
import com.yeetou.xinyongkaguanjia.db.service.DbCardToBillService;
import com.yeetou.xinyongkaguanjia.db.service.DbStreamService;
import com.yeetou.xinyongkaguanjia.info.MonthPayments;
import com.yeetou.xinyongkaguanjia.info.StreamMonthInfo;
import com.yeetou.xinyongkaguanjia.util.ExitApplication;
import com.yeetou.xinyongkaguanjia.util.NumberFormateUtil;
import com.yeetou.xinyongkaguanjia.util.StringUtil;
import com.yeetou.xinyongkaguanjia.util.ViewSetUtil;

import android.content.Intent;

import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewStub;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class CardInfoActivity extends MapActivity {
	/*
	 * 头部
	 */
	private LinearLayout LinearLayout_Ab_Left_Indicator; // 用于返回
	private TextView TextView_Ab_Title; // 设置标题 “信用卡详情”OR“储蓄卡详情”
	private LinearLayout LinearLayout_Card_Detail_More; // 更多按钮
	/*
	 * 卡信息部分
	 */
	private ImageView card_info_bank_logo;
	private TextView TextView_Ab_bank_name;
	private TextView TextView_Ab_bank_number;// 此处加括号
	private TextView TextView_Ab_Title2; // 每月账单日： OR 本月支出：
	private TextView TextView_Ab_Title3; // 每月还款日： OR 本月收入
	/*
	 * 选项卡部分
	 */
	private RelativeLayout card_info_choose_history;
	private RelativeLayout card_info_curr;
	private RelativeLayout card_info_phone;
	private RelativeLayout card_info_map;
	private TextView card_info_text1;
	private TextView card_info_text2;
	private TextView card_info_text3;
	private TextView card_info_text4;
	private ImageView card_info_line;
	private ImageView card_info_curr_line;
	private ImageView card_info_phone_line;
	private ImageView card_info_map_line;
	/*
	 * 四个界面
	 */
	private ScrollView card_info_his_scroll;
	private LinearLayout linearLayout_history;
	private RelativeLayout relativeLayout_curr;
	private LinearLayout linearLayout_bank_phone;
	private ScrollView card_info_bankphone_scroll;
	private LinearLayout linearLayout_bank_map;
	/*
	 * 四个子页面
	 */
	private MyLinearLayoutForListAdapter card_info_bar;
	private AdapterForLinearLayout adapter_bar;
	private ViewStub card_info_pie_viewstub;
	private HorizontalListView card_info_cart;

	private LinearLayout layout_card_info_pie;
	private ExpandableListView card_info_list;

	private boolean isCredit;
	private String bank_card_id;
	private DbBankCardService dbBankCardService;
	private DbStreamService dbStreamService;
	private DbCardToBillService dbCardToBillService;
	private DbBankService dbBankService;
	private DbBillsService dbBillsService;

	private DbBankCard dbBankCard;
	private List<DbCardBills> bills;
	private DbBank dbBank;
	private List<StreamMonthInfo> streamMonthInfos;
	private List<MonthPayments> monthPaymentsByCard;

	private BMapManager mapManager;// 定义搜索服务类
	private MKSearch mMKSearch;
	private MapView mapView;
	private MapController mapController;
	private boolean isMapInstance = false;

	LocationListener mLocationListener = null;// onResume时注册此listener，onPause时需要Remove
	MyLocationOverlay mLocationOverlay = null; // 定位图层

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_info);

		initData();
		setUpView();

		// 初始化MapActivity
		mapManager = new BMapManager(getApplication());

		// init方法的第一个参数需填入申请的APIKey
		mapManager.init("285B415EBAB2A92293E85502150ADA7F03C777C4", null);
		super.initMapActivity(mapManager);
		mapView = (MapView) findViewById(R.id.map_View);
		// 设置地图模式为交通地图
		mapView.setTraffic(true);
		// 设置启用内置的缩放控件
		mapView.setBuiltInZoomControls(true);
		// 设置在缩放动画过程中也显示overlay,默认为不绘制
		mapView.setDrawOverlayWhenZooming(true);

	}

	private void initData() {
		isCredit = getIntent().getBooleanExtra("isCredit", true);
		bank_card_id = getIntent().getStringExtra("bank_card_id");
		if (bank_card_id == null) {
			Toast.makeText(CardInfoActivity.this, "参数非法", Toast.LENGTH_SHORT).show();
			finish();
		}

	}

	private void setUpView() {
		dbBankCardService = new DbBankCardService(this);
		dbStreamService = new DbStreamService(this);
		dbCardToBillService = new DbCardToBillService(this);
		dbBankService = new DbBankService(this);
		dbBillsService = new DbBillsService(this);

		dbBankCard = dbBankCardService.getById(bank_card_id);
		dbBank = dbBankService.getById(dbBankCard.getBank_id() + "");

		monthPaymentsByCard = dbStreamService.getMonthPaymentsByCard(bank_card_id, isCredit);

		card_info_bank_logo = (ImageView) this.findViewById(R.id.card_info_bank_logo);
		TextView_Ab_bank_name = (TextView) this.findViewById(R.id.TextView_Ab_bank_name);
		TextView_Ab_bank_number = (TextView) this.findViewById(R.id.TextView_Ab_bank_number);
		TextView_Ab_Title2 = (TextView) this.findViewById(R.id.TextView_Ab_Title2);
		TextView_Ab_Title3 = (TextView) this.findViewById(R.id.TextView_Ab_Title3);

		card_info_choose_history = (RelativeLayout) this.findViewById(R.id.card_info_choose_history);
		card_info_curr = (RelativeLayout) this.findViewById(R.id.card_info_curr);
		card_info_phone = (RelativeLayout) this.findViewById(R.id.card_info_phone);
		card_info_map = (RelativeLayout) this.findViewById(R.id.card_info_map);
		card_info_text1 = (TextView) this.findViewById(R.id.card_info_text1);
		card_info_text2 = (TextView) this.findViewById(R.id.card_info_text2);
		card_info_text3 = (TextView) this.findViewById(R.id.card_info_text3);
		card_info_text4 = (TextView) this.findViewById(R.id.card_info_text4);

		card_info_line = (ImageView) this.findViewById(R.id.card_info_line);
		card_info_curr_line = (ImageView) this.findViewById(R.id.card_info_curr_line);
		card_info_phone_line = (ImageView) this.findViewById(R.id.card_info_phone_line);
		card_info_map_line = (ImageView) this.findViewById(R.id.card_info_map_line);

		linearLayout_history = (LinearLayout) this.findViewById(R.id.linearLayout_history);
		card_info_his_scroll = (ScrollView) this.findViewById(R.id.card_info_his_scroll);
		relativeLayout_curr = (RelativeLayout) this.findViewById(R.id.relativeLayout_curr);
		linearLayout_bank_phone = (LinearLayout) this.findViewById(R.id.linearLayout_bank_phone);
		card_info_bankphone_scroll = (ScrollView) this.findViewById(R.id.card_info_bankphone_scroll);
		linearLayout_bank_map = (LinearLayout) this.findViewById(R.id.linearLayout_bank_map);

		card_info_choose_history.setOnClickListener(new CheckListener());
		card_info_curr.setOnClickListener(new CheckListener());
		card_info_phone.setOnClickListener(new CheckListener());
		card_info_map.setOnClickListener(new CheckListener());

		if (isCredit) {
			initHead("信用卡详情");
			card_info_bank_logo.setImageBitmap(StringUtil.getBitmapFromAssert(this, dbBank.getLogo()));
			TextView_Ab_bank_name.setText(dbBank.getName() + "信用卡");
			TextView_Ab_bank_number.setText("(" + dbBankCard.getNumber() + ")");
			bills = dbBillsService.getAll(dbCardToBillService.getBillIdS(bank_card_id));

			if (bills.size() != 0) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date(bills.get(0).getBilling_date()));

				TextView_Ab_Title2.setText("每月账单日：" + cal.get(Calendar.DAY_OF_MONTH) + "日");
				cal.setTime(new Date(bills.get(0).getDue_date()));
				TextView_Ab_Title3.setText("每月还款日：" + cal.get(Calendar.DAY_OF_MONTH) + "日");
			} else {
				TextView_Ab_Title2.setText("每月账单日：-");

				TextView_Ab_Title3.setText("每月还款日：-");
			}

			if (monthPaymentsByCard.size() != 0) {

				TextView card_info_his_text = (TextView) this.findViewById(R.id.card_info_his_text);
				TextView card_info_his_text1 = (TextView) this.findViewById(R.id.card_info_his_text1);
				card_info_his_text.setText("历史账单" + monthPaymentsByCard.size() + "期");
				float maxExpend = 0;
				String maxMonth = "";
				for (MonthPayments monthPayments : monthPaymentsByCard) {
					if (monthPayments.getExpand() > maxExpend) {
						maxExpend = monthPayments.getExpand();
						maxMonth = monthPayments.getYear() + "-" + monthPayments.getMonth();
					}
				}
				card_info_his_text1.setText("历史最高：" + NumberFormateUtil.Fromate2ex(maxExpend) + "[" + maxMonth + "]");
				card_info_bar = (MyLinearLayoutForListAdapter) this.findViewById(R.id.card_info_bar);
				card_info_pie_viewstub = (ViewStub) this.findViewById(R.id.card_info_pie_viewstub);
				card_info_pie_viewstub.inflate();
				layout_card_info_pie = (LinearLayout) this.findViewById(R.id.layout_card_info_pie);
				adapter_bar = new AdapterForLinearLayout(this, monthPaymentsByCard);
				card_info_bar.setInputDatas(this, monthPaymentsByCard, layout_card_info_pie,dbBankCard.getBank_name(),dbBankCard.getNumber());
				card_info_bar.setAdapter(adapter_bar);
			} else {

				LinearLayout linearLayout_history = (LinearLayout) this.findViewById(R.id.linearLayout_history);
				linearLayout_history.removeAllViews();

				ImageView imv2 = new ImageView(this);
				imv2.setImageResource(R.drawable.card_info_his_demo);
				LayoutParams lp2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				imv2.setLayoutParams(lp2);
				linearLayout_history.addView(imv2);

			}

		} else {
			initHead("储蓄卡详情");
			MonthPayments monthPayments = dbStreamService.getCurMonthPayments(bank_card_id);
			card_info_bank_logo.setImageBitmap(StringUtil.getBitmapFromAssert(this, dbBank.getLogo()));
			TextView_Ab_bank_name.setText(dbBank.getName() + "储蓄卡");
			TextView_Ab_bank_number.setText("(" + dbBankCard.getNumber() + ")");
			TextView_Ab_Title2.setText("本月支出：￥" + monthPayments.getExpand());
			TextView_Ab_Title3.setText("本月收入：￥" + monthPayments.getIncome());

			TextView card_info_his_text = (TextView) this.findViewById(R.id.card_info_his_text);
			TextView card_info_his_text1 = (TextView) this.findViewById(R.id.card_info_his_text1);
			card_info_his_text.setVisibility(View.GONE);
			card_info_his_text1.setVisibility(View.GONE);
			card_info_cart = (HorizontalListView) this.findViewById(R.id.card_info_cart_horizon);
			card_info_cart.setVisibility(View.VISIBLE);
			BarAdapter2_AActivity baradapter = new BarAdapter2_AActivity(this, monthPaymentsByCard, ViewSetUtil.dip2px(this, 96));
			card_info_cart.setAdapter(baradapter);

		}
	}

	private void initHead(String title) {
		LinearLayout_Ab_Left_Indicator = (LinearLayout) this.findViewById(R.id.LinearLayout_Ab_Left_Indicator);
		TextView_Ab_Title = (TextView) this.findViewById(R.id.TextView_Ab_Title);
		LinearLayout_Card_Detail_More = (LinearLayout) this.findViewById(R.id.LinearLayout_Card_Detail_More);
		TextView_Ab_Title.setText(title);
		LinearLayout_Ab_Left_Indicator.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		LinearLayout_Card_Detail_More.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent toChangeCard = new Intent(CardInfoActivity.this, ChangeCardActivity.class);
				toChangeCard.putExtra("isCredit", isCredit);
				toChangeCard.putExtra("bank_card_id", bank_card_id);
				ExitApplication.getInstance().addCardInfoActivity(CardInfoActivity.this);
				startActivity(toChangeCard);
			}
		});
	}

	private class CheckListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.card_info_choose_history:
				card_info_text1.setTextColor(Color.BLACK);
				card_info_text2.setTextColor(getResources().getColor(R.color.app_grey2));
				card_info_text3.setTextColor(getResources().getColor(R.color.app_grey2));
				card_info_text4.setTextColor(getResources().getColor(R.color.app_grey2));
				card_info_line.setVisibility(View.VISIBLE);
				card_info_curr_line.setVisibility(View.GONE);
				card_info_phone_line.setVisibility(View.GONE);
				card_info_map_line.setVisibility(View.GONE);
				card_info_his_scroll.setVisibility(View.VISIBLE);
				relativeLayout_curr.setVisibility(View.GONE);
				card_info_bankphone_scroll.setVisibility(View.GONE);
				linearLayout_bank_map.setVisibility(View.GONE);
				break;
			case R.id.card_info_curr:
				card_info_text1.setTextColor(getResources().getColor(R.color.app_grey2));
				card_info_text2.setTextColor(Color.BLACK);
				card_info_text3.setTextColor(getResources().getColor(R.color.app_grey2));
				card_info_text4.setTextColor(getResources().getColor(R.color.app_grey2));
				card_info_line.setVisibility(View.GONE);
				card_info_curr_line.setVisibility(View.VISIBLE);
				card_info_phone_line.setVisibility(View.GONE);
				card_info_map_line.setVisibility(View.GONE);
				card_info_his_scroll.setVisibility(View.GONE);
				relativeLayout_curr.setVisibility(View.VISIBLE);
				card_info_bankphone_scroll.setVisibility(View.GONE);
				linearLayout_bank_map.setVisibility(View.GONE);
				if (card_info_list == null) {
					card_info_list = (ExpandableListView) CardInfoActivity.this.findViewById(R.id.card_info_list);
					streamMonthInfos = dbStreamService.getStreams(bank_card_id, isCredit);
					if (isCredit) {
						if (streamMonthInfos.size() != 0) {
							ExpandableAdapter_BActivity adapter = new ExpandableAdapter_BActivity(CardInfoActivity.this, streamMonthInfos);
							card_info_list.setAdapter(adapter);
							card_info_list.expandGroup(0);// 设置第一组张开
							card_info_list.setGroupIndicator(null);// 除去自带的箭头
						} else {
							LinearLayout layout_curr_demo = (LinearLayout) CardInfoActivity.this.findViewById(R.id.layout_curr_demo);
							layout_curr_demo.setVisibility(View.VISIBLE);
							ImageView card_info_input_email = (ImageView) layout_curr_demo.findViewById(R.id.card_info_input_email);
							card_info_input_email.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									Intent toEmail = new Intent(CardInfoActivity.this, BEmailActivity.class);
									CardInfoActivity.this.startActivity(toEmail);
									finish();
								}
							});

						}

					} else {
						ExpandableAdapter_CardInfoActivity adapter = new ExpandableAdapter_CardInfoActivity(CardInfoActivity.this, streamMonthInfos);
						card_info_list.setAdapter(adapter);
						card_info_list.expandGroup(0);// 设置第一组张开
						card_info_list.setGroupIndicator(null);// 除去自带的箭头
					}
				}

				break;
			case R.id.card_info_phone:
				card_info_text1.setTextColor(getResources().getColor(R.color.app_grey2));
				card_info_text2.setTextColor(getResources().getColor(R.color.app_grey2));
				card_info_text3.setTextColor(Color.BLACK);
				card_info_text4.setTextColor(getResources().getColor(R.color.app_grey2));
				card_info_line.setVisibility(View.GONE);
				card_info_curr_line.setVisibility(View.GONE);
				card_info_phone_line.setVisibility(View.VISIBLE);
				card_info_map_line.setVisibility(View.GONE);
				card_info_his_scroll.setVisibility(View.GONE);
				relativeLayout_curr.setVisibility(View.GONE);
				card_info_bankphone_scroll.setVisibility(View.VISIBLE);
				linearLayout_bank_map.setVisibility(View.GONE);
				setUpPhoneView(linearLayout_bank_phone, dbBank);
				break;
			case R.id.card_info_map:
				card_info_text1.setTextColor(getResources().getColor(R.color.app_grey2));
				card_info_text2.setTextColor(getResources().getColor(R.color.app_grey2));
				card_info_text3.setTextColor(getResources().getColor(R.color.app_grey2));
				card_info_text4.setTextColor(Color.BLACK);
				card_info_line.setVisibility(View.GONE);
				card_info_curr_line.setVisibility(View.GONE);
				card_info_phone_line.setVisibility(View.GONE);
				card_info_map_line.setVisibility(View.VISIBLE);
				card_info_his_scroll.setVisibility(View.GONE);
				relativeLayout_curr.setVisibility(View.GONE);
				card_info_bankphone_scroll.setVisibility(View.GONE);
				linearLayout_bank_map.setVisibility(View.VISIBLE);
				if (!isMapInstance) {
					isMapInstance = true;
					final String bankName = dbBank.getName();

					// 添加定位图层
					mLocationOverlay = new MyLocationOverlay(CardInfoActivity.this, mapView);
					mapView.getOverlays().add(mLocationOverlay);

					// 注册定位事件
					mLocationListener = new LocationListener() {

						@Override
						public void onLocationChanged(Location location) {
							if (location != null) {
								GeoPoint geoPoint = new GeoPoint((int) (location.getLatitude() * 1e6), (int) (location.getLongitude() * 1e6));
								mapView.getController().animateTo(geoPoint);
								mapController = mapView.getController();
								// 设置地图的中心
								mapController.setCenter(geoPoint);
								// 设置地图默认的缩放级别
								mapController.setZoom(16);
								// 初始化
								MKSearch mMKSearch = new MKSearch();
								mMKSearch.init(mapManager, new MySearchListener());
								// 搜索贵州大学校门口附近500米范围的自动取款机
								mMKSearch.poiSearchNearBy(bankName, geoPoint, 10000);
							}
						}
					};
					if (mapManager != null) {
						// 开启百度地图API
						// 注册定位事件，定位后将地图移动到定位点
						mapManager.getLocationManager().requestLocationUpdates(mLocationListener);
						mLocationOverlay.enableMyLocation();
						mLocationOverlay.enableCompass(); // 打开指南针
						mapManager.start();
					}
				}
				break;
			default:
				break;
			}
		}

	}

	Handler handler = new Handler();

	private void setUpPhoneView(LinearLayout linearlayout, final DbBank bank) {
		if (linearlayout.getChildCount() == 0) {
			if (bank.getHotline() != null && !bank.getHotline().equals("")) {
				View view = View.inflate(this, R.layout.card_info_phone_item, null);
				TextView card_info_phone_item_title = (TextView) view.findViewById(R.id.card_info_phone_item_title);
				TextView card_info_phone_item_content = (TextView) view.findViewById(R.id.card_info_phone_item_content);
				ImageView card_info_phone_item_btn = (ImageView) view.findViewById(R.id.card_info_phone_item_btn);
				card_info_phone_item_title.setText("服务热线");
				card_info_phone_item_content.setText(bank.getHotline());
				card_info_phone_item_btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						call(bank.getHotline());
					}
				});
				linearlayout.addView(view);
			}
			if (bank.getPhone() != null && !bank.getPhone().equals("")) {
				View view = View.inflate(this, R.layout.card_info_phone_item, null);
				TextView card_info_phone_item_title = (TextView) view.findViewById(R.id.card_info_phone_item_title);
				TextView card_info_phone_item_content = (TextView) view.findViewById(R.id.card_info_phone_item_content);
				ImageView card_info_phone_item_btn = (ImageView) view.findViewById(R.id.card_info_phone_item_btn);
				card_info_phone_item_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.phone_click));
				card_info_phone_item_title.setText("客服电话");
				card_info_phone_item_content.setText(bank.getPhone());
				card_info_phone_item_btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						call(bank.getPhone());
					}
				});
				linearlayout.addView(view);
			}
			if (bank.getManual() != null && !bank.getManual().equals("")) {
				View view = View.inflate(this, R.layout.card_info_phone_item, null);
				TextView card_info_phone_item_title = (TextView) view.findViewById(R.id.card_info_phone_item_title);
				TextView card_info_phone_item_content = (TextView) view.findViewById(R.id.card_info_phone_item_content);
				ImageView card_info_phone_item_btn = (ImageView) view.findViewById(R.id.card_info_phone_item_btn);
				card_info_phone_item_title.setText("人工服务热线");
				card_info_phone_item_content.setText(bank.getManual());
				card_info_phone_item_btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						call(bank.getManual());
					}
				});
				linearlayout.addView(view);
			}
			if (bank.getCc_yd() != null && !bank.getCc_yd().equals("")) {
				View view = View.inflate(this, R.layout.card_info_phone_item, null);
				TextView card_info_phone_item_title = (TextView) view.findViewById(R.id.card_info_phone_item_title);
				TextView card_info_phone_item_content = (TextView) view.findViewById(R.id.card_info_phone_item_content);
				ImageView card_info_phone_item_btn = (ImageView) view.findViewById(R.id.card_info_phone_item_btn);
				card_info_phone_item_title.setText("信用卡移动号");
				card_info_phone_item_content.setText(bank.getCc_yd());
				card_info_phone_item_btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						call(bank.getCc_yd());
					}
				});
				linearlayout.addView(view);
			}
			if (bank.getCc_lt() != null && !bank.getCc_lt().equals("")) {
				View view = View.inflate(this, R.layout.card_info_phone_item, null);
				TextView card_info_phone_item_title = (TextView) view.findViewById(R.id.card_info_phone_item_title);
				TextView card_info_phone_item_content = (TextView) view.findViewById(R.id.card_info_phone_item_content);
				ImageView card_info_phone_item_btn = (ImageView) view.findViewById(R.id.card_info_phone_item_btn);
				card_info_phone_item_title.setText("信用卡联通号");
				card_info_phone_item_content.setText(bank.getCc_lt());
				card_info_phone_item_btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						call(bank.getCc_lt());
					}
				});
				linearlayout.addView(view);
			}
			if (bank.getLoss() != null && !bank.getLoss().equals("")) {
				View view = View.inflate(this, R.layout.card_info_phone_item, null);
				TextView card_info_phone_item_title = (TextView) view.findViewById(R.id.card_info_phone_item_title);
				TextView card_info_phone_item_content = (TextView) view.findViewById(R.id.card_info_phone_item_content);
				ImageView card_info_phone_item_btn = (ImageView) view.findViewById(R.id.card_info_phone_item_btn);
				card_info_phone_item_title.setText("挂失电话");
				card_info_phone_item_content.setText(bank.getLoss());
				card_info_phone_item_btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						call(bank.getLoss());
					}
				});
				linearlayout.addView(view);
			}
			if (bank.getQuery_bill() != null && !bank.getQuery_bill().equals("")) {
				View view = View.inflate(this, R.layout.card_info_phone_item, null);
				TextView card_info_phone_item_title = (TextView) view.findViewById(R.id.card_info_phone_item_title);
				TextView card_info_phone_item_content = (TextView) view.findViewById(R.id.card_info_phone_item_content);
				ImageView card_info_phone_item_btn = (ImageView) view.findViewById(R.id.card_info_phone_item_btn);
				card_info_phone_item_title.setText("查询短信账单");
				card_info_phone_item_content.setText(bank.getQuery_bill());
				card_info_phone_item_btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						SMS(bank.getPhone(), bank.getQuery_bill()); // ,
																	// bank.getQuery_bill().substring(bank.getQuery_bill().indexOf("送")+1,
																	// bank.getQuery_bill().indexOf("至")));
					}
				});
				linearlayout.addView(view);
			}
			if (bank.getQuery_limit() != null && !bank.getQuery_limit().equals("")) {
				View view = View.inflate(this, R.layout.card_info_phone_item, null);
				TextView card_info_phone_item_title = (TextView) view.findViewById(R.id.card_info_phone_item_title);
				TextView card_info_phone_item_content = (TextView) view.findViewById(R.id.card_info_phone_item_content);
				ImageView card_info_phone_item_btn = (ImageView) view.findViewById(R.id.card_info_phone_item_btn);
				card_info_phone_item_title.setText("查询短信额度");
				card_info_phone_item_content.setText(bank.getQuery_limit());
				card_info_phone_item_btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						SMS(bank.getPhone(), bank.getQuery_limit()); // ,
																		// bank.getQuery_limit().substring(bank.getQuery_limit().indexOf("送")+1,
																		// bank.getQuery_limit().indexOf("至")));
					}
				});
				linearlayout.addView(view);
			}
			if (bank.getQuery_credit() != null && !bank.getQuery_credit().equals("")) {
				View view = View.inflate(this, R.layout.card_info_phone_item, null);
				TextView card_info_phone_item_title = (TextView) view.findViewById(R.id.card_info_phone_item_title);
				TextView card_info_phone_item_content = (TextView) view.findViewById(R.id.card_info_phone_item_content);
				ImageView card_info_phone_item_btn = (ImageView) view.findViewById(R.id.card_info_phone_item_btn);
				card_info_phone_item_title.setText("查询短信信用卡");
				card_info_phone_item_content.setText(bank.getQuery_credit());
				card_info_phone_item_btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						SMS(bank.getPhone(), bank.getQuery_credit()); // ,
																		// bank.getQuery_credit().substring(bank.getQuery_credit().indexOf("送")+1,
																		// bank.getQuery_credit().indexOf("至")));
					}
				});
				linearlayout.addView(view);
			}

		}
	}

	public void call(String phoneNumber) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
		startActivity(intent);
	}

	public void SMS(String phoneNumber, String body) {
		/*
		 * Uri smsToUri = Uri.parse("smsto:" + phoneNumber); Intent sendIntent =
		 * new Intent(Intent.ACTION_VIEW, smsToUri);
		 * //sendIntent.putExtra("sms_body", content);
		 * sendIntent.putExtra("address", phoneNumber); //不能设置接收号码。 不知道为什么？
		 * sendIntent.setType("vnd.android-dir/mms-sms");
		 * startActivity(sendIntent);
		 */

		Uri uri = Uri.parse("smsto:" + phoneNumber);
		Intent it = new Intent(Intent.ACTION_SENDTO, uri);
		it.putExtra("sms_body", body);
		startActivity(it);
	}

	/**
	 * * 实现MKSearchListener接口,用于实现异步搜索服务
	 */
	public class MySearchListener implements MKSearchListener {
		/** * 根据经纬度搜索地址信息结果 * * @param result 搜索结果 * @param iError 错误号 （0表示正确返回） */
		@Override
		public void onGetAddrResult(MKAddrInfo result, int iError) {
		}

		/** * 驾车路线搜索结果 * * @param result 搜索结果 * @param iError 错误号 */
		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError) {
		}

		/**
		 * * POI搜索结果（范围检索、城市POI检索、周边检索） * * @param result 搜索结果 * @param type
		 * 返回结果类型（11,12,21:poi列表 7:城市列表） * @param iError 错误号（0表示正确返回）
		 */
		@Override
		public void onGetPoiResult(MKPoiResult result, int type, int iError) {
			if (result == null) {
				return;
			}
			// PoiOverlay是baidu map api提供的用于显示POI的Overlay
			PoiOverlay poioverlay = new PoiOverlay(CardInfoActivity.this, mapView);
			// 设置搜索到的POI数据
			poioverlay.setData(result.getAllPoi());
			// 在地图上显示PoiOverlay（将搜索到的兴趣点标注在地图上）
			mapView.getOverlays().add(poioverlay);
		}

		/** * 公交换乘路线搜索结果 * * @param result 搜索结果 * @param iError 错误号（0表示正确返回） */
		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult result, int iError) {
		}

		/** * 步行路线搜索结果 * * @param result 搜索结果 * @param iError 错误号（0表示正确返回） */
		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult result, int iError) {
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	protected void onDestroy() {
		if (mapManager != null) {
			// 程序退出前需调用此方法
			mapManager.destroy();
			mapManager = null;
		}
		super.onDestroy();
	}

	@Override
	public void onPause() {
		if (mapManager != null) {
			// 终止百度地图API
			mapManager.getLocationManager().removeUpdates(mLocationListener);
			if (mLocationOverlay != null) {
				mLocationOverlay.disableMyLocation();
				mLocationOverlay.disableCompass(); // 关闭指南针
			}
			mapManager.stop();
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}
}