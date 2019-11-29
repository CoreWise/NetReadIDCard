package com.ivsign.android.IDCReader;

import android.util.Log;


import com.cw.netnfcreadidcardlib.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;

/**
 * 作者：李阳
 * 时间：2019/3/27
 * 描述：
 */
public class IDCReaderSDK {

    private static final String TAG = Constants.IDCardTAG+"Featureapi-IDCReaderSDK";

    static {
        System.loadLibrary("wltdecode");
    }

    public IDCReaderSDK() {
        Log.i(TAG, "Enter function IDCReaderSDK().");
    }

    public static int Init() {
        Log.i(TAG, "Enter function Init().");
        return wltInit(SfzFileManager.getRootFile());
    }

    public static int unpack(byte[] wltdata) {
        Log.i(TAG, "Enter function unpack().");
        byte[] licdata = new byte[]{5, 0, 1, 0, 91, 3, 51, 1, 90, -77, 30, 0};
        byte[] datawlt = new byte[1384];
        byte[] header = new byte[]{-86, -86, -86, -106, 105, 5, 8, 0, 0, -112};
        String strRet = bytesToHexString(wltdata).toUpperCase(Locale.ENGLISH);
        if(strRet.startsWith("AAAAAA9669090A000090")) {
            System.arraycopy(header, 0, datawlt, 0, header.length);
            System.arraycopy(wltdata, 10, datawlt, header.length, 4);
            System.arraycopy(wltdata, 16, datawlt, header.length + 4, 1281);
        } else {
            System.arraycopy(wltdata, 0, datawlt, 0, 1295);
        }

        return wltGetBMP(datawlt, licdata);
    }

    public static byte[] getPhoto() {
        Log.i(TAG, "Enter function getPhoto().");
        return getBytes(SfzFileManager.getPhotoPath());
    }

    public static byte[] getBytes(String filePath) {
        Log.i(TAG, "Enter function getBytes().");
        byte[] buffer = null;

        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1000];

            int n;
            while((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }

            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException var7) {
            var7.printStackTrace();
        } catch (IOException var8) {
            var8.printStackTrace();
        }

        return buffer;
    }

    public static String bytesToHexString(byte[] data) {
        Log.i(TAG, "Enter function bytesToHexString().");
        StringBuffer buffer = new StringBuffer();

        for(int i = 0; i < data.length; ++i) {
            buffer.append(byteToHexString(data[i]));
        }

        return buffer.toString();
    }

    public static String byteToHexString(byte b) {
        String s = Integer.toHexString(b & 255);
        return s.length() == 1?"0" + s:s;
    }

    public static native int wltInit(String var0);

    public static native int wltGetBMP(byte[] var0, byte[] var1);

}
