package com.yeetou.xinyongkaguanjia.util;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.yeetou.xinyongkaguanjia.info.Device;
import com.yeetou.xinyongkaguanjia.info.UserLoginInfo;

public class UserInfoUtil {

	public static UserLoginInfo info(Context context,String phone) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String number = tm.getLine1Number(); // 用户手机号
		UserLoginInfo user = new UserLoginInfo();
		user.setVer("1.0");
		user.setNumber(phone); // 测试用
		user.setDevice(device(context));
		return user;
	}

	public static Device device(Context context) {
		Device device = new Device();
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String brand = android.os.Build.BRAND;// 手机品牌
		String model = android.os.Build.MODEL; // 手机型号
		String release = android.os.Build.VERSION.RELEASE; // android系统版本号
		device.setToken(tm.getDeviceId());// imme号
		device.setMemo(brand + ";" + model + ";" + release); // 手机品牌;型号;安卓系统版本setMemo
		return device;

	}
}
