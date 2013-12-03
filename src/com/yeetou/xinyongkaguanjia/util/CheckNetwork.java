package com.yeetou.xinyongkaguanjia.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckNetwork {

	public static Boolean Isavilable(Context context){
		Boolean b = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			b = networkInfo.isAvailable();
		}
		return b;
		
	}
}
