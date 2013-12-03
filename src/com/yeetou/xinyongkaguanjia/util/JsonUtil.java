package com.yeetou.xinyongkaguanjia.util;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yeetou.xinyongkaguanjia.http.base.MsgDataBase;
import com.yeetou.xinyongkaguanjia.http.base.MsgUploadBase;

public class JsonUtil {
	public static Object Json2Object(String json, Class cla){
		Object ob = JSON.parseObject(json, cla);
		return ob;
	}

	
	public static MsgUploadBase jsonMsgUploadBase(String json) throws JSONException {
		MsgUploadBase msgub = new MsgUploadBase();
		if (json == null)
			return msgub;

		JSONObject jo = new JSONObject(json);
		int code = jo.getInt("code");
		msgub.setCode(code);
		
		if (code == 101) {
			msgub.setSms_cnt(jo.getInt("sms_cnt"));
			msgub.setSecret(jo.getString("secret"));
			msgub.setEmail(jo.getString("email"));
			msgub.setCard_cnt(jo.getInt("card_cnt"));
			List<MsgDataBase> msgDataBases = JSON.parseArray(jo.getString("data"), MsgDataBase.class);
			msgub.setData(msgDataBases);
		} else {
			msgub.setMsg(jo.getString("msg"));
		}

		return msgub;
	}
}
