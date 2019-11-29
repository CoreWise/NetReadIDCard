package com.cw.netnfcreadidcardlib.USB;



import android.content.Context;
import android.nfc.Tag;
import android.util.Log;

import com.cw.netnfcreadidcardlib.Commands;
import com.cw.netnfcreadidcardlib.Constants;
import com.cw.netnfcreadidcardlib.Interface.Conn;
import com.cw.netnfcreadidcardlib.Interface.OnUSBInitListener;
import com.cw.netnfcreadidcardlib.Utils.DataUtils;

public class USBConn implements Conn {

	private static final String TAG = Constants.TAG+"USBConn";

	private USBConnectionManager manager = null;
	private Context mContext;

	public USBConn(Context context) {
		this.mContext = context;
		manager = new USBConnectionManager(context, 4660, 22136, new OnUSBInitListener() {

			@Override
			public void success() {
				Log.i(TAG, "success");
			}

			@Override
			public void error(int code) {
				Log.i(TAG, "error:" + code);
			}
		});
	}

	@Override
	public boolean connect() {
		if (manager != null) {
			try {
				byte[] data = null;
				manager.getConnection();
				data = write2sfz(Commands.cmd0);
				if (data[0] == 0) {
					manager = null;
					manager = new USBConnectionManager(mContext, 4660, 22136, new OnUSBInitListener() {

						@Override
						public void success() {
							Log.i(TAG, "success");
						}

						@Override
						public void error(int code) {
							Log.i(TAG, "error:" + code);
						}
					});
					return false;
				}
				write2sfz(Commands.cmd1);
				return true;
			} catch (Exception e) {
				manager = null;
				manager = new USBConnectionManager(mContext, 4660, 22136, new OnUSBInitListener() {

					@Override
					public void success() {
						Log.i(TAG, "success");
					}

					@Override
					public void error(int code) {
						Log.i(TAG, "error:" + code);
					}
				});
				return false;
			}
		} else {
			manager = new USBConnectionManager(mContext, 4660, 22136, new OnUSBInitListener() {

				@Override
				public void success() {
					Log.i(TAG, "success");
				}

				@Override
				public void error(int code) {
					Log.i(TAG, "error:" + code);
				}
			});
			return false;
		}
	}

	@Override
	public byte[] getUID() {
		byte[] uid = write2sfz(Commands.GET_UID);
		if (uid != null && uid.length >= 11 && (uid[uid.length - 3] & 0xff) == 0x90 && uid[uid.length - 2] == 0x00) {
			byte[] rawUID = new byte[8];
			System.arraycopy(uid, 0, rawUID, 0, 8);
			Log.i(TAG, "uid:" + DataUtils.toHexString(rawUID));
			return rawUID;
		}
		return null;
	}

	/**
	 * 解包，加包后直接发给身份证
	 * 
	 * @param data
	 *            要发送的数据
	 * @return 身份证返回数据
	 */
	@Override
	public byte[] write(byte[] data) {
		long time = System.currentTimeMillis();
		byte[] cmd = new byte[data.length - 7];
		System.arraycopy(data, 5, cmd, 0, data.length - 7);
		byte[] receiveBuffer = new byte[2000];
		Log.i(TAG, "send 2 sfz:" + DataUtils.toHexString(toPackage(cmd)));
		int len = manager.send(toPackage(cmd), receiveBuffer);
		if (len > 0) {
			byte[] rev = new byte[len];
			System.arraycopy(receiveBuffer, 0, rev, 0, len);
			Log.i(TAG, "rev f sfz:" + DataUtils.toHexString(rev));
			long end = System.currentTimeMillis();
			Log.i(TAG, "time:" + (end - time));
			return rev;
		} else {
			return null;
		}
	}

	@Override
	public boolean close() {
		if (manager != null) {
			manager.close();
		}
		return true;
	}

	/**
	 * 不用解包，加包后直接发给身份证
	 * 
	 * @param data
	 *            要发送的数据
	 * @return 身份证返回数据
	 */
	private byte[] write2sfz(byte[] data) {
		byte[] receiveBuffer = new byte[2000];
		Log.i(TAG, "send 2 sfz:" + DataUtils.toHexString(toPackage(data)));
		int len = manager.send(toPackage(data), receiveBuffer);
		if (len > 0) {
			byte[] rev = new byte[len];
			System.arraycopy(receiveBuffer, 0, rev, 0, len);
			Log.i(TAG, "rev f sfz:" + DataUtils.toHexString(rev));
			return rev;
		} else {
			return null;
		}
	}

	/**
	 * 发给cr30s的指令封装
	 * 
	 * @param data
	 *            原始指令
	 * @return 封装后的指令
	 */
	private byte[] toPackage(byte[] data) {
		byte[] rawData = new byte[data.length + 6];
		rawData[0] = (byte) 0xca;
		rawData[1] = (byte) 0xdf;
		rawData[2] = 0x02;
		rawData[3] = (byte) (data.length >> 8);
		rawData[4] = (byte) (data.length >> 0);
		System.arraycopy(data, 0, rawData, 5, data.length);
		rawData[rawData.length - 1] = (byte) 0xe3;
		return rawData;
	}

	@Override
	public void setTag(Tag tag) {
		
	}
}