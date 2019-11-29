package com.cw.netnfcreadidcardlib.Interface;

public interface OnUSBInitListener {
	final int NOT_FOUND_DEVICE = -1;
    final int OPEN_DEVICE_FAILURE = -2;
    final int NOT_FOUND_USBINTERFACE = -3;
    final int NO_PERMISSION = -4;

    void success();

    void error(int code);
}