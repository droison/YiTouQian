package com.yeetou.xinyongkaguanjia.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypt {

	final static String s_key = "7682cb539a4ed10a6957438201bcedd0";

	// iv 动态变化，增强安全
	// AES-128-CBC加密
	// iv, 例如：8c32510d64a7be9adb38c09e672541de
	public static String encrypt(String plainText, String iv) throws Exception {

		byte[] skey = decodeHex(s_key.toCharArray());
		byte[] siv = decodeHex(iv.toCharArray());

		SecretKeySpec skeySpec1 = new SecretKeySpec(skey, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec1, new IvParameterSpec(siv));
		byte[] encrypted = cipher.doFinal(plainText.getBytes());

		return encodeHex(encrypted);
	}

	private static String encodeHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", (b & 0xFF)));
		}
		return sb.toString();
	}

	private static byte[] decodeHex(char[] data) throws Exception {
		int len = data.length;

		if ((len & 0x01) != 0) {
			throw new Exception("Odd number of characters.");
		}

		byte[] out = new byte[len >> 1];

		// two characters form the hex value.
		for (int i = 0, j = 0; j < len; i++) {
			int f = toDigit(data[j], j) << 4;
			j++;
			f = f | toDigit(data[j], j);
			j++;
			out[i] = (byte) (f & 0xFF);
		}

		return out;
	}

	private static int toDigit(char ch, int index) throws Exception {
		int digit = Character.digit(ch, 16);
		if (digit == -1) {
			throw new Exception("Illegal hexadecimal charcter " + ch
					+ " at index " + index);
		}
		return digit;
	}
	
	public static void main(String[] args) throws Exception {
        String pwd = "User@123";
        String iv = "8c32510d64a7be9adb38c09e672541de";
        System.out.println(Crypt.encrypt(pwd, iv));
	}

}
