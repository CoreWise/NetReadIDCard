package com.cw.netnfcreadidcardlib.USB;

import java.util.HashMap;
import java.util.Iterator;



import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.cw.netnfcreadidcardlib.Constants;
import com.cw.netnfcreadidcardlib.Interface.OnUSBInitListener;
import com.cw.netnfcreadidcardlib.Utils.DataUtils;

public class USBConnectionManager {

	private static final String TAG = Constants.TAG+"USBConnectionManager";

	private UsbManager manager;
	// 找到的USB设备
	private UsbDevice mUsbDevice;
	private UsbInterface mInterface;
	// 代表一个接口的某个节点的类:写数据节点
	private UsbEndpoint usbEpOut;
	// 代表一个接口的某个节点的类:读数据节点
	private UsbEndpoint usbEpIn;
	private UsbDeviceConnection mDeviceConnection;
	private final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
	private OnUSBInitListener mOnUSBInitListener;

	private Context context;
	private Handler handler;

	public USBConnectionManager(Context context, int vID, int pID, OnUSBInitListener onUSBInitListener) {
		this.context = context;
		handler = new Handler();
		this.mOnUSBInitListener = onUSBInitListener;

		manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
		HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
		Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();

		// 根据硬件id寻找设备
		while (deviceIterator.hasNext()) {
			UsbDevice device = deviceIterator.next();
			if ((device.getVendorId() == vID && device.getProductId() == pID)) {
				mUsbDevice = device;
				break;
			}
		}

		if (mUsbDevice != null) {
			// 代表USB设备的一个接口，这里0代表第一个接口，如果想知道设备有几个接口，可以调用mUsbDevice.getInterfaceCount()查询
			int interfaceCount = mUsbDevice.getInterfaceCount();
			mInterface = mUsbDevice.getInterface(interfaceCount - 1);
			if (mInterface != null) {
				if (mInterface.getEndpoint(0) != null) {
					usbEpIn = mInterface.getEndpoint(0);
				}
				if (mInterface.getEndpoint(1) != null) {
					usbEpOut = mInterface.getEndpoint(1);
				}

				// 判断是否有权限
				if (!manager.hasPermission(mUsbDevice)) {
					// 申请权限
					IntentFilter filter = new IntentFilter();
					filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
					filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
					filter.addAction(ACTION_USB_PERMISSION);
					context.registerReceiver(receiver, filter);
					PendingIntent mPermissionIntent = PendingIntent.getBroadcast(context, 0,
							new Intent(ACTION_USB_PERMISSION), 0);

					manager.requestPermission(mUsbDevice, mPermissionIntent);
				}
				onUSBInitListener.success();
			} else
				onUSBInitListener.error(OnUSBInitListener.NOT_FOUND_USBINTERFACE);
		} else
			onUSBInitListener.error(OnUSBInitListener.NOT_FOUND_DEVICE);
	}

	public int read(byte[] data) {
		return mDeviceConnection.bulkTransfer(usbEpIn, data, data.length, 3000);
	}

	public void write(byte[] data) {
		final int len = mDeviceConnection.bulkTransfer(usbEpOut, data, data.length, 3000);
		Log.d(TAG, "write to usb->" + DataUtils.toHexString(data) + "	len:" + len);
		handler.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(context, "len:" + len, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public int send(byte[] command, byte[] receiveBuffer) {
		mDeviceConnection.bulkTransfer(usbEpOut, command, command.length, 45);
		int inresult = 0;
		int sum = 0;
		byte[] buf = new byte[receiveBuffer.length];
		do {
			inresult = mDeviceConnection.bulkTransfer(usbEpIn, buf, buf.length - sum, 45);
			if (inresult > 0) {
				System.arraycopy(buf, 0, receiveBuffer, sum, inresult);
				sum += inresult;
			}
		} while (inresult > 0 && sum < receiveBuffer.length);
		return sum;
	}

	public void close() {
		if (mDeviceConnection != null && mInterface != null) {
			mDeviceConnection.releaseInterface(mInterface);
			mDeviceConnection.close();
		}
	}

	public void getConnection() throws Exception{
		// 打开设备，获取 UsbDeviceConnection 对象，连接设备，用于后面的通讯
		mDeviceConnection = manager.openDevice(mUsbDevice);
		mDeviceConnection.claimInterface(mInterface, true);
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ACTION_USB_PERMISSION)) {
				boolean granted = intent.getExtras().getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED);
				if (!granted)
					mOnUSBInitListener.error(OnUSBInitListener.NO_PERMISSION);
				context.unregisterReceiver(receiver);
			}
		}
	};
}