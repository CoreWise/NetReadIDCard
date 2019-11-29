package com.ivsign.android.IDCReader;

import android.content.Context;
import android.os.Environment;
import android.util.Log;


import com.cw.netnfcreadidcardlib.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 作者：李阳
 * 时间：2019/3/27
 * 描述：
 */
public class SfzFileManager {

    private static final String TAG = Constants.IDCardTAG+"Featureapi-SfzFileManager";
    private static String m_BasePath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static String m_RootFile;
    private static String m_RootFileLog;
    private static String m_Witlib;
    private static String m_Lic;
    private static String m_PhotoPath;

    static {
        m_RootFile = m_BasePath + "/" + "wltlib" + "/";
        m_RootFileLog = m_BasePath + "/" + "clog" + "/";
        m_Witlib = m_RootFile + "base.dat";
        m_Lic = m_RootFile + "license.lic";
        m_PhotoPath = m_RootFile + "zp.bmp";
    }

    public static String getRootFile() {
        return m_RootFile;
    }

    public static String getPhotoPath() {
        return m_PhotoPath;
    }

    public SfzFileManager(String path) {
        Log.i(TAG, "Enter function SfzFileManager().");
        if(path != null && "" != path) {
            m_BasePath = path;
        }

        m_RootFile = m_BasePath + "/" + "wltlib" + "/";
        m_RootFileLog = m_BasePath + "/" + "clog" + "/";
        m_Witlib = m_RootFile + "base.dat";
        m_Lic = m_RootFile + "license.lic";
        m_PhotoPath = m_RootFile + "zp.bmp";
    }

    public boolean initDB(Context context, int baseId, int licId) {
        Log.i(TAG, "Enter function initDB().");
        Log.i(TAG, "The root file path is: " + m_RootFile);
        Log.i(TAG, "The root log file path is: " + m_RootFileLog);
        Log.i(TAG, "The base path is: " + m_Witlib);
        Log.i(TAG, "The license path is: " + m_Lic);
        Log.i(TAG, "The photo path is: " + m_PhotoPath);
        File fileR = new File(m_RootFile);
        File fileW = new File(m_Witlib);
        File fileL = new File(m_Lic);
        if(!fileR.exists() && !fileR.mkdirs()) {
            Log.i(TAG, "Mkdir root file path unsuccessfully.");
            return false;
        } else if(!fileW.exists() && !this.InitDeviceB(context, baseId)) {
            Log.i(TAG, "Init base.dat unsuccessfully.");
            return false;
        } else if(!fileL.exists() && !this.InitDeviceL(context, licId)) {
            Log.i(TAG, "Init license.lic unsuccessfully.");
            return false;
        } else {
            Log.i(TAG, "Init db successfully.");
            return true;
        }
    }

    private boolean InitDeviceB(Context context, int baseId) {
        Log.i(TAG, "Enter function InitDeviceB().");
        InputStream input = null;
        OutputStream output = null;
        input = context.getResources().openRawResource(baseId);

        try {
            output = new FileOutputStream(m_Witlib);
            byte[] buffer = new byte[2048];

            int length;
            while((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            Log.i(TAG, "Init base.dat successfully.");
            return true;
        } catch (FileNotFoundException var17) {
            Log.e(TAG, var17.getMessage());
        } catch (IOException var18) {
            Log.e(TAG, var18.getMessage());
        } finally {
            try {
                output.flush();
                output.close();
                input.close();
                return true;
            } catch (IOException var16) {
                Log.e(TAG, var16.getMessage());
            }
        }

        return false;
    }

    private boolean InitDeviceL(Context context, int licId) {
        Log.i(TAG, "Enter function InitDeviceL().");
        InputStream input = null;
        OutputStream output = null;
        input = context.getResources().openRawResource(licId);

        try {
            output = new FileOutputStream(m_Lic);
            byte[] buffer = new byte[2048];

            int length;
            while((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            Log.i(TAG, "Init license.lic successfully.");
            return true;
        } catch (FileNotFoundException var17) {
            Log.e(TAG, var17.getMessage());
        } catch (IOException var18) {
            Log.e(TAG, var18.getMessage());
        } finally {
            try {
                output.flush();
                output.close();
                input.close();
                return true;
            } catch (IOException var16) {
                Log.e(TAG, var16.getMessage());
            }
        }

        return false;
    }
}
