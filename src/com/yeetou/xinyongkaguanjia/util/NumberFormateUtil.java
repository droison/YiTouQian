package com.yeetou.xinyongkaguanjia.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
/**
 * 
 * @author Ls
 *
 */
public class NumberFormateUtil {

	public static String Fromate0(Object obj) {
		DecimalFormat df = new DecimalFormat("#");
		return df.format(obj);

	}

	public static String Fromate2(Object obj) {

		 DecimalFormat df = new DecimalFormat("#,##0.##");
		 String result = df.format(obj);
		
		if(result.startsWith(".")){
			return 0+result;
		}else{
			return result;
		}
	}
	public static String Fromate3(Object obj) {
		DecimalFormat df = new DecimalFormat("#.#%"); //
		String result = df.format(obj);
		if(result.startsWith(".")){
			return 0+result;
		}else{
			return result;
		}

	}
	public static String Fromate4(Object obj) {
		DecimalFormat df = new DecimalFormat("#.##%"); //
		String result = df.format(obj);
		if(result.startsWith(".")){
			return 0+result;
		}else{
			return result;
		}

	}
	
	public static String Fromate0ex(Object obj) {
		 DecimalFormat df = new DecimalFormat("#,##0");
		return "￥"+df.format(obj);

	}

	public static String Fromate2ex(Object obj) {
		 DecimalFormat df = new DecimalFormat("#,##0.##");
		
		String result = df.format(obj);
		if(result.startsWith(".")){
			return "￥"+0+result;
		}else{
			return "￥"+result;
		}
	}
}
