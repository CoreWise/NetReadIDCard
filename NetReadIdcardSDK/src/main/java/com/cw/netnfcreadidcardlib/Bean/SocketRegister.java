package com.cw.netnfcreadidcardlib.Bean;

import java.io.Serializable;

public class SocketRegister implements Serializable {
	private static final long serialVersionUID = 4330631278773368310L;
	private String CMD;
	private int TYPE;
	private String NFC;
	private String ID;
	private String MSG;
	private String DEVICEID;
	private String TOKEN;

	public String getCMD() {
		return CMD;
	}

	public void setCMD(String cmd) {
		CMD = cmd;
	}

	public int getType() {
		return TYPE;
	}

	public void setType(int type) {
		TYPE = type;
	}

	public String getNfc() {
		return NFC;
	}

	public void setNfc(String nfc) {
		NFC = nfc;
	}

	public String getMsg() {
		return MSG;
	}

	public void setMsg(String msg) {
		MSG = msg;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getDEVICEID() {
		return DEVICEID;
	}

	public void setDEVICEID(String dEVICEID) {
		DEVICEID = dEVICEID;
	}

	public String getTOKEN() {
		return TOKEN;
	}

	public void setTOKEN(String tOKEN) {
		TOKEN = tOKEN;
	}
}