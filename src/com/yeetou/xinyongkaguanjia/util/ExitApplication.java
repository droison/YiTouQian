package com.yeetou.xinyongkaguanjia.util;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ExitApplication extends Application {

	private List<Activity> activityList = new LinkedList<Activity>();
	private Activity cardInfoActivity;
	private static ExitApplication instance;

	private ExitApplication() {

	}

	// 单例模式中获取唯一的ExitApplication 实例
	public static ExitApplication getInstance() {
		if (null == instance) {
			instance = new ExitApplication();
		}
		return instance;
	}

	public void addCardInfoActivity(Activity activity) {
		cardInfoActivity = activity;
	}

	public void finishCardInfoActivity() {
		if (cardInfoActivity != null) {
			cardInfoActivity.finish();
		}
	}

	// 添加Activity 到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// 遍历所有Activity 并finish

	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}

		System.exit(0);
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}