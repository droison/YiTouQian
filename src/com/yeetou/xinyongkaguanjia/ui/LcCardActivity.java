package com.yeetou.xinyongkaguanjia.ui;

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
import com.yeetou.xinyongkaguanjia.db.base.DbBank;
import com.yeetou.xinyongkaguanjia.db.service.DbBankService;
import com.yeetou.xinyongkaguanjia.http.base.LccpBase.Lccp;
import com.yeetou.xinyongkaguanjia.util.NumberFormateUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class LcCardActivity extends MapActivity {
	/*
	 * 头部
	 */
	private LinearLayout LinearLayout_Ab_Left_Indicator; // 用于返回
	private TextView TextView_Ab_Title; // 设置标题 “信用卡详情”OR“储蓄卡详情”
	private LinearLayout LinearLayout_Card_Detail_More; // 更多按钮
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
	private RelativeLayout lc_linearLayout_1;
	private RelativeLayout lc_relativeLayout_2;
	private ScrollView lc_scroll_3;
	private LinearLayout lc_linearLayout_4;

	private LinearLayout lc_linearLayout_bank_phone;

	private DbBankService dbBankService;
	private DbBank dbBank;
	private Lccp lccp;
	/*
	 * 地图相关
	 */
	private BMapManager mapManager;// 定义搜索服务类
	private MKSearch mMKSearch;
	private MapView mapView;
	private MapController mapController;

	LocationListener mLocationListener = null;// onResume时注册此listener，onPause时需要Remove
	MyLocationOverlay mLocationOverlay = null; // 定位图层

	/*
	 * 顶部5个textview
	 */
	private TextView lc_card_info_title;
	private TextView lc_card_info_title_text1;
	private TextView lc_card_info_title_text2;
	private TextView lc_card_info_title_text3;
	private TextView lc_card_info_title_text4;
	/*
	 * 专家解读选项卡
	 */
	private TextView lc_total_text;
	private TextView lc_1_text;
	private TextView lc_2_text;
	private TextView lc_3_text;
	private TextView lc_4_text;
	private ImageView lc_1_img;
	private ImageView lc_2_img;
	private ImageView lc_3_img;
	private ImageView lc_4_img;
	/*
	 * 收益计算选项卡
	 */
	private TextView lc_input;
	private TextView lc_output;
	private EditText editText;
	private TextView lc_text_month;
	private boolean isMapInstance = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lc_card_info);
		initData();
		setUpView();
		// 初始化MapActivity
		mapManager = new BMapManager(getApplication());

		// init方法的第一个参数需填入申请的APIKey
		mapManager.init("285B415EBAB2A92293E85502150ADA7F03C777C4", null);
		super.initMapActivity(mapManager);
		mapView = (MapView) this.findViewById(R.id.lc_map_View);
		// 设置地图模式为交通地图
		mapView.setTraffic(true);
		// 设置启用内置的缩放控件
		mapView.setBuiltInZoomControls(true);
		// 设置在缩放动画过程中也显示overlay,默认为不绘制
		mapView.setDrawOverlayWhenZooming(true);
	}

	private void initData() {
		Bundle b = getIntent().getExtras();
		lccp = (Lccp) b.getSerializable("lccp");
		if (lccp == null) {
			Toast.makeText(LcCardActivity.this, "参数非法", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	private void setUpView() {
		initHead("产品详情");
		dbBankService = new DbBankService(this);
		dbBank = dbBankService.getByName(lccp.getBank());
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

		lc_linearLayout_1 = (RelativeLayout) this.findViewById(R.id.lc_linearLayout_1);
		lc_relativeLayout_2 = (RelativeLayout) this.findViewById(R.id.lc_relativeLayout_2);
		lc_scroll_3 = (ScrollView) this.findViewById(R.id.lc_scroll_3);
		lc_linearLayout_4 = (LinearLayout) this.findViewById(R.id.lc_linearLayout_4);
		lc_linearLayout_bank_phone = (LinearLayout) this.findViewById(R.id.lc_linearLayout_bank_phone);

		lc_card_info_title = (TextView) findViewById(R.id.lc_card_info_title);
		lc_card_info_title_text1 = (TextView) findViewById(R.id.lc_card_info_title_text1);
		lc_card_info_title_text2 = (TextView) findViewById(R.id.lc_card_info_title_text2);
		lc_card_info_title_text3 = (TextView) findViewById(R.id.lc_card_info_title_text3);
		lc_card_info_title_text4 = (TextView) findViewById(R.id.lc_card_info_title_text4);

		lc_card_info_title.setText(lccp.getName());
		lc_card_info_title_text1.setText(String.valueOf(lccp.getProfit()) + "%");
		lc_card_info_title_text2.setText(String.valueOf(lccp.getPeriod()) + "天");
		lc_card_info_title_text3.setText(String.valueOf(lccp.getAmt()) + "万");
		String result = "";
		for (String temp : lccp.getFeature()) {
			result += temp + "\n";
		}
		lc_card_info_title_text4.setText(result);

		lc_total_text = (TextView) findViewById(R.id.lc_total_text);
		lc_1_text = (TextView) findViewById(R.id.lc_1_text);
		lc_2_text = (TextView) findViewById(R.id.lc_2_text);
		lc_3_text = (TextView) findViewById(R.id.lc_3_text);
		lc_4_text = (TextView) findViewById(R.id.lc_4_text);
		lc_1_img = (ImageView) findViewById(R.id.lc_1_img);
		lc_2_img = (ImageView) findViewById(R.id.lc_2_img);
		lc_3_img = (ImageView) findViewById(R.id.lc_3_img);
		lc_4_img = (ImageView) findViewById(R.id.lc_4_img);

		lc_total_text.setText(lccp.getAdvise().get(0));
		lc_1_text.setText(lccp.getAdvise().get(2));
		lc_2_text.setText(lccp.getAdvise().get(4));
		lc_3_text.setText(lccp.getAdvise().get(6));
		lc_4_text.setText(lccp.getAdvise().get(8));

		setIcon(1, lc_1_img);
		setIcon(3, lc_2_img);
		setIcon(5, lc_3_img);
		setIcon(7, lc_4_img);

		lc_text_month = (TextView) findViewById(R.id.lc_text_month);
		lc_text_month.setText(lccp.getPeriod() + "天后您将获得的收益");

		lc_input = (TextView) findViewById(R.id.lc_input);
		lc_output = (TextView) findViewById(R.id.lc_output);
		lc_input.setOnClickListener(new CheckListener());

		card_info_choose_history.setOnClickListener(new CheckListener());
		card_info_curr.setOnClickListener(new CheckListener());
		card_info_phone.setOnClickListener(new CheckListener());
		card_info_map.setOnClickListener(new CheckListener());

	}

	public void setIcon(int i, ImageView view) {
		if (lccp.getAdvise().get(i).equals("yt_good")) {
			view.setImageResource(R.drawable.lc_card_good);
		} else if (lccp.getAdvise().get(i).equals("yt_normal")) {
			view.setImageResource(R.drawable.lc_card_warning);
		} else {
			view.setImageResource(R.drawable.lc_card_error);
		}
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
				lc_linearLayout_1.setVisibility(View.VISIBLE);
				lc_relativeLayout_2.setVisibility(View.GONE);
				lc_scroll_3.setVisibility(View.GONE);
				lc_linearLayout_4.setVisibility(View.GONE);
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
				lc_linearLayout_1.setVisibility(View.GONE);
				lc_relativeLayout_2.setVisibility(View.VISIBLE);
				lc_scroll_3.setVisibility(View.GONE);
				lc_linearLayout_4.setVisibility(View.GONE);

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
				lc_linearLayout_1.setVisibility(View.GONE);
				lc_relativeLayout_2.setVisibility(View.GONE);
				lc_scroll_3.setVisibility(View.VISIBLE);
				lc_linearLayout_4.setVisibility(View.GONE);
				setUpPhoneView(lc_linearLayout_bank_phone, dbBank);
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
				lc_linearLayout_1.setVisibility(View.GONE);
				lc_relativeLayout_2.setVisibility(View.GONE);
				lc_scroll_3.setVisibility(View.GONE);
				lc_linearLayout_4.setVisibility(View.VISIBLE);
				if (!isMapInstance) {
					isMapInstance = true;
					final String bankName = dbBank.getName();
					// 添加定位图层
					mLocationOverlay = new MyLocationOverlay(LcCardActivity.this, mapView);
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
			case R.id.lc_input:
				editText = new EditText(LcCardActivity.this);
				DigitsKeyListener numericOnlyListener = new DigitsKeyListener(false, true);
				editText.setKeyListener(numericOnlyListener);
				AlertDialog dialog = new AlertDialog.Builder(LcCardActivity.this).setTitle("请输入购买金额，单位（万元）").setView(editText).setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							if (Integer.parseInt(editText.getText().toString()) > 10000) {
								Toast.makeText(LcCardActivity.this, "请输入小于10000的数字", Toast.LENGTH_SHORT).show();
							} else if (Integer.parseInt(editText.getText().toString()) < lccp.getAmt()) {
								Toast.makeText(LcCardActivity.this, "输入金额不能小于起购金额", Toast.LENGTH_SHORT).show();
							} else if (!editText.getText().toString().equals("")) {

								double temp = Double.parseDouble(editText.getText().toString());
								temp = temp / 365 * 100 * lccp.getProfit() * lccp.getPeriod();
								lc_input.setText(editText.getText().toString());
								lc_output.setText(NumberFormateUtil.Fromate2ex(temp));

							} else {
								Toast.makeText(LcCardActivity.this, "输入金额不能为空", Toast.LENGTH_SHORT).show();
							}
						} catch (NumberFormatException e) {
							Toast.makeText(LcCardActivity.this, "请输入金额，单位（万元）", Toast.LENGTH_SHORT).show();
						}
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).create();

				
				dialog.setOnShowListener(new OnShowListener() {
					
					@Override
					public void onShow(DialogInterface dialog) {
						InputMethodManager m = (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
						m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);   
					}
				});
				dialog.show();
				break;
			default:
				break;
			}
		}

	}

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
						call(bank.getPhone());
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
						call(bank.getPhone());
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

		Uri uri = Uri.parse("smsto:" + phoneNumber);
		Intent it = new Intent(Intent.ACTION_SENDTO, uri);
		it.putExtra("sms_body", body);
		startActivity(it);
	}

	private void initHead(String title) {
		LinearLayout_Ab_Left_Indicator = (LinearLayout) this.findViewById(R.id.LinearLayout_Ab_Left_Indicator);
		TextView_Ab_Title = (TextView) this.findViewById(R.id.TextView_Ab_Title);
		TextView_Ab_Title.setText(title);
		LinearLayout_Ab_Left_Indicator.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
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
			PoiOverlay poioverlay = new PoiOverlay(LcCardActivity.this, mapView);
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
