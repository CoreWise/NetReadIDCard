package com.cw.netnfcreadidcardlib.SFZ;


import com.cw.netnfcreadidcardlib.Bean.IdCardInfo;
import com.cw.netnfcreadidcardlib.Constants;
import com.cw.netnfcreadidcardlib.Utils.DataUtils;
import com.ivsign.android.IDCReader.IDCReaderSDK;
import com.ivsign.android.IDCReader.SfzFileManager;

import android.content.Context;
import android.util.Log;

public class AnalysisIDCard {
	private static final String TAG = Constants.TAG+"AnalysisIDCard";
	
	private Context m_Context;
	private int m_BaseId;
	private int m_LicId;
	private String m_PhotoPath;
	
	private String m_OKof2 = "AAAAAA96690508000090";//二代证
	private String m_OKof3 = "AAAAAA9669090A000090";//三代证
	
	public AnalysisIDCard(Context context, int baseId, int licId, String photoPath) {
		Log.i(TAG, "Enter function AnalysisIDCard().");

		this.m_Context = context;
		this.m_BaseId = baseId;
		this.m_LicId = licId;
		this.m_PhotoPath = photoPath;
	}
	
	public IdCardInfo decodeIdCardInfo(byte[] idCardSrcData) {
		Log.i(TAG, "Enter function decodeIdCardInfo().");

		if (null == idCardSrcData) {
			Log.i(TAG, "Do not get id card data!!!");
			return null;
		}

		byte[] retHead = new byte[10];
		System.arraycopy(idCardSrcData, 0, retHead, 0, retHead.length);

		String ret = DataUtils.toHexString(retHead);
		if (this.m_OKof2.equalsIgnoreCase(ret) || this.m_OKof3.equalsIgnoreCase(ret)) {
			Log.i(TAG, "Read id card successfully, The prefix is: " + ret);

			byte[] data = new byte[idCardSrcData.length - retHead.length];
			System.arraycopy(idCardSrcData, retHead.length, data, 0, data.length);

			short txtLen = DataUtils.getShort(data[0], data[1]);
			short imgLen = DataUtils.getShort(data[2], data[3]);

			short skipLength = 0;

			byte[] txtData = new byte[txtLen];
			System.arraycopy(data, 4 + skipLength, txtData, 0, txtLen);

			byte[] imgData = new byte[imgLen];
			System.arraycopy(data, 4 + skipLength + txtLen, imgData, 0, imgLen);

			IdCardInfo idCardInfo = null;

			try {
				String temp = null;
				idCardInfo = new IdCardInfo();

				temp = new String(txtData, 0, 30, "UTF-16LE").trim();
				idCardInfo.setName(temp);

				temp = new String(txtData, 30, 2, "UTF-16LE");
				if (temp.equals("1")) {
					temp = "男";
				} else {
					temp = "女";
				}
				idCardInfo.setSex(temp);

				temp = new String(txtData, 32, 4, "UTF-16LE");
				try {
					int code = Integer.parseInt(temp.toString());
					temp = decodeNation(code);
				} catch (Exception e) {
					temp = "";
				}
				idCardInfo.setNation(temp);

				temp = new String(txtData, 36, 16, "UTF-16LE").trim();
				idCardInfo.setBirthday(temp);

				temp = new String(txtData, 52, 70, "UTF-16LE").trim();
				idCardInfo.setAddress(temp);

				temp = new String(txtData, 122, 36, "UTF-16LE").trim();
				idCardInfo.setIdNum(temp);

				temp = new String(txtData, 158, 30, "UTF-16LE").trim();
				idCardInfo.setIssue(temp);

				temp = new String(txtData, 188, 16, "UTF-16LE").trim();
				idCardInfo.setStartDate(temp);

				temp = new String(txtData, 204, 16, "UTF-16LE").trim();
				idCardInfo.setEndDate(temp);
			} catch (Exception ex) {
				Log.i(TAG, "Analyze id card txt info unsuccessfully!!!");
				ex.printStackTrace();
				return null;
			}

			Log.i(TAG, "Analyze id card txt info successfully!!!");

			// idCardInfo.setPhoto(parsePhoto(imgData));
			idCardInfo.setPhoto(parsePhoto(idCardSrcData));

			return idCardInfo;
		}

		Log.i(TAG, "Analyze id card txt info unsuccessfully!!!");
		return null;
	}
	
