package com.corewise.idcardreadersdk.support;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;


import com.cw.netnfcreadidcardlib.Constants;
import com.cw.netnfcreadidcardlib.Interface.Conn;
import com.cw.netnfcreadidcardlib.Utils.DataUtils;

import java.io.IOException;

/**
 * jni调用类
 */
public class NativeSupport {

    private static final String TAG = Constants.TAG + "NativeSupport";

    private static final byte[] head = {(byte) 0xaa, (byte) 0xaa, (byte) 0xaa, (byte) 0x96, 0x69, 0x05, 0x08, 0, 0, (byte) 0x90};
    private static byte[] mkey;
    private static Conn mConn;
    private static byte[] mSFZData = null;
    private static byte[] fingetModleData = null;

    private Handler mHandler;

    public void setHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }


    public static void init(byte[] key, Conn conn) {
        mkey = key;
        mConn = conn;
        mSFZData = null;
        fingetModleData = null;
    }

    public static byte[] getSFZData() {
        return mSFZData;
    }

    public static byte[] getFingetModleData() {
        return fingetModleData;
    }

    public static native int registDeviceForTry(Context context);

    public static native int registDevice(Context context, String serialNumber);

    public static native int read(Context context);

    //一下为新增的三个接口，20190624
    public static native int version();

    public static native String buildDate();

    public static native int checkLicense();

    public static native int setRegisterUrl(String url);

    public static native int setWorkAllocUrl(String url);

    //获取uuid
    public static byte[] getKey() {
        Log.d(TAG, "getKey:" + DataUtils.toHexString(mkey));
        return mkey;
    }

    //有id服务器情况下会走
    public static void IDContent(byte[] sfzData) {
        Log.d(TAG, "IDContent:" + DataUtils.toHexString(sfzData));
        mSFZData = sfzData;
    }


    /**
     * 这里的方法被so，ndk里调用
     * @param data
     * @return
     * @throws IOException
     */
    public static byte[] sendData(byte[] data)  {

        byte[] rev=null ;
        try {
            rev = mConn.write(data);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"-------: "+e.toString());
        }
        if (rev == null)
            Log.d(TAG, "rev == null");
        return rev;
    }


    /*public static byte[] sendData(byte[] data) {
        byte[] rev = new byte[0];

        try {
            rev = mConn.write(data);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"--------------------: "+e.toString());
           // mHandler.sendEmptyMessage(111111345);
        }

        if (rev == null)
            Log.d(TAG, "rev == null");
        return rev;
    }*/


    public static void readSuccess(byte[] sfzData) {
        Log.d(TAG, "readSuccess:" + sfzData.length);
        byte[] data = new byte[1295];
        System.arraycopy(head, 0, data, 0, 10);
        System.arraycopy(sfzData, 15, data, 10, 4);
        System.arraycopy(sfzData, 21, data, 14, 1280);
        mSFZData = data;
    }

    public static void fingerData(byte[] fingerData) {
        Log.d(TAG, "fingerData:" + fingerData.length);
        byte[] data = new byte[1024];
        System.arraycopy(fingerData, 5, data, 0, 1024);
        fingetModleData = data;
    }

    static {
        //System.loadLibrary("KmsSupport0624");
        System.loadLibrary("KmsSupport20191129");
    }


}