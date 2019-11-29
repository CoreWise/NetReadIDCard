package com.cw.netnfcreadidcardlib.Bean;

import java.io.Serializable;

public class HttpResp implements Serializable {
	private static final long serialVersionUID = -7934568556841320773L;
	private int code;
	private String data;
	private String ip;
	private int port;
	private String token;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}