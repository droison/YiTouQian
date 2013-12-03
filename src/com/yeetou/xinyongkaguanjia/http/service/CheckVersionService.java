
package com.yeetou.xinyongkaguanjia.http.service;

import org.json.JSONObject;

import com.yeetou.xinyongkaguanjia.constants.AppConstant;
import com.yeetou.xinyongkaguanjia.http.base.HttpResponseEntity;
import com.yeetou.xinyongkaguanjia.util.StringUtil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

public class CheckVersionService implements Runnable {

    private Handler mHandler;

    private Context context;

    public CheckVersionService(Context context,Handler mHandler) {
        this.mHandler = mHandler;
        this.context = context;
    }

    public void run() {

    		Boolean b = false;
    		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
    		if (networkInfo != null) {
    			b = networkInfo.isAvailable();
    		}
    		if (!b) {
    			mHandler.sendEmptyMessage(AppConstant.HANDLER_MESSAGE_NONETWORK);
    			return;
    		}

    		HttpResponseEntity hre = HTTP.get(AppConstant.HTTPURL.checkVersion);
    		switch (hre.getHttpResponseCode()) {
    		case 200:
    			try {
    				String json = StringUtil.byte2String(hre.getB());
    				JSONObject jo = new JSONObject(json);
    	            JSONObject data = jo.getJSONObject("data");

    	            int versionCode = context.getPackageManager().getPackageInfo("com.tiancikeji.yitouqian", 0).versionCode; 	            
    	            Log.v("versionCode", ""+versionCode);
    	            int newVersion = data.getInt("version");
    	            boolean istrue = jo.getBoolean("istrue");

    	            if(!istrue){
    	            	mHandler.sendEmptyMessage(AppConstant.HANDLER_APK_STOP);
    	            }else if (newVersion > versionCode) {
    	            	mHandler.sendMessage(mHandler.obtainMessage(AppConstant.HANDLER_VERSION_UPDATE,json));
    	            }
    			} catch (Exception e) {
    				mHandler.sendEmptyMessage(AppConstant.HANDLER_HTTPSTATUS_ERROR);
    				Log.e("StringGet", "200", e);
    			}
    			break;
    		default:
    			mHandler.sendEmptyMessage(AppConstant.HANDLER_HTTPSTATUS_ERROR);
    			Log.d("StringGet", "" + hre.getHttpResponseCode());
    			break;
    		}
    	
    }

}
