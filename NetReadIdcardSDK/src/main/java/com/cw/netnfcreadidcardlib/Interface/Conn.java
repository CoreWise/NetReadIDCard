package com.cw.netnfcreadidcardlib.Interface;

import java.io.IOException;

import android.nfc.Tag;

public interface Conn {
	boolean connect();
	byte[] getUID();
	byte[] write(byte[] data) throws IOException;
	boolean close();
	void setTag(Tag tag);
}