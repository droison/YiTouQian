package com.yeetou.xinyongkaguanjia.constants;

import java.io.File;

import com.yeetou.xinyongkaguanjia.R;

import android.os.Environment;

public class AppConstant {

	public static final String BASE_DIR_CACHE = Environment.getExternalStorageDirectory() + File.separator + "yitouqian" + File.separator + "cache";
	public static final String BASE_DIR_PATH = Environment.getExternalStorageDirectory() + File.separator + "yitouqian" + File.separator + "data";

	public static final int[] cart_color = { R.color.cart_1, R.color.cart_2, R.color.cart_3, R.color.cart_4, R.color.cart_5, R.color.cart_6, R.color.cart_1 };
	public static final int bar_income = R.color.cart_orange;
	public static final int bar_expend = R.color.cart_green;

	// 查询短信的接口
	public static final int CHECK_MESSAGE_INFO = 1001;
	public static final int CHECK_MESSAGE_FINISH = 1002;

	public static final int HANDLER_VERSION_UPDATE = 20001; // 表示升级
	public static final int HANDLER_APK_DOWNLOAD_PROGRESS = 20002;
	public static final int HANDLER_APK_DOWNLOAD_FINISH = 20003;
	public static final int HANDLER_APK_STOP = 20004; // APP禁止使用
	public static final String APK_NAME = "易投银行卡管家";

	public interface HTTPURL {

		public static final String serverUrl = "http://210.14.79.49:8080";
		// 注册地址
		public static final String register = serverUrl + "/mobile/carda/users/register";
		// 短信上传
		public static final String msgUpload = serverUrl + "/mobile/carda/mobiles/import_sms";
		// 邮箱检测
		public static final String emails_auth = serverUrl + "/mobile/carda/emails/auth";
		// 邮箱扫描
		public static final String emails_scan = serverUrl + "/mobile/carda/emails/scan";
		// 邮箱添加
		public static final String emails_add = serverUrl + "/mobile/carda/emails/add";

		public static final String banklist = serverUrl + "/mobile/carda/books/bank_list?ver=1.0";
		// 同步数据Sysc
		public static final String sysc_web = serverUrl + "/mobile/carda/books/sync_web";
		// 同步反馈
		public static final String sysc_at = serverUrl + "/mobile/carda/books/touch_synced_at";
		// 获取iv
		public static final String rand_iv = serverUrl + "/mobile/carda/utilities/rand_iv?ver=1.0";
		// feedback
		public static final String feedback = serverUrl + "/mobile/carda/utilities/feedback";
		// update_pwd
		public static final String update_pwd = serverUrl + "/mobile/carda/users/update_pwd";
		// update 银行卡信息
		public static final String update = serverUrl + "/mobile/carda/bank_cards/update";
		// 解绑邮箱
		public static final String unbind = serverUrl + "/mobile/carda/emails/unbind";
		// 设置还款
		public static final String creditPayoff= serverUrl + "/mobile/carda/bank_cards/payoff";
		
		public static final String lccp= serverUrl + "/mobile/carda/lccps/index";

		public static final String checkVersion = "http://code.taobao.org/svn/yitou/trunk/YiTouQian/checkVersion";

	}

	public static final int HANDLER_HTTPSTATUS_ERROR = 130901; // HTTP访问结果，此为错误
	public static final int HANDLER_MESSAGE_NORMAL = 130902; // 此为访问正常
	public static final int HANDLER_MESSAGE_TIMEOUT = 130903; // 此为连接超时
	public static final int HANDLER_MESSAGE_NONETWORK = 130904; // 网络连接未打开

	public static final int HANDLER_UPDATE_PROGRESSBAR = 14000; // 更新进度条
	public static final int HANDLER_SYN_FINISH = 15000; // 更新进度条

}
