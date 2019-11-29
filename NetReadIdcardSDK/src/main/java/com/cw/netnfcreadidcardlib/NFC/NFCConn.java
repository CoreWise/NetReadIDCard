package com.cw.netnfcreadidcardlib.NFC;

import java.io.IOException;



import android.nfc.Tag;
import android.nfc.tech.NfcB;
import android.util.Log;

import com.cw.netnfcreadidcardlib.Constants;
import com.cw.netnfcreadidcardlib.Interface.Conn;
import com.cw.netnfcreadidcardlib.Utils.DataUtils;

public class NFCConn implements Conn {

    private static final String TAG = Constants.TAG+"NFCConn";

    private NfcB mNfcB;

    public NFCConn() {

    }

    @Override
    public boolean connect() {
        try {
            mNfcB.connect();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,e.toString());
            return false;
        }
        return true;
    }

    @Override
    public byte[] getUID() {
        return null;
    }

    @Override
    public byte[] write(byte[] srcCmd) throws IOException {
        long start = System.currentTimeMillis();
        if (null == srcCmd || 0 >= srcCmd.length) {
            return null;
        }

        byte[] cmd = new byte[srcCmd.length - 7];
        System.arraycopy(srcCmd, 5, cmd, 0, srcCmd.length - 7);
        Log.i(TAG, "send 2 sfz:" + DataUtils.toHexString(cmd));


        byte[] retNFC = mNfcB.transceive(cmd);
        if (retNFC != null) {
            Log.i(TAG, "rev f sfz:" + DataUtils.toHexString(retNFC));
        }
        int lenNFC = retNFC.length;
        if (null == retNFC || lenNFC < 2) {
            return null;
        }

        // 普通nfc机器是-3，新大陆的是-2
        if (0x90 == (short) (retNFC[lenNFC - 3] & 0xFF) && 0x00 == retNFC[lenNFC - 2]) {
            long end = System.currentTimeMillis();
            Log.i(TAG, "time:" + (end - start));
            return retNFC;
        }
        return null;
    }

    @Override
    public boolean close() {
        try {
            mNfcB.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,e.toString());
            return false;
        }
        return true;
    }

    @Override
    public void setTag(Tag tag) {
        mNfcB = NfcB.get(tag);
    }
}