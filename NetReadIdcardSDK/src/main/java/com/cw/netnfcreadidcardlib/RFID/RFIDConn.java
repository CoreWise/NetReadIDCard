package com.cw.netnfcreadidcardlib.RFID;

public class RFIDConn {
	public RFIDConn() {
		// 这里面厂商需要做一些初始化操作
	}

	public boolean connect() {
		// 这里面厂商需要实现rfid的连接操作
		return true;
	}

	public byte[] getUID() {
		// 这里面厂商需要按照文档，实现获取身份证uid号并且返回
		return null;
	}

	public byte[] write(byte[] data) {
		// 这里面厂商需要根据自己的rfid私有接口实现与身份证的交互
		return null;
	}

	public boolean close() {
		// 这里厂商需要关闭rfid并且释放内存
		return false;
	}
}