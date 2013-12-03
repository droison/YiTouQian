package com.yeetou.xinyongkaguanjia.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yeetou.xinyongkaguanjia.info.MsgInfo;

public class FindNumber {

	public static Boolean MsgInfoFilter(MsgInfo msgInfo) {
		Pattern pattern = Pattern.compile("[.]\\d{2}元"); // 匹配小数点后两个数数字的"[.]\\d{2}\\D|[.]\\d{2}?$"
		Matcher matcher = pattern.matcher(msgInfo.getBody().toString());
		if (matcher.find()) {
			return true;
		} else {
			return false;
		}

	}
}
