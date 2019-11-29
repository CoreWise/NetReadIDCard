package com.cw.netnfcreadidcardlib.Utils;

import android.text.TextUtils;

public class DataUtils {
	private static final byte[] HEX_CHAR_TABLE = { (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4',
			(byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D',
			(byte) 'E', (byte) 'F' };

	public static String toHexString(byte[] b) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			buffer.append(byteToHexString(b[i]));
		}
		return buffer.toString();
	}
	
	public static String byteToHexString(byte b) {
		String s = Integer.toHexString(b & 0xFF);
		if (s.length() == 1) {
			return "0" + s;
		} else {
			return s;
		}
	}
	
	public static short getShort(byte b1, byte b2) {
		short temp = 0;
		temp |= (b1 & 0xff);
		temp <<= 8;
		temp |= (b2 & 0xff);
		return temp;
	}

	public static byte[] getNfcCmd(byte[] data, int seq) {
		byte[] head = { 0x5a, 0x55 };
		byte lenH = 0x00;
		byte lenL = 0x00;
		byte[] end = { 0x6a, 0x69 };
		byte[] cmd = new byte[head.length + 1 + 2 + data.length + end.length];

		if (0xFF < data.length) {
			lenH = (byte) ((data.length & 0xFF00) >> 8);
			lenL = (byte) (data.length & 0x00FF);
		} else {
			lenH = 0x00;
			lenL = (byte) data.length;
		}

		System.arraycopy(head, 0, cmd, 0, head.length);
		cmd[head.length] = (byte) seq;
		cmd[head.length + 1] = lenH;
		cmd[head.length + 2] = lenL;
		System.arraycopy(data, 0, cmd, head.length + 1 + 2, data.length);
		System.arraycopy(end, 0, cmd, head.length + 1 + 2 + data.length, end.length);

		return cmd;
	}
	
	public static String getHexString(byte[] raw, int len) {
		byte[] hex = new byte[2 * len];
		int index = 0;
		int pos = 0;

		for (byte b : raw) {
			if (pos >= len)
				break;

			pos++;
			int v = b & 0xFF;
			hex[index++] = HEX_CHAR_TABLE[v >>> 4];
			hex[index++] = HEX_CHAR_TABLE[v & 0xF];
		}
		return new String(hex);
	}
	
	/**
	 * 16进制字符串转换成数组
	 * 
	 * @param hex
	 * @return
	 */
	public static byte[] hexStringTobyte(String hex) {
		if (TextUtils.isEmpty(hex)) {
			return null;
		}
		hex = hex.toUpperCase();
		int len = hex.length() / 2;
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}
	
	public static int toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}
}