	/**
	 * 解析民族原始数据
	 */
	public String decodeNation(int code) {
		Log.i(TAG, "Enter function decodeNation().");

		String nation;

		switch (code) {
		case 1:
			nation = "汉";
			break;

		case 2:
			nation = "蒙古";
			break;

		case 3:
			nation = "回";
			break;

		case 4:
			nation = "藏";
			break;

		case 5:
			nation = "维吾尔";
			break;

		case 6:
			nation = "苗";
			break;

		case 7:
			nation = "彝";
			break;

		case 8:
			nation = "壮";
			break;

		case 9:
			nation = "布依";
			break;

		case 10:
			nation = "朝鲜";
			break;

		case 11:
			nation = "满";
			break;

		case 12:
			nation = "侗";
			break;

		case 13:
			nation = "瑶";
			break;

		case 14:
			nation = "白";
			break;

		case 15:
			nation = "土家";
			break;

		case 16:
			nation = "哈尼";
			break;

		case 17:
			nation = "哈萨克";
			break;

		case 18:
			nation = "傣";
			break;

		case 19:
			nation = "黎";
			break;

		case 20:
			nation = "傈僳";
			break;

		case 21:
			nation = "佤";
			break;

		case 22:
			nation = "畲";
			break;

		case 23:
			nation = "高山";
			break;

		case 24:
			nation = "拉祜";
			break;

		case 25:
			nation = "水";
			break;

		case 26:
			nation = "东乡";
			break;

		case 27:
			nation = "纳西";
			break;
		case 28:
			nation = "景颇";
			break;
		case 29:
			nation = "柯尔克孜";
			break;
		case 30:
			nation = "土";
			break;
		case 31:
			nation = "达斡尔";
			break;
		case 32:
			nation = "仫佬";
			break;
		case 33:
			nation = "羌";
			break;
		case 34:
			nation = "布朗";
			break;
		case 35:
			nation = "撒拉";
			break;
		case 36:
			nation = "毛南";
			break;
		case 37:
			nation = "仡佬";
			break;
		case 38:
			nation = "锡伯";
			break;
		case 39:
			nation = "阿昌";
			break;
		case 40:
			nation = "普米";
			break;
		case 41:
			nation = "塔吉克";
			break;
		case 42:
			nation = "怒";
			break;

		case 43:
			nation = "乌孜别克";
			break;

		case 44:
			nation = "俄罗斯";
			break;

		case 45:
			nation = "鄂温克";
			break;

		case 46:
			nation = "德昂";
			break;

		case 47:
			nation = "保安";
			break;

		case 48:
			nation = "裕固";
			break;

		case 49:
			nation = "京";
			break;

		case 50:
			nation = "塔塔尔";
			break;

		case 51:
			nation = "独龙";
			break;

		case 52:
			nation = "鄂伦春";
			break;

		case 53:
			nation = "赫哲";
			break;

		case 54:
			nation = "门巴";
			break;

		case 55:
			nation = "珞巴";
			break;

		case 56:
			nation = "基诺";
			break;

		case 97:
			nation = "其他";
			break;

		case 98:
			nation = "外国血统中国籍人士";
			break;

		default:
			nation = "";

		}
		return nation;
	}

	/**
	 * 解析图像数据
	 * @param wltData 图像原始数据
	 * @return 图像数据
	 */
	public byte[] parsePhoto(byte[] wltData) {
		SfzFileManager sfzFileManager = new SfzFileManager(this.m_PhotoPath);

		Log.i(TAG, "To init db, the base id is: " + this.m_BaseId + ", the license id is: " + this.m_LicId + ".");

		if (sfzFileManager.initDB(this.m_Context, this.m_BaseId, this.m_LicId)) {
			Log.i(TAG, "Init db successfully,then to init photo source data!!!");
			int ret = IDCReaderSDK.Init();
			if (0 == ret) {
				Log.i(TAG, "Init photo source data successfully!!!");
				// ret = IDCReaderSDK.unpack(this.m_IdCardData);
				ret = IDCReaderSDK.unpack(wltData);
				if (1 == ret) {
					Log.i(TAG, "To Analyze photo source data successfully!!!");
					byte[] image = IDCReaderSDK.getPhoto();
					if (null != image) {
						Log.i(TAG, "Analyze id card photo info successfully!!!");
					}
					return image;
				}
			}
		}

		Log.i(TAG, "Analyze id card photo info unsuccessfully!!!");
		return null;
	}
}