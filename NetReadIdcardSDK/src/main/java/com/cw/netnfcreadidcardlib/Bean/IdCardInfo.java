package com.cw.netnfcreadidcardlib.Bean;

import android.util.Log;

public class IdCardInfo {
	private int m_Result = -1;

	public int getResult() {
		return this.m_Result;
	}

	public void setResult(int result) {
		this.m_Result = result;
	}

	private String uid;
	private String m_Name;

	private String m_Sex;

	private String m_Nation;

	private String m_Birthday;

	private String m_Address;

	private String m_IdNum;

	private String m_Issue;

	private String m_StartDate;

	private String m_EndDate;

	private byte[] m_Photo;

	private byte[] m_FingerModle;
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		Log.i("IdCardInfo", "Enter function getName().");
		return this.m_Name;
	}

	public void setName(String name) {
		Log.i("IdCardInfo", "Enter function setName().");
		this.m_Name = name;
	}

	public String getSex() {
		Log.i("IdCardInfo", "Enter function getSex().");
		return this.m_Sex;
	}

	public void setSex(String sex) {
		Log.i("IdCardInfo", "Enter function setSex().");
		this.m_Sex = sex;
	}

	public String getNation() {
		Log.i("IdCardInfo", "Enter function getNation().");
		return this.m_Nation;
	}

	public void setNation(String nation) {
		Log.i("IdCardInfo", "Enter function setNation().");
		this.m_Nation = nation;
	}

	public String getBirthday() {
		Log.i("IdCardInfo", "Enter function getBirthday().");
		return this.m_Birthday;
	}

	public void setBirthday(String birthday) {
		Log.i("IdCardInfo", "Enter function setBirthday().");
		this.m_Birthday = birthday;
	}

	public String getAddress() {
		Log.i("IdCardInfo", "Enter function getAddress().");
		return this.m_Address;
	}

	public void setAddress(String address) {
		Log.i("IdCardInfo", "Enter function setAddress().");
		this.m_Address = address;
	}

	public String getIdNum() {
		Log.i("IdCardInfo", "Enter function getIdNum().");
		return this.m_IdNum;
	}

	public void setIdNum(String idNum) {
		Log.i("IdCardInfo", "Enter function setIdNum().");
		this.m_IdNum = idNum;
	}

	public String getIssue() {
		Log.i("IdCardInfo", "Enter function getIssue().");
		return this.m_Issue;
	}

	public void setIssue(String issue) {
		Log.i("IdCardInfo", "Enter function setIssue().");
		this.m_Issue = issue;
	}

	public String getStartDate() {
		Log.i("IdCardInfo", "Enter function getStartDate().");
		return this.m_StartDate;
	}

	public void setStartDate(String startDate) {
		Log.i("IdCardInfo", "Enter function setStartDate().");
		this.m_StartDate = startDate;
	}

	public String getEndDate() {
		Log.i("IdCardInfo", "Enter function getEndDate().");
		return this.m_EndDate;
	}

	public void setEndDate(String endDate) {
		Log.i("IdCardInfo", "Enter function setEndDate().");
		this.m_EndDate = endDate;
	}

	public byte[] getPhoto() {
		Log.i("IdCardInfo", "Enter function getPhoto().");
		return this.m_Photo;
	}

	public void setPhoto(byte[] photo) {
		Log.i("IdCardInfo", "Enter function setPhoto().");
		this.m_Photo = photo;
	}

	public byte[] getM_FingerModle() {
		return m_FingerModle;
	}

	public void setM_FingerModle(byte[] m_FingerModle) {
		this.m_FingerModle = m_FingerModle;
	}
}